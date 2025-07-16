package ar.edu.davinci.excusas.service;

import ar.edu.davinci.excusas.dto.EmpleadoDTO;
import ar.edu.davinci.excusas.mapper.EmpleadoMapper;
import ar.edu.davinci.excusas.model.entities.EmpleadoEntity;
import ar.edu.davinci.excusas.repository.EmpleadoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmpleadoServiceTest {

    @Mock
    private EmpleadoRepository empleadoRepository;

    @Mock
    private EmpleadoMapper empleadoMapper;

    @InjectMocks
    private EmpleadoService empleadoService;

    private EmpleadoEntity empleadoEntity;
    private EmpleadoDTO empleadoDTO;

    @BeforeEach
    void setUp() {
        empleadoEntity = new EmpleadoEntity("Juan Pérez", "juan@test.com", 1001);
        empleadoDTO = new EmpleadoDTO(1001, "Juan Pérez", "juan@test.com");
    }

    @Test
    void testObtenerTodosLosEmpleados() {
        List<EmpleadoEntity> entities = Arrays.asList(empleadoEntity);

        when(empleadoRepository.findAll()).thenReturn(entities);
        when(empleadoMapper.toDTO(empleadoEntity)).thenReturn(empleadoDTO);

        List<EmpleadoDTO> result = empleadoService.obtenerTodosLosEmpleados();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNombre()).isEqualTo("Juan Pérez");
        verify(empleadoRepository).findAll();
        verify(empleadoMapper).toDTO(empleadoEntity);
    }

    @Test
    void testObtenerEmpleadoPorLegajo() {
        when(empleadoRepository.findByLegajo(1001)).thenReturn(Optional.of(empleadoEntity));
        when(empleadoMapper.toDTO(empleadoEntity)).thenReturn(empleadoDTO);

        Optional<EmpleadoDTO> result = empleadoService.obtenerEmpleadoPorLegajo(1001);

        assertThat(result).isPresent();
        assertThat(result.get().getNombre()).isEqualTo("Juan Pérez");
        verify(empleadoRepository).findByLegajo(1001);
        verify(empleadoMapper).toDTO(empleadoEntity);
    }

    @Test
    void testCrearEmpleado() {
        // Mock para verificar que no existe empleado con el legajo
        when(empleadoRepository.findByLegajo(1001)).thenReturn(Optional.empty());
        // Mock para verificar que no existe empleado con el email
        when(empleadoRepository.findByEmail("juan@test.com")).thenReturn(Optional.empty());
        when(empleadoMapper.toEntity(empleadoDTO)).thenReturn(empleadoEntity);
        when(empleadoRepository.save(empleadoEntity)).thenReturn(empleadoEntity);
        when(empleadoMapper.toDTO(empleadoEntity)).thenReturn(empleadoDTO);

        EmpleadoDTO result = empleadoService.crearEmpleado(empleadoDTO);

        assertThat(result.getNombre()).isEqualTo("Juan Pérez");
        verify(empleadoRepository).findByLegajo(1001);
        verify(empleadoRepository).findByEmail("juan@test.com");
        verify(empleadoRepository).save(empleadoEntity);
    }

    @Test
    void testCrearEmpleadoConLegajoExistente() {
        // Mock para simular que ya existe un empleado con el legajo
        when(empleadoRepository.findByLegajo(1001)).thenReturn(Optional.of(empleadoEntity));

        assertThatThrownBy(() -> empleadoService.crearEmpleado(empleadoDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Ya existe un empleado con el legajo: 1001");

        verify(empleadoRepository).findByLegajo(1001);
        verify(empleadoRepository, never()).save(any());
    }

    @Test
    void testCrearEmpleadoConEmailExistente() {
        // Mock para simular que no existe empleado con el legajo pero sí con el email
        when(empleadoRepository.findByLegajo(1001)).thenReturn(Optional.empty());
        when(empleadoRepository.findByEmail("juan@test.com")).thenReturn(Optional.of(empleadoEntity));

        assertThatThrownBy(() -> empleadoService.crearEmpleado(empleadoDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Ya existe un empleado con el email: juan@test.com");

        verify(empleadoRepository).findByLegajo(1001);
        verify(empleadoRepository).findByEmail("juan@test.com");
        verify(empleadoRepository, never()).save(any());
    }
}
