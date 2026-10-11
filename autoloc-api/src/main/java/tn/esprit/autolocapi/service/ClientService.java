
package tn.esprit.autolocapi.service;

import org.springframework.stereotype.Service;
import tn.esprit.autolocapi.domain.Client;
import tn.esprit.autolocapi.repository.IClientRepository;


@Service
public abstract class ClientService extends AbstractCrudService<Client>
        implements IClientService {

   protected ClientService(IClientRepository repository) {
        super(repository);
    }
}
