package ar.edu.davinci.excusas.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class ProntuarioDTO {
    
    private Long id;
    
    @NotNull(message = "La fecha de creación no puede ser nula")
    private LocalDateTime fechaCreacion;
    
    @NotNull(message = "El legajo del empleado no puede ser nulo")
    private Integer empleadoLegajo;
    
    private String empleadoNombre;
    
    private String empleadoEmail;
    
    @NotNull(message = "El ID de la excusa no puede ser nulo")
    private Long excusaId;
    
    private String excusaMotivo;
    
    private String excusaTipo;
    
    public ProntuarioDTO() {}
    
    public ProntuarioDTO(Long id, LocalDateTime fechaCreacion, Integer empleadoLegajo, 
                        String empleadoNombre, String empleadoEmail, Long excusaId, 
                        String excusaMotivo, String excusaTipo) {
        this.id = id;
        this.fechaCreacion = fechaCreacion;
        this.empleadoLegajo = empleadoLegajo;
        this.empleadoNombre = empleadoNombre;
        this.empleadoEmail = empleadoEmail;
        this.excusaId = excusaId;
        this.excusaMotivo = excusaMotivo;
        this.excusaTipo = excusaTipo;
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    
    public Integer getEmpleadoLegajo() { return empleadoLegajo; }
    public void setEmpleadoLegajo(Integer empleadoLegajo) { this.empleadoLegajo = empleadoLegajo; }
    
    public String getEmpleadoNombre() { return empleadoNombre; }
    public void setEmpleadoNombre(String empleadoNombre) { this.empleadoNombre = empleadoNombre; }
    
    public String getEmpleadoEmail() { return empleadoEmail; }
    public void setEmpleadoEmail(String empleadoEmail) { this.empleadoEmail = empleadoEmail; }
    
    public Long getExcusaId() { return excusaId; }
    public void setExcusaId(Long excusaId) { this.excusaId = excusaId; }
    
    public String getExcusaMotivo() { return excusaMotivo; }
    public void setExcusaMotivo(String excusaMotivo) { this.excusaMotivo = excusaMotivo; }
    
    public String getExcusaTipo() { return excusaTipo; }
    public void setExcusaTipo(String excusaTipo) { this.excusaTipo = excusaTipo; }
    
    @Override
    public String toString() {
        return "ProntuarioDTO{" +
                "id=" + id +
                ", fechaCreacion=" + fechaCreacion +
                ", empleadoLegajo=" + empleadoLegajo +
                ", empleadoNombre='" + empleadoNombre + '\'' +
                ", empleadoEmail='" + empleadoEmail + '\'' +
                ", excusaId=" + excusaId +
                ", excusaMotivo='" + excusaMotivo + '\'' +
                ", excusaTipo='" + excusaTipo + '\'' +
                '}';
    }
}
