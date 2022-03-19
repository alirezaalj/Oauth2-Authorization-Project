package ir.alirezaalijani.security.authorization.service.domain.request;

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
public class ForgetUsernameRequest {

    @NotNull(message = "Email is required")
    @NotEmpty(message = "Email is required")
    @Size(min = 5,max = 100,message = "Email must have min 5 ,max 100 character ")
    @Email(message = "Email Not Valid!")
    private String email;
}
