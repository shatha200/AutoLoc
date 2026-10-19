package tn.esprit.autolocapi.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import tn.esprit.autolocapi.domain.Contrat;

public interface IContratRepository  extends CrudRepository<Contrat,Long>, JpaRepository<Contrat, Long> {
}
