package ir.alirezaalijani.security.authorization.service.service;

import ir.alirezaalijani.security.authorization.service.config.ApplicationConfigData;
import ir.alirezaalijani.security.authorization.service.domain.request.PasswordUserTokenRequest;
import ir.alirezaalijani.security.authorization.service.domain.request.RegisterRequest;
import ir.alirezaalijani.security.authorization.service.mail.MailService;
import ir.alirezaalijani.security.authorization.service.mail.model.DefaultTemplateMail;
import ir.alirezaalijani.security.authorization.service.mail.model.UserVerificationMail;
import ir.alirezaalijani.security.authorization.service.repository.RoleRepository;
import ir.alirezaalijani.security.authorization.service.repository.TokenRepository;
import ir.alirezaalijani.security.authorization.service.repository.UserRepository;
import ir.alirezaalijani.security.authorization.service.repository.model.ApplicationToken;
import ir.alirezaalijani.security.authorization.service.repository.model.User;
import ir.alirezaalijani.security.authorization.service.security.service.encryption.DataEncryptor;
import ir.alirezaalijani.security.authorization.service.security.token.UserMailToken;
import ir.alirezaalijani.security.authorization.service.web.error.exception.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class UserRegisterServiceImpl implements UserRegisterService {
    public final static long DAY_IN_MS = 1000 * 60 * 60 * 24;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final DataEncryptor simpleJsonEncryptor;
    private final MailService mailService;
    private final TokenRepository tokenRepository;
    private final ApplicationConfigData applicationConfigData;

    private final UserService userService;
    public UserRegisterServiceImpl(UserRepository userRepository,
                                   PasswordEncoder passwordEncoder,
                                   RoleRepository roleRepository,
                                   @Qualifier("springJsonEncryptor") DataEncryptor simpleJsonEncryptor,
                                   MailService mailService,
                                   TokenRepository tokenRepository, ApplicationConfigData applicationConfigData,
                                   UserService userService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.simpleJsonEncryptor = simpleJsonEncryptor;
        this.mailService = mailService;
        this.tokenRepository = tokenRepository;
        this.applicationConfigData = applicationConfigData;
        this.userService = userService;
    }

    @Override
    public User openSignUp(RegisterRequest request) {
        var roles = Collections.singleton(roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new EntityNotFoundException(this.getClass(), "Role Not found")));
        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(
                        DigestUtils.md5DigestAsHex(request.getPassword().getBytes(StandardCharsets.UTF_8)))
                )
                .emailVerification(false) // after email verification
                .enable(true) // default signup
                .accountNonExpired(false)
                .credentialsNonExpired(false)
                .accountNonLocked(false)
                .serviceAccess(true)
                .roles(roles).build();
        var newUser = userRepository.save(user);
        log.info("Register new User {} was successful",newUser.getUsername());
        sendVerificationEmail(newUser);
        return newUser;
    }
    
    @Override
    public boolean resendEmailVerificationEmail(String email) {
        return sendVerificationEmail(userService.findUserByEmail(email));
    }

    @Override
    public boolean sendUsernameEmail(String email) {
        var user=userService.findUserByEmail(email);
        var userInfoText="Username:"+user.getUsername()+" <\br>"+
                "Email:"+user.getEmail()+" <\br>"+
                "Enable:"+(user.getEmailVerification()&&user.getEnable());
        var userInfoEmail= new DefaultTemplateMail(
                user.getEmail(),"no-reply@" + applicationConfigData.application_host,
                "Account Info",
                userInfoText,applicationConfigData.application_host,
                "mail/default-mail"
        );
        mailService.publishMail(userInfoEmail);
        return true;
    }

    @Override
    public boolean sendPasswordEmail(String email) {
        var user=userService.findUserByUsername(email);
        var mailToken = UserMailToken.builder()
                .id(UUID.randomUUID().toString())
                .username(user.getUsername())
                .email(user.getEmail())
                .expiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10)))
                .build();
        var token = simpleJsonEncryptor.encryptDataToToken(mailToken);
        if(token!=null){
            var actionUrl=applicationConfigData.sec_login_email_validate_url
                    .replace("{token}", token)
                    .replace("{path}","password");
            var verificationMail =
                    new UserVerificationMail(user.getEmail(),
                            "no-reply@" + applicationConfigData.application_host,
                            "Change Your Password",
                            applicationConfigData.application_host,
                            actionUrl,
                            "You Can use the link at blow to change your account password.",
                            "mail/default-mail"
                    );
            var applicationToken = ApplicationToken.builder()
                    .id(mailToken.getId())
                    .type("PASSWORD_CHANGE")
                    .username(mailToken.getUsername())
                    .expired(false)
                    .token(token)
                    .build();
            tokenRepository.save(applicationToken);
            mailService.publishMail(verificationMail);
            return true;
        }
        return false;
    }

    private boolean sendVerificationEmail(User user){
        var mailToken = UserMailToken.builder()
                .id(UUID.randomUUID().toString())
                .username(user.getUsername())
                .email(user.getEmail())
                .expiration(new Date(System.currentTimeMillis() + (5 * DAY_IN_MS)))
                .build();
        var token = simpleJsonEncryptor.encryptDataToToken(mailToken);
        if(token!=null){
            var emailVerifyActionUrl = applicationConfigData.sec_login_email_validate_url
                    .replace("{token}", token)
                    .replace("{path}","email");
            var verificationMail =
                    new UserVerificationMail(user.getEmail(),
                            "no-reply@" + applicationConfigData.application_host,
                            "Verify Your Account",
                            applicationConfigData.application_host,
                            emailVerifyActionUrl,
                            "Register new Account was successfully. Pleas Verify Your Email",
                            "mail/verify-mail"
                    );
            var applicationToken = ApplicationToken.builder()
                    .id(mailToken.getId())
                    .type("MAIL_VERIFICATION")
                    .username(mailToken.getUsername())
                    .expired(false)
                    .token(token)
                    .build();
            tokenRepository.save(applicationToken);
            mailService.publishMail(verificationMail);
        }
        return false;
    }


    @Override
    public boolean emailVerification(String token) {
        if (token != null) {
            UserMailToken mailToken = simpleJsonEncryptor.decryptTokenToData(token, UserMailToken.class);
            if (mailToken != null && tokenCanBeUsed(mailToken.getId()) && mailToken.getExpiration().compareTo(new Date()) > 0) {
                User user = userService.findUserByUsername(mailToken.getUsername());
                if (user != null && !user.getEmailVerification()) {
                    user.setEmailVerification(true);
                    userRepository.save(user);
                    log.info("User {} verify the email {}",user.getUsername(),user.getEmail());
                    tokenRepository.updateTokenUsed(mailToken.getId());
                    log.info("Token '{}' is used",mailToken.getId());
                    return true;
                }
            }
        }
        log.info("Verifying email failed");
        return false;
    }

    @Override
    public boolean passwordTokenIsValid(String token) {
        if (token != null) {
            UserMailToken mailToken = simpleJsonEncryptor.decryptTokenToData(token, UserMailToken.class);
            if (mailToken != null && tokenCanBeUsed(mailToken.getId()) && mailToken.getExpiration().compareTo(new Date()) > 0) {
                User user = userService.findUserByUsername(mailToken.getUsername());
                if (user != null && user.getEnable()) {
                    log.info("Password token is valid {}",mailToken.getId());
                    return true;
                }
            }
        }
        log.info("Validating password token failed");
        return false;
    }

    @Override
    public boolean passwordChange(PasswordUserTokenRequest forgetRequest) {
        if (forgetRequest != null && forgetRequest.getToken()!=null) {
            UserMailToken mailToken = simpleJsonEncryptor.decryptTokenToData(forgetRequest.getToken(), UserMailToken.class);
            if (mailToken != null && tokenCanBeUsed(mailToken.getId()) && mailToken.getExpiration().compareTo(new Date()) > 0) {
                User user = userService.findUserByUsername(mailToken.getUsername());
                if (user != null && user.getEnable()) {
                    log.info("Password token is valid {}",mailToken.getId());
                    user.setPassword(passwordEncoder.encode(
                            DigestUtils.md5DigestAsHex(forgetRequest.getNewPassword().getBytes(StandardCharsets.UTF_8))
                    ));
                    userRepository.save(user);
                    log.info("User {} password change",user.getUsername());
                    tokenRepository.updateTokenUsed(mailToken.getId());
                    log.info("Token '{}' is used",mailToken.getId());
                    return true;
                }
            }
        }
        log.info("Validating password token failed");
        return false;
    }

    @Override
    public boolean tokenCanBeUsed(String id) {
        return !tokenRepository.existsByIdAndExpired(id, true);
    }


    @Override
    public boolean userExistByEmailAndEmailVerified(String email,boolean verification) {
        return userRepository.existsByEmailAndEmailVerification(email,verification);
    }

    @Override
    public boolean userExistByUsernameAndAccountEnable(String username, boolean b) {
        return userRepository.existsByUsernameAndEmailVerificationAndEnable(username,b,b);
    }
}
