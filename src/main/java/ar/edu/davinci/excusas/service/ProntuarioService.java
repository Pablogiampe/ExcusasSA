package ar.edu.davinci.excusas.service;

import ar.edu.davinci.excusas.dto.ProntuarioDTO;
import ar.edu.davinci.excusas.mapper.ProntuarioMapper;
import ar.edu.davinci.excusas.model.entities.ProntuarioEntity;
import ar.edu.davinci.excusas.repository.ProntuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ProntuarioService {
    
    @Autowired
    private ProntuarioRepository prontuarioRepository;
    
    @Autowired
    private ProntuarioMapper prontuarioMapper;
    
    public List<ProntuarioDTO> obtenerTodosLosProntuarios() {
        return prontuarioRepository.findAllOrderByFechaCreacionDesc()
                .stream()
                .map(prontuarioMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<ProntuarioDTO> obtenerProntuariosPorEmpleado(Integer legajo) {
        return prontuarioRepository.findByEmpleadoLegajo(legajo)
                .stream()
                .map(prontuarioMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<ProntuarioDTO> obtenerProntuariosPorExcusa(Long excusaId) {
        return prontuarioRepository.findByExcusaId(excusaId)
                .stream()
                .map(prontuarioMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<ProntuarioDTO> obtenerProntuariosPorFecha(LocalDateTime fechaDesde, LocalDateTime fechaHasta) {
        return prontuarioRepository.findByFechaCreacionBetween(fechaDesde, fechaHasta)
                .stream()
                .map(prontuarioMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<ProntuarioDTO> buscarProntuarios(Integer legajo, LocalDateTime fechaDesde, LocalDateTime fechaHasta) {
        if (legajo != null && fechaDesde != null && fechaHasta != null) {
            return prontuarioRepository.findByEmpleadoLegajoAndFechaCreacionBetween(legajo, fechaDesde, fechaHasta)
                    .stream()
                    .map(prontuarioMapper::toDTO)
                    .collect(Collectors.toList());
        } else if (fechaDesde != null && fechaHasta != null) {
            return obtenerProntuariosPorFecha(fechaDesde, fechaHasta);
        } else if (legajo != null) {
            return obtenerProntuariosPorEmpleado(legajo);
        } else {
            return obtenerTodosLosProntuarios();
        }
    }
    
    public Optional<ProntuarioDTO> obtenerProntuarioPorId(Long id) {
        return prontuarioRepository.findById(id)
                .map(prontuarioMapper::toDTO);
    }
}
