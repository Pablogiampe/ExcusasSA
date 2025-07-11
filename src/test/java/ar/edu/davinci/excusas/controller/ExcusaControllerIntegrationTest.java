package ar.edu.davinci.excusas.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ExcusaControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/api/excusas";
    }

    private void registrarExcusa(int legajo, String motivo) {
        ExcusaController.RegistrarExcusaRequest requestBody = new ExcusaController.RegistrarExcusaRequest();
        requestBody.setLegajo(legajo);
        requestBody.setMotivo(motivo);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ExcusaController.RegistrarExcusaRequest> request = new HttpEntity<>(requestBody, headers);

        restTemplate.exchange(getBaseUrl(), HttpMethod.POST, request, String.class);
    }

    @Test
    public void testRegistrarExcusa_DebeRetornarExcusaRegistrada() throws Exception {
        ExcusaController.RegistrarExcusaRequest requestBody = new ExcusaController.RegistrarExcusaRequest();
        requestBody.setLegajo(2001); // Empleado existente en ExcusaControllerNuevo
        requestBody.setMotivo("QUEDARSE_DORMIDO");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ExcusaController.RegistrarExcusaRequest> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                getBaseUrl(), HttpMethod.POST, request, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("\"motivo\":\"QUEDARSE_DORMIDO\""));
        assertTrue(response.getBody().contains("\"legajoEmpleado\":2001"));
    }

    @Test
    public void testRegistrarExcusa_EmpleadoNoExiste_DebeRetornar400() throws Exception {
        ExcusaController.RegistrarExcusaRequest requestBody = new ExcusaController.RegistrarExcusaRequest();
        requestBody.setLegajo(9999); // Empleado que no existe
        requestBody.setMotivo("QUEDARSE_DORMIDO");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ExcusaController.RegistrarExcusaRequest> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                getBaseUrl(), HttpMethod.POST, request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testRegistrarExcusa_MotivoInvalido_DebeRetornar400() throws Exception {
        ExcusaController.RegistrarExcusaRequest requestBody = new ExcusaController.RegistrarExcusaRequest();
        requestBody.setLegajo(2001);
        requestBody.setMotivo("MOTIVO_INEXISTENTE"); // Motivo inválido

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ExcusaController.RegistrarExcusaRequest> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                getBaseUrl(), HttpMethod.POST, request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testObtenerExcusasPorEmpleado_DebeRetornarListaDeExcusas() throws Exception {
        registrarExcusa(2001, "QUEDARSE_DORMIDO");
        registrarExcusa(2001, "PERDI_TRANSPORTE");
        registrarExcusa(2002, "CUIDADO_FAMILIAR");

        ResponseEntity<String> response = restTemplate.getForEntity(getBaseUrl() + "/2001", String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("\"legajoEmpleado\":2001"));
        assertTrue(response.getBody().contains("\"motivo\":\"QUEDARSE_DORMIDO\""));
        assertTrue(response.getBody().contains("\"motivo\":\"PERDI_TRANSPORTE\""));
        assertFalse(response.getBody().contains("\"legajoEmpleado\":2002")); // No debe contener excusas de otro empleado
    }

    @Test
    public void testObtenerTodasLasExcusas_SinFiltros_DebeRetornarTodasLasExcusas() throws Exception {
        registrarExcusa(2001, "QUEDARSE_DORMIDO");
        registrarExcusa(2002, "PERDIDA_SUMINISTRO");

        ResponseEntity<String> response = restTemplate.getForEntity(getBaseUrl(), String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("\"motivo\":\"QUEDARSE_DORMIDO\""));
        assertTrue(response.getBody().contains("\"motivo\":\"PERDIDA_SUMINISTRO\""));
    }

    @Test
    public void testObtenerTodasLasExcusas_ConFiltroMotivo_DebeRetornarExcusasFiltradas() throws Exception {
        registrarExcusa(2001, "QUEDARSE_DORMIDO");
        registrarExcusa(2002, "PERDIDA_SUMINISTRO");

        ResponseEntity<String> response = restTemplate.getForEntity(getBaseUrl() + "?motivo=QUEDARSE_DORMIDO", String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("\"motivo\":\"QUEDARSE_DORMIDO\""));
        assertFalse(response.getBody().contains("\"motivo\":\"PERDIDA_SUMINISTRO\""));
    }

    @Test
    public void testBuscarExcusas_PorLegajoYFecha_DebeRetornarExcusasFiltradas() throws Exception {
        registrarExcusa(2001, "QUEDARSE_DORMIDO");
        registrarExcusa(2001, "PERDI_TRANSPORTE");
        registrarExcusa(2002, "CUIDADO_FAMILIAR");

        String fechaHoy = LocalDate.now().toString();

        ResponseEntity<String> response = restTemplate.getForEntity(
                getBaseUrl() + "/busqueda?legajo=2001&fechaDesde=" + fechaHoy + "&fechaHasta=" + fechaHoy, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("\"legajoEmpleado\":2001"));
        assertTrue(response.getBody().contains("\"motivo\":\"QUEDARSE_DORMIDO\""));
        assertTrue(response.getBody().contains("\"motivo\":\"PERDI_TRANSPORTE\""));
        assertFalse(response.getBody().contains("\"legajoEmpleado\":2002"));
    }

    @Test
    public void testObtenerExcusasRechazadas_DebeRetornarListaVaciaPorDefecto() throws Exception {
        registrarExcusa(2001, "QUEDARSE_DORMIDO");

        ResponseEntity<String> response = restTemplate.getForEntity(getBaseUrl() + "/rechazadas", String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("[]", response.getBody()); // Esperamos una lista vacía
    }

    @Test
    public void testEliminarExcusas_ConFechaLimite_DebeRetornarCantidadEliminadas() throws Exception {
        registrarExcusa(2001, "QUEDARSE_DORMIDO");
        registrarExcusa(2002, "PERDIDA_SUMINISTRO");

        String fechaFutura = LocalDate.now().plusDays(1).toString();

        ResponseEntity<String> response = restTemplate.exchange(
                getBaseUrl() + "/eliminar?fechaLimite=" + fechaFutura, HttpMethod.DELETE, null, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("\"cantidadEliminadas\":2")); // Se eliminan las 2 excusas
    }

    @Test
    public void testEliminarExcusas_SinFechaLimite_DebeRetornar400() throws Exception {
        ResponseEntity<String> response = restTemplate.exchange(
                getBaseUrl() + "/eliminar", HttpMethod.DELETE, null, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}