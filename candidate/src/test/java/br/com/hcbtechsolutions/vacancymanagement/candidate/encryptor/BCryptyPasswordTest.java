package br.com.hcbtechsolutions.vacancymanagement.candidate.encryptor;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BCryptyPasswordTest {

    private IPasswordEncrypty passwordEncryptor;

    @BeforeEach
    public void setUp() {
        passwordEncryptor = new BCryptyPassword();
    }

    @Test
    public void testEncryptPassword() {
        // Given
        String plainPassword = "securePassword123";

        // When
        String encryptedPassword = passwordEncryptor.encryptyPassword(plainPassword);

        // Then
        assertNotNull(encryptedPassword, "Encrypted password should not be null");
        assertNotEquals(plainPassword, encryptedPassword, "Encrypted password should not be equal to the plain password");
        assertTrue(encryptedPassword.startsWith("$2a$"), "Encrypted password should follow BCrypt format");
    }

    @Test
    public void testEncryptPasswordDifferentOutputs() {
        // Given
        String plainPassword = "samePassword";

        // When
        String encryptedPassword1 = passwordEncryptor.encryptyPassword(plainPassword);
        String encryptedPassword2 = passwordEncryptor.encryptyPassword(plainPassword);

        // Then
        assertNotEquals(encryptedPassword1, encryptedPassword2, "Encrypting the same password should produce different results");
    }

    @Test
    public void testVerifyPassword_Success() {
        // Given
        String plainPassword = "passwordToCheck";
        String encryptedPassword = passwordEncryptor.encryptyPassword(plainPassword);

        // When
        boolean isPasswordMatch = passwordEncryptor.validatePasswordEncrypted(plainPassword, encryptedPassword);

        // Then
        assertTrue(isPasswordMatch, "The plain password should match the encrypted password");
    }

    @Test
    public void testVerifyPassword_Failure() {
        // Given
        String plainPassword = "correctPassword";
        String encryptedPassword = passwordEncryptor.encryptyPassword(plainPassword);

        // When
        boolean isPasswordMatch = passwordEncryptor.validatePasswordEncrypted("wrongPassword", encryptedPassword);

        // Then
        assertFalse(isPasswordMatch, "The plain password should not match the encrypted password");
    }

    @Test
    public void testEncryptAndVerifyWithEmptyPassword() {
        // Given
        String emptyPassword = "";

        // When
        String encryptedPassword = passwordEncryptor.encryptyPassword(emptyPassword);
        boolean isPasswordMatch = passwordEncryptor.validatePasswordEncrypted(emptyPassword, encryptedPassword);

        // Then
        assertTrue(isPasswordMatch, "Empty password should match its own encrypted form");
    }
}
