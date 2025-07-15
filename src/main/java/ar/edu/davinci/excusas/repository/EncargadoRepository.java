package ar.edu.davinci.excusas.repository;

import ar.edu.davinci.excusas.model.entities.EncargadoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EncargadoRepository extends JpaRepository<EncargadoEntity, Integer> {
    
    Optional<EncargadoEntity> findByEmail(String email);
    
    boolean existsByLegajo(Integer legajo);
    
    boolean existsByEmail(String email);
}
