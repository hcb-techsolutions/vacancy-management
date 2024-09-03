    package br.com.hcbtechsolutions.vacancymanagement.candidate.service;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;

    import br.com.hcbtechsolutions.vacancymanagement.candidate.encryptor.BCryptyPassword;
    import br.com.hcbtechsolutions.vacancymanagement.candidate.entity.CandidateEntity;
    import br.com.hcbtechsolutions.vacancymanagement.candidate.exceptions.CandidateFoundException;
    import br.com.hcbtechsolutions.vacancymanagement.candidate.repository.ICandidateRepository;

    @Service
    public class CandidateService implements ICandidateService {

        @Autowired
        private ICandidateRepository repository;

        @Autowired
        private BCryptyPassword encryptor;

        @Override
        public CandidateEntity create(CandidateEntity candidate) {
            repository.findByUsernameOrEmail(candidate.getUsername(), candidate.getEmail())
                .ifPresent(candidate1 -> {
                    throw new CandidateFoundException();
                });
            
            var encryptyPassword = encryptor.encryptyPassword(candidate.getPassword());
            candidate.setPassword(encryptyPassword);
            
            return repository.save(candidate);
        }
    }
