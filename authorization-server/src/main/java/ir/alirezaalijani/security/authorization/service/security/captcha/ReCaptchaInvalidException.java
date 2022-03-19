package ir.alirezaalijani.security.authorization.service.security.captcha;

import java.io.Serial;

public final class ReCaptchaInvalidException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 5861310537366287163L;

    public ReCaptchaInvalidException() {
        super();
    }

    public ReCaptchaInvalidException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ReCaptchaInvalidException(final String message) {
        super(message);
    }

    public ReCaptchaInvalidException(final Throwable cause) {
        super(cause);
    }

}
