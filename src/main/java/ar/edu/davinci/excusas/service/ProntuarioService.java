package ar.edu.davinci.excusas.service;

import ar.edu.davinci.excusas.dto.ProntuarioDTO;
import ar.edu.davinci.excusas.mapper.ProntuarioMapper;
import ar.edu.davinci.excusas.repository.ProntuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProntuarioService {

    private final ProntuarioRepository prontuarioRepository;
    private final ProntuarioMapper prontuarioMapper;

    @Autowired
    public ProntuarioService(ProntuarioRepository prontuarioRepository, ProntuarioMapper prontuarioMapper) {
        this.prontuarioRepository = prontuarioRepository;
        this.prontuarioMapper = prontuarioMapper;
    }

    @Transactional(readOnly = true)
    public List<ProntuarioDTO> obtenerTodosLosProntuarios() {
        return prontuarioRepository.findAll().stream()
                .map(prontuarioMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProntuarioDTO> obtenerProntuariosPorLegajo(int legajo) {
        return prontuarioRepository.findByEmpleadoLegajo(legajo).stream()
                .map(prontuarioMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void limpiarProntuarios() {
        prontuarioRepository.deleteAll();
    }
}
