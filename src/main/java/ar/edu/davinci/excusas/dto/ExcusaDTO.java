package ar.edu.davinci.excusas.dto;

import ar.edu.davinci.excusas.model.excusas.MotivoExcusa;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class ExcusaDTO {
    
    private Long id;
    
    @NotNull(message = "El motivo no puede ser nulo")
    private MotivoExcusa motivo;
    
    @NotNull(message = "El tipo de excusa no puede ser nulo")
    private String tipoExcusa;
    
    @NotNull(message = "La fecha de creación no puede ser nula")
    private LocalDateTime fechaCreacion;
    
    private Boolean aceptada;
    
    private String encargadoQueManejo;
    
    @NotNull(message = "El legajo del empleado no puede ser nulo")
    private Integer empleadoLegajo;
    
    private String empleadoNombre;
    
    private String empleadoEmail;
    
    public ExcusaDTO() {}
    
    public ExcusaDTO(Long id, MotivoExcusa motivo, String tipoExcusa, LocalDateTime fechaCreacion, 
                     Boolean aceptada, String encargadoQueManejo, Integer empleadoLegajo, 
                     String empleadoNombre, String empleadoEmail) {
        this.id = id;
        this.motivo = motivo;
        this.tipoExcusa = tipoExcusa;
        this.fechaCreacion = fechaCreacion;
        this.aceptada = aceptada;
        this.encargadoQueManejo = encargadoQueManejo;
        this.empleadoLegajo = empleadoLegajo;
        this.empleadoNombre = empleadoNombre;
        this.empleadoEmail = empleadoEmail;
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public MotivoExcusa getMotivo() { return motivo; }
    public void setMotivo(MotivoExcusa motivo) { this.motivo = motivo; }
    
    public String getTipoExcusa() { return tipoExcusa; }
    public void setTipoExcusa(String tipoExcusa) { this.tipoExcusa = tipoExcusa; }
    
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    
    public Boolean getAceptada() { return aceptada; }
    public void setAceptada(Boolean aceptada) { this.aceptada = aceptada; }
    
    public String getEncargadoQueManejo() { return encargadoQueManejo; }
    public void setEncargadoQueManejo(String encargadoQueManejo) { this.encargadoQueManejo = encargadoQueManejo; }
    
    public Integer getEmpleadoLegajo() { return empleadoLegajo; }
    public void setEmpleadoLegajo(Integer empleadoLegajo) { this.empleadoLegajo = empleadoLegajo; }
    
    public String getEmpleadoNombre() { return empleadoNombre; }
    public void setEmpleadoNombre(String empleadoNombre) { this.empleadoNombre = empleadoNombre; }
    
    public String getEmpleadoEmail() { return empleadoEmail; }
    public void setEmpleadoEmail(String empleadoEmail) { this.empleadoEmail = empleadoEmail; }
    
    @Override
    public String toString() {
        return "ExcusaDTO{" +
                "id=" + id +
                ", motivo=" + motivo +
                ", tipoExcusa='" + tipoExcusa + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                ", aceptada=" + aceptada +
                ", encargadoQueManejo='" + encargadoQueManejo + '\'' +
                ", empleadoLegajo=" + empleadoLegajo +
                ", empleadoNombre='" + empleadoNombre + '\'' +
                ", empleadoEmail='" + empleadoEmail + '\'' +
                '}';
    }
}
