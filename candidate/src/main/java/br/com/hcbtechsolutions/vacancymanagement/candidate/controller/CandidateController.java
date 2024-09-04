package br.com.hcbtechsolutions.vacancymanagement.candidate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.hcbtechsolutions.vacancymanagement.candidate.dto.CandidateDTO;
import br.com.hcbtechsolutions.vacancymanagement.candidate.service.ICandidateService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/candidate")
public class CandidateController implements ICandidateController{

    @Autowired
    private ICandidateService service;

    @Override
    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody CandidateDTO candidate) {
        try {
            var candidateEntity = service.create(candidate.toEntity());
            return ResponseEntity.ok(candidateEntity);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
