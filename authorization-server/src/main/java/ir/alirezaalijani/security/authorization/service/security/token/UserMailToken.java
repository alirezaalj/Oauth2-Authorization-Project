package ir.alirezaalijani.security.authorization.service.security.token;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserMailToken {
    private String id;
    private String username;
    private String email;
    private Date expiration;
}
