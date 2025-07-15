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
    
    @NotNull(message = "La fecha de creación no puede ser nula")
    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empleado_legajo", referencedColumnName = "legajo")
    @NotNull(message = "El empleado no puede ser nulo")
    private EmpleadoEntity empleado;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "excusa_id")
    @NotNull(message = "La excusa no puede ser nula")
    private ExcusaEntity excusa;
    
    public ProntuarioEntity() {
        this.fechaCreacion = LocalDateTime.now();
    }
    
    public ProntuarioEntity(EmpleadoEntity empleado, ExcusaEntity excusa) {
        this();
        this.empleado = empleado;
        this.excusa = excusa;
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
    
    @Override
    public String toString() {
        return "ProntuarioEntity{" +
                "id=" + id +
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }
}
