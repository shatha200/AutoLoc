package tn.esprit.autolocapi.service;

import tn.esprit.autolocapi.dto.EquipementDTO;

import java.util.List;

public interface IEquipementService {
    EquipementDTO create(EquipementDTO equipementDTO);
    EquipementDTO findById(Long id);
    List<EquipementDTO> findAll();
    EquipementDTO update(Long id, EquipementDTO equipementDTO);
    void deleteById(Long id);
}
