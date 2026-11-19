package tn.esprit.autolocapi.service;

import tn.esprit.autolocapi.dto.VehiculeDTO;

import java.util.List;

public interface IVehiculeService {
    VehiculeDTO create(VehiculeDTO vehiculeDTO);
    VehiculeDTO findById(Long id);
    List<VehiculeDTO> findAll();
    VehiculeDTO update(Long id, VehiculeDTO vehiculeDTO);
    void deleteById(Long id);

    // --- Services d'affectation ---

    /** Ajoute un équipement à la liste des équipements d'un véhicule */
    VehiculeDTO affecterVehiculeAEquipement(Long idVehicule, Long idEquipement);

    /** Crée une maintenance pour un véhicule identifié par son immatriculation et passe son statut à MAINTENANCE */
    VehiculeDTO affecterMaintenanceAvehicule(String description, String immatriculation);
}
