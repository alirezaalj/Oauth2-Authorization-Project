package ir.alirezaalijani.security.authorization.service.security.service.attempt;

public interface LoginAttemptService {
    void loginSucceeded(final String key);
    void loginFailed(final String key);
    boolean isBlocked(final String key);

    void unameFailed(String authenticationName);
    void unameSucceeded(String authenticationName);
    boolean unameBlocked(String authenticationName);
}
