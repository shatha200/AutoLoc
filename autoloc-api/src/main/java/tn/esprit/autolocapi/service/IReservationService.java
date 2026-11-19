package tn.esprit.autolocapi.service;

import tn.esprit.autolocapi.dto.ReservationDTO;
import tn.esprit.autolocapi.dto.ReservationResponseDTO;
import tn.esprit.autolocapi.dto.ReservationWithContratRequestDTO;

import java.util.List;

public interface IReservationService {
    ReservationDTO create(ReservationDTO reservationDTO);
    ReservationDTO findById(Long id);
    List<ReservationDTO> findAll();
    ReservationDTO update(Long id, ReservationDTO reservationDTO);
    void deleteById(Long id);

    // --- Services d'affectation ---

    /** Affecte un client (trouvé par nom+prénom) à une réservation existante */
    ReservationResponseDTO affecterReservationAClient(Long idReservation, String nom, String prenom);

    /** Crée une réservation et son contrat associé en une seule opération atomique */
    ReservationResponseDTO creerReservationEtContratAssocie(ReservationWithContratRequestDTO requestDTO);
}
