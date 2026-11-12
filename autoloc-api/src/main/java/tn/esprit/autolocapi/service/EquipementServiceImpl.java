package tn.esprit.autolocapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.autolocapi.domain.Equipement;
import tn.esprit.autolocapi.dto.EquipementDTO;
import tn.esprit.autolocapi.exception.ResourceNotFoundException;
import tn.esprit.autolocapi.mapper.EquipementMapper;
import tn.esprit.autolocapi.repository.IEquipementRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipementServiceImpl implements IEquipementService {

    private final IEquipementRepository equipementRepository;
    private final EquipementMapper equipementMapper;

    @Override
    public EquipementDTO create(EquipementDTO equipementDTO) {
        if (equipementDTO.getIdEquipement() != null) {
            throw new IllegalArgumentException("Un nouvel équipement ne doit pas avoir d'identifiant");
        }
        verifier(equipementDTO);
        Equipement equipement = equipementMapper.toEntity(equipementDTO);
        Equipement saved = equipementRepository.save(equipement);
        return equipementMapper.toDto(saved);
    }

    @Override
    public EquipementDTO findById(Long id) {
        Equipement equipement = equipementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipement", id));
        return equipementMapper.toDto(equipement);
    }

    @Override
    public List<EquipementDTO> findAll() {
        return equipementMapper.toDtoList(equipementRepository.findAll());
    }

    @Override
    public EquipementDTO update(Long id, EquipementDTO equipementDTO) {
        Equipement existant = equipementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipement", id));
        verifier(equipementDTO);
        existant.setLibelle(equipementDTO.getLibelle());
        return equipementMapper.toDto(equipementRepository.save(existant));
    }

    @Override
    public void deleteById(Long id) {
        if (!equipementRepository.existsById(id)) {
            throw new ResourceNotFoundException("Equipement", id);
        }
        equipementRepository.deleteById(id);
    }

    private void verifier(EquipementDTO equipementDTO) {
        if (equipementDTO.getLibelle() == null || equipementDTO.getLibelle().isBlank()) {
            throw new IllegalArgumentException("Le libellé de l'équipement est obligatoire");
        }
    }
}
