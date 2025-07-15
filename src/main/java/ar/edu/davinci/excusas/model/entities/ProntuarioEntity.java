package ar.edu.davinci.excusas.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "prontuarios")
public class ProntuarioEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @Column(nullable = false)
    private LocalDateTime fechaCreacion;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empleado_legajo", nullable = false)
    private EmpleadoEntity empleado;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "excusa_id", nullable = false)
    private ExcusaEntity excusa;
    
    public ProntuarioEntity() {}
    
    public ProntuarioEntity(EmpleadoEntity empleado, ExcusaEntity excusa) {
        this.empleado = empleado;
        this.excusa = excusa;
        this.fechaCreacion = LocalDateTime.now();
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    
    public EmpleadoEntity getEmpleado() { return empleado; }
    public void setEmpleado(EmpleadoEntity empleado) { this.empleado = empleado; }
    
    public ExcusaEntity getExcusa() { return excusa; }
    public void setExcusa(ExcusaEntity excusa) { this.excusa = excusa; }
}
