package tn.esprit.autolocapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.autolocapi.domain.Agence;
import tn.esprit.autolocapi.dto.AgenceDTO;
import tn.esprit.autolocapi.exception.ResourceNotFoundException;
import tn.esprit.autolocapi.mapper.AgenceMapper;
import tn.esprit.autolocapi.repository.IAgenceRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AgenceServiceImpl implements IAgenceService {

    private final IAgenceRepository agenceRepository;
    private final AgenceMapper agenceMapper;

    @Override
    public AgenceDTO create(AgenceDTO agenceDTO) {
        if (agenceDTO.getIdAgence() != null) {
            throw new IllegalArgumentException("Une nouvelle agence ne doit pas avoir d'identifiant");
        }
        verifier(agenceDTO);
        Agence agence = agenceMapper.toEntity(agenceDTO);
        Agence saved = agenceRepository.save(agence);
        return agenceMapper.toDto(saved);
    }

    @Override
    public AgenceDTO findById(Long id) {
        Agence agence = agenceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agence", id));
        return agenceMapper.toDto(agence);
    }

    @Override
    public List<AgenceDTO> findAll() {
        return agenceMapper.toDtoList(agenceRepository.findAll());
    }

    @Override
    public AgenceDTO update(Long id, AgenceDTO agenceDTO) {
        Agence existant = agenceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agence", id));
        verifier(agenceDTO);
        existant.setNom(agenceDTO.getNom());
        existant.setVille(agenceDTO.getVille());
        existant.setAdresse(agenceDTO.getAdresse());
        existant.setTelephone(agenceDTO.getTelephone());
        return agenceMapper.toDto(agenceRepository.save(existant));
    }

    @Override
    public void deleteById(Long id) {
        if (!agenceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Agence", id);
        }
        agenceRepository.deleteById(id);
    }

    private void verifier(AgenceDTO agenceDTO) {
        if (agenceDTO.getNom() == null || agenceDTO.getNom().isBlank()) {
            throw new IllegalArgumentException("Le nom de l'agence est obligatoire");
        }
    }
}
