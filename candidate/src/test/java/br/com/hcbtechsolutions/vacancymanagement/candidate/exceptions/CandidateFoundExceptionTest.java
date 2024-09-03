package br.com.hcbtechsolutions.vacancymanagement.candidate.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class CandidateFoundExceptionTest {
    @Test
        public void testCandidateFoundExceptionMessage() {
            // When & Then: Verify that the exception is thrown with the expected message
            CandidateFoundException exception = assertThrows(CandidateFoundException.class, () -> {
                throw new CandidateFoundException();
            });

            assertEquals("Candidate already exists", exception.getMessage());
        }
}
