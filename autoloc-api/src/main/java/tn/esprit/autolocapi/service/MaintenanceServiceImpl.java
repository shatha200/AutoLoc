package tn.esprit.autolocapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.autolocapi.domain.Maintenance;
import tn.esprit.autolocapi.dto.MaintenanceDTO;
import tn.esprit.autolocapi.exception.ResourceNotFoundException;
import tn.esprit.autolocapi.mapper.MaintenanceMapper;
import tn.esprit.autolocapi.repository.IMaintenanceRepository;
import tn.esprit.autolocapi.repository.IVehiculeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MaintenanceServiceImpl implements IMaintenanceService {

    private final IMaintenanceRepository maintenanceRepository;
    private final IVehiculeRepository vehiculeRepository;
    private final MaintenanceMapper maintenanceMapper;

    @Override
    public MaintenanceDTO create(MaintenanceDTO maintenanceDTO) {
        if (maintenanceDTO.getIdMaintenance() != null) {
            throw new IllegalArgumentException("Une nouvelle maintenance ne doit pas avoir d'identifiant");
        }
        verifier(maintenanceDTO);
        Maintenance maintenance = maintenanceMapper.toEntity(maintenanceDTO);
        if (maintenanceDTO.getVehiculeId() != null) {
            vehiculeRepository.findById(maintenanceDTO.getVehiculeId()).ifPresent(maintenance::setVehicule);
        }
        Maintenance saved = maintenanceRepository.save(maintenance);
        return maintenanceMapper.toDto(saved);
    }

    @Override
    public MaintenanceDTO findById(Long id) {
        Maintenance maintenance = maintenanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Maintenance", id));
        return maintenanceMapper.toDto(maintenance);
    }

    @Override
    public List<MaintenanceDTO> findAll() {
        return maintenanceMapper.toDtoList(maintenanceRepository.findAll());
    }

    @Override
    public MaintenanceDTO update(Long id, MaintenanceDTO maintenanceDTO) {
        Maintenance existant = maintenanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Maintenance", id));
        verifier(maintenanceDTO);
        existant.setDateDebut(maintenanceDTO.getDateDebut());
        existant.setDateFin(maintenanceDTO.getDateFin());
        existant.setDescription(maintenanceDTO.getDescription());
        if (maintenanceDTO.getVehiculeId() != null) {
            vehiculeRepository.findById(maintenanceDTO.getVehiculeId()).ifPresent(existant::setVehicule);
        }
        return maintenanceMapper.toDto(maintenanceRepository.save(existant));
    }

    @Override
    public void deleteById(Long id) {
        if (!maintenanceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Maintenance", id);
        }
        maintenanceRepository.deleteById(id);
    }

    private void verifier(MaintenanceDTO maintenanceDTO) {
        if (maintenanceDTO.getDateDebut() != null && maintenanceDTO.getDateFin() != null
                && maintenanceDTO.getDateFin().isBefore(maintenanceDTO.getDateDebut())) {
            throw new IllegalArgumentException("La date de fin est antérieure à la date de début");
        }
    }
}
