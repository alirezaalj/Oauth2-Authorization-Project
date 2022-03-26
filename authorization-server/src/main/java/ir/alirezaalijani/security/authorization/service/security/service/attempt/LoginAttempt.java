package ir.alirezaalijani.security.authorization.service.security.service.attempt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serial;
import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class LoginAttempt implements Serializable{

    @JsonIgnore
    @Serial
    private static final long serialVersionUID = 1L;
    @JsonProperty
    private String ip;
    @JsonProperty
    private int attempts;
    @JsonProperty
    private long expireAt;

    public void increment(int dl){
        this.attempts+=dl;
    }

    public void setExpireAt(long expire){
        this.expireAt=expire;
    }

    public boolean isExpired(){
        return System.currentTimeMillis() > this.expireAt;
    }
}
