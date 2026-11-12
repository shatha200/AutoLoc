package tn.esprit.autolocapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tn.esprit.autolocapi.domain.Vehicule;
import tn.esprit.autolocapi.dto.VehiculeDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VehiculeMapper {

    @Mapping(source = "agence.idAgence", target = "agenceId")
    @Mapping(source = "agence.nom", target = "agenceNom")
    VehiculeDTO toDto(Vehicule vehicule);

    List<VehiculeDTO> toDtoList(List<Vehicule> vehicules);

    @Mapping(target = "agence", ignore = true)
    @Mapping(target = "maintenances", ignore = true)
    @Mapping(target = "equipements", ignore = true)
    @Mapping(target = "reservations", ignore = true)
    Vehicule toEntity(VehiculeDTO dto);
}
