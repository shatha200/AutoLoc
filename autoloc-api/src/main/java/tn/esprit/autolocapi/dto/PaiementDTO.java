package tn.esprit.autolocapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tn.esprit.autolocapi.domain.ModePaiement;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO représentant un paiement effectué pour un contrat")
public class PaiementDTO {

    @Schema(description = "Identifiant du paiement", example = "1")
    private Long idPaiement;

    @Schema(description = "Montant du règlement", example = "250.00")
    @NotNull(message = "Le montant est obligatoire")
    @DecimalMin(value = "0.01", message = "Le montant doit être supérieur à zéro")
    private BigDecimal montant;

    @Schema(description = "Date de réalisation du paiement", example = "2026-04-10")
    @NotNull(message = "La date de paiement est obligatoire")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate datePaiement;

    @Schema(description = "Mode de paiement (CARTE, ESPECES, VIREMENT)", example = "CARTE")
    @NotNull(message = "Le mode de paiement est obligatoire")
    private ModePaiement modePaiement;

    @Schema(description = "Identifiant du contrat rattaché", example = "1")
    private Long contratId;
}
