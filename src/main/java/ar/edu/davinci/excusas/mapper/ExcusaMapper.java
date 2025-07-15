package ar.edu.davinci.excusas.mapper;

import ar.edu.davinci.excusas.dto.ExcusaDTO;
import ar.edu.davinci.excusas.model.entities.ExcusaEntity;
import ar.edu.davinci.excusas.model.excusas.Excusa;
import org.springframework.stereotype.Component;

@Component
public class ExcusaMapper {
    
    public ExcusaDTO toDTO(ExcusaEntity entity) {
        if (entity == null) return null;
        
        return new ExcusaDTO(
            entity.getId(),
            entity.getMotivo().toString(),
            entity.getEmpleado().getLegajo(),
            entity.getEmpleado().getNombre(),
            entity.getEmpleado().getEmail(),
            entity.getTipoExcusa(),
            entity.getFechaCreacion(),
            entity.getAceptada(),
            entity.getEncargadoQueManejo()
        );
    }
    
    public ExcusaDTO toDTO(Excusa excusa) {
        if (excusa == null) return null;
        
        return new ExcusaDTO(
            null, // ID se asigna en la base de datos
            excusa.getMotivo().toString(),
            excusa.getEmpleado().getLegajo(),
            excusa.getEmpleado().getNombre(),
            excusa.getEmpleado().getEmail(),
            excusa.getClass().getSimpleName(),
            null, // Fecha se asigna en la entidad
            null, // Se actualiza después del procesamiento
            null  // Se actualiza después del procesamiento
        );
    }
}
