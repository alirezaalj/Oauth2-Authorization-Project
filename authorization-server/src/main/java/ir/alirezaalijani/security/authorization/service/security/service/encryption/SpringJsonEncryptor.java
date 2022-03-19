package ir.alirezaalijani.security.authorization.service.security.service.encryption;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.alirezaalijani.security.authorization.service.config.ApplicationConfigData;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component("springJsonEncryptor")
public class SpringJsonEncryptor implements DataEncryptor{

    private final TextEncryptor encryptor;
    private final ObjectMapper objectMapper;

    public SpringJsonEncryptor(ApplicationConfigData configData,ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
       this.encryptor = Encryptors.text(configData.sec_enc_token_key, configData.sec_enc_token_salt);
    }

    @Override
    public String encryptDataToToken(Object data) {
        if (Objects.nonNull(data)){
            try {
                return encryptor.encrypt(objectMapper.writeValueAsString(data));
            } catch (JsonProcessingException e) {
                log.error("Encrypt Object To Token Failed");
            }
        }
        return null;
    }

    @Override
    public <T> T decryptTokenToData(String token, Class<T> dataClass) {
        if (token!=null){
            try {
                return objectMapper.readValue(encryptor.decrypt(token),dataClass);
            }catch (JsonProcessingException | EncryptionOperationNotPossibleException e){
                log.info("Decrypt Token To Json Failed");
            }
        }
        return null;
    }
}
