package ar.edu.davinci.excusas.repository;

import ar.edu.davinci.excusas.model.entities.ExcusaEntity;
import ar.edu.davinci.excusas.model.excusas.MotivoExcusa;
import org.springframework.data.jpa.repository.JpaRepository;
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
    
    @Query("SELECT e FROM ExcusaEntity e WHERE " +
           "(:fechaDesde IS NULL OR e.fechaCreacion >= :fechaDesde) AND " +
           "(:fechaHasta IS NULL OR e.fechaCreacion <= :fechaHasta) AND " +
           "(:motivo IS NULL OR e.motivo = :motivo)")
    List<ExcusaEntity> findByFiltros(@Param("fechaDesde") LocalDateTime fechaDesde,
                                     @Param("fechaHasta") LocalDateTime fechaHasta,
                                     @Param("motivo") MotivoExcusa motivo);
    
    @Query("SELECT e FROM ExcusaEntity e WHERE e.empleado.legajo = :legajo AND " +
           "(:fechaDesde IS NULL OR e.fechaCreacion >= :fechaDesde) AND " +
           "(:fechaHasta IS NULL OR e.fechaCreacion <= :fechaHasta)")
    List<ExcusaEntity> findByEmpleadoAndFechas(@Param("legajo") Integer legajo,
                                               @Param("fechaDesde") LocalDateTime fechaDesde,
                                               @Param("fechaHasta") LocalDateTime fechaHasta);
    
    @Query("SELECT COUNT(e) FROM ExcusaEntity e WHERE e.fechaCreacion < :fechaLimite")
    long countByFechaCreacionBefore(@Param("fechaLimite") LocalDateTime fechaLimite);
    
    void deleteByFechaCreacionBefore(LocalDateTime fechaLimite);
}
