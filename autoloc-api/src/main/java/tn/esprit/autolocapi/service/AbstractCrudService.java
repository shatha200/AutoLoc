package tn.esprit.autolocapi.service;


import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

    public abstract class AbstractCrudService<T>
            implements ICrudService<T> {

        protected final JpaRepository<T, Long> repository;

        protected AbstractCrudService(JpaRepository<T, Long> repository) {
            this.repository = repository;
        }

        @Override
        public T save(T entity) {
            return repository.save(entity);
        }

        @Override
        public T update(T entity) {
            return repository.save(entity);
        }

        @Override
        public Optional<T> findById(Long id) {
            return repository.findById(id);
        }

        @Override
        public List<T> findAll() {
            return repository.findAll();
        }

        @Override
        public void deleteById(Long id) {
            repository.deleteById(id);
        }
    }


