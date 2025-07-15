package ar.edu.davinci.excusas.service;

import ar.edu.davinci.excusas.dto.EncargadoDTO;
import ar.edu.davinci.excusas.mapper.EncargadoMapper;
import ar.edu.davinci.excusas.model.entities.EncargadoEntity;
import ar.edu.davinci.excusas.repository.EncargadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    
    public List<EncargadoDTO> obtenerTodosLosEncargados() {
        return encargadoRepository.findAllOrderedByHierarchy()
                .stream()
                .map(encargadoMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    public Optional<EncargadoDTO> obtenerEncargadoPorLegajo(Integer legajo) {
        return encargadoRepository.findByLegajo(legajo)
                .map(encargadoMapper::toDTO);
    }
    
    public EncargadoDTO crearEncargado(EncargadoDTO encargadoDTO) {
        if (encargadoRepository.existsByLegajo(encargadoDTO.getLegajo())) {
            throw new IllegalArgumentException("Ya existe un encargado con el legajo: " + encargadoDTO.getLegajo());
        }
        
        if (encargadoRepository.existsByEmail(encargadoDTO.getEmail())) {
            throw new IllegalArgumentException("Ya existe un encargado con el email: " + encargadoDTO.getEmail());
        }
        
        // Validar tipo de encargado
        if (!esTipoEncargadoValido(encargadoDTO.getTipoEncargado())) {
            throw new IllegalArgumentException("Tipo de encargado no válido: " + encargadoDTO.getTipoEncargado());
        }
        
        // Validar modo
        if (!esModoValido(encargadoDTO.getModo())) {
            throw new IllegalArgumentException("Modo no válido: " + encargadoDTO.getModo());
        }
        
        EncargadoEntity entity = encargadoMapper.toEntity(encargadoDTO);
        EncargadoEntity savedEntity = encargadoRepository.save(entity);
        return encargadoMapper.toDTO(savedEntity);
    }
    
    public EncargadoDTO cambiarModoEncargado(Integer legajo, String nuevoModo) {
        EncargadoEntity encargado = encargadoRepository.findByLegajo(legajo)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró encargado con legajo: " + legajo));
        
        if (!esModoValido(nuevoModo)) {
            throw new IllegalArgumentException("Modo no válido: " + nuevoModo);
        }
        
        encargado.setModo(nuevoModo);
        
        EncargadoEntity updatedEntity = encargadoRepository.save(encargado);
        return encargadoMapper.toDTO(updatedEntity);
    }
    
    public void eliminarEncargado(Integer legajo) {
        if (!encargadoRepository.existsByLegajo(legajo)) {
            throw new IllegalArgumentException("No se encontró encargado con legajo: " + legajo);
        }
        encargadoRepository.deleteById(legajo);
    }
    
    public List<EncargadoDTO> obtenerEncargadosPorTipo(String tipoEncargado) {
        return encargadoRepository.findByTipoEncargado(tipoEncargado)
                .stream()
                .map(encargadoMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<EncargadoDTO> obtenerEncargadosPorModo(String modo) {
        return encargadoRepository.findByModo(modo)
                .stream()
                .map(encargadoMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    private boolean esTipoEncargadoValido(String tipo) {
        return tipo != null && (
            tipo.equals("Recepcionista") ||
            tipo.equals("SupervisorArea") ||
            tipo.equals("GerenteRRHH") ||
            tipo.equals("CEO")
        );
    }
    
    private boolean esModoValido(String modo) {
        return modo != null && (
            modo.equals("NORMAL") ||
            modo.equals("PRODUCTIVO") ||
            modo.equals("VAGO")
        );
    }
    
    public boolean existeEncargado(Integer legajo) {
        return encargadoRepository.existsByLegajo(legajo);
    }
}
