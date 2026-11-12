package tn.esprit.autolocapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.autolocapi.domain.CategorieVehicule;
import tn.esprit.autolocapi.domain.StatutVehicule;
import tn.esprit.autolocapi.dto.AgenceDTO;
import tn.esprit.autolocapi.dto.VehiculeDTO;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class VehiculeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Test
    @DisplayName("POST /vehicules - Création réussie avec association DTO Agence (201 CREATED)")
    void createVehicule_validDTO_shouldReturnCreatedDTO() throws Exception {
        // Create an agence first
        AgenceDTO agenceDto = AgenceDTO.builder()
                .nom("Agence Ennasr")
                .ville("Ariana")
                .adresse("Avenue Hedi Nouira")
                .telephone("+21671888999")
                .build();

        String agenceResponse = mockMvc.perform(post("/agences")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(agenceDto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        AgenceDTO createdAgence = objectMapper.readValue(agenceResponse, AgenceDTO.class);

        VehiculeDTO vehiculeDTO = VehiculeDTO.builder()
                .immatriculation("999-TN-1111")
                .marque("Volkswagen")
                .modele("Golf 8")
                .categorie(CategorieVehicule.BERLINE)
                .tarifJournalier(new BigDecimal("120.00"))
                .statut(StatutVehicule.DISPONIBLE)
                .agenceId(createdAgence.getIdAgence())
                .build();

        mockMvc.perform(post("/vehicules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehiculeDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idVehicule").isNumber())
                .andExpect(jsonPath("$.immatriculation").value("999-TN-1111"))
                .andExpect(jsonPath("$.marque").value("Volkswagen"))
                .andExpect(jsonPath("$.modele").value("Golf 8"))
                .andExpect(jsonPath("$.agenceId").value(createdAgence.getIdAgence()))
                .andExpect(jsonPath("$.agenceNom").value("Agence Ennasr"));
    }

    @Test
    @DisplayName("POST /vehicules - Échec de validation tarif négatif (400 BAD REQUEST)")
    void createVehicule_negativePrice_shouldReturnBadRequest() throws Exception {
        VehiculeDTO vehiculeDTO = VehiculeDTO.builder()
                .immatriculation("888-TN-2222")
                .marque("Toyota")
                .modele("Yaris")
                .categorie(CategorieVehicule.CITADINE)
                .tarifJournalier(new BigDecimal("-50.00")) // Invalide
                .statut(StatutVehicule.DISPONIBLE)
                .build();

        mockMvc.perform(post("/vehicules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehiculeDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.validationErrors.tarifJournalier").value("Le tarif journalier ne peut pas être négatif"));
    }

    @Test
    @DisplayName("GET /vehicules - Vérification de l'absence de récursion infinie dans le DTO")
    void getAllVehicules_shouldReturnCleanListWithoutRecursion() throws Exception {
        mockMvc.perform(get("/vehicules"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
