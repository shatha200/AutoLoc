
package tn.esprit.autolocapi.service;

import org.springframework.stereotype.Service;
import tn.esprit.autolocapi.domain.Agence;
import tn.esprit.autolocapi.repository.IAgenceRepository;


@Service
public class AgenceService extends AbstractCrudService<Agence>
        implements IAgenceService {

    public AgenceService(IAgenceRepository repository) {
        super(repository);
    }
}
