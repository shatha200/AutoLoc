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
import tn.esprit.autolocapi.dto.ContratDTO;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ContratControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Test
    @DisplayName("POST /contrats - Succès avec DTO valide (201 CREATED)")
    void createContrat_validDTO_shouldReturnCreated() throws Exception {
        ContratDTO dto = ContratDTO.builder()
                .dateSignature(LocalDate.now())
                .montantTotal(new BigDecimal("450.00"))
                .valide(true)
                .build();

        mockMvc.perform(post("/contrats")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idContrat").isNumber())
                .andExpect(jsonPath("$.montantTotal").value(450.00))
                .andExpect(jsonPath("$.valide").value(true));
    }

    @Test
    @DisplayName("POST /contrats - Échec validation montant négatif (400 BAD REQUEST)")
    void createContrat_negativeMontant_shouldReturnBadRequest() throws Exception {
        ContratDTO dto = ContratDTO.builder()
                .dateSignature(LocalDate.now())
                .montantTotal(new BigDecimal("-100.00")) // Invalide
                .valide(false)
                .build();

        mockMvc.perform(post("/contrats")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.validationErrors.montantTotal").value("Le montant total ne peut pas être négatif"));
    }
}
