
package tn.esprit.autolocapi.service;

import org.springframework.stereotype.Service;
import tn.esprit.autolocapi.domain.Employe;
import tn.esprit.autolocapi.repository.IEmployeRepository;

@Service
public class EmployeService extends AbstractCrudService<Employe>
        implements IEmployeService {
    public EmployeService(IEmployeRepository repository) {
        super(repository);
    }
}
