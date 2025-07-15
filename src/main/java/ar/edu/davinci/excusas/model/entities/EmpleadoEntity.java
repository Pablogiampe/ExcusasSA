package ar.edu.davinci.excusas.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "empleados")
public class EmpleadoEntity {
    
    @Id
    @Positive(message = "El legajo debe ser un número positivo")
    @Column(name = "legajo", unique = true, nullable = false)
    private Integer legajo;
    
    @NotBlank(message = "El nombre no puede estar vacío")
    @Column(name = "nombre", nullable = false)
    private String nombre;
    
    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El email debe tener un formato válido")
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    
    @OneToMany(mappedBy = "empleado", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ExcusaEntity> excusas = new ArrayList<>();
    
    @OneToMany(mappedBy = "empleado", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProntuarioEntity> prontuarios = new ArrayList<>();
    
    public EmpleadoEntity() {}
    
    public EmpleadoEntity(String nombre, String email, Integer legajo) {
        this.nombre = nombre;
        this.email = email;
        this.legajo = legajo;
    }
    
    // Getters y Setters
    public Integer getLegajo() { return legajo; }
    public void setLegajo(Integer legajo) { this.legajo = legajo; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public List<ExcusaEntity> getExcusas() { return excusas; }
    public void setExcusas(List<ExcusaEntity> excusas) { this.excusas = excusas; }
    
    public List<ProntuarioEntity> getProntuarios() { return prontuarios; }
    public void setProntuarios(List<ProntuarioEntity> prontuarios) { this.prontuarios = prontuarios; }
    
    @Override
    public String toString() {
        return "EmpleadoEntity{" +
                "legajo=" + legajo +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
