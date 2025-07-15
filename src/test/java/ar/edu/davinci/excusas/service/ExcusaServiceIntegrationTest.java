package ar.edu.davinci.excusas.service;

import ar.edu.davinci.excusas.dto.ExcusaDTO;
import ar.edu.davinci.excusas.model.excusas.MotivoExcusa;
import ar.edu.davinci.excusas.repository.ExcusaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ExcusaServiceIntegrationTest {

    @Autowired
    private ExcusaService excusaService;

    @Autowired
    private ExcusaRepository excusaRepository;

    @Test
    public void testRegistrarExcusaPersisteDatos() {
        // Given
        int legajo = 2001; // Empleado que existe por defecto
        MotivoExcusa motivo = MotivoExcusa.PERDIDA_SUMINISTRO;

        // When
        ExcusaDTO excusaRegistrada = excusaService.registrarExcusa(legajo, motivo);

        // Then
        assertNotNull(excusaRegistrada);
        assertEquals(motivo.toString(), excusaRegistrada.getMotivo());
        assertEquals(legajo, excusaRegistrada.getLegajoEmpleado());

        // Verificar que se persistió en la base de datos
        List<ar.edu.davinci.excusas.model.entities.ExcusaEntity> excusasEnDB = excusaRepository.findByEmpleadoLegajo(legajo);
        assertTrue(excusasEnDB.stream().anyMatch(e -> e.getMotivo() == motivo));
    }

    @Test
    public void testObtenerExcusasPorEmpleadoRecuperaDeDatos() {
        // Given
        int legajo = 2002;
        excusaService.registrarExcusa(legajo, MotivoExcusa.CUIDADO_FAMILIAR);
        excusaService.registrarExcusa(legajo, MotivoExcusa.PERDI_TRANSPORTE);

        // When
        List<ExcusaDTO> excusas = excusaService.obtenerExcusasPorEmpleado(legajo);

        // Then
        assertEquals(2, excusas.size());
        assertTrue(excusas.stream().allMatch(e -> e.getLegajoEmpleado().equals(legajo)));
        assertTrue(excusas.stream().anyMatch(e -> e.getMotivo().equals("CUIDADO_FAMILIAR")));
        assertTrue(excusas.stream().anyMatch(e -> e.getMotivo().equals("PERDI_TRANSPORTE")));
    }

    @Test
    public void testObtenerTodasLasExcusasRecuperaDeDatos() {
        // Given
        excusaService.registrarExcusa(2001, MotivoExcusa.QUEDARSE_DORMIDO);
        excusaService.registrarExcusa(2002, MotivoExcusa.PERDIDA_SUMINISTRO);

        // When
        List<ExcusaDTO> todasLasExcusas = excusaService.obtenerTodasLasExcusas(null, null, null);

        // Then
        assertTrue(todasLasExcusas.size() >= 2);
        assertTrue(todasLasExcusas.stream().anyMatch(e -> e.getMotivo().equals("QUEDARSE_DORMIDO")));
        assertTrue(todasLasExcusas.stream().anyMatch(e -> e.getMotivo().equals("PERDIDA_SUMINISTRO")));
    }

    @Test
    public void testBuscarExcusasConFiltrosFuncionaCorrectamente() {
        // Given
        int legajo = 2003;
        excusaService.registrarExcusa(legajo, MotivoExcusa.IRRELEVANTE);
        excusaService.registrarExcusa(legajo, MotivoExcusa.CUIDADO_FAMILIAR);

        LocalDate hoy = LocalDate.now();

        // When
        List<ExcusaDTO> excusasFiltradas = excusaService.buscarExcusas(legajo, hoy, hoy);

        // Then
        assertEquals(2, excusasFiltradas.size());
        assertTrue(excusasFiltradas.stream().allMatch(e -> e.getLegajoEmpleado().equals(legajo)));
    }

    @Test
    public void testEliminarExcusasEliminaDeDatos() {
        // Given
        excusaService.registrarExcusa(2001, MotivoExcusa.QUEDARSE_DORMIDO);
        excusaService.registrarExcusa(2002, MotivoExcusa.PERDI_TRANSPORTE);
        
        long countAntes = excusaRepository.count();
        assertTrue(countAntes >= 2);

        LocalDate fechaFutura = LocalDate.now().plusDays(1);

        // When
        int cantidadEliminadas = excusaService.eliminarExcusas(fechaFutura);

        // Then
        assertTrue(cantidadEliminadas >= 2);
        long countDespues = excusaRepository.count();
        assertTrue(countDespues < countAntes);
    }

    @Test
    public void testRegistrarExcusaConEmpleadoInexistenteFalla() {
        // Given
        int legajoInexistente = 9999;

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            excusaService.registrarExcusa(legajoInexistente, MotivoExcusa.QUEDARSE_DORMIDO);
        });
    }
}
