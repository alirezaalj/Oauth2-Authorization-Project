package ir.alirezaalijani.security.authorization.service.web;

import ir.alirezaalijani.security.authorization.service.domain.request.RegisterRequest;
import ir.alirezaalijani.security.authorization.service.domain.request.ResendEmailTokenRequest;
import ir.alirezaalijani.security.authorization.service.security.captcha.ICaptchaService;
import ir.alirezaalijani.security.authorization.service.service.UserRegisterService;
import ir.alirezaalijani.security.authorization.service.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping("register")
public class RegisterController {

    private final UserRegisterService userRegisterService;
    private final UserService userService;

    public RegisterController(UserRegisterService userRegisterService,
                              UserService userService) {
        this.userRegisterService = userRegisterService;
        this.userService = userService;
    }

    @GetMapping("sign-up")
    public String signUpPage(Model model) {
        model.addAttribute("sign_up_request", new RegisterRequest());
        return ViewNames.REGISTER;
    }

    @PostMapping("sign-up")
    public String signUpUser(@ModelAttribute("sign_up_request")
                             @Valid RegisterRequest signUpRequest,
                             BindingResult bindingResult,
                             HttpServletRequest request, Model model) {
        if (bindingResult.hasErrors()) {
            return ViewNames.REGISTER;
        } else if (!signUpRequest.getPassword().equals(signUpRequest.getRePassword())) {
            bindingResult.rejectValue("rePassword", "error.sign_up_request", "Passwords not match");
        }

        if (userService.userExistByUsername(signUpRequest.getUsername())) {
            bindingResult.rejectValue("username", "error.sign_up_request", "Username is used");
        }
        if (userService.userExistByEmail(signUpRequest.getEmail())) {
            bindingResult.rejectValue("email", "error.sign_up_request", "Email is used");
        }
        if (bindingResult.hasErrors()) {
            return ViewNames.REGISTER;
        }
        if (userRegisterService.openSignUp(signUpRequest) != null) {
            return ViewNames.REGISTER_SUCCESS;
        }
        model.addAttribute("error", "Sorry something went wrong please try again");
        return ViewNames.REGISTER;
    }

    @GetMapping("resend/verification-email")
    public String resendVerificationEmail(Model model) {
        model.addAttribute("resend_email_token_request", new ResendEmailTokenRequest());
        return ViewNames.RESEND_ACCOUNT_VALIDATE_EMAIL;
    }

    @PostMapping("resend/verification-email")
    public String resendVerificationEmailPost(@ModelAttribute("resend_email_token_request")
                                              @Valid ResendEmailTokenRequest resendEmailTokenRequest,
                                              BindingResult bindingResult,
                                              HttpServletRequest request, Model model) {
        if (bindingResult.hasErrors()) return ViewNames.RESEND_ACCOUNT_VALIDATE_EMAIL;

        if (!userService.userExistByEmail(resendEmailTokenRequest.getEmail())) {
            bindingResult.rejectValue("email", "error.resend_email_token_request", "This Email Not Registered");
        }
        if (userRegisterService.userExistByEmailAndEmailVerified(resendEmailTokenRequest.getEmail(), true)) {
            bindingResult.rejectValue("email", "error.resend_email_token_request", "This Email Is Verified");
        }
        if (bindingResult.hasErrors()) return ViewNames.RESEND_ACCOUNT_VALIDATE_EMAIL;
        if (userRegisterService.resendEmailVerificationEmail(resendEmailTokenRequest.getEmail())) {
            model.addAttribute("message", "My message");
            return ViewNames.REGISTER_SUCCESS;
        }
        model.addAttribute("error", "Sorry something went wrong please try again");
        return ViewNames.RESEND_ACCOUNT_VALIDATE_EMAIL;
    }

    private static class ViewNames {
        private static final String REGISTER = "register/register";
        private static final String REGISTER_SUCCESS = "register/register-success";
        private static final String RESEND_ACCOUNT_VALIDATE_EMAIL = "register/resend-account-validate-email";
    }
}
