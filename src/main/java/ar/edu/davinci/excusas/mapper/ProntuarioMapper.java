package ar.edu.davinci.excusas.mapper;

import ar.edu.davinci.excusas.dto.ProntuarioDTO;
import ar.edu.davinci.excusas.model.entities.ProntuarioEntity;
import org.springframework.stereotype.Component;

@Component
public class ProntuarioMapper {
    
    public ProntuarioDTO toDTO(ProntuarioEntity entity) {
        if (entity == null) {
            return null;
        }
        
        return new ProntuarioDTO(
            entity.getId(),
            entity.getFechaCreacion(),
            entity.getEmpleado() != null ? entity.getEmpleado().getLegajo() : null,
            entity.getEmpleado() != null ? entity.getEmpleado().getNombre() : null,
            entity.getEmpleado() != null ? entity.getEmpleado().getEmail() : null,
            entity.getExcusa() != null ? entity.getExcusa().getId() : null,
            entity.getExcusa() != null ? entity.getExcusa().getMotivo().toString() : null,
            entity.getExcusa() != null ? entity.getExcusa().getTipoExcusa() : null
        );
    }
    
    public ProntuarioEntity toEntity(ProntuarioDTO dto) {
        if (dto == null) {
            return null;
        }
        
        ProntuarioEntity entity = new ProntuarioEntity();
        entity.setId(dto.getId());
        entity.setFechaCreacion(dto.getFechaCreacion());
        
        return entity;
    }
}
