package tn.esprit.autolocapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tn.esprit.autolocapi.domain.Reservation;
import tn.esprit.autolocapi.dto.ReservationDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

    @Mapping(source = "vehicule.idVehicule", target = "vehiculeId")
    @Mapping(source = "vehicule.modele", target = "vehiculeModele")
    @Mapping(source = "client.idClient", target = "clientId")
    @Mapping(target = "clientNomComplet", expression = "java(reservation.getClient() != null ? (reservation.getClient().getPrenom() + \" \" + reservation.getClient().getNom()) : null)")
    @Mapping(source = "contrat.idContrat", target = "contratId")
    ReservationDTO toDto(Reservation reservation);

    List<ReservationDTO> toDtoList(List<Reservation> reservations);

    @Mapping(target = "vehicule", ignore = true)
    @Mapping(target = "client", ignore = true)
    @Mapping(target = "contrat", ignore = true)
    Reservation toEntity(ReservationDTO dto);
}
