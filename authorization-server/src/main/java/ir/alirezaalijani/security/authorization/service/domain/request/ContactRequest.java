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
public class ContactRequest {
    @NotEmpty(message = "Name is required")
    @NotNull(message = "Name is required")
    @Size(min = 3,max = 40,message = "Name must contain a length of at least 3 and a maximum of 40 characters.")
    private String name;
    @Email(message = "Enter valid Email!")
    private String email;
    @NotEmpty(message = "Subject is required")
    @NotNull(message = "Subject is required")
    @Size(min = 3,max = 100,message = "Subject must contain a length of at least 3 and a maximum of 100 characters.")
    private String subject;
    @NotEmpty(message = "Message is required")
    @NotNull(message = "Message is required")
    @Size(min = 20,max = 250,message = "Message must contain a length of at least 20 and a maximum of 250 characters.")
    private String message;
}
