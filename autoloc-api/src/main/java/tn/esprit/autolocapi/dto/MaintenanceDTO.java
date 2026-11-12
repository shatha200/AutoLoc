package tn.esprit.autolocapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO représentant une opération de maintenance de véhicule")
public class MaintenanceDTO {

    @Schema(description = "Identifiant de la maintenance", example = "1")
    private Long idMaintenance;

    @Schema(description = "Date de début de la maintenance (format AAAA-MM-JJ)", example = "2026-03-01")
    @NotNull(message = "La date de début est obligatoire")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateDebut;

    @Schema(description = "Date de fin prévue ou effective (format AAAA-MM-JJ)", example = "2026-03-05")
    @NotNull(message = "La date de fin est obligatoire")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateFin;

    @Schema(description = "Description de l'intervention technique", example = "Vidange moteur et changement des plaquettes de frein")
    @NotBlank(message = "La description est obligatoire")
    @Size(max = 255, message = "La description ne doit pas dépasser 255 caractères")
    private String description;

    @Schema(description = "Identifiant du véhicule concerné", example = "1")
    private Long vehiculeId;

    @Schema(description = "Immatriculation du véhicule concerné", example = "234-TN-5678")
    private String vehiculeImmatriculation;
}
