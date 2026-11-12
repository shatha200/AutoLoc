package tn.esprit.autolocapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
@Schema(description = "DTO représentant un client")
public class ClientDTO {

    @Schema(description = "Identifiant du client", example = "1")
    private Long idClient;

    @Schema(description = "Nom de famille du client", example = "Ben Salem")
    @NotBlank(message = "Le nom du client est obligatoire")
    @Size(max = 50, message = "Le nom ne doit pas dépasser 50 caractères")
    private String nom;

    @Schema(description = "Prénom du client", example = "Ahmed")
    @NotBlank(message = "Le prénom du client est obligatoire")
    @Size(max = 50, message = "Le prénom ne doit pas dépasser 50 caractères")
    private String prenom;

    @Schema(description = "Adresse email valide", example = "ahmed.bensalem@email.com")
    @NotBlank(message = "L'e-mail du client est obligatoire")
    @Email(message = "L'adresse e-mail n'est pas valide")
    @Size(max = 100, message = "L'adresse e-mail ne doit pas dépasser 100 caractères")
    private String email;

    @Schema(description = "Numéro de téléphone", example = "+216 98 765 432")
    @NotBlank(message = "Le numéro de téléphone est obligatoire")
    @Pattern(regexp = "^[+0-9\\s-]{8,20}$", message = "Le format du numéro de téléphone est invalide")
    private String telephone;

    @Schema(description = "Numéro de permis de conduire", example = "TN-12345678")
    @NotBlank(message = "Le numéro de permis est obligatoire")
    @Size(max = 30, message = "Le numéro de permis ne doit pas dépasser 30 caractères")
    private String numPermis;

    @Schema(description = "Date d'inscription du client (format AAAA-MM-JJ)", example = "2026-01-15")
    @NotNull(message = "La date d'inscription est obligatoire")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateInscription;
}
