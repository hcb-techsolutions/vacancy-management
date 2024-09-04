package br.com.hcbtechsolutions.vacancymanagement.candidate.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import br.com.hcbtechsolutions.vacancymanagement.candidate.entity.CandidateEntity;
import br.com.hcbtechsolutions.vacancymanagement.candidate.repository.ICandidateRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class CandidateControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ICandidateRepository repository;

    @Test
    public void whenPostRequestToCandidate_thenCorrectResponse() throws Exception {
        // Test JSON
        String candidateJson = """
                {
                    "name": "Sophia Frazier",
                    "username": "sophiafrazier",
                    "email": "sophiafrazier@example.com", 
                    "password": "password123",
                    "description": "Software Engineer"
                }
                """;

        // Perform POST request
        mockMvc.perform(post("/api/candidate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(candidateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Sophia Frazier"))
                .andExpect(jsonPath("$.email").value("sophiafrazier@example.com"));

        // Verify candidate is saved in the database
        CandidateEntity candidate = repository.findByUsernameOrEmail("sophiafrazier", "sophiafrazier@example.com").get();
        assertNotNull(candidate);
        assertNotNull(candidate.getId());
        assertEquals("Sophia Frazier", candidate.getName());
    }

    @Test
    void whenPostRequestToCandidateWithSameUsername_thenBadRequest() throws Exception {
        // Test JSON
        String candidateJson = """
                {
                    "name": "Sophia Frazier",
                    "username": "sf200",
                    "email": "sophiafrazier@example.com", 
                    "password": "password123",
                    "description": "Software Engineer"
                }
                """;
        String newCandidateJson = """
                {
                    "name": "Steven Frazier",
                    "username": "sf200",
                    "email": "stevenfrazier@example.com", 
                    "password": "password123",
                    "description": "Software Engineer"
                }
                """;

        // Perform POST request
        mockMvc.perform(post("/api/candidate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(candidateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Sophia Frazier"))
                .andExpect(jsonPath("$.email").value("sophiafrazier@example.com"));

                // Perform POST request
        mockMvc.perform(post("/api/candidate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newCandidateJson))
                .andExpect(status().isBadRequest());

        // Verify candidate with username already exists
        Optional<CandidateEntity> candidate = repository.findByUsernameOrEmail("sf200", "sophiafrazier@example.com");
        assertTrue(candidate.isPresent());
        assertEquals("sf200", candidate.get().getUsername());
    }

    @Test
    void whenPostRequestToCandidateWithSameEmail_thenBadRequest() throws Exception {
        // Test JSON
        String candidateJson = """
                {
                    "name": "Sophia Frazier",
                    "username": "sophiafrazier",
                    "email": "sf200@example.com", 
                    "password": "password123",
                    "description": "Software Engineer"
                }
                """;
        String newCandidateJson = """
                {
                    "name": "Steven Frazier",
                    "username": "sevenfrazier",
                    "email": "sf200@example.com", 
                    "password": "password123",
                    "description": "Software Engineer"
                }
                """;

        // Perform POST request
        mockMvc.perform(post("/api/candidate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(candidateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Sophia Frazier"))
                .andExpect(jsonPath("$.email").value("sf200@example.com"));

                // Perform POST request
        mockMvc.perform(post("/api/candidate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newCandidateJson))
                .andExpect(status().isBadRequest());

        // Verify candidate with username already exists
        Optional<CandidateEntity> candidate = repository.findByUsernameOrEmail("sofiafrazier", "sf200@example.com");
        assertTrue(candidate.isPresent());
        assertEquals("sf200@example.com", candidate.get().getEmail());
    }

    @Test
    public void whenPostRequestToCandidateWithEmptyName_thenBadRequest() throws Exception {
        // Test JSON
        String candidateJson = """
                {
                    "name": "",
                    "username": "bicaw",
                    "email": "bicaw@example.com", 
                    "password": "password123",
                    "description": "Software Engineer"
                }
                """; // Empty name 

        // Perform POST request
        mockMvc.perform(post("/api/candidate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(candidateJson))
                .andExpect(status().isBadRequest());

        // Verify candidate is not saved in the database
        Optional<CandidateEntity> candidate = repository.findByUsernameOrEmail("bicaw", "bicaw@dusewavu.bm");
        assertTrue(candidate.isEmpty());
    }

    @Test
    public void whenPostRequestToCandidateWithInvalidEmail_thenBadRequest() throws Exception {
        // Test JSON 
        String candidateJson = """
                {
                    "name": "John Doe",
                    "username": "johndoe",
                    "email": "johndoe.com", 
                    "password": "password123",
                    "description": "Software Engineer"
                }
                """; // Invalid email

        // Perform POST request
        mockMvc.perform(post("/api/candidate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(candidateJson))
                .andExpect(status().isBadRequest());

        // Verify candidate is not saved in the database
        Optional<CandidateEntity> candidate = repository.findByUsernameOrEmail("johndoe", "johndoe.com");
        assertTrue(candidate.isEmpty());
    }

    @Test
    public void whenPostRequestToCandidateWithInvalidPassword_thenBadRequest() throws Exception {
        // Test JSON
        String candidateJson = """
                {
                    "name": "John Doe",
                    "username": "johndoe",
                    "email": "johndoe@example.com", 
                    "password": "pass",
                    "description": "Software Engineer"
                }
                """; // Invalid password

        // Perform POST request
        mockMvc.perform(post("/api/candidate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(candidateJson))
                .andExpect(status().isBadRequest());

        // Verify candidate is not saved in the database
        Optional<CandidateEntity> candidate = repository.findByUsernameOrEmail("johndoe", "johndoe@example.com");
        assertTrue(candidate.isEmpty());
    }

    @Test
    void whenPostRequestToCandidateWithWhitespaceUsername_thenBadRequest() throws Exception {
        // Test JSON
        String candidateJson = """
                {
                    "name": "Brett Boone",
                    "username": "brett boone",
                    "email": "brett.boone@example.com", 
                    "password": "password123",
                    "description": "Software Engineer"
                }
                """; // Whitespace username

        // Perform POST request
        mockMvc.perform(post("/api/candidate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(candidateJson))
                .andExpect(status().isBadRequest());

        // Verify candidate is not saved in the database
        Optional<CandidateEntity> candidate = repository.findByUsernameOrEmail("johndoe", "johndoe@example.com");
        assertTrue(candidate.isEmpty());
    }
}
