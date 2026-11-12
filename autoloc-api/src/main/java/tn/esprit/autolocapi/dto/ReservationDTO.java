package tn.esprit.autolocapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tn.esprit.autolocapi.domain.StatutReservation;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO représentant une réservation")
public class ReservationDTO {

    @Schema(description = "Identifiant de la réservation", example = "1")
    private Long idReservation;

    @Schema(description = "Date de début de location", example = "2026-05-01")
    @NotNull(message = "La date de début est obligatoire")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateDebut;

    @Schema(description = "Date de fin de location", example = "2026-05-07")
    @NotNull(message = "La date de fin est obligatoire")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateFin;

    @Schema(description = "Statut de la réservation (EN_ATTENTE, CONFIRMEE, ANNULEE, TERMINEE)", example = "CONFIRMEE")
    @NotNull(message = "Le statut est obligatoire")
    private StatutReservation statut;

    @Schema(description = "Identifiant du véhicule réservé", example = "1")
    private Long vehiculeId;

    @Schema(description = "Modèle du véhicule réservé", example = "Renault Clio")
    private String vehiculeModele;

    @Schema(description = "Identifiant du client ayant réservé", example = "1")
    private Long clientId;

    @Schema(description = "Nom complet du client", example = "Ahmed Ben Salem")
    private String clientNomComplet;

    @Schema(description = "Identifiant du contrat associé s'il existe", example = "1")
    private Long contratId;
}
