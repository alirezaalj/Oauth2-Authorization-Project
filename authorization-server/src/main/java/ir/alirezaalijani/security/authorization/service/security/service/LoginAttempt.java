package ir.alirezaalijani.security.authorization.service.security.service;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
public class LoginAttempt implements Serializable {
    @Serial
    private static final long serialVersionUID = -7817224776021728682L;

    private String ip;
    private int attempts;
    private long expireAt;
    public void increment(int dl){
        this.attempts+=dl;
    }

    public void setExpireAt(long expire){
        this.expireAt=expire;
    }

    public boolean isExpired(){
        if (System.currentTimeMillis()>this.expireAt){
            return true;
        }
        return false;
    }
}
