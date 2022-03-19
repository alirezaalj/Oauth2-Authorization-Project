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
public class ForgetPasswordRequest {
    @NotNull(message = "Username is required")
    @NotEmpty(message = "Username is required")
    @Size(min = 5 ,max = 20)
    @UsernameValidate
    private String username;
}
