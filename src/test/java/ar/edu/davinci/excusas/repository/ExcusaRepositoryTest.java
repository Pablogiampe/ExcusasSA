package ar.edu.davinci.excusas.repository;

import ar.edu.davinci.excusas.model.entities.EmpleadoEntity;
import ar.edu.davinci.excusas.model.entities.ExcusaEntity;
import ar.edu.davinci.excusas.model.excusas.MotivoExcusa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class ExcusaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ExcusaRepository excusaRepository;

    private EmpleadoEntity empleado1;
    private EmpleadoEntity empleado2;

    @BeforeEach
    public void setUp() {
        empleado1 = new EmpleadoEntity("Juan Pérez", "juan@test.com", 2001);
        empleado2 = new EmpleadoEntity("María García", "maria@test.com", 2002);
        
        entityManager.persistAndFlush(empleado1);
        entityManager.persistAndFlush(empleado2);
    }

    @Test
    public void testGuardarYRecuperarExcusa() {
        // Given
        ExcusaEntity excusa = new ExcusaEntity(MotivoExcusa.QUEDARSE_DORMIDO, "ExcusaTrivial", empleado1);

        // When
        ExcusaEntity excusaGuardada = excusaRepository.save(excusa);
        entityManager.flush();

        // Then
        assertNotNull(excusaGuardada);
        assertNotNull(excusaGuardada.getId());
        assertEquals(MotivoExcusa.QUEDARSE_DORMIDO, excusaGuardada.getMotivo());
        assertEquals("ExcusaTrivial", excusaGuardada.getTipoExcusa());
        assertEquals(empleado1.getLegajo(), excusaGuardada.getEmpleado().getLegajo());
        assertNotNull(excusaGuardada.getFechaCreacion());
    }

    @Test
    public void testBuscarExcusasPorEmpleado() {
        // Given
        ExcusaEntity excusa1 = new ExcusaEntity(MotivoExcusa.QUEDARSE_DORMIDO, "ExcusaTrivial", empleado1);
        ExcusaEntity excusa2 = new ExcusaEntity(MotivoExcusa.PERDI_TRANSPORTE, "ExcusaTrivial", empleado1);
        ExcusaEntity excusa3 = new ExcusaEntity(MotivoExcusa.CUIDADO_FAMILIAR, "ExcusaCuidadoFamiliar", empleado2);
        
        entityManager.persistAndFlush(excusa1);
        entityManager.persistAndFlush(excusa2);
        entityManager.persistAndFlush(excusa3);

        // When
        List<ExcusaEntity> excusasEmpleado1 = excusaRepository.findByEmpleadoLegajo(2001);
        List<ExcusaEntity> excusasEmpleado2 = excusaRepository.findByEmpleadoLegajo(2002);

        // Then
        assertEquals(2, excusasEmpleado1.size());
        assertEquals(1, excusasEmpleado2.size());
        
        assertTrue(excusasEmpleado1.stream().allMatch(e -> e.getEmpleado().getLegajo().equals(2001)));
        assertTrue(excusasEmpleado2.stream().allMatch(e -> e.getEmpleado().getLegajo().equals(2002)));
    }

    @Test
    public void testBuscarExcusasPorMotivo() {
        // Given
        ExcusaEntity excusa1 = new ExcusaEntity(MotivoExcusa.QUEDARSE_DORMIDO, "ExcusaTrivial", empleado1);
        ExcusaEntity excusa2 = new ExcusaEntity(MotivoExcusa.QUEDARSE_DORMIDO, "ExcusaTrivial", empleado2);
        ExcusaEntity excusa3 = new ExcusaEntity(MotivoExcusa.CUIDADO_FAMILIAR, "ExcusaCuidadoFamiliar", empleado1);
        
        entityManager.persistAndFlush(excusa1);
        entityManager.persistAndFlush(excusa2);
        entityManager.persistAndFlush(excusa3);

        // When
        List<ExcusaEntity> excusasDormido = excusaRepository.findByMotivo(MotivoExcusa.QUEDARSE_DORMIDO);
        List<ExcusaEntity> excusasFamiliar = excusaRepository.findByMotivo(MotivoExcusa.CUIDADO_FAMILIAR);

        // Then
        assertEquals(2, excusasDormido.size());
        assertEquals(1, excusasFamiliar.size());
        
        assertTrue(excusasDormido.stream().allMatch(e -> e.getMotivo() == MotivoExcusa.QUEDARSE_DORMIDO));
        assertTrue(excusasFamiliar.stream().allMatch(e -> e.getMotivo() == MotivoExcusa.CUIDADO_FAMILIAR));
    }

    @Test
    public void testBuscarExcusasPorFechas() {
        // Given
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime ayer = ahora.minusDays(1);
        LocalDateTime manana = ahora.plusDays(1);
        
        ExcusaEntity excusaAyer = new ExcusaEntity(MotivoExcusa.QUEDARSE_DORMIDO, "ExcusaTrivial", empleado1);
        excusaAyer.setFechaCreacion(ayer);
        
        ExcusaEntity excusaHoy = new ExcusaEntity(MotivoExcusa.PERDI_TRANSPORTE, "ExcusaTrivial", empleado1);
        excusaHoy.setFechaCreacion(ahora);
        
        entityManager.persistAndFlush(excusaAyer);
        entityManager.persistAndFlush(excusaHoy);

        // When
        List<ExcusaEntity> excusasDesdeAyer = excusaRepository.findByFiltros(ayer, null, null);
        List<ExcusaEntity> excusasHastaAyer = excusaRepository.findByFiltros(null, ayer.plusHours(1), null);

        // Then
        assertEquals(2, excusasDesdeAyer.size());
        assertEquals(1, excusasHastaAyer.size());
    }

    @Test
    public void testContarExcusasAnterioresAFecha() {
        // Given
        LocalDateTime fechaLimite = LocalDateTime.now();
        LocalDateTime fechaAnterior = fechaLimite.minusDays(1);
        
        ExcusaEntity excusaAnterior = new ExcusaEntity(MotivoExcusa.QUEDARSE_DORMIDO, "ExcusaTrivial", empleado1);
        excusaAnterior.setFechaCreacion(fechaAnterior);
        
        ExcusaEntity excusaPosterior = new ExcusaEntity(MotivoExcusa.PERDI_TRANSPORTE, "ExcusaTrivial", empleado1);
        excusaPosterior.setFechaCreacion(fechaLimite.plusHours(1));
        
        entityManager.persistAndFlush(excusaAnterior);
        entityManager.persistAndFlush(excusaPosterior);

        // When
        long count = excusaRepository.countByFechaCreacionBefore(fechaLimite);

        // Then
        assertEquals(1, count);
    }
}
