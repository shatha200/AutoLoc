package tn.esprit.autolocapi.service;

import tn.esprit.autolocapi.dto.ContratDTO;

import java.util.List;

public interface IContratService {
    ContratDTO create(ContratDTO contratDTO);
    ContratDTO findById(Long id);
    List<ContratDTO> findAll();
    ContratDTO update(Long id, ContratDTO contratDTO);
    void deleteById(Long id);
}
