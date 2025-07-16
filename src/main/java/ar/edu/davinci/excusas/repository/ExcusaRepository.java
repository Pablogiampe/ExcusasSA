package ar.edu.davinci.excusas.repository;

import ar.edu.davinci.excusas.model.entities.ExcusaEntity;
import ar.edu.davinci.excusas.model.excusas.MotivoExcusa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ExcusaRepository extends JpaRepository<ExcusaEntity, Long> {
    
    List<ExcusaEntity> findByEmpleadoLegajo(Integer legajo);
    
    List<ExcusaEntity> findByAceptada(Boolean aceptada);
    
    List<ExcusaEntity> findByMotivo(MotivoExcusa motivo);
    
    List<ExcusaEntity> findByEncargadoQueManejo(String encargado);
    
    @Query("SELECT e FROM ExcusaEntity e WHERE e.fechaCreacion BETWEEN :fechaDesde AND :fechaHasta")
    List<ExcusaEntity> findByFechaCreacionBetween(@Param("fechaDesde") LocalDateTime fechaDesde, 
                                                  @Param("fechaHasta") LocalDateTime fechaHasta);
    
    @Query("SELECT e FROM ExcusaEntity e WHERE e.empleado.legajo = :legajo AND e.fechaCreacion BETWEEN :fechaDesde AND :fechaHasta")
    List<ExcusaEntity> findByEmpleadoLegajoAndFechaCreacionBetween(@Param("legajo") Integer legajo,
                                                                   @Param("fechaDesde") LocalDateTime fechaDesde,
                                                                   @Param("fechaHasta") LocalDateTime fechaHasta);
    
    @Modifying
    @Query("DELETE FROM ExcusaEntity e WHERE e.fechaCreacion < :fechaLimite")
    int deleteByFechaCreacionBefore(@Param("fechaLimite") LocalDateTime fechaLimite);
    
    @Query("SELECT COUNT(e) FROM ExcusaEntity e WHERE e.fechaCreacion < :fechaLimite")
    long countByFechaCreacionBefore(@Param("fechaLimite") LocalDateTime fechaLimite);
}
