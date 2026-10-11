package tn.esprit.autolocapi.service;
import java.util.List;
import java.util.Optional;

public interface ICrudService<T> {
    T save(T entity);
    T update(T entity);
    Optional<T> findById(Long id);
    List<T> findAll();
    void deleteById(Long id);
}
