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
import tn.esprit.autolocapi.domain.StatutReservation;
import tn.esprit.autolocapi.dto.ReservationDTO;

import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Test
    @DisplayName("POST /reservations - Règle métier: dateFin antérieure à dateDebut renvoie 400")
    void createReservation_dateFinBeforeDateDebut_shouldReturnBadRequest() throws Exception {
        ReservationDTO dto = ReservationDTO.builder()
                .dateDebut(LocalDate.of(2026, 6, 10))
                .dateFin(LocalDate.of(2026, 6, 5)) // date de fin antérieure !
                .statut(StatutReservation.EN_ATTENTE)
                .build();

        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message", containsString("La date de fin est antérieure à la date de début")));
    }
}
