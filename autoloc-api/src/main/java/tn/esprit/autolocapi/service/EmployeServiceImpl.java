package tn.esprit.autolocapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.autolocapi.domain.Employe;
import tn.esprit.autolocapi.dto.EmployeDTO;
import tn.esprit.autolocapi.exception.ResourceNotFoundException;
import tn.esprit.autolocapi.mapper.EmployeMapper;
import tn.esprit.autolocapi.repository.IAgenceRepository;
import tn.esprit.autolocapi.repository.IEmployeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeServiceImpl implements IEmployeService {

    private final IEmployeRepository employeRepository;
    private final IAgenceRepository agenceRepository;
    private final EmployeMapper employeMapper;

    @Override
    public EmployeDTO create(EmployeDTO employeDTO) {
        if (employeDTO.getIdEmploye() != null) {
            throw new IllegalArgumentException("Un nouvel employé ne doit pas avoir d'identifiant");
        }
        verifier(employeDTO);
        Employe employe = employeMapper.toEntity(employeDTO);
        if (employeDTO.getAgenceId() != null) {
            agenceRepository.findById(employeDTO.getAgenceId()).ifPresent(employe::setAgence);
        }
        Employe saved = employeRepository.save(employe);
        return employeMapper.toDto(saved);
    }

    @Override
    public EmployeDTO findById(Long id) {
        Employe employe = employeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employe", id));
        return employeMapper.toDto(employe);
    }

    @Override
    public List<EmployeDTO> findAll() {
        return employeMapper.toDtoList(employeRepository.findAll());
    }

    @Override
    public EmployeDTO update(Long id, EmployeDTO employeDTO) {
        Employe existant = employeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employe", id));
        verifier(employeDTO);
        existant.setNom(employeDTO.getNom());
        existant.setPrenom(employeDTO.getPrenom());
        existant.setRole(employeDTO.getRole());
        if (employeDTO.getAgenceId() != null) {
            agenceRepository.findById(employeDTO.getAgenceId()).ifPresent(existant::setAgence);
        }
        return employeMapper.toDto(employeRepository.save(existant));
    }

    @Override
    public void deleteById(Long id) {
        if (!employeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Employe", id);
        }
        employeRepository.deleteById(id);
    }

    private void verifier(EmployeDTO employeDTO) {
        if (employeDTO.getNom() == null || employeDTO.getNom().isBlank()) {
            throw new IllegalArgumentException("Le nom de l'employé est obligatoire");
        }
        if (employeDTO.getRole() == null) {
            throw new IllegalArgumentException("Le rôle de l'employé est obligatoire");
        }
    }
}
