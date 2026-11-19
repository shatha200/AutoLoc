package tn.esprit.autolocapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autolocapi.domain.Equipement;
import tn.esprit.autolocapi.domain.Maintenance;
import tn.esprit.autolocapi.domain.StatutVehicule;
import tn.esprit.autolocapi.domain.Vehicule;
import tn.esprit.autolocapi.dto.VehiculeDTO;
import tn.esprit.autolocapi.exception.ResourceNotFoundException;
import tn.esprit.autolocapi.mapper.VehiculeMapper;
import tn.esprit.autolocapi.repository.IAgenceRepository;
import tn.esprit.autolocapi.repository.IEquipementRepository;
import tn.esprit.autolocapi.repository.IMaintenanceRepository;
import tn.esprit.autolocapi.repository.IVehiculeRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VehiculeServiceImpl implements IVehiculeService {

    private final IVehiculeRepository vehiculeRepository;
    private final IAgenceRepository agenceRepository;
    private final IEquipementRepository equipementRepository;
    private final IMaintenanceRepository maintenanceRepository;
    private final VehiculeMapper vehiculeMapper;

    @Override
    public VehiculeDTO create(VehiculeDTO vehiculeDTO) {
        if (vehiculeDTO.getIdVehicule() != null) {
            throw new IllegalArgumentException("Un nouveau véhicule ne doit pas avoir d'identifiant");
        }
        verifier(vehiculeDTO);
        Vehicule vehicule = vehiculeMapper.toEntity(vehiculeDTO);
        if (vehiculeDTO.getAgenceId() != null) {
            agenceRepository.findById(vehiculeDTO.getAgenceId()).ifPresent(vehicule::setAgence);
        }
        Vehicule saved = vehiculeRepository.save(vehicule);
        return vehiculeMapper.toDto(saved);
    }

    @Override
    public VehiculeDTO findById(Long id) {
        Vehicule vehicule = vehiculeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicule", id));
        return vehiculeMapper.toDto(vehicule);
    }

    @Override
    public List<VehiculeDTO> findAll() {
        return vehiculeMapper.toDtoList(vehiculeRepository.findAll());
    }

    @Override
    public VehiculeDTO update(Long id, VehiculeDTO vehiculeDTO) {
        Vehicule existant = vehiculeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicule", id));
        verifier(vehiculeDTO);
        existant.setImmatriculation(vehiculeDTO.getImmatriculation());
        existant.setMarque(vehiculeDTO.getMarque());
        existant.setModele(vehiculeDTO.getModele());
        existant.setCategorie(vehiculeDTO.getCategorie());
        existant.setTarifJournalier(vehiculeDTO.getTarifJournalier());
        existant.setStatut(vehiculeDTO.getStatut());
        if (vehiculeDTO.getAgenceId() != null) {
            agenceRepository.findById(vehiculeDTO.getAgenceId()).ifPresent(existant::setAgence);
        }
        return vehiculeMapper.toDto(vehiculeRepository.save(existant));
    }

    @Override
    public void deleteById(Long id) {
        if (!vehiculeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Vehicule", id);
        }
        vehiculeRepository.deleteById(id);
    }

    // =========================================================================
    // Services d'affectation
    // =========================================================================

    /**
     * Associe un équipement existant à un véhicule existant.
     *
     * Relation : Vehicule @ManyToMany Equipement
     * Côté propriétaire (PARENT) : Vehicule — c'est lui qui possède @ManyToMany sans mappedBy.
     * => L'affectation s'effectue en ajoutant l'équipement dans vehicule.getEquipements() puis en sauvegardant Vehicule.
     */
    @Override
    @Transactional
    public VehiculeDTO affecterVehiculeAEquipement(Long idVehicule, Long idEquipement) {
        // 1. Récupérer le véhicule (parent de la relation ManyToMany)
        Vehicule vehicule = vehiculeRepository.findById(idVehicule)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicule", idVehicule));

        // 2. Récupérer l'équipement
        Equipement equipement = equipementRepository.findById(idEquipement)
                .orElseThrow(() -> new ResourceNotFoundException("Equipement", idEquipement));

        // 3. Vérifier que l'équipement n'est pas déjà associé
        List<Equipement> equipements = vehicule.getEquipements();
        boolean dejaAssocie = equipements.stream()
                .anyMatch(e -> e.getIdEquipement().equals(idEquipement));
        if (dejaAssocie) {
            throw new IllegalStateException(
                    "L'équipement « " + equipement.getLibelle() + " » est déjà associé à ce véhicule");
        }

        // 4. Affecter l'équipement au véhicule (côté propriétaire) et sauvegarder
        equipements.add(equipement);
        vehicule.setEquipements(equipements);
        Vehicule saved = vehiculeRepository.save(vehicule);
        return vehiculeMapper.toDto(saved);
    }

    /**
     * Crée une maintenance pour un véhicule identifié par son immatriculation
     * et fait passer son statut à MAINTENANCE.
     *
     * Relation : Maintenance @ManyToOne Vehicule
     * Côté propriétaire (PARENT) : Maintenance — c'est elle qui possède @ManyToOne Vehicule (la FK idVehicule).
     * => L'affectation s'effectue en appelant maintenance.setVehicule(vehicule) puis en sauvegardant Maintenance.
     * On met également à jour le statut du véhicule.
     */
    @Override
    @Transactional
    public VehiculeDTO affecterMaintenanceAvehicule(String description, String immatriculation) {
        // 1. Récupérer le véhicule par immatriculation
        Vehicule vehicule = vehiculeRepository.findByImmatriculationIgnoreCase(immatriculation)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Aucun véhicule trouvé avec l'immatriculation « " + immatriculation + " »"));

        // 2. Créer la maintenance et affecter le véhicule (côté propriétaire = Maintenance)
        Maintenance maintenance = new Maintenance();
        maintenance.setDescription(description);
        maintenance.setDateDebut(LocalDate.now());
        maintenance.setDateFin(LocalDate.now().plusDays(1));  // date de fin provisoire
        maintenance.setVehicule(vehicule);  // affectation côté propriétaire
        maintenanceRepository.save(maintenance);

        // 3. Mettre à jour le statut du véhicule
        vehicule.setStatut(StatutVehicule.MAINTENANCE);
        Vehicule saved = vehiculeRepository.save(vehicule);
        return vehiculeMapper.toDto(saved);
    }

    // =========================================================================
    // Méthodes utilitaires privées
    // =========================================================================

    private void verifier(VehiculeDTO vehiculeDTO) {
        if (vehiculeDTO.getTarifJournalier() != null && vehiculeDTO.getTarifJournalier().signum() < 0) {
            throw new IllegalArgumentException("Le tarif journalier ne peut pas être négatif");
        }
    }
}

