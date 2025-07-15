package ar.edu.davinci.excusas.repository;

import ar.edu.davinci.excusas.model.entities.EmpleadoEntity;
import ar.edu.davinci.excusas.model.entities.ExcusaEntity;
import ar.edu.davinci.excusas.model.entities.ProntuarioEntity;
import ar.edu.davinci.excusas.model.excusas.MotivoExcusa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class ProntuarioRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProntuarioRepository prontuarioRepository;

    private EmpleadoEntity empleado1;
    private EmpleadoEntity empleado2;
    private ExcusaEntity excusa1;
    private ExcusaEntity excusa2;

    @BeforeEach
    public void setUp() {
        empleado1 = new EmpleadoEntity("Juan Pérez", "juan@test.com", 2001);
        empleado2 = new EmpleadoEntity("María García", "maria@test.com", 2002);
        
        entityManager.persistAndFlush(empleado1);
        entityManager.persistAndFlush(empleado2);
        
        excusa1 = new ExcusaEntity(MotivoExcusa.INCREIBLE_INVEROSIMIL, "ExcusaInverosimil", empleado1);
        excusa2 = new ExcusaEntity(MotivoExcusa.INCREIBLE_INVEROSIMIL, "ExcusaInverosimil", empleado2);
        
        entityManager.persistAndFlush(excusa1);
        entityManager.persistAndFlush(excusa2);
    }

    @Test
    public void testGuardarYRecuperarProntuario() {
        // Given
        ProntuarioEntity prontuario = new ProntuarioEntity(empleado1, excusa1);

        // When
        ProntuarioEntity prontuarioGuardado = prontuarioRepository.save(prontuario);
        entityManager.flush();

        // Then
        assertNotNull(prontuarioGuardado);
        assertNotNull(prontuarioGuardado.getId());
        assertEquals(empleado1.getLegajo(), prontuarioGuardado.getEmpleado().getLegajo());
        assertEquals(excusa1.getId(), prontuarioGuardado.getExcusa().getId());
        assertNotNull(prontuarioGuardado.getFechaCreacion());
    }

    @Test
    public void testBuscarProntuariosPorEmpleado() {
        // Given
        ProntuarioEntity prontuario1 = new ProntuarioEntity(empleado1, excusa1);
        ProntuarioEntity prontuario2 = new ProntuarioEntity(empleado2, excusa2);
        
        entityManager.persistAndFlush(prontuario1);
        entityManager.persistAndFlush(prontuario2);

        // When
        List<ProntuarioEntity> prontuariosEmpleado1 = prontuarioRepository.findByEmpleadoLegajo(2001);
        List<ProntuarioEntity> prontuariosEmpleado2 = prontuarioRepository.findByEmpleadoLegajo(2002);

        // Then
        assertEquals(1, prontuariosEmpleado1.size());
        assertEquals(1, prontuariosEmpleado2.size());
        
        assertEquals(2001, prontuariosEmpleado1.get(0).getEmpleado().getLegajo());
        assertEquals(2002, prontuariosEmpleado2.get(0).getEmpleado().getLegajo());
    }

    @Test
    public void testEliminarTodosLosProntuarios() {
        // Given
        ProntuarioEntity prontuario1 = new ProntuarioEntity(empleado1, excusa1);
        ProntuarioEntity prontuario2 = new ProntuarioEntity(empleado2, excusa2);
        
        entityManager.persistAndFlush(prontuario1);
        entityManager.persistAndFlush(prontuario2);

        // When
        prontuarioRepository.deleteAll();
        entityManager.flush();

        // Then
        List<ProntuarioEntity> todosProntuarios = prontuarioRepository.findAll();
        assertTrue(todosProntuarios.isEmpty());
    }

    @Test
    public void testRelacionConEmpleadoYExcusa() {
        // Given
        ProntuarioEntity prontuario = new ProntuarioEntity(empleado1, excusa1);
        entityManager.persistAndFlush(prontuario);

        // When
        ProntuarioEntity prontuarioRecuperado = prontuarioRepository.findById(prontuario.getId()).orElse(null);

        // Then
        assertNotNull(prontuarioRecuperado);
        assertNotNull(prontuarioRecuperado.getEmpleado());
        assertNotNull(prontuarioRecuperado.getExcusa());
        
        assertEquals("Juan Pérez", prontuarioRecuperado.getEmpleado().getNombre());
        assertEquals(MotivoExcusa.INCREIBLE_INVEROSIMIL, prontuarioRecuperado.getExcusa().getMotivo());
    }
}
