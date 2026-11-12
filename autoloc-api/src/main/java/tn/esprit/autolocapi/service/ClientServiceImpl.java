package tn.esprit.autolocapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.autolocapi.domain.Client;
import tn.esprit.autolocapi.dto.ClientDTO;
import tn.esprit.autolocapi.exception.ResourceNotFoundException;
import tn.esprit.autolocapi.mapper.ClientMapper;
import tn.esprit.autolocapi.repository.IClientRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements IClientService {

    private final IClientRepository clientRepository;
    private final ClientMapper clientMapper;

    @Override
    public ClientDTO create(ClientDTO clientDTO) {
        if (clientDTO.getIdClient() != null) {
            throw new IllegalArgumentException("Un nouveau client ne doit pas avoir d'identifiant");
        }
        verifier(clientDTO);
        Client client = clientMapper.toEntity(clientDTO);
        Client saved = clientRepository.save(client);
        return clientMapper.toDto(saved);
    }

    @Override
    public ClientDTO findById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client", id));
        return clientMapper.toDto(client);
    }

    @Override
    public List<ClientDTO> findAll() {
        return clientMapper.toDtoList(clientRepository.findAll());
    }

    @Override
    public ClientDTO update(Long id, ClientDTO clientDTO) {
        Client existant = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client", id));
        verifier(clientDTO);
        existant.setNom(clientDTO.getNom());
        existant.setPrenom(clientDTO.getPrenom());
        existant.setEmail(clientDTO.getEmail());
        existant.setTelephone(clientDTO.getTelephone());
        existant.setNumPermis(clientDTO.getNumPermis());
        existant.setDateInscription(clientDTO.getDateInscription());
        return clientMapper.toDto(clientRepository.save(existant));
    }

    @Override
    public void deleteById(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new ResourceNotFoundException("Client", id);
        }
        clientRepository.deleteById(id);
    }

    private void verifier(ClientDTO clientDTO) {
        if (clientDTO.getNom() == null || clientDTO.getNom().isBlank()) {
            throw new IllegalArgumentException("Le nom du client est obligatoire");
        }
        if (clientDTO.getEmail() == null || clientDTO.getEmail().isBlank()) {
            throw new IllegalArgumentException("L'e-mail du client est obligatoire");
        }
    }
}
