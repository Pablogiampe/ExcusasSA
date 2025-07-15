package ar.edu.davinci.excusas.service;

import ar.edu.davinci.excusas.dto.EmpleadoDTO;
import ar.edu.davinci.excusas.mapper.EmpleadoMapper;
import ar.edu.davinci.excusas.model.entities.EmpleadoEntity;
import ar.edu.davinci.excusas.repository.EmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmpleadoService {
    
    @Autowired
    private EmpleadoRepository empleadoRepository;
    
    @Autowired
    private EmpleadoMapper empleadoMapper;
    
    public List<EmpleadoDTO> obtenerTodosLosEmpleados() {
        return empleadoRepository.findAll()
                .stream()
                .map(empleadoMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    public Optional<EmpleadoDTO> obtenerEmpleadoPorLegajo(Integer legajo) {
        return empleadoRepository.findByLegajo(legajo)
                .map(empleadoMapper::toDTO);
    }
    
    public EmpleadoDTO crearEmpleado(EmpleadoDTO empleadoDTO) {
        if (empleadoRepository.existsByLegajo(empleadoDTO.getLegajo())) {
            throw new IllegalArgumentException("Ya existe un empleado con el legajo: " + empleadoDTO.getLegajo());
        }
        
        if (empleadoRepository.existsByEmail(empleadoDTO.getEmail())) {
            throw new IllegalArgumentException("Ya existe un empleado con el email: " + empleadoDTO.getEmail());
        }
        
        EmpleadoEntity entity = empleadoMapper.toEntity(empleadoDTO);
        EmpleadoEntity savedEntity = empleadoRepository.save(entity);
        return empleadoMapper.toDTO(savedEntity);
    }
    
    public EmpleadoDTO actualizarEmpleado(Integer legajo, EmpleadoDTO empleadoDTO) {
        EmpleadoEntity existingEntity = empleadoRepository.findByLegajo(legajo)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró empleado con legajo: " + legajo));
        
        // Verificar si el email ya existe en otro empleado
        if (!existingEntity.getEmail().equals(empleadoDTO.getEmail()) && 
            empleadoRepository.existsByEmail(empleadoDTO.getEmail())) {
            throw new IllegalArgumentException("Ya existe un empleado con el email: " + empleadoDTO.getEmail());
        }
        
        existingEntity.setNombre(empleadoDTO.getNombre());
        existingEntity.setEmail(empleadoDTO.getEmail());
        
        EmpleadoEntity updatedEntity = empleadoRepository.save(existingEntity);
        return empleadoMapper.toDTO(updatedEntity);
    }
    
    public void eliminarEmpleado(Integer legajo) {
        if (!empleadoRepository.existsByLegajo(legajo)) {
            throw new IllegalArgumentException("No se encontró empleado con legajo: " + legajo);
        }
        empleadoRepository.deleteById(legajo);
    }
    
    public List<EmpleadoDTO> buscarEmpleadosPorNombre(String nombre) {
        return empleadoRepository.findByNombreContaining(nombre)
                .stream()
                .map(empleadoMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    public boolean existeEmpleado(Integer legajo) {
        return empleadoRepository.existsByLegajo(legajo);
    }
}
