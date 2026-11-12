package tn.esprit.autolocapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tn.esprit.autolocapi.domain.CategorieVehicule;
import tn.esprit.autolocapi.domain.StatutVehicule;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO représentant un véhicule")
public class VehiculeDTO {

    @Schema(description = "Identifiant du véhicule", example = "1")
    private Long idVehicule;

    @Schema(description = "Numéro d'immatriculation unique", example = "234-TN-5678")
    @NotBlank(message = "L'immatriculation est obligatoire")
    @Size(max = 20, message = "L'immatriculation ne doit pas dépasser 20 caractères")
    private String immatriculation;

    @Schema(description = "Marque du véhicule", example = "Renault")
    @NotBlank(message = "La marque est obligatoire")
    @Size(max = 50, message = "La marque ne doit pas dépasser 50 caractères")
    private String marque;

    @Schema(description = "Modèle du véhicule", example = "Clio")
    @NotBlank(message = "Le modèle est obligatoire")
    @Size(max = 50, message = "Le modèle ne doit pas dépasser 50 caractères")
    private String modele;

    @Schema(description = "Catégorie du véhicule", example = "CITADINE")
    @NotNull(message = "La catégorie est obligatoire")
    private CategorieVehicule categorie;

    @Schema(description = "Tarif journalier en dinars", example = "85.50")
    @NotNull(message = "Le tarif journalier est obligatoire")
    @PositiveOrZero(message = "Le tarif journalier ne peut pas être négatif")
    private BigDecimal tarifJournalier;

    @Schema(description = "Statut actuel du véhicule", example = "DISPONIBLE")
    @NotNull(message = "Le statut est obligatoire")
    private StatutVehicule statut;

    @Schema(description = "Identifiant de l'agence de rattachement", example = "1")
    private Long agenceId;

    @Schema(description = "Nom de l'agence de rattachement", example = "Agence Tunis Centre")
    private String agenceNom;
}
