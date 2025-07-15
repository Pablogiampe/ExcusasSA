package ar.edu.davinci.excusas.repository;

import ar.edu.davinci.excusas.model.entities.EmpleadoEntity;
import ar.edu.davinci.excusas.model.entities.ExcusaEntity;
import ar.edu.davinci.excusas.model.excusas.MotivoExcusa;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ExcusaRepositoryTest {
    
    @Autowired
    private TestEntityManager entityManager;
    
    @Autowired
    private ExcusaRepository excusaRepository;
    
    @Test
    public void testFindByEmpleadoLegajo() {
        // Given
        EmpleadoEntity empleado = new EmpleadoEntity("Juan Pérez", "juan@test.com", 1001);
        entityManager.persistAndFlush(empleado);
        
        ExcusaEntity excusa = new ExcusaEntity(MotivoExcusa.QUEDARSE_DORMIDO, "ExcusaTrivial", empleado);
        entityManager.persistAndFlush(excusa);
        
        // When
        List<ExcusaEntity> excusas = excusaRepository.findByEmpleadoLegajo(1001);
        
        // Then
        assertThat(excusas).hasSize(1);
        assertThat(excusas.get(0).getMotivo()).isEqualTo(MotivoExcusa.QUEDARSE_DORMIDO);
        assertThat(excusas.get(0).getEmpleado().getLegajo()).isEqualTo(1001);
    }
    
    @Test
    public void testFindByAceptada() {
        // Given
        EmpleadoEntity empleado = new EmpleadoEntity("María García", "maria@test.com", 1002);
        entityManager.persistAndFlush(empleado);
        
        ExcusaEntity excusaAceptada = new ExcusaEntity(MotivoExcusa.CUIDADO_FAMILIAR, "ExcusaCuidadoFamiliar", empleado);
        excusaAceptada.setAceptada(true);
        entityManager.persistAndFlush(excusaAceptada);
        
        ExcusaEntity excusaRechazada = new ExcusaEntity(MotivoExcusa.INCREIBLE_INVEROSIMIL, "ExcusaInverosimil", empleado);
        excusaRechazada.setAceptada(false);
        entityManager.persistAndFlush(excusaRechazada);
        
        // When
        List<ExcusaEntity> excusasAceptadas = excusaRepository.findByAceptada(true);
        List<ExcusaEntity> excusasRechazadas = excusaRepository.findByAceptada(false);
        
        // Then
        assertThat(excusasAceptadas).hasSize(1);
        assertThat(excusasAceptadas.get(0).getMotivo()).isEqualTo(MotivoExcusa.CUIDADO_FAMILIAR);
        
        assertThat(excusasRechazadas).hasSize(1);
        assertThat(excusasRechazadas.get(0).getMotivo()).isEqualTo(MotivoExcusa.INCREIBLE_INVEROSIMIL);
    }
    
    @Test
    public void testFindByFechaCreacionBetween() {
        // Given
        EmpleadoEntity empleado = new EmpleadoEntity("Carlos López", "carlos@test.com", 1003);
        entityManager.persistAndFlush(empleado);
        
        LocalDateTime fechaInicio = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime fechaFin = LocalDateTime.of(2024, 1, 31, 23, 59);
        
        ExcusaEntity excusaEnRango = new ExcusaEntity(MotivoExcusa.PERDI_TRANSPORTE, "ExcusaTrivial", empleado);
        excusaEnRango.setFechaCreacion(LocalDateTime.of(2024, 1, 15, 8, 30));
        entityManager.persistAndFlush(excusaEnRango);
        
        ExcusaEntity excusaFueraDeRango = new ExcusaEntity(MotivoExcusa.PERDIDA_SUMINISTRO, "ExcusaPerdidaSuministro", empleado);
        excusaFueraDeRango.setFechaCreacion(LocalDateTime.of(2024, 2, 15, 8, 30));
        entityManager.persistAndFlush(excusaFueraDeRango);
        
        // When
        List<ExcusaEntity> excusasEnRango = excusaRepository.findByFechaCreacionBetween(fechaInicio, fechaFin);
        
        // Then
        assertThat(excusasEnRango).hasSize(1);
        assertThat(excusasEnRango.get(0).getMotivo()).isEqualTo(MotivoExcusa.PERDI_TRANSPORTE);
    }
}
