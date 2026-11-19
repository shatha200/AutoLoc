package tn.esprit.autolocapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "DTO de réponse enrichi représentant une réservation avec ses associations")
public class ReservationResponseDTO {

    @Schema(description = "Identifiant de la réservation", example = "1")
    private Long idReservation;

    @Schema(description = "Date de début de location", example = "2026-05-01")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateDebut;

    @Schema(description = "Date de fin de location", example = "2026-05-07")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateFin;

    @Schema(description = "Statut de la réservation", example = "CONFIRMEE")
    private StatutReservation statut;

    // --- Client ---
    @Schema(description = "Identifiant du client", example = "1")
    private Long clientId;

    @Schema(description = "Nom complet du client", example = "Ahmed Ben Salem")
    private String clientNomComplet;

    @Schema(description = "Email du client", example = "ahmed.bensalem@mail.tn")
    private String clientEmail;

    @Schema(description = "Téléphone du client", example = "+216 20 000 000")
    private String clientTelephone;

    // --- Véhicule ---
    @Schema(description = "Identifiant du véhicule", example = "1")
    private Long vehiculeId;

    @Schema(description = "Immatriculation du véhicule", example = "234-TN-5678")
    private String vehiculeImmatriculation;

    @Schema(description = "Marque et modèle du véhicule", example = "Renault Clio")
    private String vehiculeMarqueModele;

    // --- Contrat ---
    @Schema(description = "Identifiant du contrat associé, s'il existe", example = "1")
    private Long contratId;
}
