package tn.esprit.autolocapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO représentant un contrat de location")
public class ContratDTO {

    @Schema(description = "Identifiant du contrat", example = "1")
    private Long idContrat;

    @Schema(description = "Date de signature du contrat", example = "2026-05-01")
    @NotNull(message = "La date de signature est obligatoire")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateSignature;

    @Schema(description = "Montant total de la location en dinars", example = "598.50")
    @NotNull(message = "Le montant total est obligatoire")
    @DecimalMin(value = "0.0", inclusive = true, message = "Le montant total ne peut pas être négatif")
    private BigDecimal montantTotal;

    @Schema(description = "Indique si le contrat a été validé", example = "true")
    private boolean valide;

    @Schema(description = "Identifiant de la réservation rattachée", example = "1")
    private Long reservationId;
}
