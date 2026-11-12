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
import tn.esprit.autolocapi.dto.AgenceDTO;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AgenceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Test
    @DisplayName("POST /agences - Succès avec DTO valide (201 CREATED)")
    void createAgence_validDTO_shouldReturnCreated() throws Exception {
        AgenceDTO dto = AgenceDTO.builder()
                .nom("Agence Tunis Centre")
                .ville("Tunis")
                .adresse("12 Avenue Habib Bourguiba")
                .telephone("+21671123456")
                .build();

        mockMvc.perform(post("/agences")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idAgence").isNumber())
                .andExpect(jsonPath("$.nom").value("Agence Tunis Centre"))
                .andExpect(jsonPath("$.ville").value("Tunis"))
                .andExpect(jsonPath("$.adresse").value("12 Avenue Habib Bourguiba"))
                .andExpect(jsonPath("$.telephone").value("+21671123456"));
    }

    @Test
    @DisplayName("POST /agences - Échec de validation avec champs obligatoires vides (400 BAD REQUEST)")
    void createAgence_invalidDTO_shouldReturnValidationErrors() throws Exception {
        AgenceDTO invalidDto = AgenceDTO.builder()
                .nom("") // @NotBlank
                .ville("") // @NotBlank
                .adresse("") // @NotBlank
                .telephone("invalid-phone") // @Pattern
                .build();

        mockMvc.perform(post("/agences")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Échec de validation des données fournies"))
                .andExpect(jsonPath("$.validationErrors.nom").exists())
                .andExpect(jsonPath("$.validationErrors.ville").exists())
                .andExpect(jsonPath("$.validationErrors.adresse").exists())
                .andExpect(jsonPath("$.validationErrors.telephone").exists());
    }

    @Test
    @DisplayName("GET /agences/{id} - Ressource introuvable gérée par GlobalExceptionHandler (404 NOT FOUND)")
    void getAgence_notFound_shouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/agences/{id}", 99999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message", containsString("Agence introuvable avec l'identifiant 99999")));
    }

    @Test
    @DisplayName("GET /agences - Liste retournée sous forme de DTO (200 OK)")
    void getAllAgences_shouldReturnDTOList() throws Exception {
        mockMvc.perform(get("/agences"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
