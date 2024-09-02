package br.com.hcbtechsolutions.vacancymanagement.candidate.encryptors;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class BCryptyPassword implements IPasswordEncrypty {

    @Override
    public String encryptyPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    @Override
    public boolean validatePasswordEncrypted(String password, String encryptedPassword) {
        return BCrypt.checkpw(password, encryptedPassword);
    }
}
