package tn.esprit.autolocapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO représentant une agence")
public class AgenceDTO {

    @Schema(description = "Identifiant de l'agence (généré automatiquement)", example = "1")
    private Long idAgence;

    @Schema(description = "Nom de l'agence", example = "Agence Tunis Centre")
    @NotBlank(message = "Le nom de l'agence est obligatoire")
    @Size(max = 100, message = "Le nom de l'agence ne doit pas dépasser 100 caractères")
    private String nom;

    @Schema(description = "Ville de l'agence", example = "Tunis")
    @NotBlank(message = "La ville est obligatoire")
    @Size(max = 50, message = "La ville ne doit pas dépasser 50 caractères")
    private String ville;

    @Schema(description = "Adresse complète de l'agence", example = "12 Avenue Habib Bourguiba")
    @NotBlank(message = "L'adresse est obligatoire")
    @Size(max = 150, message = "L'adresse ne doit pas dépasser 150 caractères")
    private String adresse;

    @Schema(description = "Numéro de téléphone de contact", example = "+216 71 123 456")
    @NotBlank(message = "Le numéro de téléphone est obligatoire")
    @Pattern(regexp = "^[+0-9\\s-]{8,20}$", message = "Le format du numéro de téléphone est invalide")
    private String telephone;
}
