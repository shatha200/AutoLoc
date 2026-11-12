package tn.esprit.autolocapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tn.esprit.autolocapi.domain.Client;
import tn.esprit.autolocapi.dto.ClientDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    ClientDTO toDto(Client client);

    List<ClientDTO> toDtoList(List<Client> clients);

    @Mapping(target = "reservations", ignore = true)
    Client toEntity(ClientDTO dto);
}
