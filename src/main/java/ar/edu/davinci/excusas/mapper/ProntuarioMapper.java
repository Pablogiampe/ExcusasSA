package ar.edu.davinci.excusas.mapper;

import ar.edu.davinci.excusas.dto.ProntuarioDTO;
import ar.edu.davinci.excusas.model.entities.ProntuarioEntity;
import ar.edu.davinci.excusas.model.prontuario.Prontuario;
import org.springframework.stereotype.Component;

@Component
public class ProntuarioMapper {
    
    public ProntuarioDTO toDTO(ProntuarioEntity entity) {
        if (entity == null) return null;
        
        return new ProntuarioDTO(
            entity.getId(),
            entity.getEmpleado().getLegajo(),
            entity.getEmpleado().getNombre(),
            entity.getEmpleado().getEmail(),
            entity.getExcusa().getMotivo().toString(),
            entity.getExcusa().getTipoExcusa(),
            entity.getFechaCreacion()
        );
    }
    
    public ProntuarioDTO toDTO(Prontuario prontuario) {
        if (prontuario == null) return null;
        
        return new ProntuarioDTO(
            null, // ID se asigna en la base de datos
            prontuario.getEmpleado().getLegajo(),
            prontuario.getEmpleado().getNombre(),
            prontuario.getEmpleado().getEmail(),
            prontuario.getExcusa().getMotivo().toString(),
            prontuario.getExcusa().getClass().getSimpleName(),
            null // Fecha se asigna en la entidad
        );
    }
}
