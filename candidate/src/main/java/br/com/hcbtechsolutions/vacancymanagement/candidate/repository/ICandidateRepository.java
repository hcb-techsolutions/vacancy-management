package br.com.hcbtechsolutions.vacancymanagement.candidate.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.hcbtechsolutions.vacancymanagement.candidate.entity.CandidateEntity;

public interface ICandidateRepository extends JpaRepository<CandidateEntity, UUID> {
    Optional<CandidateEntity> findByUsername(String username);

    Optional<CandidateEntity> findByUsernameOrEmail(String username, String email);
}
