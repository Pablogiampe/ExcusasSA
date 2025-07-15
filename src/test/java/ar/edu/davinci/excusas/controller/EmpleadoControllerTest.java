package ar.edu.davinci.excusas.controller;

import ar.edu.davinci.excusas.dto.EmpleadoDTO;
import ar.edu.davinci.excusas.service.EmpleadoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmpleadoController.class)
public class EmpleadoControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private EmpleadoService empleadoService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    void testObtenerTodosLosEmpleados() throws Exception {
        // Given
        EmpleadoDTO empleado1 = new EmpleadoDTO(1001, "Juan Pérez", "juan@test.com");
        EmpleadoDTO empleado2 = new EmpleadoDTO(1002, "María García", "maria@test.com");
        
        when(empleadoService.obtenerTodosLosEmpleados()).thenReturn(Arrays.asList(empleado1, empleado2));
        
        // When & Then
        mockMvc.perform(get("/empleados"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].legajo").value(1001))
                .andExpect(jsonPath("$[0].nombre").value("Juan Pérez"))
                .andExpect(jsonPath("$[1].legajo").value(1002))
                .andExpect(jsonPath("$[1].nombre").value("María García"));
    }
    
    @Test
    void testObtenerEmpleadoPorLegajo() throws Exception {
        // Given
        EmpleadoDTO empleado = new EmpleadoDTO(1001, "Juan Pérez", "juan@test.com");
        when(empleadoService.obtenerEmpleadoPorLegajo(1001)).thenReturn(Optional.of(empleado));
        
        // When & Then
        mockMvc.perform(get("/empleados/1001"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.legajo").value(1001))
                .andExpect(jsonPath("$.nombre").value("Juan Pérez"))
                .andExpect(jsonPath("$.email").value("juan@test.com"));
    }
    
    @Test
    void testObtenerEmpleadoPorLegajoNoEncontrado() throws Exception {
        // Given
        when(empleadoService.obtenerEmpleadoPorLegajo(9999)).thenReturn(Optional.empty());
        
        // When & Then
        mockMvc.perform(get("/empleados/9999"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void testCrearEmpleado() throws Exception {
        // Given
        EmpleadoDTO nuevoEmpleado = new EmpleadoDTO(1001, "Juan Pérez", "juan@test.com");
        when(empleadoService.crearEmpleado(any(EmpleadoDTO.class))).thenReturn(nuevoEmpleado);
        
        // When & Then
        mockMvc.perform(post("/empleados")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevoEmpleado)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.legajo").value(1001))
                .andExpect(jsonPath("$.nombre").value("Juan Pérez"))
                .andExpect(jsonPath("$.email").value("juan@test.com"));
    }
    
    @Test
    void testCrearEmpleadoConDatosInvalidos() throws Exception {
        // Given
        EmpleadoDTO empleadoInvalido = new EmpleadoDTO(null, "", "email-invalido");
        
        // When & Then
        mockMvc.perform(post("/empleados")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empleadoInvalido)))
                .andExpect(status().isBadRequest());
    }
}
