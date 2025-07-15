package ar.edu.davinci.excusas.controller;

import ar.edu.davinci.excusas.model.prontuario.AdministradorProntuarios;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
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
        restTemplate.exchange(getBaseUrl(), HttpMethod.DELETE, null, String.class);

        AdministradorProntuarios.reset();
    }

    @Test
    public void testObtenerTodosLosProntuarios_SinProntuarios_DebeRetornarArrayVacio() throws Exception {
        ResponseEntity<String> response = restTemplate.getForEntity(getBaseUrl(), String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("[]", response.getBody());
    }

    @Test
    public void testObtenerTodosLosProntuarios_ConProntuarios_DebeRetornarFormatoEsperado() throws Exception {
        ResponseEntity<String> excusaResponse = restTemplate.postForEntity(
                getEmpleadosUrl() + "/2001/excusas/INCREIBLE_INVEROSIMIL", null, String.class);

        assertEquals(HttpStatus.OK, excusaResponse.getStatusCode());
        assertNotNull(excusaResponse.getBody());
        assertTrue(excusaResponse.getBody().contains("INCREIBLE_INVEROSIMIL"));

        ResponseEntity<String> response = restTemplate.getForEntity(getBaseUrl(), String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        assertFalse(response.getBody().equals("[]"), "Debería haber al menos un prontuario");

        assertTrue(response.getBody().startsWith("["));
        assertTrue(response.getBody().endsWith("]"));

        assertTrue(response.getBody().contains("\"numeroLegajo\""));
        assertTrue(response.getBody().contains("\"nombreEmpleado\""));
        assertTrue(response.getBody().contains("\"emailEmpleado\""));
        assertTrue(response.getBody().contains("\"tipoExcusa\""));
        assertTrue(response.getBody().contains("\"motivoExcusa\""));

        assertTrue(response.getBody().contains("\"numeroLegajo\":2001"));
        assertTrue(response.getBody().contains("\"nombreEmpleado\":\"Juan Pérez\""));
        assertTrue(response.getBody().contains("\"emailEmpleado\":\"juan.perez@empresa.com\""));
        assertTrue(response.getBody().contains("\"tipoExcusa\":\"ExcusaInverosimil\""));
        assertTrue(response.getBody().contains("\"motivoExcusa\":\"INCREIBLE_INVEROSIMIL\""));
    }

    @Test
    public void testObtenerProntuariosPorEmpleado_DebeRetornarFormatoEsperado() throws Exception {
        ResponseEntity<String> excusaResponse = restTemplate.postForEntity(
                getEmpleadosUrl() + "/2001/excusas/INCREIBLE_INVEROSIMIL", null, String.class);

        assertEquals(HttpStatus.OK, excusaResponse.getStatusCode());
        assertNotNull(excusaResponse.getBody());
        assertTrue(excusaResponse.getBody().contains("INCREIBLE_INVEROSIMIL"));

        ResponseEntity<String> response = restTemplate.getForEntity(getBaseUrl() + "/empleado/2001", String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        assertTrue(response.getBody().startsWith("["));
        assertTrue(response.getBody().endsWith("]"));

        assertTrue(response.getBody().contains("\"numeroLegajo\""));
        assertTrue(response.getBody().contains("\"nombreEmpleado\""));

        assertTrue(response.getBody().contains("\"numeroLegajo\":2001"));
        assertTrue(response.getBody().contains("\"nombreEmpleado\":\"Juan Pérez\""));
    }

    @Test
    public void testLimpiarProntuarios_DebeRetornar200() throws Exception {
        ResponseEntity<String> response = restTemplate.exchange(
                getBaseUrl(), HttpMethod.DELETE, null, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
