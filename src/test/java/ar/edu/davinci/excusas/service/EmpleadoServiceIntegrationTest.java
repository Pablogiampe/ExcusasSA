package ar.edu.davinci.excusas.service;

import ar.edu.davinci.excusas.dto.EmpleadoDTO;
import ar.edu.davinci.excusas.dto.ExcusaDTO;
import ar.edu.davinci.excusas.model.excusas.MotivoExcusa;
import ar.edu.davinci.excusas.repository.EmpleadoRepository;
import ar.edu.davinci.excusas.repository.ExcusaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class EmpleadoServiceIntegrationTest {

    @Autowired
    private EmpleadoService empleadoService;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private ExcusaRepository excusaRepository;

    @Test
    public void testCrearEmpleadoPersisteDatos() {
        // Given
        String nombre = "Nuevo Empleado";
        String email = "nuevo@test.com";
        int legajo = 9001;

        // When
        EmpleadoDTO empleadoCreado = empleadoService.crearEmpleado(nombre, email, legajo);

        // Then
        assertNotNull(empleadoCreado);
        assertEquals(nombre, empleadoCreado.getNombre());
        assertEquals(email, empleadoCreado.getEmail());
        assertEquals(legajo, empleadoCreado.getLegajo());

        // Verificar que se persistió en la base de datos
        assertTrue(empleadoRepository.existsByLegajo(legajo));
        assertTrue(empleadoRepository.existsByEmail(email));
    }

    @Test
    public void testObtenerEmpleadosRecuperaDeDatos() {
        // Given - Los empleados se inicializan automáticamente en el servicio

        // When
        List<EmpleadoDTO> empleados = empleadoService.obtenerTodosLosEmpleados();

        // Then
        assertFalse(empleados.isEmpty());
        assertTrue(empleados.size() >= 3); // Al menos los 3 empleados iniciales

        // Verificar que contiene los empleados iniciales
        assertTrue(empleados.stream().anyMatch(e -> e.getNombre().equals("Juan Pérez")));
        assertTrue(empleados.stream().anyMatch(e -> e.getNombre().equals("María García")));
        assertTrue(empleados.stream().anyMatch(e -> e.getNombre().equals("Carlos López")));
    }

    @Test
    public void testGenerarExcusaPersisteDatos() {
        // Given
        int legajo = 2001; // Empleado que existe por defecto

        // When
        ExcusaDTO excusaGenerada = empleadoService.generarExcusa(legajo, MotivoExcusa.QUEDARSE_DORMIDO);

        // Then
        assertNotNull(excusaGenerada);
        assertEquals("QUEDARSE_DORMIDO", excusaGenerada.getMotivo());
        assertEquals(legajo, excusaGenerada.getLegajoEmpleado());

        // Verificar que se persistió en la base de datos
        List<ar.edu.davinci.excusas.model.entities.ExcusaEntity> excusasEnDB = excusaRepository.findByEmpleadoLegajo(legajo);
        assertFalse(excusasEnDB.isEmpty());
        assertTrue(excusasEnDB.stream().anyMatch(e -> e.getMotivo() == MotivoExcusa.QUEDARSE_DORMIDO));
    }

    @Test
    public void testEliminarEmpleadoEliminaDeDatos() {
        // Given
        String nombre = "Empleado a Eliminar";
        String email = "eliminar@test.com";
        int legajo = 9002;
        
        empleadoService.crearEmpleado(nombre, email, legajo);
        assertTrue(empleadoRepository.existsByLegajo(legajo));

        // When
        boolean eliminado = empleadoService.eliminarEmpleado(legajo);

        // Then
        assertTrue(eliminado);
        assertFalse(empleadoRepository.existsByLegajo(legajo));
    }

    @Test
    public void testObtenerEmpleadoPorLegajoRecuperaDeDatos() {
        // Given
        int legajo = 2001; // Empleado que existe por defecto

        // When
        Optional<EmpleadoDTO> empleado = empleadoService.obtenerEmpleadoPorLegajo(legajo);

        // Then
        assertTrue(empleado.isPresent());
        assertEquals("Juan Pérez", empleado.get().getNombre());
        assertEquals("juan.perez@empresa.com", empleado.get().getEmail());
        assertEquals(legajo, empleado.get().getLegajo());
    }

    @Test
    public void testCrearEmpleadoConLegajoDuplicadoFalla() {
        // Given
        int legajo = 2001; // Legajo que ya existe

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            empleadoService.crearEmpleado("Duplicado", "duplicado@test.com", legajo);
        });
    }
}
