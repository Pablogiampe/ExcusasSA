package ar.edu.davinci.excusas.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "encargados")
public class EncargadoEntity {
    
    @Id
    @Positive
    private Integer legajo;
    
    @NotBlank
    @Column(nullable = false)
    private String nombre;
    
    @NotBlank
    @Email
    @Column(nullable = false, unique = true)
    private String email;
    
    @NotBlank
    @Column(nullable = false)
    private String tipoEncargado;
    
    @NotBlank
    @Column(nullable = false)
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
}
