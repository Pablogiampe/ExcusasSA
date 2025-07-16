package ar.edu.davinci.excusas.controller;

import ar.edu.davinci.excusas.dto.EmpleadoDTO;
import ar.edu.davinci.excusas.dto.EncargadoDTO;
import ar.edu.davinci.excusas.service.EmpleadoService;
import ar.edu.davinci.excusas.service.EncargadoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ValidationTest {

    @Autowired
    private EmpleadoService empleadoService;

    @Autowired
    private EncargadoService encargadoService;

    @BeforeEach
    void setUp() {
    }

    @Test
    @Transactional
    public void testCrearEmpleadoConDatosNulos() {
        assertThrows(IllegalArgumentException.class, () -> {
            empleadoService.crearEmpleado(null);
        });

        EmpleadoDTO empleadoSinLegajo = new EmpleadoDTO(null, "Juan Pérez", "juan@email.com");
        assertThrows(IllegalArgumentException.class, () -> {
            empleadoService.crearEmpleado(empleadoSinLegajo);
        });

        EmpleadoDTO empleadoLegajoNegativo = new EmpleadoDTO(-1, "Juan Pérez", "juan@email.com");
        assertThrows(IllegalArgumentException.class, () -> {
            empleadoService.crearEmpleado(empleadoLegajoNegativo);
        });

        EmpleadoDTO empleadoSinNombre = new EmpleadoDTO(1001, "", "juan@email.com");
        assertThrows(IllegalArgumentException.class, () -> {
            empleadoService.crearEmpleado(empleadoSinNombre);
        });

        EmpleadoDTO empleadoSinEmail = new EmpleadoDTO(1001, "Juan Pérez", "");
        assertThrows(IllegalArgumentException.class, () -> {
            empleadoService.crearEmpleado(empleadoSinEmail);
        });
    }

    @Test
    @Transactional
    public void testCrearEmpleadoConLegajoDuplicado() {
        EmpleadoDTO empleado1 = new EmpleadoDTO(1050, "Juan Pérez", "juan@email.com");
        empleadoService.crearEmpleado(empleado1);

        EmpleadoDTO empleado2 = new EmpleadoDTO(1050, "María García", "maria@email.com");
        assertThrows(IllegalArgumentException.class, () -> {
            empleadoService.crearEmpleado(empleado2);
        });
    }

    @Test
    @Transactional
    public void testCrearEmpleadoConEmailDuplicado() {
        EmpleadoDTO empleado1 = new EmpleadoDTO(1009, "Juan Pérez", "juan@email.com");
        empleadoService.crearEmpleado(empleado1);

        EmpleadoDTO empleado2 = new EmpleadoDTO(1002, "María García", "juan@email.com");
        assertThrows(IllegalArgumentException.class, () -> {
            empleadoService.crearEmpleado(empleado2);
        });
    }

    @Test
    @Transactional
    public void testCrearEncargadoConTipoInvalido() {
        EncargadoDTO encargado = new EncargadoDTO(2001, "Ana López", "ana@email.com", "TIPO_INVALIDO", "NORMAL");
        assertThrows(IllegalArgumentException.class, () -> {
            encargadoService.crearEncargado(encargado);
        });
    }

    @Test
    @Transactional
    public void testCrearEncargadoConModoInvalido() {
        EncargadoDTO encargado = new EncargadoDTO(2001, "Ana López", "ana@email.com", "CEO", "MODO_INVALIDO");
        assertThrows(IllegalArgumentException.class, () -> {
            encargadoService.crearEncargado(encargado);
        });
    }

    @Test
    @Transactional
    public void testCrearEmpleadoValido() {
        EmpleadoDTO empleado = new EmpleadoDTO(1023, "Juan Pérez", "juan@email.com");
        EmpleadoDTO empleadoCreado = empleadoService.crearEmpleado(empleado);

        assertNotNull(empleadoCreado);
        assertEquals(1023, empleadoCreado.getLegajo());
        assertEquals("Juan Pérez", empleadoCreado.getNombre());
        assertEquals("juan@email.com", empleadoCreado.getEmail());
    }

    @Test
    @Transactional
    public void testCrearEncargadoValido() {
        EncargadoDTO encargado = new EncargadoDTO(1036, "Ana López", "ana@email.com", "CEO", "NORMAL");
        EncargadoDTO encargadoCreado = encargadoService.crearEncargado(encargado);

        assertNotNull(encargadoCreado);
        assertEquals(1036, encargadoCreado.getLegajo());
        assertEquals("Ana López", encargadoCreado.getNombre());
        assertEquals("ana@email.com", encargadoCreado.getEmail());
        assertEquals("CEO", encargadoCreado.getTipoEncargado());
        assertEquals("NORMAL", encargadoCreado.getModo());
    }
}
