package ir.alirezaalijani.security.authorization.service.service;

import ir.alirezaalijani.security.authorization.service.domain.request.PasswordUserTokenRequest;
import ir.alirezaalijani.security.authorization.service.domain.request.RegisterRequest;
import ir.alirezaalijani.security.authorization.service.repository.model.User;

public interface UserRegisterService {
    User openSignUp(RegisterRequest openSignUpRequest);
    boolean emailVerification(String token);
    boolean tokenCanBeUsed(String id);
    boolean userExistByEmailAndEmailVerified(String email,boolean verification);
    boolean resendEmailVerificationEmail(String email);
    boolean sendUsernameEmail(String email);
    boolean sendPasswordEmail(String email);
    boolean userExistByUsernameAndAccountEnable(String username, boolean b);

    boolean passwordTokenIsValid(String token);

    boolean passwordChange(PasswordUserTokenRequest forgetRequest);
}
