package ar.edu.davinci.excusas.model.entities;

import ar.edu.davinci.excusas.model.excusas.MotivoExcusa;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "excusas")
public class ExcusaEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MotivoExcusa motivo;
    
    @NotNull
    @Column(nullable = false)
    private String tipoExcusa;
    
    @NotNull
    @Column(nullable = false)
    private LocalDateTime fechaCreacion;
    
    @Column
    private Boolean aceptada;
    
    @Column
    private String encargadoQueManejo;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empleado_legajo", nullable = false)
    private EmpleadoEntity empleado;
    
    @OneToOne(mappedBy = "excusa", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ProntuarioEntity prontuario;
    
    public ExcusaEntity() {}
    
    public ExcusaEntity(MotivoExcusa motivo, String tipoExcusa, EmpleadoEntity empleado) {
        this.motivo = motivo;
        this.tipoExcusa = tipoExcusa;
        this.empleado = empleado;
        this.fechaCreacion = LocalDateTime.now();
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
    
    public EmpleadoEntity getEmpleado() { return empleado; }
    public void setEmpleado(EmpleadoEntity empleado) { this.empleado = empleado; }
    
    public ProntuarioEntity getProntuario() { return prontuario; }
    public void setProntuario(ProntuarioEntity prontuario) { this.prontuario = prontuario; }
}
