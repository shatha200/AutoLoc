
package tn.esprit.autolocapi.service;

import org.springframework.stereotype.Service;
import tn.esprit.autolocapi.domain.Paiement;
import tn.esprit.autolocapi.repository.IPaiementRepository;

@Service
public class PaiementService extends AbstractCrudService<Paiement>
        implements IPaiementService {
    public PaiementService(IPaiementRepository repository) {
        super(repository);
    }
}
