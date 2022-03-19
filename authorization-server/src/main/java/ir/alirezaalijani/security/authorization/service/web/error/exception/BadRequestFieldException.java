package ir.alirezaalijani.security.authorization.service.web.error.exception;

import org.springframework.validation.BindingResult;

public class BadRequestFieldException  extends RuntimeException {

    private BindingResult bindingResult;
    public BadRequestFieldException(String message, BindingResult bindingResult) {
        super(message);
        this.bindingResult = bindingResult;
    }

    public BindingResult getBindingResult() {
        return bindingResult;
    }
}
