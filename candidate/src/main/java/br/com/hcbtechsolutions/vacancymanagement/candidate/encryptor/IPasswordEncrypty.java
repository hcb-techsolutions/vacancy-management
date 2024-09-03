package br.com.hcbtechsolutions.vacancymanagement.candidate.encryptor;

public interface IPasswordEncrypty {
    String encryptyPassword(String password);
    boolean validatePasswordEncrypted(String password, String encryptedPassword);
}
