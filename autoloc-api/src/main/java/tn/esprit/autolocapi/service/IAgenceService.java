package tn.esprit.autolocapi.service;

import tn.esprit.autolocapi.dto.AgenceDTO;

import java.util.List;

public interface IAgenceService {
    AgenceDTO create(AgenceDTO agenceDTO);
    AgenceDTO findById(Long id);
    List<AgenceDTO> findAll();
    AgenceDTO update(Long id, AgenceDTO agenceDTO);
    void deleteById(Long id);
}
