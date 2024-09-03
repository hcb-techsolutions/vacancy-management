package br.com.hcbtechsolutions.vacancymanagement.candidate.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.hcbtechsolutions.vacancymanagement.candidate.entity.CandidateEntity;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class CandidateDTOTest {

    private Validator validator;
    private CandidateDTO candidateDTO;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        candidateDTO = CandidateDTO.builder()
                .id(UUID.randomUUID())
                .name("John Doe")
                .username("johndoe")
                .email("john.doe@example.com")
                .password("password123")
                .description("Software Engineer")
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    public void testCandidateDTOValid() {
        Set<ConstraintViolation<CandidateDTO>> violations = validator.validate(candidateDTO);
        assertTrue(violations.isEmpty(), "Expected no validation violations");
    }

    @Test
    public void testUsernameCannotContainWhitespace() {
        candidateDTO = CandidateDTO.builder()
                .id(UUID.randomUUID())
                .name("John Doe")
                .username("john doe") // Contains whitespace
                .email("john.doe@example.com")
                .password("password123")
                .description("Software Engineer")
                .createdAt(LocalDateTime.now())
                .build();

        Set<ConstraintViolation<CandidateDTO>> violations = validator.validate(candidateDTO);
        assertEquals(1, violations.size(), "Expected one validation violation for username containing whitespace");
        assertEquals("username cannot contain whitespace", violations.iterator().next().getMessage());
    }

    @Test
    public void testEmailMustBeValid() {
        candidateDTO = CandidateDTO.builder()
                .id(UUID.randomUUID())
                .name("John Doe")
                .username("johndoe")
                .email("invalid-email") // Invalid email
                .password("password123")
                .description("Software Engineer")
                .createdAt(LocalDateTime.now())
                .build();

        Set<ConstraintViolation<CandidateDTO>> violations = validator.validate(candidateDTO);
        assertEquals(1, violations.size(), "Expected one validation violation for invalid email");
        assertEquals("must be a well-formed email address", violations.iterator().next().getMessage());
    }

    @Test
    public void testPasswordLength() {
        candidateDTO = CandidateDTO.builder()
                .id(UUID.randomUUID())
                .name("John Doe")
                .username("johndoe")
                .email("john.doe@example.com")
                .password("short") // Password too short
                .description("Software Engineer")
                .createdAt(LocalDateTime.now())
                .build();

        Set<ConstraintViolation<CandidateDTO>> violations = validator.validate(candidateDTO);
        assertEquals(1, violations.size(), "Expected one validation violation for short password");
        assertEquals("password must be at least 8 characters", violations.iterator().next().getMessage());
    }

    @Test
    public void testToEntity() {
        CandidateEntity candidateEntity = candidateDTO.toEntity();
        assertEquals(candidateDTO.id(), candidateEntity.getId());
        assertEquals(candidateDTO.name(), candidateEntity.getName());
        assertEquals(candidateDTO.username(), candidateEntity.getUsername());
        assertEquals(candidateDTO.email(), candidateEntity.getEmail());
        assertEquals(candidateDTO.password(), candidateEntity.getPassword());
        assertEquals(candidateDTO.description(), candidateEntity.getDescription());
        assertEquals(candidateDTO.createdAt(), candidateEntity.getCreatedAt());
    }

    @Test
    public void testFromEntity() {
        CandidateEntity candidateEntity = new CandidateEntity();
        candidateEntity.setId(UUID.randomUUID());
        candidateEntity.setName("John Doe");
        candidateEntity.setUsername("johndoe");
        candidateEntity.setEmail("john.doe@example.com");
        candidateEntity.setPassword("password123");
        candidateEntity.setDescription("Software Engineer");
        candidateEntity.setCreatedAt(LocalDateTime.now());

        CandidateDTO candidateDTO = CandidateDTO.fromEntity(candidateEntity);

        assertEquals(candidateEntity.getId(), candidateDTO.id());
        assertEquals(candidateEntity.getName(), candidateDTO.name());
        assertEquals(candidateEntity.getUsername(), candidateDTO.username());
        assertEquals(candidateEntity.getEmail(), candidateDTO.email());
        assertEquals(candidateEntity.getPassword(), candidateDTO.password());
        assertEquals(candidateEntity.getDescription(), candidateDTO.description());
        assertEquals(candidateEntity.getCreatedAt(), candidateDTO.createdAt());
    }
}
