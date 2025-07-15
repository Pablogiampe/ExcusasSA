package ar.edu.davinci.excusas.controller;

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
        ResponseEntity<String> response = restTemplate.getForEntity(getBaseUrl(), String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

         assertTrue(response.getBody().startsWith("["));
        assertTrue(response.getBody().endsWith("]"));

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
        ResponseEntity<String> response = restTemplate.getForEntity(getBaseUrl() + "/1001", String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        assertTrue(response.getBody().contains("\"legajo\":1001"));
        assertTrue(response.getBody().contains("\"nombre\":\"Ana García\""));
        assertTrue(response.getBody().contains("\"email\":\"ana@excusas.com\""));
        assertTrue(response.getBody().contains("\"tipoEncargado\":\"Recepcionista\""));
    }

    @Test
    public void testCambiarModoEncargado_DebeRetornarMensajeExitoso() throws Exception {
        EncargadoController.CambiarModoRequest modoRequest = new EncargadoController.CambiarModoRequest();
        modoRequest.setModo("VAGO");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<EncargadoController.CambiarModoRequest> request = new HttpEntity<>(modoRequest, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                getBaseUrl() + "/1001/modo", HttpMethod.PUT, request, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Modo cambiado exitosamente a: VAGO", response.getBody());
    }

    @Test
    public void testCambiarModoInvalido_DebeRetornar400() throws Exception {
        EncargadoController.CambiarModoRequest modoRequest = new EncargadoController.CambiarModoRequest();
        modoRequest.setModo("MODO_INEXISTENTE");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<EncargadoController.CambiarModoRequest> request = new HttpEntity<>(modoRequest, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                getBaseUrl() + "/1001/modo", HttpMethod.PUT, request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("Modo no válido"));
    }
    @Test
    public void testCrearEncargado_DebeRetornarEncargadoCreado() throws Exception {
        EncargadoController.CrearEncargadoRequest crearRequest = new EncargadoController.CrearEncargadoRequest();
        crearRequest.setNombre("Nuevo Encargado");
        crearRequest.setEmail("nuevo.encargado@excusas.com");
        crearRequest.setLegajo(1005); // Un legajo que no exista
        crearRequest.setTipoEncargado("Recepcionista");
        crearRequest.setModo("NORMAL");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<EncargadoController.CrearEncargadoRequest> request = new HttpEntity<>(crearRequest, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                getBaseUrl(), HttpMethod.POST, request, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("\"legajo\":1005"));
        assertTrue(response.getBody().contains("\"nombre\":\"Nuevo Encargado\""));
        assertTrue(response.getBody().contains("\"tipoEncargado\":\"Recepcionista\""));
    }

    @Test
    public void testCrearEncargado_LegajoExistente_DebeRetornar400() throws Exception {
        EncargadoController.CrearEncargadoRequest crearRequest = new EncargadoController.CrearEncargadoRequest();
        crearRequest.setNombre("Encargado Duplicado");
        crearRequest.setEmail("duplicado@excusas.com");
        crearRequest.setLegajo(1001); // Legajo existente
        crearRequest.setTipoEncargado("Recepcionista");
        crearRequest.setModo("NORMAL");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<EncargadoController.CrearEncargadoRequest> request = new HttpEntity<>(crearRequest, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                getBaseUrl(), HttpMethod.POST, request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testCrearEncargado_TipoInvalido_DebeRetornar400() throws Exception {
        EncargadoController.CrearEncargadoRequest crearRequest = new EncargadoController.CrearEncargadoRequest();
        crearRequest.setNombre("Encargado Invalido");
        crearRequest.setEmail("invalido@excusas.com");
        crearRequest.setLegajo(1006);
        crearRequest.setTipoEncargado("TIPO_INEXISTENTE"); // Tipo inválido
        crearRequest.setModo("NORMAL");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<EncargadoController.CrearEncargadoRequest> request = new HttpEntity<>(crearRequest, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                getBaseUrl(), HttpMethod.POST, request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
