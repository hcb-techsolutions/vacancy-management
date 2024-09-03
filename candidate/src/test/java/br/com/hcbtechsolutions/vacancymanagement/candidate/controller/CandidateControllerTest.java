package br.com.hcbtechsolutions.vacancymanagement.candidate.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.com.hcbtechsolutions.vacancymanagement.candidate.dto.CandidateDTO;
import br.com.hcbtechsolutions.vacancymanagement.candidate.entity.CandidateEntity;
import br.com.hcbtechsolutions.vacancymanagement.candidate.service.ICandidateService;

public class CandidateControllerTest {

    @Mock
    private ICandidateService candidateService;

    @InjectMocks
    private CandidateController candidateController;

    private CandidateDTO candidateDTO;
    private CandidateEntity candidateEntity = new CandidateEntity();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Creating an instance of CandidateDTO for use in tests.
        candidateDTO = new CandidateDTO(null,"John Doe", "johndoe", "john.doe@example.com", "john1234", "Software developer", null);

        // Setting up a CandidateEntity instance to mimic the service response.
        candidateEntity
            .builder()
            .id(UUID.randomUUID())
            .name(candidateDTO.name())
            .username(candidateDTO.username())
            .email(candidateDTO.email())
            .password(candidateDTO.password())
            .description(candidateDTO.description())
            .createdAt(LocalDateTime.now())
            .build();
    }

    @Test
    public void testCreateCandidate_Success() {
        // Mocking the service response when the create method is invoked.
        when(candidateService.create(any(CandidateEntity.class))).thenReturn(candidateEntity);

        // Invoking the create method of the controller.
        ResponseEntity<Object> response = candidateController.create(candidateDTO);

        // Checking if the response is as expected.
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(candidateEntity, response.getBody());
    }

    @Test

    public void testCreateCandidate_BadRequest() {
        // Mocking an exception response from the service.
        String errorMessage = "Candidate creation failed";
        doThrow(new RuntimeException(errorMessage)).when(candidateService).create(any(CandidateEntity.class));

        // Invoking the controller's create operation.
        ResponseEntity<Object> response = candidateController.create(candidateDTO);

        // Checking if the response is as expected.
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(errorMessage, response.getBody());
    }
}
