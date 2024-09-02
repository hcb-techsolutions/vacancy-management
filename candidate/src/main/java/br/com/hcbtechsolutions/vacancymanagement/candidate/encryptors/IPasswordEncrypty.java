package br.com.hcbtechsolutions.vacancymanagement.candidate.encryptors;

public interface IPasswordEncrypty {
    String encryptyPassword(String password);
    boolean validatePasswordEncrypted(String password, String encryptedPassword);
}
