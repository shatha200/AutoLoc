package tn.esprit.autolocapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tn.esprit.autolocapi.domain.Contrat;
import tn.esprit.autolocapi.dto.ContratDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ContratMapper {

    @Mapping(source = "reservation.idReservation", target = "reservationId")
    ContratDTO toDto(Contrat contrat);

    List<ContratDTO> toDtoList(List<Contrat> contrats);

    @Mapping(target = "reservation", ignore = true)
    @Mapping(target = "paiements", ignore = true)
    Contrat toEntity(ContratDTO dto);
}
