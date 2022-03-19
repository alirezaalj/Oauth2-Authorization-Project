package ir.alirezaalijani.security.authorization.service.domain.request;

import ir.alirezaalijani.security.authorization.service.domain.validators.PasswordValidate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PasswordUserTokenRequest {
    @NotEmpty
    @NotNull
    @Size(min = 5)
    private String token;
    @NotEmpty
    @NotNull
    @Size(min = 8,max = 20)
    @PasswordValidate
    private String newPassword;
    @NotEmpty
    @NotNull
    @Size(min = 8,max = 20)
    @PasswordValidate
    private String reNewPassword;
}
