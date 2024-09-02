package br.com.hcbtechsolutions.vacancymanagement.candidate.exceptions;

public class CandidateFoundException extends RuntimeException {

    public CandidateFoundException() {
        super("Candidate already exists");
    }
}
