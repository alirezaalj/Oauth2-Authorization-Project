package ir.alirezaalijani.security.authorization.service.domain.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResendEmailTokenRequest {
    @Email(message = "Email Not Valid!")
    @Size(min = 5,max = 100,message = "Email must have min 5 ,max 100 character ")
    private String email;
}
