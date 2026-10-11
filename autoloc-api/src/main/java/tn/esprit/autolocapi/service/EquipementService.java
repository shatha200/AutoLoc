
package tn.esprit.autolocapi.service;

import org.springframework.stereotype.Service;
import tn.esprit.autolocapi.domain.Equipement;
import tn.esprit.autolocapi.repository.IEquipementRepository;

@Service
public class EquipementService extends AbstractCrudService<Equipement>
        implements IEquipementService {
    public EquipementService(IEquipementRepository repository) {
        super(repository);
    }
}
