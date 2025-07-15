package ar.edu.davinci.excusas.dto;

import java.time.LocalDateTime;

public class ProntuarioDTO {
    
    private Long id;
    private Integer legajoEmpleado;
    private String nombreEmpleado;
    private String emailEmpleado;
    private String motivoExcusa;
    private String tipoExcusa;
    private LocalDateTime fechaCreacion;
    
    public ProntuarioDTO() {}
    
    public ProntuarioDTO(Long id, Integer legajoEmpleado, String nombreEmpleado, String emailEmpleado,
                         String motivoExcusa, String tipoExcusa, LocalDateTime fechaCreacion) {
        this.id = id;
        this.legajoEmpleado = legajoEmpleado;
        this.nombreEmpleado = nombreEmpleado;
        this.emailEmpleado = emailEmpleado;
        this.motivoExcusa = motivoExcusa;
        this.tipoExcusa = tipoExcusa;
        this.fechaCreacion = fechaCreacion;
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Integer getLegajoEmpleado() { return legajoEmpleado; }
    public void setLegajoEmpleado(Integer legajoEmpleado) { this.legajoEmpleado = legajoEmpleado; }
    
    public String getNombreEmpleado() { return nombreEmpleado; }
    public void setNombreEmpleado(String nombreEmpleado) { this.nombreEmpleado = nombreEmpleado; }
    
    public String getEmailEmpleado() { return emailEmpleado; }
    public void setEmailEmpleado(String emailEmpleado) { this.emailEmpleado = emailEmpleado; }
    
    public String getMotivoExcusa() { return motivoExcusa; }
    public void setMotivoExcusa(String motivoExcusa) { this.motivoExcusa = motivoExcusa; }
    
    public String getTipoExcusa() { return tipoExcusa; }
    public void setTipoExcusa(String tipoExcusa) { this.tipoExcusa = tipoExcusa; }
    
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
