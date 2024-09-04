package br.com.hcbtechsolutions.vacancymanagement.candidate.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class CandidateDTOUnitTest {

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
    public void testNameCannotBeEmpty() {
        candidateDTO = CandidateDTO.builder()
                .id(UUID.randomUUID())
                .name("") // Empty name
                .username("johndoe")
                .email("john.doe@example.com")
                .password("password123")
                .description("Software Engineer")
                .createdAt(LocalDateTime.now())
                .build();

        Set<ConstraintViolation<CandidateDTO>> violations = validator.validate(candidateDTO);
        assertEquals(1, violations.size(), "Expected one validation violation for name being empty");
        assertEquals("name cannot be blank", violations.iterator().next().getMessage());
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
}
