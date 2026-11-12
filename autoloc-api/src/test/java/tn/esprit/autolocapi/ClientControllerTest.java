package tn.esprit.autolocapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.autolocapi.dto.ClientDTO;

import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Test
    @DisplayName("POST /clients - Succès avec DTO valide (201 CREATED)")
    void createClient_validDTO_shouldReturnCreated() throws Exception {
        ClientDTO dto = ClientDTO.builder()
                .nom("Gharbi")
                .prenom("Mariem")
                .email("mariem.gharbi@test.tn")
                .telephone("+21620112233")
                .numPermis("TN-99887766")
                .dateInscription(LocalDate.now())
                .build();

        mockMvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idClient").isNumber())
                .andExpect(jsonPath("$.nom").value("Gharbi"))
                .andExpect(jsonPath("$.prenom").value("Mariem"))
                .andExpect(jsonPath("$.email").value("mariem.gharbi@test.tn"))
                .andExpect(jsonPath("$.numPermis").value("TN-99887766"));
    }

    @Test
    @DisplayName("POST /clients - Échec validation e-mail invalide (400 BAD REQUEST)")
    void createClient_invalidEmail_shouldReturnBadRequest() throws Exception {
        ClientDTO dto = ClientDTO.builder()
                .nom("Gharbi")
                .prenom("Mariem")
                .email("not-an-email") // Invalide
                .telephone("+21620112233")
                .numPermis("TN-99887766")
                .dateInscription(LocalDate.now())
                .build();

        mockMvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.validationErrors.email").value("L'adresse e-mail n'est pas valide"));
    }
}
