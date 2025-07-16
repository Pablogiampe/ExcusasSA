package ar.edu.davinci.excusas.repository;

import ar.edu.davinci.excusas.model.entities.EmpleadoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmpleadoRepository extends JpaRepository<EmpleadoEntity, Integer> {
    
    Optional<EmpleadoEntity> findByLegajo(Integer legajo);
    
    Optional<EmpleadoEntity> findByEmail(String email);
    
    boolean existsByLegajo(Integer legajo);
    
    boolean existsByEmail(String email);
    
    @Query("SELECT e FROM EmpleadoEntity e WHERE e.nombre LIKE %:nombre%")
    java.util.List<EmpleadoEntity> findByNombreContaining(@Param("nombre") String nombre);
}
