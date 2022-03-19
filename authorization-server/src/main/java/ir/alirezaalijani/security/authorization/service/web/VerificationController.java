package ir.alirezaalijani.security.authorization.service.web;

import ir.alirezaalijani.security.authorization.service.domain.request.PasswordUserTokenRequest;
import ir.alirezaalijani.security.authorization.service.service.UserRegisterService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("verification")
public class VerificationController {

    private final UserRegisterService userRegisterService;

    public VerificationController(UserRegisterService userRegisterService) {
        this.userRegisterService = userRegisterService;
    }

    @GetMapping("email/{token}")
    public String emailVerification(@PathVariable String token,Model model) {
        if (userRegisterService.emailVerification(token)) {
            return ViewNames.MAIL_IS_VERIFY;
        }
        model.addAttribute("title","Email Verification Failed");
        model.addAttribute("message","Pleas try to resend new verification email");
        model.addAttribute("action","/register/resend/verification-email");
        return ViewNames.MAIL_VERIFY_FAIL;
    }

    @GetMapping("password/{token}")
    public String passwordVerification(@PathVariable String token,Model model) {
        if (userRegisterService.passwordTokenIsValid(token)) {
            model.addAttribute("token",token);
            var obj= new PasswordUserTokenRequest();
            obj.setToken(token);
            model.addAttribute("forget_request",obj);
            return ViewNames.PASSWORD_CHANGE;
        }
        model.addAttribute("title","Link not valid");
        model.addAttribute("message","Pleas try to resend new verification email");
        model.addAttribute("action","/forget/password");
        return ViewNames.MAIL_VERIFY_FAIL;
    }


    private static class ViewNames{
        private static final String PASSWORD_CHANGE="forget/change-password";
        private static final String MAIL_IS_VERIFY="mail/mail-is-verify";
        private static final String MAIL_VERIFY_FAIL = "error/error-fail-default";
    }
}
