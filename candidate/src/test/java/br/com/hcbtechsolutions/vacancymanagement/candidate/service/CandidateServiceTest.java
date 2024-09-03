package br.com.hcbtechsolutions.vacancymanagement.candidate.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.hcbtechsolutions.vacancymanagement.candidate.encryptor.BCryptyPassword;
import br.com.hcbtechsolutions.vacancymanagement.candidate.entity.CandidateEntity;
import br.com.hcbtechsolutions.vacancymanagement.candidate.exceptions.CandidateFoundException;
import br.com.hcbtechsolutions.vacancymanagement.candidate.repository.ICandidateRepository;

public class CandidateServiceTest {

    @Mock
    private ICandidateRepository repository;

    @Mock
    private BCryptyPassword encryptor;

    @InjectMocks
    private CandidateService service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateCandidateSuccessfully() {
        // Arrange
        CandidateEntity candidate = new CandidateEntity();
        candidate.setUsername("testuser");
        candidate.setEmail("testuser@example.com");
        candidate.setPassword("plaintextpassword");

        when(repository.findByUsernameOrEmail(eq("testuser"), eq("testuser@example.com")))
            .thenReturn(Optional.empty());
        when(encryptor.encryptyPassword("plaintextpassword"))
            .thenReturn("encryptedpassword");
        when(repository.save(any(CandidateEntity.class)))
            .thenReturn(candidate);

        // Act
        CandidateEntity createdCandidate = service.create(candidate);

        // Assert
        assertEquals("encryptedpassword", createdCandidate.getPassword());
        verify(repository, times(1)).save(candidate);
    }

    @Test
    public void testCreateCandidateAlreadyExists() {
        // Arrange
        CandidateEntity candidate = new CandidateEntity();
        candidate.setUsername("existinguser");
        candidate.setEmail("existinguser@example.com");

        when(repository.findByUsernameOrEmail(eq("existinguser"), eq("existinguser@example.com")))
            .thenReturn(Optional.of(new CandidateEntity()));

        // Act & Assert
        assertThrows(CandidateFoundException.class, () -> service.create(candidate));
        verify(repository, never()).save(any(CandidateEntity.class));
    }
}
