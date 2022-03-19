package ir.alirezaalijani.security.authorization.service.web;

import ir.alirezaalijani.security.authorization.service.domain.request.ForgetPasswordRequest;
import ir.alirezaalijani.security.authorization.service.domain.request.ForgetUsernameRequest;
import ir.alirezaalijani.security.authorization.service.domain.request.PasswordUserTokenRequest;
import ir.alirezaalijani.security.authorization.service.security.captcha.ICaptchaService;
import ir.alirezaalijani.security.authorization.service.service.UserRegisterService;
import ir.alirezaalijani.security.authorization.service.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("forget")
public class ForgetController {

    private final UserService userService;
    private final UserRegisterService userRegisterService;

    public ForgetController(UserService userService,
                            UserRegisterService userRegisterService) {
        this.userService = userService;
        this.userRegisterService = userRegisterService;
    }

    /**
     * @return forget password page
     */
    @GetMapping("password")
    public String forgetPasswordPage(Model model, @RequestParam(name = "message", defaultValue = "") String message) {
        model.addAttribute("forget_request", new ForgetPasswordRequest());
        model.addAttribute("message", message);
        return ViewNames.FORGET_PASSWORD;
    }

    /**
     * @param forgetRequest get username to find user and send recover email
     * @param bindingResult validate inputs
     * @param request       used fot
     * @param model         g-recaptcha-response
     * @return message default page
     */
    @PostMapping("password")
    public String forgetPasswordPost(@ModelAttribute("forget_request")
                                     @Valid ForgetPasswordRequest forgetRequest,
                                     BindingResult bindingResult,
                                     HttpServletRequest request, Model model) {
        if (bindingResult.hasErrors()) return ViewNames.FORGET_PASSWORD;
        final String response_v2 = request.getParameter("g-recaptcha-response");

        if (!userService.userExistByUsername(forgetRequest.getUsername())) {
            bindingResult.rejectValue("username", "error.forget_request", "This Email Not Registered");
        }
        if (userRegisterService.userExistByUsernameAndAccountEnable(forgetRequest.getUsername(), false)) {
            bindingResult.rejectValue("username", "error.forget_request", "This Account Is Not Verified. or Account is disabled");
        }
        if (bindingResult.hasErrors()) return ViewNames.FORGET_PASSWORD;

        if (userRegisterService.sendPasswordEmail(forgetRequest.getUsername())) {
            model.addAttribute("title", "SUCCESS");
            model.addAttribute("message", "Recover password link is send successfully to your email;<br/> Pleas check your email!");
            return ViewNames.MESSAGE_DEFAULT;
        }
        model.addAttribute("error", "Sorry something went wrong please try again");
        return ViewNames.FORGET_PASSWORD;
    }

    @PostMapping("change/password")
    public String changePasswordPost(@ModelAttribute("forget_request")
                                     @Valid PasswordUserTokenRequest forgetRequest,
                                     BindingResult bindingResult,
                                     HttpServletRequest request, Model model) {
        if (bindingResult.hasErrors()) return ViewNames.PASSWORD_CHANGE;
        if (!forgetRequest.getNewPassword().equals(forgetRequest.getReNewPassword())) {
            bindingResult.rejectValue("reNewPassword", "error.forget_request", "Passwords not match!");
            return ViewNames.PASSWORD_CHANGE;
        }

        if (userRegisterService.passwordChange(forgetRequest)) {
            model.addAttribute("title", "SUCCESS");
            model.addAttribute("message", "Your password is change successfully");
            return ViewNames.MESSAGE_DEFAULT;
        }
        model.addAttribute("error", "Sorry something went wrong please try again");
        return ViewNames.PASSWORD_CHANGE;
    }

    @GetMapping("username")
    public String forgetUsernamePage(Model model, @RequestParam(name = "message", defaultValue = "") String message) {
        model.addAttribute("forget_request", new ForgetUsernameRequest());
        model.addAttribute("message", message);
        model.addAttribute("resend", false);
        return ViewNames.FORGET_USERNAME;
    }

    @PostMapping("username")
    public String forgetUsernamePost(@ModelAttribute("forget_request")
                                     @Valid ForgetUsernameRequest forgetRequest,
                                     BindingResult bindingResult,
                                     HttpServletRequest request, Model model) {
        if (bindingResult.hasErrors()) return ViewNames.FORGET_USERNAME;

        if (!userService.userExistByEmail(forgetRequest.getEmail())) {
            bindingResult.rejectValue("email", "error.forget_request", "This Email Not Registered");
        }
        if (!userRegisterService.userExistByEmailAndEmailVerified(forgetRequest.getEmail(), true)) {
            bindingResult.rejectValue("email", "error.forget_request", "This Email Is Not Verified");
        }
        if (bindingResult.hasErrors()) return ViewNames.FORGET_USERNAME;

        if (userRegisterService.sendUsernameEmail(forgetRequest.getEmail())) {
            model.addAttribute("title", "SUCCESS");
            model.addAttribute("message", "Your Account info send successfully;<br/> Pleas check your email!");
            return ViewNames.MESSAGE_DEFAULT;
        }

        model.addAttribute("error", "Sorry something went wrong please try again");

        return ViewNames.FORGET_USERNAME;
    }

    private static class ViewNames {
        private static final String PASSWORD_CHANGE = "forget/change-password";
        private static final String FORGET_USERNAME = "forget/forget-username";
        private static final String FORGET_PASSWORD = "forget/forget-password";
        private static final String MESSAGE_DEFAULT = "message-default";
    }
}
