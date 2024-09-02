package br.com.hcbtechsolutions.vacancymanagement.candidate.dto;

public record ErrorMessageDTO(
    String message,
    String field
) {}
