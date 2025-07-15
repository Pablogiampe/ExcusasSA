package ar.edu.davinci.excusas.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class EncargadoDTO {
    
    @Positive(message = "El legajo debe ser un número positivo")
    private Integer legajo;
    
    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;
    
    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El email debe tener un formato válido")
    private String email;
    
    @NotBlank(message = "El tipo de encargado no puede estar vacío")
    private String tipoEncargado;
    
    @NotBlank(message = "El modo no puede estar vacío")
    private String modo;
    
    public EncargadoDTO() {}
    
    public EncargadoDTO(Integer legajo, String nombre, String email, String tipoEncargado, String modo) {
        this.legajo = legajo;
        this.nombre = nombre;
        this.email = email;
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
        return "EncargadoDTO{" +
                "legajo=" + legajo +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", tipoEncargado='" + tipoEncargado + '\'' +
                ", modo='" + modo + '\'' +
                '}';
    }
}
