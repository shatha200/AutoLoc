package tn.esprit.autolocapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO représentant un équipement optionnel d'un véhicule")
public class EquipementDTO {

    @Schema(description = "Identifiant de l'équipement", example = "1")
    private Long idEquipement;

    @Schema(description = "Libellé de l'équipement", example = "GPS Intégré")
    @NotBlank(message = "Le libellé de l'équipement est obligatoire")
    @Size(max = 100, message = "Le libellé ne doit pas dépasser 100 caractères")
    private String libelle;
}
