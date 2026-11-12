package tn.esprit.autolocapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tn.esprit.autolocapi.domain.Maintenance;
import tn.esprit.autolocapi.dto.MaintenanceDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MaintenanceMapper {

    @Mapping(source = "vehicule.idVehicule", target = "vehiculeId")
    @Mapping(source = "vehicule.immatriculation", target = "vehiculeImmatriculation")
    MaintenanceDTO toDto(Maintenance maintenance);

    List<MaintenanceDTO> toDtoList(List<Maintenance> maintenances);

    @Mapping(target = "vehicule", ignore = true)
    Maintenance toEntity(MaintenanceDTO dto);
}
