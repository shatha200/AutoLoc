package tn.esprit.autolocapi.service;

import tn.esprit.autolocapi.dto.ContratDTO;

import java.time.LocalDate;
import java.util.List;

public interface IContratService {
    ContratDTO create(ContratDTO contratDTO);
    ContratDTO findById(Long id);
    List<ContratDTO> findAll();
    ContratDTO update(Long id, ContratDTO contratDTO);
    void deleteById(Long id);

    // --- Services d'affectation ---

    /**
     * Crée un contrat et l'associe à la dernière réservation confirmée non encore liée à un contrat.
     * Le montant total est calculé automatiquement à partir du tarif journalier et de la durée.
     */
    ContratDTO affecterContratAReservation(LocalDate dateSignature, LocalDate dateDebut, LocalDate dateFin);
}
