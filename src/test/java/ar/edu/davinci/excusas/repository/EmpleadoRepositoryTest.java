package ar.edu.davinci.excusas.repository;

import ar.edu.davinci.excusas.model.entities.EmpleadoEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class EmpleadoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Test
    public void testGuardarYRecuperarEmpleado() {
        // Given
        EmpleadoEntity empleado = new EmpleadoEntity("Juan Pérez", "juan@test.com", 1001);

        // When
        EmpleadoEntity empleadoGuardado = empleadoRepository.save(empleado);
        entityManager.flush();

        // Then
        assertNotNull(empleadoGuardado);
        assertEquals("Juan Pérez", empleadoGuardado.getNombre());
        assertEquals("juan@test.com", empleadoGuardado.getEmail());
        assertEquals(1001, empleadoGuardado.getLegajo());
    }

    @Test
    public void testBuscarPorLegajo() {
        // Given
        EmpleadoEntity empleado = new EmpleadoEntity("María García", "maria@test.com", 1002);
        entityManager.persistAndFlush(empleado);

        // When
        Optional<EmpleadoEntity> encontrado = empleadoRepository.findById(1002);

        // Then
        assertTrue(encontrado.isPresent());
        assertEquals("María García", encontrado.get().getNombre());
        assertEquals("maria@test.com", encontrado.get().getEmail());
    }

    @Test
    public void testBuscarPorEmail() {
        // Given
        EmpleadoEntity empleado = new EmpleadoEntity("Carlos López", "carlos@test.com", 1003);
        entityManager.persistAndFlush(empleado);

        // When
        Optional<EmpleadoEntity> encontrado = empleadoRepository.findByEmail("carlos@test.com");

        // Then
        assertTrue(encontrado.isPresent());
        assertEquals("Carlos López", encontrado.get().getNombre());
        assertEquals(1003, encontrado.get().getLegajo());
    }

    @Test
    public void testExistePorLegajo() {
        // Given
        EmpleadoEntity empleado = new EmpleadoEntity("Ana Rodríguez", "ana@test.com", 1004);
        entityManager.persistAndFlush(empleado);

        // When & Then
        assertTrue(empleadoRepository.existsByLegajo(1004));
        assertFalse(empleadoRepository.existsByLegajo(9999));
    }

    @Test
    public void testExistePorEmail() {
        // Given
        EmpleadoEntity empleado = new EmpleadoEntity("Pedro Martínez", "pedro@test.com", 1005);
        entityManager.persistAndFlush(empleado);

        // When & Then
        assertTrue(empleadoRepository.existsByEmail("pedro@test.com"));
        assertFalse(empleadoRepository.existsByEmail("noexiste@test.com"));
    }

    @Test
    public void testEliminarEmpleado() {
        // Given
        EmpleadoEntity empleado = new EmpleadoEntity("Luis González", "luis@test.com", 1006);
        entityManager.persistAndFlush(empleado);

        // When
        empleadoRepository.deleteById(1006);
        entityManager.flush();

        // Then
        Optional<EmpleadoEntity> encontrado = empleadoRepository.findById(1006);
        assertFalse(encontrado.isPresent());
    }
}
