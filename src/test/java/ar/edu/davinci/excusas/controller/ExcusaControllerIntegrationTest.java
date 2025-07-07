package ar.edu.davinci.excusas.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

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

    @Test
    public void testObtenerMotivosDisponibles_DebeRetornarTodosLosMotivosConFormatoEsperado() throws Exception {
        // When
        ResponseEntity<String> response = restTemplate.getForEntity(getBaseUrl() + "/motivos", String.class);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        // Verificar que es un array JSON
        assertTrue(response.getBody().startsWith("["));
        assertTrue(response.getBody().endsWith("]"));

        // Verificar que contiene todos los motivos esperados
        assertTrue(response.getBody().contains("\"codigo\":\"QUEDARSE_DORMIDO\""));
        assertTrue(response.getBody().contains("\"descripcion\":\"El empleado se quedó dormido\""));
        assertTrue(response.getBody().contains("\"tipoExcusa\":\"Trivial\""));

        assertTrue(response.getBody().contains("\"codigo\":\"PERDI_TRANSPORTE\""));
        assertTrue(response.getBody().contains("\"descripcion\":\"El empleado perdió el transporte\""));

        assertTrue(response.getBody().contains("\"codigo\":\"PERDIDA_SUMINISTRO\""));
        assertTrue(response.getBody().contains("\"descripcion\":\"Hubo pérdida de suministro eléctrico\""));
        assertTrue(response.getBody().contains("\"tipoExcusa\":\"Moderada - Pérdida Suministro\""));

        assertTrue(response.getBody().contains("\"codigo\":\"CUIDADO_FAMILIAR\""));
        assertTrue(response.getBody().contains("\"descripcion\":\"Necesita cuidar a un familiar\""));
        assertTrue(response.getBody().contains("\"tipoExcusa\":\"Moderada - Cuidado Familiar\""));

        assertTrue(response.getBody().contains("\"codigo\":\"IRRELEVANTE\""));
        assertTrue(response.getBody().contains("\"descripcion\":\"Motivo irrelevante o complejo\""));
        assertTrue(response.getBody().contains("\"tipoExcusa\":\"Compleja\""));

        assertTrue(response.getBody().contains("\"codigo\":\"INCREIBLE_INVEROSIMIL\""));
        assertTrue(response.getBody().contains("\"descripcion\":\"Motivo increíble e inverosímil\""));
        assertTrue(response.getBody().contains("\"tipoExcusa\":\"Inverosímil\""));
    }

    @Test
    public void testObtenerTiposDeExcusa_DebeRetornarTodosLosTiposConFormatoEsperado() throws Exception {
        // When
        ResponseEntity<String> response = restTemplate.getForEntity(getBaseUrl() + "/tipos", String.class);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        // Verificar que es un array JSON
        assertTrue(response.getBody().startsWith("["));
        assertTrue(response.getBody().endsWith("]"));

        // Verificar que contiene todos los tipos esperados
        assertTrue(response.getBody().contains("\"ExcusaTrivial\""));
        assertTrue(response.getBody().contains("\"ExcusaModerada\""));
        assertTrue(response.getBody().contains("\"ExcusaPerdidaSuministro\""));
        assertTrue(response.getBody().contains("\"ExcusaCuidadoFamiliar\""));
        assertTrue(response.getBody().contains("\"ExcusaCompleja\""));
        assertTrue(response.getBody().contains("\"ExcusaInverosimil\""));
    }
}
