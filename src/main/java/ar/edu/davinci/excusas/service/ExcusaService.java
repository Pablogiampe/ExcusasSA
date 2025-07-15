package ar.edu.davinci.excusas.service;

import ar.edu.davinci.excusas.dto.ExcusaDTO;
import ar.edu.davinci.excusas.mapper.EmpleadoMapper;
import ar.edu.davinci.excusas.mapper.ExcusaMapper;
import ar.edu.davinci.excusas.model.empleados.Empleado;
import ar.edu.davinci.excusas.model.entities.EmpleadoEntity;
import ar.edu.davinci.excusas.model.entities.ExcusaEntity;
import ar.edu.davinci.excusas.model.excusas.Excusa;
import ar.edu.davinci.excusas.model.excusas.MotivoExcusa;
import ar.edu.davinci.excusas.repository.EmpleadoRepository;
import ar.edu.davinci.excusas.repository.ExcusaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ExcusaService {

    private final ExcusaRepository excusaRepository;
    private final EmpleadoRepository empleadoRepository;
    private final ExcusaMapper excusaMapper;
    private final EmpleadoMapper empleadoMapper;
    private final EmpleadoService empleadoService;

    @Autowired
    public ExcusaService(ExcusaRepository excusaRepository,
                        EmpleadoRepository empleadoRepository,
                        ExcusaMapper excusaMapper,
                        EmpleadoMapper empleadoMapper,
                        EmpleadoService empleadoService) {
        this.excusaRepository = excusaRepository;
        this.empleadoRepository = empleadoRepository;
        this.excusaMapper = excusaMapper;
        this.empleadoMapper = empleadoMapper;
        this.empleadoService = empleadoService;
    }

    public ExcusaDTO registrarExcusa(int legajo, MotivoExcusa motivo) {
        Optional<EmpleadoEntity> empleadoEntityOpt = empleadoRepository.findById(legajo);

        if (empleadoEntityOpt.isEmpty()) {
            throw new IllegalArgumentException("No se encontró empleado con legajo: " + legajo);
        }

        EmpleadoEntity empleadoEntity = empleadoEntityOpt.get();
        Empleado empleado = empleadoMapper.toDomainModel(empleadoEntity);
        
        // Generar excusa usando el modelo de dominio
        Excusa excusa = empleado.generarYEnviarExcusa(motivo, empleadoService.getLineaEncargados());
        
        // Crear entidad para persistir
        ExcusaEntity excusaEntity = new ExcusaEntity(motivo, excusa.getClass().getSimpleName(), empleadoEntity);
        ExcusaEntity excusaGuardada = excusaRepository.save(excusaEntity);

        return excusaMapper.toDTO(excusaGuardada);
    }

    @Transactional(readOnly = true)
    public List<ExcusaDTO> obtenerExcusasPorEmpleado(int legajo) {
        return excusaRepository.findByEmpleadoLegajo(legajo).stream()
                .map(excusaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ExcusaDTO> obtenerTodasLasExcusas(LocalDate fechaDesde, LocalDate fechaHasta, String motivo) {
        LocalDateTime fechaDesdeTime = fechaDesde != null ? fechaDesde.atStartOfDay() : null;
        LocalDateTime fechaHastaTime = fechaHasta != null ? fechaHasta.atTime(23, 59, 59) : null;
        MotivoExcusa motivoEnum = motivo != null ? MotivoExcusa.valueOf(motivo.toUpperCase()) : null;
        
        return excusaRepository.findByFiltros(fechaDesdeTime, fechaHastaTime, motivoEnum).stream()
                .map(excusaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ExcusaDTO> buscarExcusas(int legajo, LocalDate fechaDesde, LocalDate fechaHasta) {
        LocalDateTime fechaDesdeTime = fechaDesde != null ? fechaDesde.atStartOfDay() : null;
        LocalDateTime fechaHastaTime = fechaHasta != null ? fechaHasta.atTime(23, 59, 59) : null;
        
        return excusaRepository.findByEmpleadoAndFechas(legajo, fechaDesdeTime, fechaHastaTime).stream()
                .map(excusaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ExcusaDTO> obtenerExcusasRechazadas() {
        return excusaRepository.findByAceptada(false).stream()
                .map(excusaMapper::toDTO)
                .collect(Collectors.toList());
    }

    public int eliminarExcusas(LocalDate fechaLimite) {
        if (fechaLimite == null) {
            throw new IllegalArgumentException("La fecha límite no puede ser nula");
        }

        LocalDateTime fechaLimiteTime = fechaLimite.atStartOfDay();
        long cantidadEliminadas = excusaRepository.countByFechaCreacionBefore(fechaLimiteTime);
        
        excusaRepository.deleteByFechaCreacionBefore(fechaLimiteTime);

        return (int) cantidadEliminadas;
    }

    @Transactional(readOnly = true)
    public List<ExcusaDTO> obtenerTodasLasExcusas() {
        return excusaRepository.findAll().stream()
                .map(excusaMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void clearExcusas() {
        excusaRepository.deleteAll();
    }
}
