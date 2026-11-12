package tn.esprit.autolocapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tn.esprit.autolocapi.domain.Employe;
import tn.esprit.autolocapi.dto.EmployeDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployeMapper {

    @Mapping(source = "agence.idAgence", target = "agenceId")
    @Mapping(source = "agence.nom", target = "agenceNom")
    EmployeDTO toDto(Employe employe);

    List<EmployeDTO> toDtoList(List<Employe> employes);

    @Mapping(target = "agence", ignore = true)
    Employe toEntity(EmployeDTO dto);
}
