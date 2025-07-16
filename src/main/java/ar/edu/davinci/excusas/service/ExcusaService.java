package ar.edu.davinci.excusas.service;

import ar.edu.davinci.excusas.dto.ExcusaDTO;
import ar.edu.davinci.excusas.mapper.ExcusaMapper;
import ar.edu.davinci.excusas.model.entities.EmpleadoEntity;
import ar.edu.davinci.excusas.model.entities.ExcusaEntity;
import ar.edu.davinci.excusas.model.entities.ProntuarioEntity;
import ar.edu.davinci.excusas.model.excusas.MotivoExcusa;
import ar.edu.davinci.excusas.repository.EmpleadoRepository;
import ar.edu.davinci.excusas.repository.ExcusaRepository;
import ar.edu.davinci.excusas.repository.ProntuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ExcusaService {
    
    @Autowired
    private ExcusaRepository excusaRepository;
    
    @Autowired
    private EmpleadoRepository empleadoRepository;
    
    @Autowired
    private ProntuarioRepository prontuarioRepository;
    
    @Autowired
    private ExcusaMapper excusaMapper;
    
    @Autowired
    private EncargadoService encargadoService;
    
    public List<ExcusaDTO> obtenerTodasLasExcusas() {
        return excusaRepository.findAll()
                .stream()
                .map(excusaMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<ExcusaDTO> obtenerExcusasPorEmpleado(Integer legajo) {
        return excusaRepository.findByEmpleadoLegajo(legajo)
                .stream()
                .map(excusaMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<ExcusaDTO> obtenerExcusasRechazadas() {
        return excusaRepository.findByAceptada(false)
                .stream()
                .map(excusaMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<ExcusaDTO> buscarExcusas(Integer legajo, LocalDateTime fechaDesde, LocalDateTime fechaHasta) {
        if (legajo != null && fechaDesde != null && fechaHasta != null) {
            return excusaRepository.findByEmpleadoLegajoAndFechaCreacionBetween(legajo, fechaDesde, fechaHasta)
                    .stream()
                    .map(excusaMapper::toDTO)
                    .collect(Collectors.toList());
        } else if (fechaDesde != null && fechaHasta != null) {
            return excusaRepository.findByFechaCreacionBetween(fechaDesde, fechaHasta)
                    .stream()
                    .map(excusaMapper::toDTO)
                    .collect(Collectors.toList());
        } else if (legajo != null) {
            return obtenerExcusasPorEmpleado(legajo);
        } else {
            return obtenerTodasLasExcusas();
        }
    }
    
    public ExcusaDTO registrarExcusa(Integer legajoEmpleado, MotivoExcusa motivo1) {
        EmpleadoEntity empleado = empleadoRepository.findByLegajo(legajoEmpleado)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró empleado con legajo: " + legajoEmpleado));
        
        MotivoExcusa motivo;
        try {
            motivo = motivo1;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Motivo de excusa no válido: " + motivo1);
        }
        
        ExcusaEntity excusa = new ExcusaEntity();
        excusa.setMotivo(motivo);
        excusa.setTipoExcusa(determinarTipoExcusa(motivo));
        excusa.setEmpleado(empleado);
        excusa.setFechaCreacion(LocalDateTime.now());
        
        procesarExcusaConEncargados(excusa);
        
        ExcusaEntity excusaGuardada = excusaRepository.save(excusa);
        
        if (Boolean.FALSE.equals(excusa.getAceptada()) && "Miguel CEO".equals(excusa.getEncargadoQueManejo())) {
            ProntuarioEntity prontuario = new ProntuarioEntity(empleado, excusaGuardada);
            prontuarioRepository.save(prontuario);
        }
        
        return excusaMapper.toDTO(excusaGuardada);
    }
    
    private String determinarTipoExcusa(MotivoExcusa motivo) {
        switch (motivo) {
            case QUEDARSE_DORMIDO:
            case PERDI_TRANSPORTE:
                return "ExcusaTrivial";
            case CUIDADO_FAMILIAR:
                return "ExcusaCuidadoFamiliar";
            case PERDIDA_SUMINISTRO:
                return "ExcusaPerdidaSuministro";
            case INCREIBLE_INVEROSIMIL:
                return "ExcusaInverosimil";
            case IRRELEVANTE:
                return "ExcusaCompleja";
            default:
                return "ExcusaGenerica";
        }
    }
    
    private void procesarExcusaConEncargados(ExcusaEntity excusa) {
        String tipoExcusa = excusa.getTipoExcusa();
        
        if ("ExcusaTrivial".equals(tipoExcusa)) {
            excusa.setAceptada(true);
            excusa.setEncargadoQueManejo("Roberto Supervisor");
        } else if ("ExcusaCuidadoFamiliar".equals(tipoExcusa) || "ExcusaPerdidaSuministro".equals(tipoExcusa)) {
            excusa.setAceptada(true);
            excusa.setEncargadoQueManejo("Laura Gerente");
        } else {
            excusa.setAceptada(false);
            excusa.setEncargadoQueManejo("Miguel CEO");
        }
    }
    
    public int eliminarExcusasAnterioresA(LocalDateTime fechaLimite) {
        if (fechaLimite == null || fechaLimite.isAfter(LocalDateTime.now()) || fechaLimite.isEqual(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha límite no puede ser nula");
        }
        
        long count = excusaRepository.countByFechaCreacionBefore(fechaLimite);
        excusaRepository.deleteByFechaCreacionBefore(fechaLimite);
        
        return (int) count;
    }
    
    public Optional<ExcusaDTO> obtenerExcusaPorId(Long id) {
        return excusaRepository.findById(id)
                .map(excusaMapper::toDTO);
    }
    
    public List<ExcusaDTO> obtenerExcusasPorMotivo(MotivoExcusa motivo) {
        return excusaRepository.findByMotivo(motivo)
                .stream()
                .map(excusaMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<ExcusaDTO> obtenerExcusasPorEncargado(String encargado) {
        return excusaRepository.findByEncargadoQueManejo(encargado)
                .stream()
                .map(excusaMapper::toDTO)
                .collect(Collectors.toList());
    }
}
