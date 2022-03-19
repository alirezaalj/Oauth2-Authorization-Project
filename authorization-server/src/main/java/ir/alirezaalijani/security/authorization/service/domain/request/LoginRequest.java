package ir.alirezaalijani.security.authorization.service.domain.request;

import ir.alirezaalijani.security.authorization.service.domain.validators.UsernameValidate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @NotNull(message = "Username is required")
    @Size(min =5,max = 20,message = "Username must have min 5 ,max 30 character ")
    @UsernameValidate
    private String username;
    @NotNull(message = "Password is required")
    @NotEmpty(message = "Password is required")
    @Size(min = 8,max = 20,message = "Password must have min 8 ,max 30 character ")
    private String password;
    private Boolean remember;
}
