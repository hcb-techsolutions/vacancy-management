package br.com.hcbtechsolutions.vacancymanagement.candidate.entity;

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

public class CandidateEntityTest {

    private CandidateEntity candidateEntity;

    private Validator validator;

    @BeforeEach
    public void setUp() {
        candidateEntity = CandidateEntity.builder()
                .id(UUID.randomUUID())
                .name("John Doe")
                .username("johndoe")
                .email("john.doe@example.com")
                .password("password123")
                .description("Software Engineer")
                .createdAt(LocalDateTime.now())
                .build();

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testId() {
        UUID expectedId = UUID.randomUUID();
        candidateEntity.setId(expectedId);
        assertEquals(expectedId, candidateEntity.getId());
    }

    @Test
    public void testName() {
        String expectedName = "John Smith";
        candidateEntity.setName(expectedName);
        assertEquals(expectedName, candidateEntity.getName());
    }

    @Test
    public void testUsername() {
        String expectedUsername = "johndoe";
        candidateEntity.setUsername(expectedUsername);
        assertEquals(expectedUsername, candidateEntity.getUsername());
    }

    @Test
    public void testEmail() {
        String expectedEmail = "john.doe@example.com";
        candidateEntity.setEmail(expectedEmail);
        assertEquals(expectedEmail, candidateEntity.getEmail());
    }

    @Test   
    public void testPassword() {
        String expectedPassword = "password123";
        candidateEntity.setPassword(expectedPassword);
        assertEquals(expectedPassword, candidateEntity.getPassword());
    }

    @Test
    public void testDescription() {
        String expectedDescription = "Software Developer";
        candidateEntity.setDescription(expectedDescription);
        assertEquals(expectedDescription, candidateEntity.getDescription());
    }

    @Test
    public void testCreatedAt() {
        LocalDateTime expectedCreatedAt = LocalDateTime.now();
        candidateEntity.setCreatedAt(expectedCreatedAt);
        assertEquals(expectedCreatedAt, candidateEntity.getCreatedAt());
    }

    @Test 
    public void testCandidateEntityValid() {
        Set<ConstraintViolation<CandidateEntity>> violations = validator.validate(candidateEntity);
        assertTrue(violations.isEmpty(), "Expected no validation violations");
    }
}
