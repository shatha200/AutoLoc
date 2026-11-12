package tn.esprit.autolocapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tn.esprit.autolocapi.domain.Paiement;
import tn.esprit.autolocapi.dto.PaiementDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PaiementMapper {

    @Mapping(source = "contrat.idContrat", target = "contratId")
    PaiementDTO toDto(Paiement paiement);

    List<PaiementDTO> toDtoList(List<Paiement> paiements);

    @Mapping(target = "contrat", ignore = true)
    Paiement toEntity(PaiementDTO dto);
}
