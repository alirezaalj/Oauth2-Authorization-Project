package ir.alirezaalijani.security.authorization.service.security.service.encryption;

public interface DataEncryptor {
    String encryptDataToToken(Object data);
    <T> T decryptTokenToData(String token,Class<T> dataClass);
}
