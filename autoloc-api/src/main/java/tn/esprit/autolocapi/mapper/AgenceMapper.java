package tn.esprit.autolocapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tn.esprit.autolocapi.domain.Agence;
import tn.esprit.autolocapi.dto.AgenceDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AgenceMapper {

    AgenceDTO toDto(Agence agence);

    List<AgenceDTO> toDtoList(List<Agence> agences);

    @Mapping(target = "employes", ignore = true)
    @Mapping(target = "vehicules", ignore = true)
    Agence toEntity(AgenceDTO dto);
}
