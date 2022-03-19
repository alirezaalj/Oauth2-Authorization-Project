package ir.alirezaalijani.security.authorization.service.service;

import ir.alirezaalijani.security.authorization.service.mail.MailSendEvent;
import ir.alirezaalijani.security.authorization.service.mail.model.PasswordChangeMail;
import ir.alirezaalijani.security.authorization.service.repository.UserRepository;
import ir.alirezaalijani.security.authorization.service.repository.model.User;
import ir.alirezaalijani.security.authorization.service.security.service.encryption.DataEncryptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher applicationEventPublisher;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           ApplicationEventPublisher applicationEventPublisher) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with id %s not fond", username)));
    }


    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with id %s not fond", email)));
    }

    @Override
    public User findUserByUsernameOrEmail(String username) {
        return userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with id %s not fond", username)));
    }


    @Override
    public Integer findIdByUsername(String name) {
        return userRepository.findIdByUsername(name)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with id %s not fond", name)));
    }

    @Override
    public boolean userExistByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Boolean userExistByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public Boolean userExistByUsernameOrEmail(String username, String email) {
        return userRepository.existsByUsernameOrEmail(username, email);
    }

    @Override
    public void updateUserLastLogin(String name) {
        userRepository.updateUserLastLogin(name, new Date());
    }

    @Override
    public void updateUserPassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        sendPasswordChangeEmail(user, "warning", "Warning! Your account password has changed. If you have not made this change, create a new password for your account using the link below.");
    }

    @Override
    public void updateUserPasswordByToken(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public User passwordTokenValidation(String token) {
        return null;
    }

    @Override
    public boolean checkUserPasswordMach(User user, String lastPassword) {
        return passwordEncoder.matches(user.getPassword(), lastPassword);
    }

    @Override
    public boolean sendPasswordChangeEmail(User user, String type, String message) {
        PasswordChangeMail userPasswordChangeMail = null;
//                new PasswordChangeMail(user.getEmail(), type,
//                       "host",
//                        user.getUsername(), message,
//                        simpleJsonEncryptor
//                                .encryptDataToToken(new PasswordChangeMailToken(UUID.randomUUID().toString(), user.getUsername()
//                                                , user.getEmail()
//                                                , new Date(System.currentTimeMillis() + (DAY_IN_MS))
//                                        )
//                                )
//                );

        applicationEventPublisher.publishEvent(new MailSendEvent(userPasswordChangeMail));
        return true;
    }

}
