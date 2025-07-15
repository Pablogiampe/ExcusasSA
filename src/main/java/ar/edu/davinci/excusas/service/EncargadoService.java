package ar.edu.davinci.excusas.service;

import ar.edu.davinci.excusas.dto.EncargadoDTO;
import ar.edu.davinci.excusas.mapper.EncargadoMapper;
import ar.edu.davinci.excusas.model.empleados.encargados.*;
import ar.edu.davinci.excusas.model.empleados.encargados.modos.*;
import ar.edu.davinci.excusas.model.entities.EncargadoEntity;
import ar.edu.davinci.excusas.model.mail.EmailSenderImpl;
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

    private final EncargadoRepository encargadoRepository;
    private final EncargadoMapper encargadoMapper;

    @Autowired
    public EncargadoService(EncargadoRepository encargadoRepository, EncargadoMapper encargadoMapper) {
        this.encargadoRepository = encargadoRepository;
        this.encargadoMapper = encargadoMapper;
        inicializarEncargados();
    }

    private void inicializarEncargados() {
        if (encargadoRepository.count() == 0) {
            encargadoRepository.save(new EncargadoEntity("Ana García", "ana@excusas.com", 1001, "Recepcionista", "NORMAL"));
            encargadoRepository.save(new EncargadoEntity("Carlos López", "carlos@excusas.com", 1002, "SupervisorArea", "PRODUCTIVO"));
            encargadoRepository.save(new EncargadoEntity("María Rodríguez", "maria@excusas.com", 1003, "GerenteRRHH", "NORMAL"));
            encargadoRepository.save(new EncargadoEntity("Roberto Silva", "roberto@excusas.com", 1004, "CEO", "NORMAL"));
        }
    }

    @Transactional(readOnly = true)
    public List<EncargadoDTO> obtenerTodosLosEncargados() {
        return encargadoRepository.findAll().stream()
                .map(encargadoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<EncargadoDTO> obtenerEncargadoPorLegajo(int legajo) {
        return encargadoRepository.findById(legajo)
                .map(encargadoMapper::toDTO);
    }

    public EncargadoDTO cambiarModoEncargado(int legajo, String tipoModo) {
        Optional<EncargadoEntity> encargadoOpt = encargadoRepository.findById(legajo);

        if (encargadoOpt.isEmpty()) {
            throw new IllegalArgumentException("No se encontró encargado con legajo: " + legajo);
        }

        // Validar que el modo sea válido
        validarModo(tipoModo);

        EncargadoEntity encargado = encargadoOpt.get();
        encargado.setModo(tipoModo.toUpperCase());
        
        EncargadoEntity encargadoActualizado = encargadoRepository.save(encargado);
        return encargadoMapper.toDTO(encargadoActualizado);
    }

    public EncargadoDTO crearEncargado(String nombre, String email, int legajo,
                                      String tipoEncargado, String tipoModo) {
        if (encargadoRepository.existsByLegajo(legajo)) {
            throw new IllegalArgumentException("Ya existe un encargado con el legajo: " + legajo);
        }
        
        if (encargadoRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Ya existe un encargado con el email: " + email);
        }

        // Validar tipo de encargado y modo
        validarTipoEncargado(tipoEncargado);
        validarModo(tipoModo);

        EncargadoEntity nuevoEncargado = new EncargadoEntity(nombre, email, legajo, tipoEncargado.toUpperCase(), tipoModo.toUpperCase());
        EncargadoEntity encargadoGuardado = encargadoRepository.save(nuevoEncargado);

        return encargadoMapper.toDTO(encargadoGuardado);
    }

    private void validarModo(String tipoModo) {
        try {
            switch (tipoModo.toUpperCase()) {
                case "NORMAL":
                case "PRODUCTIVO":
                case "VAGO":
                    break;
                default:
                    throw new IllegalArgumentException("Modo no válido: " + tipoModo);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Modo no válido: " + tipoModo);
        }
    }

    private void validarTipoEncargado(String tipo) {
        try {
            switch (tipo.toUpperCase()) {
                case "RECEPCIONISTA":
                case "SUPERVISORAREA":
                case "GERENTERRHH":
                case "CEO":
                    break;
                default:
                    throw new IllegalArgumentException("Tipo de encargado no válido: " + tipo);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Tipo de encargado no válido: " + tipo);
        }
    }
}
