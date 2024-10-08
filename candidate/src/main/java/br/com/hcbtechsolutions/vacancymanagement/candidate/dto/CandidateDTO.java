package br.com.hcbtechsolutions.vacancymanagement.candidate.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.validator.constraints.Length;

import br.com.hcbtechsolutions.vacancymanagement.candidate.entity.CandidateEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record CandidateDTO(
    UUID id,
    
    @NotBlank(message = "name cannot be blank")
    String name,
    
    @NotBlank
    @Pattern(regexp = "\\S+", message = "username cannot contain whitespace")
    String username,
    
    @Email(message = "must be a well-formed email address")
    String email,
    
    @Length(min = 8, message = "password must be at least 8 characters")
    String password,
    
    String description,
    
    LocalDateTime createdAt
) {
    public CandidateEntity toEntity() {
        return new CandidateEntity(id, name, username, email, password, description, createdAt);
    }

    public static CandidateDTO fromEntity(CandidateEntity candidate) {
        return new CandidateDTO(candidate.getId(), candidate.getName(), candidate.getUsername(), candidate.getEmail(), candidate.getPassword(), candidate.getDescription(), candidate.getCreatedAt());
    }
}
