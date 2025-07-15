package ar.edu.davinci.excusas.model.entities;

import ar.edu.davinci.excusas.model.excusas.MotivoExcusa;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "excusas")
public class ExcusaEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @NotNull(message = "El motivo no puede ser nulo")
    @Column(name = "motivo", nullable = false)
    private MotivoExcusa motivo;
    
    @NotNull(message = "El tipo de excusa no puede ser nulo")
    @Column(name = "tipo_excusa", nullable = false)
    private String tipoExcusa;
    
    @NotNull(message = "La fecha de creación no puede ser nula")
    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;
    
    @Column(name = "aceptada")
    private Boolean aceptada;
    
    @Column(name = "encargado_que_manejo")
    private String encargadoQueManejo;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empleado_legajo", referencedColumnName = "legajo")
    @NotNull(message = "El empleado no puede ser nulo")
    private EmpleadoEntity empleado;
    
    @OneToMany(mappedBy = "excusa", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProntuarioEntity> prontuarios = new ArrayList<>();
    
    public ExcusaEntity() {
        this.fechaCreacion = LocalDateTime.now();
    }
    
    public ExcusaEntity(MotivoExcusa motivo, String tipoExcusa, EmpleadoEntity empleado) {
        this();
        this.motivo = motivo;
        this.tipoExcusa = tipoExcusa;
        this.empleado = empleado;
    }
    
    // Getters y Setters
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
    
    public List<ProntuarioEntity> getProntuarios() { return prontuarios; }
    public void setProntuarios(List<ProntuarioEntity> prontuarios) { this.prontuarios = prontuarios; }
    
    @Override
    public String toString() {
        return "ExcusaEntity{" +
                "id=" + id +
                ", motivo=" + motivo +
                ", tipoExcusa='" + tipoExcusa + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                ", aceptada=" + aceptada +
                ", encargadoQueManejo='" + encargadoQueManejo + '\'' +
                '}';
    }
}
