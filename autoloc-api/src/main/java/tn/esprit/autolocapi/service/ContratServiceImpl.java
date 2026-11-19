package tn.esprit.autolocapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autolocapi.domain.Contrat;
import tn.esprit.autolocapi.domain.Reservation;
import tn.esprit.autolocapi.dto.ContratDTO;
import tn.esprit.autolocapi.exception.ResourceNotFoundException;
import tn.esprit.autolocapi.mapper.ContratMapper;
import tn.esprit.autolocapi.repository.IContratRepository;
import tn.esprit.autolocapi.repository.IReservationRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContratServiceImpl implements IContratService {

    private final IContratRepository contratRepository;
    private final IReservationRepository reservationRepository;
    private final ContratMapper contratMapper;

    @Override
    public ContratDTO create(ContratDTO contratDTO) {
        if (contratDTO.getIdContrat() != null) {
            throw new IllegalArgumentException("Un nouveau contrat ne doit pas avoir d'identifiant");
        }
        verifier(contratDTO);
        Contrat contrat = contratMapper.toEntity(contratDTO);
        if (contratDTO.getReservationId() != null) {
            reservationRepository.findById(contratDTO.getReservationId()).ifPresent(contrat::setReservation);
        }
        Contrat saved = contratRepository.save(contrat);
        return contratMapper.toDto(saved);
    }

    @Override
    public ContratDTO findById(Long id) {
        Contrat contrat = contratRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contrat", id));
        return contratMapper.toDto(contrat);
    }

    @Override
    public List<ContratDTO> findAll() {
        return contratMapper.toDtoList(contratRepository.findAll());
    }

    @Override
    public ContratDTO update(Long id, ContratDTO contratDTO) {
        Contrat existant = contratRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contrat", id));
        verifier(contratDTO);
        existant.setDateSignature(contratDTO.getDateSignature());
        existant.setMontantTotal(contratDTO.getMontantTotal());
        existant.setValide(contratDTO.isValide());
        if (contratDTO.getReservationId() != null) {
            reservationRepository.findById(contratDTO.getReservationId()).ifPresent(existant::setReservation);
        }
        return contratMapper.toDto(contratRepository.save(existant));
    }

    @Override
    public void deleteById(Long id) {
        if (!contratRepository.existsById(id)) {
            throw new ResourceNotFoundException("Contrat", id);
        }
        contratRepository.deleteById(id);
    }

    // =========================================================================
    // Services d'affectation
    // =========================================================================

    /**
     * Crée un contrat et l'affecte à la première réservation confirmée sans contrat.
     * Le montant total est calculé automatiquement (tarifJournalier × nombre de jours).
     *
     * Relation : Contrat @OneToOne Reservation
     * Côté propriétaire (PARENT) : Contrat — c'est lui qui possède @OneToOne Reservation (sans mappedBy).
     * => L'affectation s'effectue en appelant contrat.setReservation(reservation) puis en sauvegardant Contrat.
     *
     * @param dateSignature date de signature du contrat
     * @param dateDebut     date de début de location (pour calcul du montant si différente de la réservation)
     * @param dateFin       date de fin de location
     */
    @Override
    @Transactional
    public ContratDTO affecterContratAReservation(LocalDate dateSignature, LocalDate dateDebut, LocalDate dateFin) {
        if (dateFin != null && dateDebut != null && dateFin.isBefore(dateDebut)) {
            throw new IllegalArgumentException("La date de fin est antérieure à la date de début");
        }

        // Recherche de la première réservation CONFIRMEE sans contrat
        Reservation reservation = contratRepository.findFirstReservationConfirmeeWithoutContrat()
                .orElseThrow(() -> new IllegalStateException(
                        "Aucune réservation confirmée disponible sans contrat associé"));

        // Calcul du montant total
        BigDecimal montantTotal = BigDecimal.ZERO;
        LocalDate debut = dateDebut != null ? dateDebut : reservation.getDateDebut();
        LocalDate fin = dateFin != null ? dateFin : reservation.getDateFin();

        if (reservation.getVehicule() != null
                && reservation.getVehicule().getTarifJournalier() != null
                && debut != null && fin != null) {
            long nbJours = ChronoUnit.DAYS.between(debut, fin);
            if (nbJours < 1) nbJours = 1;
            montantTotal = reservation.getVehicule().getTarifJournalier()
                    .multiply(BigDecimal.valueOf(nbJours));
        }

        // Création du contrat
        Contrat contrat = new Contrat();
        contrat.setDateSignature(dateSignature != null ? dateSignature : LocalDate.now());
        contrat.setMontantTotal(montantTotal);
        contrat.setValide(true);
        contrat.setReservation(reservation);

        Contrat saved = contratRepository.save(contrat);
        return contratMapper.toDto(saved);
    }

    // =========================================================================
    // Méthodes utilitaires privées
    // =========================================================================

    private void verifier(ContratDTO contratDTO) {
        if (contratDTO.getMontantTotal() != null && contratDTO.getMontantTotal().signum() < 0) {
            throw new IllegalArgumentException("Le montant total ne peut pas être négatif");
        }
    }
}

