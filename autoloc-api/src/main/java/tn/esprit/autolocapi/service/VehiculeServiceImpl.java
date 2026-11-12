package tn.esprit.autolocapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.autolocapi.domain.Vehicule;
import tn.esprit.autolocapi.dto.VehiculeDTO;
import tn.esprit.autolocapi.exception.ResourceNotFoundException;
import tn.esprit.autolocapi.mapper.VehiculeMapper;
import tn.esprit.autolocapi.repository.IAgenceRepository;
import tn.esprit.autolocapi.repository.IVehiculeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehiculeServiceImpl implements IVehiculeService {

    private final IVehiculeRepository vehiculeRepository;
    private final IAgenceRepository agenceRepository;
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

    private void verifier(VehiculeDTO vehiculeDTO) {
        if (vehiculeDTO.getTarifJournalier() != null && vehiculeDTO.getTarifJournalier().signum() < 0) {
            throw new IllegalArgumentException("Le tarif journalier ne peut pas être négatif");
        }
    }
}
