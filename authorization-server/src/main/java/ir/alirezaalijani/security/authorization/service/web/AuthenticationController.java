package ir.alirezaalijani.security.authorization.service.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("auth")
public class AuthenticationController {

    @GetMapping(value = "login")
    public String loginPage(@RequestParam(name = "message", defaultValue = "") String message,
                            Model model){
        model.addAttribute("message", message);
        return "login/login";
    }
}
