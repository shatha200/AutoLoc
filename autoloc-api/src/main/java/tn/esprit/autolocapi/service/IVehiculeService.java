package tn.esprit.autolocapi.service;

import tn.esprit.autolocapi.dto.VehiculeDTO;

import java.util.List;

public interface IVehiculeService {
    VehiculeDTO create(VehiculeDTO vehiculeDTO);
    VehiculeDTO findById(Long id);
    List<VehiculeDTO> findAll();
    VehiculeDTO update(Long id, VehiculeDTO vehiculeDTO);
    void deleteById(Long id);
}
