package ar.edu.davinci.excusas.repository;

import ar.edu.davinci.excusas.model.entities.EncargadoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EncargadoRepository extends JpaRepository<EncargadoEntity, Integer> {
    
    Optional<EncargadoEntity> findByLegajo(Integer legajo);
    
    Optional<EncargadoEntity> findByEmail(String email);
    
    List<EncargadoEntity> findByTipoEncargado(String tipoEncargado);
    
    List<EncargadoEntity> findByModo(String modo);
    
    boolean existsByLegajo(Integer legajo);
    
    boolean existsByEmail(String email);
    
    @Query("SELECT e FROM EncargadoEntity e ORDER BY " +
           "CASE e.tipoEncargado " +
           "WHEN 'Recepcionista' THEN 1 " +
           "WHEN 'SupervisorArea' THEN 2 " +
           "WHEN 'GerenteRRHH' THEN 3 " +
           "WHEN 'CEO' THEN 4 " +
           "ELSE 5 END")
    List<EncargadoEntity> findAllOrderedByHierarchy();
    
    @Query("SELECT e FROM EncargadoEntity e WHERE e.nombre LIKE %:nombre%")
    List<EncargadoEntity> findByNombreContaining(@Param("nombre") String nombre);
}
