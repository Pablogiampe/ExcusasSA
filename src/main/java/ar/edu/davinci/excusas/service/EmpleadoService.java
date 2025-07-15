package ar.edu.davinci.excusas.service;

import ar.edu.davinci.excusas.dto.EmpleadoDTO;
import ar.edu.davinci.excusas.dto.ExcusaDTO;
import ar.edu.davinci.excusas.mapper.EmpleadoMapper;
import ar.edu.davinci.excusas.mapper.ExcusaMapper;
import ar.edu.davinci.excusas.model.empleados.Empleado;
import ar.edu.davinci.excusas.model.empleados.encargados.LineaEncargados;
import ar.edu.davinci.excusas.model.entities.EmpleadoEntity;
import ar.edu.davinci.excusas.model.entities.ExcusaEntity;
import ar.edu.davinci.excusas.model.excusas.Excusa;
import ar.edu.davinci.excusas.model.excusas.MotivoExcusa;
import ar.edu.davinci.excusas.model.mail.EmailSenderImpl;
import ar.edu.davinci.excusas.repository.EmpleadoRepository;
import ar.edu.davinci.excusas.repository.ExcusaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmpleadoService {

    private final EmpleadoRepository empleadoRepository;
    private final ExcusaRepository excusaRepository;
    private final EmpleadoMapper empleadoMapper;
    private final ExcusaMapper excusaMapper;
    private final LineaEncargados lineaEncargados;

    @Autowired
    public EmpleadoService(EmpleadoRepository empleadoRepository, 
                          ExcusaRepository excusaRepository,
                          EmpleadoMapper empleadoMapper,
                          ExcusaMapper excusaMapper) {
        this.empleadoRepository = empleadoRepository;
        this.excusaRepository = excusaRepository;
        this.empleadoMapper = empleadoMapper;
        this.excusaMapper = excusaMapper;
        this.lineaEncargados = new LineaEncargados(new EmailSenderImpl());
        inicializarEmpleados();
    }

    private void inicializarEmpleados() {
        if (empleadoRepository.count() == 0) {
            empleadoRepository.save(new EmpleadoEntity("Juan Pérez", "juan.perez@empresa.com", 2001));
            empleadoRepository.save(new EmpleadoEntity("María García", "maria.garcia@empresa.com", 2002));
            empleadoRepository.save(new EmpleadoEntity("Carlos López", "carlos.lopez@empresa.com", 2003));
        }
    }

    @Transactional(readOnly = true)
    public List<EmpleadoDTO> obtenerTodosLosEmpleados() {
        return empleadoRepository.findAll().stream()
                .map(empleadoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<EmpleadoDTO> obtenerEmpleadoPorLegajo(int legajo) {
        return empleadoRepository.findById(legajo)
                .map(empleadoMapper::toDTO);
    }

    public EmpleadoDTO crearEmpleado(String nombre, String email, int legajo) {
        if (empleadoRepository.existsByLegajo(legajo)) {
            throw new IllegalArgumentException("Ya existe un empleado con el legajo: " + legajo);
        }
        
        if (empleadoRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Ya existe un empleado con el email: " + email);
        }

        EmpleadoEntity nuevoEmpleado = new EmpleadoEntity(nombre, email, legajo);
        EmpleadoEntity empleadoGuardado = empleadoRepository.save(nuevoEmpleado);
        
        return empleadoMapper.toDTO(empleadoGuardado);
    }

    public ExcusaDTO generarExcusa(int legajo, MotivoExcusa motivo) {
        Optional<EmpleadoEntity> empleadoEntityOpt = empleadoRepository.findById(legajo);

        if (empleadoEntityOpt.isEmpty()) {
            throw new IllegalArgumentException("No se encontró empleado con legajo: " + legajo);
        }

        EmpleadoEntity empleadoEntity = empleadoEntityOpt.get();
        Empleado empleado = empleadoMapper.toDomainModel(empleadoEntity);
        
        // Generar excusa usando el modelo de dominio
        Excusa excusa = empleado.generarYEnviarExcusa(motivo, lineaEncargados);
        
        // Crear entidad para persistir
        ExcusaEntity excusaEntity = new ExcusaEntity(motivo, excusa.getClass().getSimpleName(), empleadoEntity);
        ExcusaEntity excusaGuardada = excusaRepository.save(excusaEntity);
        
        return excusaMapper.toDTO(excusaGuardada);
    }

    public boolean eliminarEmpleado(int legajo) {
        if (empleadoRepository.existsByLegajo(legajo)) {
            empleadoRepository.deleteById(legajo);
            return true;
        }
        return false;
    }

    @Transactional(readOnly = true)
    public boolean existeEmpleadoConLegajo(int legajo) {
        return empleadoRepository.existsByLegajo(legajo);
    }

    public LineaEncargados getLineaEncargados() {
        return lineaEncargados;
    }
}
