package tn.esprit.autolocapi.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.autolocapi.dto.VehiculeDTO;
import tn.esprit.autolocapi.service.IVehiculeService;

import java.util.List;

@RestController
@RequestMapping("/vehicules")
@RequiredArgsConstructor
@Tag(name = "Vehicules", description = "Gestion des véhicules")
public class VehiculeController {

    private final IVehiculeService vehiculeService;

    @Operation(summary = "Lister les vehicules")
    @ApiResponse(responseCode = "200", description = "Liste retournée sous forme de DTO")
    @GetMapping
    public List<VehiculeDTO> findAll() {
        return vehiculeService.findAll();
    }

    @Operation(summary = "Consulter un véhicule")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Véhicule trouvé"),
            @ApiResponse(responseCode = "404", description = "Véhicule introuvable")})
    @GetMapping("/{id}")
    public VehiculeDTO findById(@Parameter(description = "Identifiant du véhicule") @PathVariable Long id) {
        return vehiculeService.findById(id);
    }

    @Operation(summary = "Créer un véhicule")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Véhicule créé"),
            @ApiResponse(responseCode = "400", description = "Données invalides")})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VehiculeDTO create(@Parameter(description = "Données du véhicule à créer") @Valid @RequestBody VehiculeDTO vehiculeDTO) {
        return vehiculeService.create(vehiculeDTO);
    }

    @Operation(summary = "Modifier un véhicule")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Véhicule modifié"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "404", description = "Véhicule introuvable")})
    @PutMapping("/{id}")
    public VehiculeDTO update(@Parameter(description = "Identifiant du véhicule") @PathVariable Long id,
                             @Parameter(description = "Nouvelles données du véhicule") @Valid @RequestBody VehiculeDTO vehiculeDTO) {
        return vehiculeService.update(id, vehiculeDTO);
    }

    @Operation(summary = "Supprimer un véhicule")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Véhicule supprimé"),
            @ApiResponse(responseCode = "404", description = "Véhicule introuvable")})
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@Parameter(description = "Identifiant du véhicule") @PathVariable Long id) {
        vehiculeService.deleteById(id);
    }

    // =========================================================================
    // Endpoints d'affectation
    // =========================================================================

    @Operation(summary = "Affecter un équipement à un véhicule",
               description = "Associe l'équipement identifié par idEquipement au véhicule identifié par idVehicule")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Équipement affecté au véhicule"),
            @ApiResponse(responseCode = "400", description = "Équipement déjà associé"),
            @ApiResponse(responseCode = "404", description = "Véhicule ou équipement introuvable")})
    @PutMapping("/{idVehicule}/equipements/{idEquipement}")
    public VehiculeDTO affecterVehiculeAEquipement(
            @Parameter(description = "Identifiant du véhicule") @PathVariable Long idVehicule,
            @Parameter(description = "Identifiant de l'équipement") @PathVariable Long idEquipement) {
        return vehiculeService.affecterVehiculeAEquipement(idVehicule, idEquipement);
    }

    @Operation(summary = "Affecter une maintenance à un véhicule",
               description = "Crée une maintenance sur le véhicule identifié par son immatriculation et passe son statut à MAINTENANCE")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Maintenance créée et véhicule mis à jour"),
            @ApiResponse(responseCode = "400", description = "Immatriculation introuvable")})
    @PutMapping("/maintenance")
    public VehiculeDTO affecterMaintenanceAvehicule(
            @Parameter(description = "Description de la maintenance") @RequestParam String description,
            @Parameter(description = "Immatriculation du véhicule") @RequestParam String immatriculation) {
        return vehiculeService.affecterMaintenanceAvehicule(description, immatriculation);
    }
}

