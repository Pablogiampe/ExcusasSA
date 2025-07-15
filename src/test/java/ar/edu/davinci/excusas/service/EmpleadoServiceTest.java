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
        // Given
        List<EmpleadoEntity> entities = Arrays.asList(empleadoEntity);
        List<EmpleadoDTO> expectedDTOs = Arrays.asList(empleadoDTO);
        
        when(empleadoRepository.findAll()).thenReturn(entities);
        when(empleadoMapper.toDTO(empleadoEntity)).thenReturn(empleadoDTO);
        
        // When
        List<EmpleadoDTO> result = empleadoService.obtenerTodosLosEmpleados();
        
        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNombre()).isEqualTo("Juan Pérez");
        verify(empleadoRepository).findAll();
        verify(empleadoMapper).toDTO(empleadoEntity);
    }
    
    @Test
    void testObtenerEmpleadoPorLegajo() {
        // Given
        when(empleadoRepository.findByLegajo(1001)).thenReturn(Optional.of(empleadoEntity));
        when(empleadoMapper.toDTO(empleadoEntity)).thenReturn(empleadoDTO);
        
        // When
        Optional<EmpleadoDTO> result = empleadoService.obtenerEmpleadoPorLegajo(1001);
        
        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getNombre()).isEqualTo("Juan Pérez");
        verify(empleadoRepository).findByLegajo(1001);
        verify(empleadoMapper).toDTO(empleadoEntity);
    }
    
    @Test
    void testCrearEmpleado() {
        // Given
        when(empleadoRepository.existsByLegajo(1001)).thenReturn(false);
        when(empleadoRepository.existsByEmail("juan@test.com")).thenReturn(false);
        when(empleadoMapper.toEntity(empleadoDTO)).thenReturn(empleadoEntity);
        when(empleadoRepository.save(empleadoEntity)).thenReturn(empleadoEntity);
        when(empleadoMapper.toDTO(empleadoEntity)).thenReturn(empleadoDTO);
        
        // When
        EmpleadoDTO result = empleadoService.crearEmpleado(empleadoDTO);
        
        // Then
        assertThat(result.getNombre()).isEqualTo("Juan Pérez");
        verify(empleadoRepository).existsByLegajo(1001);
        verify(empleadoRepository).existsByEmail("juan@test.com");
        verify(empleadoRepository).save(empleadoEntity);
    }
    
    @Test
    void testCrearEmpleadoConLegajoExistente() {
        // Given
        when(empleadoRepository.existsByLegajo(1001)).thenReturn(true);
        
        // When & Then
        assertThatThrownBy(() -> empleadoService.crearEmpleado(empleadoDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Ya existe un empleado con el legajo: 1001");
        
        verify(empleadoRepository).existsByLegajo(1001);
        verify(empleadoRepository, never()).save(any());
    }
    
    @Test
    void testCrearEmpleadoConEmailExistente() {
        // Given
        when(empleadoRepository.existsByLegajo(1001)).thenReturn(false);
        when(empleadoRepository.existsByEmail("juan@test.com")).thenReturn(true);
        
        // When & Then
        assertThatThrownBy(() -> empleadoService.crearEmpleado(empleadoDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Ya existe un empleado con el email: juan@test.com");
        
        verify(empleadoRepository).existsByLegajo(1001);
        verify(empleadoRepository).existsByEmail("juan@test.com");
        verify(empleadoRepository, never()).save(any());
    }
}
