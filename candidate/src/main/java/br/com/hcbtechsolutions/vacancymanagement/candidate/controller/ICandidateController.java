package br.com.hcbtechsolutions.vacancymanagement.candidate.controller;

import org.springframework.http.ResponseEntity;

import br.com.hcbtechsolutions.vacancymanagement.candidate.dto.CandidateDTO;

public interface ICandidateController {
    ResponseEntity<Object> create(CandidateDTO candidate);
}
