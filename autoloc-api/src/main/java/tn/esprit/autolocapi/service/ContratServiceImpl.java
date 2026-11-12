package tn.esprit.autolocapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.autolocapi.domain.Contrat;
import tn.esprit.autolocapi.dto.ContratDTO;
import tn.esprit.autolocapi.exception.ResourceNotFoundException;
import tn.esprit.autolocapi.mapper.ContratMapper;
import tn.esprit.autolocapi.repository.IContratRepository;
import tn.esprit.autolocapi.repository.IReservationRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContratServiceImpl implements IContratService {

    private final IContratRepository contratRepository;
    private final IReservationRepository reservationRepository;
    private final ContratMapper contratMapper;

    @Override
    public ContratDTO create(ContratDTO contratDTO) {
        if (contratDTO.getIdContrat() != null) {
            throw new IllegalArgumentException("Un nouveau contrat ne doit pas avoir d'identifiant");
        }
        verifier(contratDTO);
        Contrat contrat = contratMapper.toEntity(contratDTO);
        if (contratDTO.getReservationId() != null) {
            reservationRepository.findById(contratDTO.getReservationId()).ifPresent(contrat::setReservation);
        }
        Contrat saved = contratRepository.save(contrat);
        return contratMapper.toDto(saved);
    }

    @Override
    public ContratDTO findById(Long id) {
        Contrat contrat = contratRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contrat", id));
        return contratMapper.toDto(contrat);
    }

    @Override
    public List<ContratDTO> findAll() {
        return contratMapper.toDtoList(contratRepository.findAll());
    }

    @Override
    public ContratDTO update(Long id, ContratDTO contratDTO) {
        Contrat existant = contratRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contrat", id));
        verifier(contratDTO);
        existant.setDateSignature(contratDTO.getDateSignature());
        existant.setMontantTotal(contratDTO.getMontantTotal());
        existant.setValide(contratDTO.isValide());
        if (contratDTO.getReservationId() != null) {
            reservationRepository.findById(contratDTO.getReservationId()).ifPresent(existant::setReservation);
        }
        return contratMapper.toDto(contratRepository.save(existant));
    }

    @Override
    public void deleteById(Long id) {
        if (!contratRepository.existsById(id)) {
            throw new ResourceNotFoundException("Contrat", id);
        }
        contratRepository.deleteById(id);
    }

    private void verifier(ContratDTO contratDTO) {
        if (contratDTO.getMontantTotal() != null && contratDTO.getMontantTotal().signum() < 0) {
            throw new IllegalArgumentException("Le montant total ne peut pas être négatif");
        }
    }
}
