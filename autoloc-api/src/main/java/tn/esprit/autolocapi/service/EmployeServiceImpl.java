
package tn.esprit.autolocapi.service;

import org.springframework.stereotype.Service;
import tn.esprit.autolocapi.domain.Employe;
import tn.esprit.autolocapi.repository.IEmployeRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;

import tn.esprit.autolocapi.exception.ResourceNotFoundException;


@Service
@RequiredArgsConstructor
public class EmployeServiceImpl implements IEmployeService {

    private final IEmployeRepository employeRepository;

    @Override
    public Employe create(Employe employe) {
        if (employe.getId() != null) {
            throw new IllegalArgumentException(
                    "Un nouvel élément ne doit pas avoir d'identifiant");
        }
        verifier(employe);
        return employeRepository.save(employe);
    }

    @Override
    public Employe findById(Long id) {
        return employeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employe", id));
    }

    @Override
    public List<Employe> findAll() {
        return employeRepository.findAll();
    }

    @Override
    public Employe update(Long id, Employe employe) {
        Employe existant = findById(id);
        verifier(employe);
        existant.setNom(employe.getNom());
        existant.setRole(employe.getPoste());
        return employeRepository.save(existant);
    }

    @Override
    public void deleteById(Long id) {
        if (!employeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Employe", id);
        }
        employeRepository.deleteById(id);
    }

    private void verifier(Employe employe) {
        if (employe.getNom() == null || employe.getNom().isBlank()) {
            throw new IllegalArgumentException("nom obligatoire");
        }
        if (employe.getPoste() == null ) {
            throw new IllegalArgumentException("poste obligatoire");
        }
    }
}
