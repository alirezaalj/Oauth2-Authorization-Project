package ir.alirezaalijani.security.authorization.service.domain.request;

import ir.alirezaalijani.security.authorization.service.domain.validators.AgreementValidate;
import ir.alirezaalijani.security.authorization.service.domain.validators.PasswordValidate;
import ir.alirezaalijani.security.authorization.service.domain.validators.UsernameValidate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotEmpty(message = "Username is required")
    @NotNull(message = "Username is required")
    @UsernameValidate
    @Size(min = 5,max = 20,message = "Username must have min 5 ,max 20 character ")
    private String username;
    @Email(message = "Email Not Valid!")
    @Size(min = 5,max = 100,message = "Email must have min 5 ,max 100 character ")
    private String email;
    @NotEmpty(message = "Password is required")
    @NotNull(message = "Password is required")
    @Size(min = 8,max = 20,message ="Password must have min 5 ,max 30 character ")
    @PasswordValidate
    private String password;
    @NotEmpty(message = "Re Password is required")
    @NotNull(message = "Re Password is required")
    @Size(min = 8,max = 30,message = "Password must have min 5 ,max 30 character ")
    private String rePassword;
    @AgreementValidate
    @NotNull(message = "Agreement is required")
    private Boolean agreement;
}
