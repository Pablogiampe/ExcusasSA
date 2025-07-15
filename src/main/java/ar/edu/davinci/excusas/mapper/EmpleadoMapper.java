package ar.edu.davinci.excusas.mapper;

import ar.edu.davinci.excusas.dto.EmpleadoDTO;
import ar.edu.davinci.excusas.model.empleados.Empleado;
import ar.edu.davinci.excusas.model.entities.EmpleadoEntity;
import org.springframework.stereotype.Component;

@Component
public class EmpleadoMapper {
    
    public EmpleadoDTO toDTO(EmpleadoEntity entity) {
        if (entity == null) return null;
        
        return new EmpleadoDTO(
            entity.getLegajo(),
            entity.getNombre(),
            entity.getEmail()
        );
    }
    
    public EmpleadoDTO toDTO(Empleado empleado) {
        if (empleado == null) return null;
        
        return new EmpleadoDTO(
            empleado.getLegajo(),
            empleado.getNombre(),
            empleado.getEmail()
        );
    }
    
    public EmpleadoEntity toEntity(EmpleadoDTO dto) {
        if (dto == null) return null;
        
        return new EmpleadoEntity(
            dto.getNombre(),
            dto.getEmail(),
            dto.getLegajo()
        );
    }
    
    public Empleado toDomainModel(EmpleadoEntity entity) {
        if (entity == null) return null;
        
        return new Empleado(
            entity.getNombre(),
            entity.getEmail(),
            entity.getLegajo()
        );
    }
    
    public EmpleadoEntity toEntity(Empleado empleado) {
        if (empleado == null) return null;
        
        return new EmpleadoEntity(
            empleado.getNombre(),
            empleado.getEmail(),
            empleado.getLegajo()
        );
    }
}
