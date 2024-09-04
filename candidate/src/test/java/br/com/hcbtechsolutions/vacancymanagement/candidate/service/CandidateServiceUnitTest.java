package br.com.hcbtechsolutions.vacancymanagement.candidate.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.hcbtechsolutions.vacancymanagement.candidate.encryptor.BCryptyPassword;
import br.com.hcbtechsolutions.vacancymanagement.candidate.entity.CandidateEntity;
import br.com.hcbtechsolutions.vacancymanagement.candidate.exceptions.CandidateFoundException;
import br.com.hcbtechsolutions.vacancymanagement.candidate.repository.ICandidateRepository;

@SpringBootTest
public class CandidateServiceUnitTest {

    @Mock
    private ICandidateRepository repository;

    @InjectMocks
    private CandidateService service;

    @Mock
    private BCryptyPassword encryptor;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void whenCreateCandidate_thenCandidateShouldBeCreatedWithEncryptedPassword() {
        CandidateEntity candidate = CandidateEntity.builder()
                                    .id(UUID.randomUUID())
                                    .name("Ellen McBride")
                                    .username("ellenmcbride")
                                    .email("ellenmcbride@example")
                                    .password("password123")
                                    .description("Software Engineer")
                                    .createdAt(LocalDateTime.now())
                                    .build();

        // Mock repository to simulate creating candidate
        when(repository.findByUsernameOrEmail(candidate.getUsername(), candidate.getEmail()))
            .thenReturn(Optional.empty());
        when(repository.save(any(CandidateEntity.class))).thenReturn(candidate);
        when(encryptor.encryptyPassword(candidate.getPassword())).thenReturn("encryptedpassword");
        
        CandidateEntity createdCandidate = service.create(candidate);
        
        assertNotNull(createdCandidate);
        assertNotNull(createdCandidate.getId());
        assertEquals(candidate.getUsername(), createdCandidate.getUsername());
        assertEquals("encryptedpassword", createdCandidate.getPassword());
        
        // Verify that repository.findByUsernameOrEmail is called once
        verify(repository, times(1))
                .findByUsernameOrEmail(candidate.getUsername(), candidate.getEmail());
        
        // Verify that repository.save is called once
        verify(repository, times(1)).save(candidate);
    }

    @Test
    public void whenCreateExistingCandidate_thenThrowCandidateFoundException() {

        CandidateEntity existingCandidate = CandidateEntity.builder()
                                            .username("ellenmcbride")
                                            .email("ellenmcbride@example")
                                            .password("password123")
                                            .build();

        CandidateEntity newCandidate = CandidateEntity.builder()
                                            .username("ellenmcbride")
                                            .email("ellenmcbride@example")
                                            .password("password123")
                                            .build();
        
        // Mock repository to simulate existing candidate
        when(repository.findByUsernameOrEmail(newCandidate.getUsername(), newCandidate.getEmail()))
                .thenReturn(Optional.of(existingCandidate));
        
        assertThrows(CandidateFoundException.class, () -> service.create(newCandidate));

        // Verify that repository.findByUsernameOrEmail is called once
        verify(repository, times(1))
                .findByUsernameOrEmail(newCandidate.getUsername(), newCandidate.getEmail());
        
        // Verify that repository.save is not called
        verify(repository, never()).save(any(CandidateEntity.class));
    }
}
