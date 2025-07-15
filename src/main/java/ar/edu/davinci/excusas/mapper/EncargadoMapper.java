package ar.edu.davinci.excusas.mapper;

import ar.edu.davinci.excusas.dto.EncargadoDTO;
import ar.edu.davinci.excusas.model.entities.EncargadoEntity;
import org.springframework.stereotype.Component;

@Component
public class EncargadoMapper {
    
    public EncargadoDTO toDTO(EncargadoEntity entity) {
        if (entity == null) {
            return null;
        }
        
        return new EncargadoDTO(
            entity.getLegajo(),
            entity.getNombre(),
            entity.getEmail(),
            entity.getTipoEncargado(),
            entity.getModo()
        );
    }
    
    public EncargadoEntity toEntity(EncargadoDTO dto) {
        if (dto == null) {
            return null;
        }
        
        return new EncargadoEntity(
            dto.getNombre(),
            dto.getEmail(),
            dto.getLegajo(),
            dto.getTipoEncargado(),
            dto.getModo()
        );
    }
}
