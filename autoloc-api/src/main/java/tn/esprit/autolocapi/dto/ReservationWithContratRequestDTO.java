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
@Schema(description = "DTO de requête pour créer une réservation et son contrat associé en une seule opération")
public class ReservationWithContratRequestDTO {

    // ---- Données de la réservation ----
    @Schema(description = "Date de début de location", example = "2026-06-01")
    @NotNull(message = "La date de début est obligatoire")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateDebut;

    @Schema(description = "Date de fin de location", example = "2026-06-07")
    @NotNull(message = "La date de fin est obligatoire")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateFin;

    @Schema(description = "Statut initial de la réservation", example = "EN_ATTENTE")
    @NotNull(message = "Le statut est obligatoire")
    private StatutReservation statut;

    @Schema(description = "Identifiant du véhicule à réserver", example = "1")
    private Long vehiculeId;

    @Schema(description = "Identifiant du client effectuant la réservation", example = "1")
    private Long clientId;

    // ---- Données du contrat associé ----
    @Schema(description = "Date de signature du contrat", example = "2026-06-01")
    @NotNull(message = "La date de signature du contrat est obligatoire")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateSignature;
}
