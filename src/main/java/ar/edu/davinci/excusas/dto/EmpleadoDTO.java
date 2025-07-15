package ar.edu.davinci.excusas.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class EmpleadoDTO {
    
    @Positive(message = "El legajo debe ser un número positivo")
    private Integer legajo;
    
    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;
    
    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El email debe tener un formato válido")
    private String email;
    
    public EmpleadoDTO() {}
    
    public EmpleadoDTO(Integer legajo, String nombre, String email) {
        this.legajo = legajo;
        this.nombre = nombre;
        this.email = email;
    }
    
    // Getters y Setters
    public Integer getLegajo() { return legajo; }
    public void setLegajo(Integer legajo) { this.legajo = legajo; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
