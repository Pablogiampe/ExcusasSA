package ar.edu.davinci.excusas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmpleadoControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/api/empleados";
    }

    @Test
    public void testObtenerTodosLosEmpleados_DebeRetornarListaConFormatoEsperado() throws Exception {
        // When
        ResponseEntity<String> response = restTemplate.getForEntity(getBaseUrl(), String.class);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        // Verificar que es un array JSON
        assertTrue(response.getBody().startsWith("["));
        assertTrue(response.getBody().endsWith("]"));

        // Verificar que contiene los empleados iniciales
        assertTrue(response.getBody().contains("Juan Pérez"));
        assertTrue(response.getBody().contains("María García"));
        assertTrue(response.getBody().contains("Carlos López"));

        // Verificar estructura JSON esperada
        assertTrue(response.getBody().contains("\"nombre\""));
        assertTrue(response.getBody().contains("\"email\""));
        assertTrue(response.getBody().contains("\"legajo\""));
    }

    @Test
    public void testObtenerEmpleadoPorLegajo_DebeRetornarEmpleadoConFormatoEsperado() throws Exception {
        // When
        ResponseEntity<String> response = restTemplate.getForEntity(getBaseUrl() + "/2001", String.class);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        // Verificar formato JSON del empleado
        assertTrue(response.getBody().contains("\"nombre\":\"Juan Pérez\""));
        assertTrue(response.getBody().contains("\"email\":\"juan.perez@empresa.com\""));
        assertTrue(response.getBody().contains("\"legajo\":2001"));
    }

    @Test
    public void testObtenerEmpleadoInexistente_DebeRetornar404() throws Exception {
        // When
        ResponseEntity<String> response = restTemplate.getForEntity(getBaseUrl() + "/9999", String.class);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testCrearEmpleado_DebeRetornarEmpleadoCreadoConFormatoEsperado() throws Exception {
        // Given
        EmpleadoController.EmpleadoRequest nuevoEmpleado = new EmpleadoController.EmpleadoRequest();
        nuevoEmpleado.setNombre("Pedro Martínez");
        nuevoEmpleado.setEmail("pedro.martinez@empresa.com");
        nuevoEmpleado.setLegajo(3001);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<EmpleadoController.EmpleadoRequest> request = new HttpEntity<>(nuevoEmpleado, headers);

        // When
        ResponseEntity<String> response = restTemplate.postForEntity(getBaseUrl(), request, String.class);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        // Verificar formato de respuesta
        assertTrue(response.getBody().contains("\"nombre\":\"Pedro Martínez\""));
        assertTrue(response.getBody().contains("\"email\":\"pedro.martinez@empresa.com\""));
        assertTrue(response.getBody().contains("\"legajo\":3001"));
    }

    @Test
    public void testGenerarExcusaTrivial_DebeRetornarExcusaConFormatoEsperado() throws Exception {
        // Given
        EmpleadoController.ExcusaRequest excusaRequest = new EmpleadoController.ExcusaRequest();
        excusaRequest.setMotivo("QUEDARSE_DORMIDO");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<EmpleadoController.ExcusaRequest> request = new HttpEntity<>(excusaRequest, headers);

        // When
        ResponseEntity<String> response = restTemplate.postForEntity(
                getBaseUrl() + "/2001/excusas", request, String.class);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        // Verificar formato de respuesta de excusa
        assertTrue(response.getBody().contains("\"tipoExcusa\":\"ExcusaTrivial\""));
        assertTrue(response.getBody().contains("\"motivo\":\"QUEDARSE_DORMIDO\""));
        assertTrue(response.getBody().contains("\"nombreEmpleado\":\"Juan Pérez\""));
        assertTrue(response.getBody().contains("\"legajoEmpleado\":2001"));
    }

    @Test
    public void testGenerarExcusaModerada_DebeRetornarExcusaConFormatoEsperado() throws Exception {
        // Given
        EmpleadoController.ExcusaRequest excusaRequest = new EmpleadoController.ExcusaRequest();
        excusaRequest.setMotivo("PERDIDA_SUMINISTRO");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<EmpleadoController.ExcusaRequest> request = new HttpEntity<>(excusaRequest, headers);

        // When
        ResponseEntity<String> response = restTemplate.postForEntity(
                getBaseUrl() + "/2002/excusas", request, String.class);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        // Verificar formato de respuesta de excusa moderada
        assertTrue(response.getBody().contains("\"tipoExcusa\":\"ExcusaPerdidaSuministro\""));
        assertTrue(response.getBody().contains("\"motivo\":\"PERDIDA_SUMINISTRO\""));
        assertTrue(response.getBody().contains("\"nombreEmpleado\":\"María García\""));
        assertTrue(response.getBody().contains("\"legajoEmpleado\":2002"));
    }

    @Test
    public void testGenerarExcusaCompleja_DebeRetornarExcusaConFormatoEsperado() throws Exception {
        // Given
        EmpleadoController.ExcusaRequest excusaRequest = new EmpleadoController.ExcusaRequest();
        excusaRequest.setMotivo("IRRELEVANTE");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<EmpleadoController.ExcusaRequest> request = new HttpEntity<>(excusaRequest, headers);

        // When
        ResponseEntity<String> response = restTemplate.postForEntity(
                getBaseUrl() + "/2003/excusas", request, String.class);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        // Verificar formato de respuesta de excusa compleja
        assertTrue(response.getBody().contains("\"tipoExcusa\":\"ExcusaCompleja\""));
        assertTrue(response.getBody().contains("\"motivo\":\"IRRELEVANTE\""));
        assertTrue(response.getBody().contains("\"nombreEmpleado\":\"Carlos López\""));
        assertTrue(response.getBody().contains("\"legajoEmpleado\":2003"));
    }

    @Test
    public void testEliminarEmpleado_DebeRetornar200() throws Exception {
        // When
        ResponseEntity<String> response = restTemplate.exchange(
                getBaseUrl() + "/2001", HttpMethod.DELETE, null, String.class);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
