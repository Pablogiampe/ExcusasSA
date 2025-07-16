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
        List<EmpleadoEntity> empleados = empleadoRepository.findAll();
        return empleados.stream()
                .map(empleadoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<EmpleadoDTO> obtenerEmpleadoPorLegajo(Integer legajo) {
        if (legajo == null || legajo <= 0) {
            throw new IllegalArgumentException("El legajo debe ser un número positivo");
        }

        Optional<EmpleadoEntity> empleado = empleadoRepository.findByLegajo(legajo);
        return empleado.map(empleadoMapper::toDTO);
    }

    public EmpleadoDTO crearEmpleado(EmpleadoDTO empleadoDTO) {
        if (empleadoDTO == null) {
            throw new IllegalArgumentException("Los datos del empleado no pueden ser nulos");
        }

        if (empleadoDTO.getLegajo() == null || empleadoDTO.getLegajo() <= 0) {
            throw new IllegalArgumentException("El legajo debe ser un número positivo");
        }

        if (empleadoDTO.getNombre() == null || empleadoDTO.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }

        if (empleadoDTO.getEmail() == null || empleadoDTO.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El email no puede estar vacío");
        }

        if (empleadoRepository.findByLegajo(empleadoDTO.getLegajo()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un empleado con el legajo: " + empleadoDTO.getLegajo());
        }

        if (empleadoRepository.findByEmail(empleadoDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un empleado con el email: " + empleadoDTO.getEmail());
        }

        EmpleadoEntity empleadoEntity = empleadoMapper.toEntity(empleadoDTO);
        EmpleadoEntity empleadoGuardado = empleadoRepository.save(empleadoEntity);
        return empleadoMapper.toDTO(empleadoGuardado);
    }

    public EmpleadoDTO actualizarEmpleado(Integer legajo, EmpleadoDTO empleadoDTO) {
        if (legajo == null || legajo <= 0) {
            throw new IllegalArgumentException("El legajo debe ser un número positivo");
        }

        if (empleadoDTO == null) {
            throw new IllegalArgumentException("Los datos del empleado no pueden ser nulos");
        }

        Optional<EmpleadoEntity> empleadoExistente = empleadoRepository.findByLegajo(legajo);
        if (empleadoExistente.isEmpty()) {
            throw new IllegalArgumentException("No se encontró un empleado con el legajo: " + legajo);
        }

        // Validaciones adicionales
        if (empleadoDTO.getNombre() == null || empleadoDTO.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }

        if (empleadoDTO.getEmail() == null || empleadoDTO.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El email no puede estar vacío");
        }

        Optional<EmpleadoEntity> empleadoConMismoEmail = empleadoRepository.findByEmail(empleadoDTO.getEmail());
        if (empleadoConMismoEmail.isPresent() && !empleadoConMismoEmail.get().getLegajo().equals(legajo)) {
            throw new IllegalArgumentException("Ya existe otro empleado con el email: " + empleadoDTO.getEmail());
        }

        EmpleadoEntity empleado = empleadoExistente.get();
        empleado.setNombre(empleadoDTO.getNombre());
        empleado.setEmail(empleadoDTO.getEmail());

        EmpleadoEntity empleadoActualizado = empleadoRepository.save(empleado);
        return empleadoMapper.toDTO(empleadoActualizado);
    }

    public void eliminarEmpleado(Integer legajo) {
        if (legajo == null || legajo <= 0) {
            throw new IllegalArgumentException("El legajo debe ser un número positivo");
        }

        Optional<EmpleadoEntity> empleado = empleadoRepository.findByLegajo(legajo);
        if (empleado.isEmpty()) {
            throw new IllegalArgumentException("No se encontró un empleado con el legajo: " + legajo);
        }

        empleadoRepository.delete(empleado.get());
    }

    public List<EmpleadoDTO> buscarEmpleadosPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de búsqueda no puede estar vacío");
        }

        List<EmpleadoEntity> empleados = empleadoRepository.findByNombreContaining(nombre.trim());
        return empleados.stream()
                .map(empleadoMapper::toDTO)
                .collect(Collectors.toList());
    }
}
