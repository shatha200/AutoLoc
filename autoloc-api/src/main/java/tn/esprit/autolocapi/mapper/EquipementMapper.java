package tn.esprit.autolocapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tn.esprit.autolocapi.domain.Equipement;
import tn.esprit.autolocapi.dto.EquipementDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EquipementMapper {

    EquipementDTO toDto(Equipement equipement);

    List<EquipementDTO> toDtoList(List<Equipement> equipements);

    @Mapping(target = "vehicules", ignore = true)
    Equipement toEntity(EquipementDTO dto);
}
