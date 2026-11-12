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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.autolocapi.dto.EquipementDTO;
import tn.esprit.autolocapi.service.IEquipementService;

import java.util.List;

@RestController
@RequestMapping("/equipements")
@RequiredArgsConstructor
@Tag(name = "Equipements", description = "Gestion des équipements")
public class EquipementController {

    private final IEquipementService equipementService;

    @Operation(summary = "Lister les equipements")
    @ApiResponse(responseCode = "200", description = "Liste retournée sous forme de DTO")
    @GetMapping
    public List<EquipementDTO> findAll() {
        return equipementService.findAll();
    }

    @Operation(summary = "Consulter un équipement")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Équipement trouvé"),
            @ApiResponse(responseCode = "404", description = "Équipement introuvable")})
    @GetMapping("/{id}")
    public EquipementDTO findById(@Parameter(description = "Identifiant de l'équipement") @PathVariable Long id) {
        return equipementService.findById(id);
    }

    @Operation(summary = "Créer un équipement")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Équipement créé"),
            @ApiResponse(responseCode = "400", description = "Données invalides")})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EquipementDTO create(@Parameter(description = "Données de l'équipement à créer") @Valid @RequestBody EquipementDTO equipementDTO) {
        return equipementService.create(equipementDTO);
    }

    @Operation(summary = "Modifier un équipement")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Équipement modifié"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "404", description = "Équipement introuvable")})
    @PutMapping("/{id}")
    public EquipementDTO update(@Parameter(description = "Identifiant de l'équipement") @PathVariable Long id,
                                @Parameter(description = "Nouvelles données de l'équipement") @Valid @RequestBody EquipementDTO equipementDTO) {
        return equipementService.update(id, equipementDTO);
    }

    @Operation(summary = "Supprimer un équipement")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Équipement supprimé"),
            @ApiResponse(responseCode = "404", description = "Équipement introuvable")})
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@Parameter(description = "Identifiant de l'équipement") @PathVariable Long id) {
        equipementService.deleteById(id);
    }
}
