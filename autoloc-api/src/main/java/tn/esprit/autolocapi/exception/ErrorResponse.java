package tn.esprit.autolocapi.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Structure centralisée pour le retour des erreurs de l'API")
public class ErrorResponse {

    @Schema(description = "Horodatage de l'erreur", example = "2026-11-12T04:30:00")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    @Schema(description = "Code de statut HTTP", example = "400")
    private int status;

    @Schema(description = "Titre ou libellé court du statut HTTP", example = "Bad Request")
    private String error;

    @Schema(description = "Message explicatif détaillé de l'erreur", example = "Données invalides fournies")
    private String message;

    @Schema(description = "Chemin URI de la requête ayant causé l'erreur", example = "/api/vehicules")
    private String path;

    @Schema(description = "Détail des erreurs de validation par champ")
    private Map<String, String> validationErrors;
}
