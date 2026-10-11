
package tn.esprit.autolocapi.service;

import org.springframework.stereotype.Service;
import tn.esprit.autolocapi.domain.Vehicule;
import tn.esprit.autolocapi.repository.IVehiculeRepository;

@Service
public class VehiculeService extends AbstractCrudService<Vehicule>
        implements IVehiculeService {
    public VehiculeService(IVehiculeRepository repository) {
        super(repository);
    }
}
