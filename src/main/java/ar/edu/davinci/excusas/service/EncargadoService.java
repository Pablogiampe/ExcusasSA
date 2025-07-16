package ar.edu.davinci.excusas.service;

import ar.edu.davinci.excusas.dto.EncargadoDTO;
import ar.edu.davinci.excusas.mapper.EncargadoMapper;
import ar.edu.davinci.excusas.model.entities.EncargadoEntity;
import ar.edu.davinci.excusas.repository.EncargadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class EncargadoService {

    @Autowired
    private EncargadoRepository encargadoRepository;

    @Autowired
    private EncargadoMapper encargadoMapper;

    private static final List<String> TIPOS_ENCARGADO_VALIDOS = Arrays.asList(
            "CEO", "GERENTE_RRHH", "SUPERVISOR_AREA", "RECEPCIONISTA", "RECHAZADOR"
    );

    private static final List<String> MODOS_VALIDOS = Arrays.asList(
            "NORMAL", "PRODUCTIVO", "VAGO"
    );

    public List<EncargadoDTO> obtenerTodosLosEncargados() {
        List<EncargadoEntity> encargados = encargadoRepository.findAll();
        return encargados.stream()
                .map(encargadoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<EncargadoDTO> obtenerEncargadoPorLegajo(Integer legajo) {
        if (legajo == null || legajo <= 0) {
            throw new IllegalArgumentException("El legajo debe ser un número positivo");
        }

        Optional<EncargadoEntity> encargado = encargadoRepository.findByLegajo(legajo);
        return encargado.map(encargadoMapper::toDTO);
    }

    public EncargadoDTO crearEncargado(EncargadoDTO encargadoDTO) {
        // Validaciones de negocio
        if (encargadoDTO == null) {
            throw new IllegalArgumentException("Los datos del encargado no pueden ser nulos");
        }

        if (encargadoDTO.getLegajo() == null || encargadoDTO.getLegajo() <= 0) {
            throw new IllegalArgumentException("El legajo debe ser un número positivo");
        }

        if (encargadoDTO.getNombre() == null || encargadoDTO.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }

        if (encargadoDTO.getEmail() == null || encargadoDTO.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El email no puede estar vacío");
        }

        if (encargadoDTO.getTipoEncargado() == null || encargadoDTO.getTipoEncargado().trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de encargado no puede estar vacío");
        }

        if (!TIPOS_ENCARGADO_VALIDOS.contains(encargadoDTO.getTipoEncargado().toUpperCase())) {
            throw new IllegalArgumentException("Tipo de encargado inválido. Valores válidos: " + TIPOS_ENCARGADO_VALIDOS);
        }

        if (encargadoDTO.getModo() == null || encargadoDTO.getModo().trim().isEmpty()) {
            throw new IllegalArgumentException("El modo no puede estar vacío");
        }

        if (!MODOS_VALIDOS.contains(encargadoDTO.getModo().toUpperCase())) {
            throw new IllegalArgumentException("Modo inválido. Valores válidos: " + MODOS_VALIDOS);
        }

        if (encargadoRepository.findByLegajo(encargadoDTO.getLegajo()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un encargado con el legajo: " + encargadoDTO.getLegajo());
        }

        if (encargadoRepository.findByEmail(encargadoDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un encargado con el email: " + encargadoDTO.getEmail());
        }

        EncargadoEntity encargadoEntity = encargadoMapper.toEntity(encargadoDTO);
        EncargadoEntity encargadoGuardado = encargadoRepository.save(encargadoEntity);
        return encargadoMapper.toDTO(encargadoGuardado);
    }

    public EncargadoDTO cambiarModoEncargado(Integer legajo, String nuevoModo) {
        if (legajo == null || legajo <= 0) {
            throw new IllegalArgumentException("El legajo debe ser un número positivo");
        }

        if (nuevoModo == null || nuevoModo.trim().isEmpty()) {
            throw new IllegalArgumentException("El nuevo modo no puede estar vacío");
        }

        if (!MODOS_VALIDOS.contains(nuevoModo.toUpperCase())) {
            throw new IllegalArgumentException("Modo inválido. Valores válidos: " + MODOS_VALIDOS);
        }

        Optional<EncargadoEntity> encargadoExistente = encargadoRepository.findByLegajo(legajo);
        if (encargadoExistente.isEmpty()) {
            throw new IllegalArgumentException("No se encontró un encargado con el legajo: " + legajo);
        }

        EncargadoEntity encargado = encargadoExistente.get();
        encargado.setModo(nuevoModo.toUpperCase());

        EncargadoEntity encargadoActualizado = encargadoRepository.save(encargado);
        return encargadoMapper.toDTO(encargadoActualizado);
    }

    public void eliminarEncargado(Integer legajo) {
        if (legajo == null || legajo <= 0) {
            throw new IllegalArgumentException("El legajo debe ser un número positivo");
        }

        Optional<EncargadoEntity> encargado = encargadoRepository.findByLegajo(legajo);
        if (encargado.isEmpty()) {
            throw new IllegalArgumentException("No se encontró un encargado con el legajo: " + legajo);
        }

        encargadoRepository.delete(encargado.get());
    }
}
