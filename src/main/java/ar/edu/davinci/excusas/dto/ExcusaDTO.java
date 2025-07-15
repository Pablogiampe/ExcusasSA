package ar.edu.davinci.excusas.dto;

import java.time.LocalDateTime;

public class ExcusaDTO {
    
    private Long id;
    private String motivo;
    private Integer legajoEmpleado;
    private String nombreEmpleado;
    private String emailEmpleado;
    private String tipoExcusa;
    private LocalDateTime fechaCreacion;
    private Boolean aceptada;
    private String encargadoQueManejo;
    
    public ExcusaDTO() {}
    
    public ExcusaDTO(Long id, String motivo, Integer legajoEmpleado, String nombreEmpleado, 
                     String emailEmpleado, String tipoExcusa, LocalDateTime fechaCreacion,
                     Boolean aceptada, String encargadoQueManejo) {
        this.id = id;
        this.motivo = motivo;
        this.legajoEmpleado = legajoEmpleado;
        this.nombreEmpleado = nombreEmpleado;
        this.emailEmpleado = emailEmpleado;
        this.tipoExcusa = tipoExcusa;
        this.fechaCreacion = fechaCreacion;
        this.aceptada = aceptada;
        this.encargadoQueManejo = encargadoQueManejo;
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    
    public Integer getLegajoEmpleado() { return legajoEmpleado; }
    public void setLegajoEmpleado(Integer legajoEmpleado) { this.legajoEmpleado = legajoEmpleado; }
    
    public String getNombreEmpleado() { return nombreEmpleado; }
    public void setNombreEmpleado(String nombreEmpleado) { this.nombreEmpleado = nombreEmpleado; }
    
    public String getEmailEmpleado() { return emailEmpleado; }
    public void setEmailEmpleado(String emailEmpleado) { this.emailEmpleado = emailEmpleado; }
    
    public String getTipoExcusa() { return tipoExcusa; }
    public void setTipoExcusa(String tipoExcusa) { this.tipoExcusa = tipoExcusa; }
    
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    
    public Boolean getAceptada() { return aceptada; }
    public void setAceptada(Boolean aceptada) { this.aceptada = aceptada; }
    
    public String getEncargadoQueManejo() { return encargadoQueManejo; }
    public void setEncargadoQueManejo(String encargadoQueManejo) { this.encargadoQueManejo = encargadoQueManejo; }
}
