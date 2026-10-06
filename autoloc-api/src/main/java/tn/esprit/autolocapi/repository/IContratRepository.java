package tn.esprit.autolocapi.repository;
import org.springframework.data.repository.CrudRepository;
import tn.esprit.autolocapi.domain.Contrat;

public interface IContratRepository  extends CrudRepository<Contrat,Long>{
}
