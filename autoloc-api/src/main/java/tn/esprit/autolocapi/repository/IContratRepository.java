package tn.esprit.autolocapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tn.esprit.autolocapi.domain.Contrat;
import tn.esprit.autolocapi.domain.Reservation;

import java.util.Optional;

public interface IContratRepository extends JpaRepository<Contrat, Long> {

    boolean existsByReservation(Reservation reservation);

    /** Retourne la première réservation confirmée sans contrat associé */
    @Query("SELECT r FROM Reservation r WHERE r.statut = 'CONFIRMEE' AND r.contrat IS NULL ORDER BY r.idReservation ASC")
    Optional<Reservation> findFirstReservationConfirmeeWithoutContrat();
}
