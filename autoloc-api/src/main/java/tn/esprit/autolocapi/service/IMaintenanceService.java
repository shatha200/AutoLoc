package tn.esprit.autolocapi.service;

import tn.esprit.autolocapi.dto.MaintenanceDTO;

import java.util.List;

public interface IMaintenanceService {
    MaintenanceDTO create(MaintenanceDTO maintenanceDTO);
    MaintenanceDTO findById(Long id);
    List<MaintenanceDTO> findAll();
    MaintenanceDTO update(Long id, MaintenanceDTO maintenanceDTO);
    void deleteById(Long id);
}
