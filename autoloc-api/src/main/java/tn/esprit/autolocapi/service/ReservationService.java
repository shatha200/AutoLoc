
package tn.esprit.autolocapi.service;

import org.springframework.stereotype.Service;
import tn.esprit.autolocapi.domain.Reservation;
import tn.esprit.autolocapi.repository.IReservationRepository;

@Service
public class ReservationService extends AbstractCrudService<Reservation>
        implements IReservationService {
    public ReservationService(IReservationRepository repository) {
        super(repository);
    }
}