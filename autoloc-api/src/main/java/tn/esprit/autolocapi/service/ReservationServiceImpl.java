package tn.esprit.autolocapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autolocapi.domain.Client;
import tn.esprit.autolocapi.domain.Contrat;
import tn.esprit.autolocapi.domain.Reservation;
import tn.esprit.autolocapi.domain.StatutReservation;
import tn.esprit.autolocapi.dto.ReservationDTO;
import tn.esprit.autolocapi.dto.ReservationResponseDTO;
import tn.esprit.autolocapi.dto.ReservationWithContratRequestDTO;
import tn.esprit.autolocapi.exception.ResourceNotFoundException;
import tn.esprit.autolocapi.mapper.ReservationMapper;
import tn.esprit.autolocapi.repository.IClientRepository;
import tn.esprit.autolocapi.repository.IContratRepository;
import tn.esprit.autolocapi.repository.IReservationRepository;
import tn.esprit.autolocapi.repository.IVehiculeRepository;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements IReservationService {

    private final IReservationRepository reservationRepository;
    private final IVehiculeRepository vehiculeRepository;
    private final IClientRepository clientRepository;
    private final IContratRepository contratRepository;
    private final ReservationMapper reservationMapper;

    @Override
    public ReservationDTO create(ReservationDTO reservationDTO) {
        if (reservationDTO.getIdReservation() != null) {
            throw new IllegalArgumentException("Une nouvelle réservation ne doit pas avoir d'identifiant");
        }
        verifier(reservationDTO);
        Reservation reservation = reservationMapper.toEntity(reservationDTO);
        if (reservationDTO.getVehiculeId() != null) {
            vehiculeRepository.findById(reservationDTO.getVehiculeId()).ifPresent(reservation::setVehicule);
        }
        if (reservationDTO.getClientId() != null) {
            clientRepository.findById(reservationDTO.getClientId()).ifPresent(reservation::setClient);
        }
        Reservation saved = reservationRepository.save(reservation);
        return reservationMapper.toDto(saved);
    }

    @Override
    public ReservationDTO findById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", id));
        return reservationMapper.toDto(reservation);
    }

    @Override
    public List<ReservationDTO> findAll() {
        return reservationMapper.toDtoList(reservationRepository.findAll());
    }

    @Override
    public ReservationDTO update(Long id, ReservationDTO reservationDTO) {
        Reservation existant = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", id));
        verifier(reservationDTO);
        existant.setDateDebut(reservationDTO.getDateDebut());
        existant.setDateFin(reservationDTO.getDateFin());
        existant.setStatut(reservationDTO.getStatut());
        if (reservationDTO.getVehiculeId() != null) {
            vehiculeRepository.findById(reservationDTO.getVehiculeId()).ifPresent(existant::setVehicule);
        }
        if (reservationDTO.getClientId() != null) {
            clientRepository.findById(reservationDTO.getClientId()).ifPresent(existant::setClient);
        }
        return reservationMapper.toDto(reservationRepository.save(existant));
    }

    @Override
    public void deleteById(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Reservation", id);
        }
        reservationRepository.deleteById(id);
    }

    // =========================================================================
    // Services d'affectation
    // =========================================================================

    /**
     * Affecte un client (trouvé par nom + prénom) à une réservation existante.
     *
     * Relation : Reservation @ManyToOne Client
     * Côté propriétaire (PARENT) : Reservation — c'est elle qui possède la FK idClient.
     * => L'affectation s'effectue en appelant reservation.setClient(client) puis en sauvegardant Reservation.
     */
    @Override
    @Transactional
    public ReservationResponseDTO affecterReservationAClient(Long idReservation, String nom, String prenom) {
        // 1. Récupérer la réservation (parent de la relation ManyToOne)
        Reservation reservation = reservationRepository.findById(idReservation)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", idReservation));

        // 2. Récupérer le client par nom + prénom
        Client client = clientRepository.findByNomIgnoreCaseAndPrenomIgnoreCase(nom, prenom)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Aucun client trouvé avec le nom « " + prenom + " " + nom + " »"));

        // 3. Affecter le client à la réservation (côté propriétaire) et sauvegarder
        reservation.setClient(client);
        Reservation saved = reservationRepository.save(reservation);
        return reservationMapper.toResponseDto(saved);
    }

    /**
     * Crée atomiquement une réservation et son contrat associé.
     *
     * Relation : Contrat @OneToOne Reservation
     * Côté propriétaire (PARENT) : Contrat — c'est lui qui possède la FK idReservation.
     * => L'affectation s'effectue en appelant contrat.setReservation(reservation) puis en sauvegardant Contrat.
     * Le montant total est calculé à partir du tarif journalier du véhicule × nombre de jours.
     */
    @Override
    @Transactional
    public ReservationResponseDTO creerReservationEtContratAssocie(ReservationWithContratRequestDTO requestDTO) {
        if (requestDTO.getDateDebut() != null && requestDTO.getDateFin() != null
                && requestDTO.getDateFin().isBefore(requestDTO.getDateDebut())) {
            throw new IllegalArgumentException("La date de fin est antérieure à la date de début");
        }

        // 1. Création et persistance de la réservation
        Reservation reservation = new Reservation();
        reservation.setDateDebut(requestDTO.getDateDebut());
        reservation.setDateFin(requestDTO.getDateFin());
        reservation.setStatut(requestDTO.getStatut() != null ? requestDTO.getStatut() : StatutReservation.EN_ATTENTE);

        if (requestDTO.getVehiculeId() != null) {
            vehiculeRepository.findById(requestDTO.getVehiculeId())
                    .ifPresent(reservation::setVehicule);
        }
        if (requestDTO.getClientId() != null) {
            clientRepository.findById(requestDTO.getClientId())
                    .ifPresent(reservation::setClient);
        }
        Reservation savedReservation = reservationRepository.save(reservation);

        // 2. Calcul du montant total (tarifJournalier × nbJours)
        BigDecimal montantTotal = BigDecimal.ZERO;
        if (savedReservation.getVehicule() != null
                && savedReservation.getVehicule().getTarifJournalier() != null
                && savedReservation.getDateDebut() != null
                && savedReservation.getDateFin() != null) {
            long nbJours = ChronoUnit.DAYS.between(savedReservation.getDateDebut(), savedReservation.getDateFin());
            if (nbJours < 1) nbJours = 1;
            montantTotal = savedReservation.getVehicule().getTarifJournalier()
                    .multiply(BigDecimal.valueOf(nbJours));
        }

        // 3. Création du contrat et affectation de la réservation
        //    Contrat est le propriétaire de la relation OneToOne => contrat.setReservation(...)
        Contrat contrat = new Contrat();
        contrat.setDateSignature(requestDTO.getDateSignature());
        contrat.setMontantTotal(montantTotal);
        contrat.setValide(false);
        contrat.setReservation(savedReservation);  // affectation côté propriétaire
        contratRepository.save(contrat);

        // 4. Rechargement de la réservation pour inclure le contrat dans la réponse
        Reservation refreshed = reservationRepository.findById(savedReservation.getIdReservation())
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", savedReservation.getIdReservation()));
        return reservationMapper.toResponseDto(refreshed);
    }

    // =========================================================================
    // Méthodes utilitaires privées
    // =========================================================================

    private void verifier(ReservationDTO reservationDTO) {
        if (reservationDTO.getDateDebut() != null && reservationDTO.getDateFin() != null
                && reservationDTO.getDateFin().isBefore(reservationDTO.getDateDebut())) {
            throw new IllegalArgumentException("La date de fin est antérieure à la date de début");
        }
    }
}

