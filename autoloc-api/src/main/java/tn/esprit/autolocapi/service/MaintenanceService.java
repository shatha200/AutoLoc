
package tn.esprit.autolocapi.service;

import org.springframework.stereotype.Service;
import tn.esprit.autolocapi.domain.Maintenance;
import tn.esprit.autolocapi.repository.IMaintenanceRepository;

@Service
public class MaintenanceService extends AbstractCrudService<Maintenance>
        implements IMaintenanceService {
    public MaintenanceService(IMaintenanceRepository repository) {
        super(repository);
    }
}
