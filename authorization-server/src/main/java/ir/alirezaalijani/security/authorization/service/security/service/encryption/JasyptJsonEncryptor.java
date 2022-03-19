package ir.alirezaalijani.security.authorization.service.security.service.encryption;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.alirezaalijani.security.authorization.service.config.ApplicationConfigData;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import org.jasypt.iv.RandomIvGenerator;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component("jasyptJsonEncryptor")
public class JasyptJsonEncryptor implements DataEncryptor {

    private final StandardPBEStringEncryptor encryptor;
    private final ObjectMapper objectMapper;

    public JasyptJsonEncryptor(ApplicationConfigData configData,
                               ObjectMapper objectMapper) {

        this.objectMapper = objectMapper;
        this.encryptor = new StandardPBEStringEncryptor();
        this.encryptor.setPassword(configData.sec_enc_token_key);
        this.encryptor.setAlgorithm("PBEWithHMACSHA512AndAES_256");
        this.encryptor.setIvGenerator(new RandomIvGenerator());
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
                log.info("Decrypt Token To Jason Failed");
            }
        }
        return null;
    }
}
