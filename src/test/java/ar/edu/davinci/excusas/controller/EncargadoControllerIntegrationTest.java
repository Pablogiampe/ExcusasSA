package ar.edu.davinci.excusas.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EncargadoControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/api/encargados";
    }

    @Test
    public void testObtenerTodosLosEncargados_DebeRetornarListaConFormatoEsperado() throws Exception {
        // When
        ResponseEntity<String> response = restTemplate.getForEntity(getBaseUrl(), String.class);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        // Verificar que es un array JSON
        assertTrue(response.getBody().startsWith("["));
        assertTrue(response.getBody().endsWith("]"));

        // Verificar que contiene los encargados iniciales
        assertTrue(response.getBody().contains("\"nombre\":\"Ana García\""));
        assertTrue(response.getBody().contains("\"tipoEncargado\":\"Recepcionista\""));
        assertTrue(response.getBody().contains("\"legajo\":1001"));

        assertTrue(response.getBody().contains("\"nombre\":\"Carlos López\""));
        assertTrue(response.getBody().contains("\"tipoEncargado\":\"SupervisorArea\""));
        assertTrue(response.getBody().contains("\"legajo\":1002"));

        assertTrue(response.getBody().contains("\"nombre\":\"María Rodríguez\""));
        assertTrue(response.getBody().contains("\"tipoEncargado\":\"GerenteRRHH\""));
        assertTrue(response.getBody().contains("\"legajo\":1003"));

        assertTrue(response.getBody().contains("\"nombre\":\"Roberto Silva\""));
        assertTrue(response.getBody().contains("\"tipoEncargado\":\"CEO\""));
        assertTrue(response.getBody().contains("\"legajo\":1004"));
    }

    @Test
    public void testObtenerEncargadoPorLegajo_DebeRetornarEncargadoConFormatoEsperado() throws Exception {
        // When
        ResponseEntity<String> response = restTemplate.getForEntity(getBaseUrl() + "/1001", String.class);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        // Verificar formato JSON del encargado
        assertTrue(response.getBody().contains("\"legajo\":1001"));
        assertTrue(response.getBody().contains("\"nombre\":\"Ana García\""));
        assertTrue(response.getBody().contains("\"email\":\"ana@excusas.com\""));
        assertTrue(response.getBody().contains("\"tipoEncargado\":\"Recepcionista\""));
    }

    @Test
    public void testCambiarModoEncargado_DebeRetornarMensajeExitoso() throws Exception {
        // Given
        EncargadoController.CambiarModoRequest modoRequest = new EncargadoController.CambiarModoRequest();
        modoRequest.setModo("VAGO");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<EncargadoController.CambiarModoRequest> request = new HttpEntity<>(modoRequest, headers);

        // When
        ResponseEntity<String> response = restTemplate.exchange(
                getBaseUrl() + "/1001/modo", HttpMethod.PUT, request, String.class);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Modo cambiado exitosamente a: VAGO", response.getBody());
    }

    @Test
    public void testCambiarModoInvalido_DebeRetornar400() throws Exception {
        // Given
        EncargadoController.CambiarModoRequest modoRequest = new EncargadoController.CambiarModoRequest();
        modoRequest.setModo("MODO_INEXISTENTE");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<EncargadoController.CambiarModoRequest> request = new HttpEntity<>(modoRequest, headers);

        // When
        ResponseEntity<String> response = restTemplate.exchange(
                getBaseUrl() + "/1001/modo", HttpMethod.PUT, request, String.class);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("Modo no válido"));
    }
}
