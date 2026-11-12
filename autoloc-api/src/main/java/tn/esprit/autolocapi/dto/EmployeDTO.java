package tn.esprit.autolocapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tn.esprit.autolocapi.domain.RoleEmploye;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO représentant un employé")
public class EmployeDTO {

    @Schema(description = "Identifiant de l'employé", example = "1")
    private Long idEmploye;

    @Schema(description = "Nom de famille de l'employé", example = "Trabelsi")
    @NotBlank(message = "Le nom de l'employé est obligatoire")
    @Size(max = 50, message = "Le nom ne doit pas dépasser 50 caractères")
    private String nom;

    @Schema(description = "Prénom de l'employé", example = "Sami")
    @NotBlank(message = "Le prénom de l'employé est obligatoire")
    @Size(max = 50, message = "Le prénom ne doit pas dépasser 50 caractères")
    private String prenom;

    @Schema(description = "Rôle ou poste de l'employé (AGENT, MANAGER)", example = "AGENT")
    @NotNull(message = "Le rôle de l'employé est obligatoire")
    private RoleEmploye role;

    @Schema(description = "Identifiant de l'agence affectée", example = "1")
    private Long agenceId;

    @Schema(description = "Nom de l'agence affectée", example = "Agence Tunis Centre")
    private String agenceNom;
}
