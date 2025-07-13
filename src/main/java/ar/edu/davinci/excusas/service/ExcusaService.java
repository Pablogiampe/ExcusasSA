package ar.edu.davinci.excusas.service;

import ar.edu.davinci.excusas.model.empleados.Empleado;
import ar.edu.davinci.excusas.model.excusas.Excusa;
import ar.edu.davinci.excusas.model.excusas.MotivoExcusa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExcusaService {

    private final List<Excusa> excusas = new ArrayList<>();
    private final EmpleadoService empleadoService;

    @Autowired
    public ExcusaService(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    public Excusa registrarExcusa(int legajo, MotivoExcusa motivo) {
        Optional<Empleado> empleadoOpt = empleadoService.obtenerEmpleadoPorLegajo(legajo);

        if (empleadoOpt.isEmpty()) {
            throw new IllegalArgumentException("No se encontró empleado con legajo: " + legajo);
        }

        Empleado empleado = empleadoOpt.get();
        Excusa excusa = empleado.generarYEnviarExcusa(motivo, empleadoService.getLineaEncargados());
        excusas.add(excusa);

        return excusa;
    }

    public List<Excusa> obtenerExcusasPorEmpleado(int legajo) {
        return excusas.stream()
                .filter(e -> e.getEmpleado().getLegajo() == legajo)
                .collect(Collectors.toList());
    }

    public List<Excusa> obtenerTodasLasExcusas(LocalDate fechaDesde, LocalDate fechaHasta, String motivo) {
        return excusas.stream()
                .filter(e -> fechaDesde == null || !LocalDate.now().isBefore(fechaDesde))
                .filter(e -> fechaHasta == null || !LocalDate.now().isAfter(fechaHasta))
                .filter(e -> motivo == null || e.getMotivo().toString().equalsIgnoreCase(motivo))
                .collect(Collectors.toList());
    }

    public List<Excusa> buscarExcusas(int legajo, LocalDate fechaDesde, LocalDate fechaHasta) {
        return excusas.stream()
                .filter(e -> e.getEmpleado().getLegajo() == legajo)
                .filter(e -> fechaDesde == null || !LocalDate.now().isBefore(fechaDesde))
                .filter(e -> fechaHasta == null || !LocalDate.now().isAfter(fechaHasta))
                .collect(Collectors.toList());
    }

    public List<Excusa> obtenerExcusasRechazadas() {
        // Por ahora retorna lista vacía, se puede implementar lógica específica
        return new ArrayList<>();
    }

    public int eliminarExcusas(LocalDate fechaLimite) {
        if (fechaLimite == null) {
            throw new IllegalArgumentException("La fecha límite no puede ser nula");
        }

        int cantidadEliminadas = (int) excusas.stream()
                .filter(e -> LocalDate.now().isBefore(fechaLimite))
                .count();

        excusas.removeIf(e -> LocalDate.now().isBefore(fechaLimite));

        return cantidadEliminadas;
    }

    public List<Excusa> obtenerTodasLasExcusas() {
        return new ArrayList<>(excusas);
    }

    public void clearExcusas() {
        excusas.clear();
    }
}
