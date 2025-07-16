package ar.edu.davinci.excusas.mapper;

import ar.edu.davinci.excusas.dto.ExcusaDTO;
import ar.edu.davinci.excusas.model.entities.ExcusaEntity;
import org.springframework.stereotype.Component;

@Component
public class ExcusaMapper {
    
    public ExcusaDTO toDTO(ExcusaEntity entity) {
        if (entity == null) {
            return null;
        }
        
        return new ExcusaDTO(
            entity.getId(),
            entity.getMotivo(),
            entity.getTipoExcusa(),
            entity.getFechaCreacion(),
            entity.getAceptada(),
            entity.getEncargadoQueManejo(),
            entity.getEmpleado() != null ? entity.getEmpleado().getLegajo() : null,
            entity.getEmpleado() != null ? entity.getEmpleado().getNombre() : null,
            entity.getEmpleado() != null ? entity.getEmpleado().getEmail() : null
        );
    }
    
    public ExcusaEntity toEntity(ExcusaDTO dto) {
        if (dto == null) {
            return null;
        }
        
        ExcusaEntity entity = new ExcusaEntity();
        entity.setId(dto.getId());
        entity.setMotivo(dto.getMotivo());
        entity.setTipoExcusa(dto.getTipoExcusa());
        entity.setFechaCreacion(dto.getFechaCreacion());
        entity.setAceptada(dto.getAceptada());
        entity.setEncargadoQueManejo(dto.getEncargadoQueManejo());
        
        return entity;
    }
}
