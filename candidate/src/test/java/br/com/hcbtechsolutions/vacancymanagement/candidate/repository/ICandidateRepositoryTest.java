package br.com.hcbtechsolutions.vacancymanagement.candidate.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import br.com.hcbtechsolutions.vacancymanagement.candidate.entity.CandidateEntity;

@DataJpaTest
@ActiveProfiles("test") // Use the "test" profile to apply test-specific configurations
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // Disable replacing the database
    public class ICandidateRepositoryTest {

    
    @Autowired
    private ICandidateRepository candidateRepository;

    private CandidateEntity candidateEntity;

    private UUID candidateId;

    @BeforeEach
    public void setUp() {
        candidateEntity = CandidateEntity.builder()
                .name("John Doe")
                .username("johndoe")
                .email("john.doe@example.com")
                .password("password123")
                .description("Software Engineer")
                .build();

        candidateRepository.save(candidateEntity);

        candidateId = candidateEntity.getId();

    }

    @Test
    public void testFindByUsername() {
        // When
        Optional<CandidateEntity> foundCandidate = candidateRepository.findByUsername(candidateEntity.getUsername());

        // Then
        assertTrue(foundCandidate.isPresent());
        assertEquals(candidateEntity.getUsername(), foundCandidate.get().getUsername());

    }

    @Test
    public void testFindByUsernameOrEmail_withUsername() {
        // When
        Optional<CandidateEntity> foundCandidate = candidateRepository.findByUsernameOrEmail(candidateEntity.getUsername(), "non.existent.email@example.com");

        // Then
        assertTrue(foundCandidate.isPresent());
        assertEquals(candidateEntity.getUsername(), foundCandidate.get().getUsername());
    }

    @Test
    public void testFindByUsernameOrEmail_withEmail() {
        // When
        Optional<CandidateEntity> foundCandidate = candidateRepository.findByUsernameOrEmail("nonexistentusername", candidateEntity.getEmail());

        // Then
        assertTrue(foundCandidate.isPresent());
        assertEquals(candidateEntity.getEmail(), foundCandidate.get().getEmail());
    }

    @Test
    public void testFindByUsernameOrEmail_notFound() {
        // When
        Optional<CandidateEntity> foundCandidate = candidateRepository.findByUsernameOrEmail("nonexistentusername", "non.existent.email@example.com");

        // Then
        assertFalse(foundCandidate.isPresent());
    }
    
    @Test
    public void testSaveAndFindById() {
   
        // When
        Optional<CandidateEntity> foundCandidate = candidateRepository.findById(candidateId);
    
        // Then
        assertTrue(foundCandidate.isPresent());
        assertEquals(candidateId, foundCandidate.get().getId());
    }
}
