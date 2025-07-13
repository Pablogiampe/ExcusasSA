package ar.edu.davinci.excusas.service;

import ar.edu.davinci.excusas.model.prontuario.AdministradorProntuarios;
import ar.edu.davinci.excusas.model.prontuario.Prontuario;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProntuarioService {

    public List<Prontuario> obtenerTodosLosProntuarios() {
        return AdministradorProntuarios.getInstance().getProntuarios();
    }

    public List<Prontuario> obtenerProntuariosPorLegajo(int legajo) {
        return AdministradorProntuarios.getInstance().getProntuarios()
                .stream()
                .filter(p -> p.getEmpleado().getLegajo() == legajo)
                .collect(Collectors.toList());
    }

    public void limpiarProntuarios() {
        AdministradorProntuarios.reset();
    }
}