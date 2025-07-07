package ar.edu.davinci.excusas.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProntuarioControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/api/prontuarios";
    }

    private String getEmpleadosUrl() {
        return "http://localhost:" + port + "/api/empleados";
    }

    @BeforeEach
    public void setUp() {
        // Limpiar prontuarios antes de cada test
        restTemplate.exchange(getBaseUrl(), HttpMethod.DELETE, null, String.class);
    }

    @Test
    public void testObtenerTodosLosProntuarios_SinProntuarios_DebeRetornarArrayVacio() throws Exception {
        // When
        ResponseEntity<String> response = restTemplate.getForEntity(getBaseUrl(), String.class);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("[]", response.getBody());
    }

    @Test
    public void testObtenerTodosLosProntuarios_ConProntuarios_DebeRetornarFormatoEsperado() throws Exception {
        // Given - Generar una excusa inverosímil para crear un prontuario
        EmpleadoController.ExcusaRequest excusaRequest = new EmpleadoController.ExcusaRequest();
        excusaRequest.setMotivo("INCREIBLE_INVEROSIMIL");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<EmpleadoController.ExcusaRequest> request = new HttpEntity<>(excusaRequest, headers);

        restTemplate.postForEntity(getEmpleadosUrl() + "/2001/excusas", request, String.class);

        // When
        ResponseEntity<String> response = restTemplate.getForEntity(getBaseUrl(), String.class);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        
        // Verificar formato JSON del prontuario
        assertTrue(response.getBody().startsWith("["));
        assertTrue(response.getBody().endsWith("]"));
        assertTrue(response.getBody().contains("\"numeroLegajo\":2001"));
        assertTrue(response.getBody().contains("\"nombreEmpleado\":\"Juan Pérez\""));
        assertTrue(response.getBody().contains("\"emailEmpleado\":\"juan.perez@empresa.com\""));
        assertTrue(response.getBody().contains("\"tipoExcusa\":\"ExcusaInverosimil\""));
        assertTrue(response.getBody().contains("\"motivoExcusa\":\"INCREIBLE_INVEROSIMIL\""));
    }

    @Test
    public void testObtenerProntuariosPorEmpleado_DebeRetornarFormatoEsperado() throws Exception {
        // Given - Generar una excusa inverosímil para crear un prontuario
        EmpleadoController.ExcusaRequest excusaRequest = new EmpleadoController.ExcusaRequest();
        excusaRequest.setMotivo("INCREIBLE_INVEROSIMIL");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<EmpleadoController.ExcusaRequest> request = new HttpEntity<>(excusaRequest, headers);

        restTemplate.postForEntity(getEmpleadosUrl() + "/2001/excusas", request, String.class);

        // When
        ResponseEntity<String> response = restTemplate.getForEntity(getBaseUrl() + "/empleado/2001", String.class);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        
        // Verificar formato específico del empleado
        assertTrue(response.getBody().contains("\"numeroLegajo\":2001"));
        assertTrue(response.getBody().contains("\"nombreEmpleado\":\"Juan Pérez\""));
    }

    @Test
    public void testLimpiarProntuarios_DebeRetornar200() throws Exception {
        // When
        ResponseEntity<String> response = restTemplate.exchange(
                getBaseUrl(), HttpMethod.DELETE, null, String.class);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
