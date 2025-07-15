package ar.edu.davinci.excusas.repository;

import ar.edu.davinci.excusas.model.entities.ProntuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProntuarioRepository extends JpaRepository<ProntuarioEntity, Long> {
    
    List<ProntuarioEntity> findByEmpleadoLegajo(Integer legajo);
    
    void deleteAll();
}
