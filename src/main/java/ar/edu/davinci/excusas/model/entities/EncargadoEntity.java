package ar.edu.davinci.excusas.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "encargados")
public class EncargadoEntity {
    
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
    
    @NotBlank(message = "El tipo de encargado no puede estar vacío")
    @Column(name = "tipo_encargado", nullable = false)
    private String tipoEncargado;
    
    @NotBlank(message = "El modo no puede estar vacío")
    @Column(name = "modo", nullable = false)
    private String modo;
    
    public EncargadoEntity() {}
    
    public EncargadoEntity(String nombre, String email, Integer legajo, String tipoEncargado, String modo) {
        this.nombre = nombre;
        this.email = email;
        this.legajo = legajo;
        this.tipoEncargado = tipoEncargado;
        this.modo = modo;
    }
    
    // Getters y Setters
    public Integer getLegajo() { return legajo; }
    public void setLegajo(Integer legajo) { this.legajo = legajo; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getTipoEncargado() { return tipoEncargado; }
    public void setTipoEncargado(String tipoEncargado) { this.tipoEncargado = tipoEncargado; }
    
    public String getModo() { return modo; }
    public void setModo(String modo) { this.modo = modo; }
    
    @Override
    public String toString() {
        return "EncargadoEntity{" +
                "legajo=" + legajo +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", tipoEncargado='" + tipoEncargado + '\'' +
                ", modo='" + modo + '\'' +
                '}';
    }
}
