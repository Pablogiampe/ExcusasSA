package ar.edu.davinci.excusas.repository;

import ar.edu.davinci.excusas.model.entities.ProntuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProntuarioRepository extends JpaRepository<ProntuarioEntity, Long> {
    
    List<ProntuarioEntity> findByEmpleadoLegajo(Integer legajo);
    
    List<ProntuarioEntity> findByExcusaId(Long excusaId);
    
    @Query("SELECT p FROM ProntuarioEntity p WHERE p.fechaCreacion BETWEEN :fechaDesde AND :fechaHasta")
    List<ProntuarioEntity> findByFechaCreacionBetween(@Param("fechaDesde") LocalDateTime fechaDesde,
                                                       @Param("fechaHasta") LocalDateTime fechaHasta);
    
    @Query("SELECT p FROM ProntuarioEntity p WHERE p.empleado.legajo = :legajo AND p.fechaCreacion BETWEEN :fechaDesde AND :fechaHasta")
    List<ProntuarioEntity> findByEmpleadoLegajoAndFechaCreacionBetween(@Param("legajo") Integer legajo,
                                                                        @Param("fechaDesde") LocalDateTime fechaDesde,
                                                                        @Param("fechaHasta") LocalDateTime fechaHasta);
    
    @Query("SELECT p FROM ProntuarioEntity p ORDER BY p.fechaCreacion DESC")
    List<ProntuarioEntity> findAllOrderByFechaCreacionDesc();
}
