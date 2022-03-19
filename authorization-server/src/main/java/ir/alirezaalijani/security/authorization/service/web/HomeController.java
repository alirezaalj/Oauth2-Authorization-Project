package ir.alirezaalijani.security.authorization.service.web;


import ir.alirezaalijani.security.authorization.service.config.ApplicationConfigData;
import ir.alirezaalijani.security.authorization.service.domain.request.ContactRequest;
import ir.alirezaalijani.security.authorization.service.security.captcha.ICaptchaService;
import ir.alirezaalijani.security.authorization.service.mail.model.DefaultTemplateMail;
import ir.alirezaalijani.security.authorization.service.mail.model.MailMessage;
import ir.alirezaalijani.security.authorization.service.mail.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;


@Slf4j
@Controller
@RequestMapping
public class HomeController {

    private static final String contactMessage = """
            Hi Im {name} contact with you from website
            my email:{email}
            """;
    private final Resource faviconIco;
    private final MailService mailService;
    private final ApplicationConfigData configData;

    public HomeController(@Value("classpath:static/assets/img/favicon.png") Resource faviconIco,
                          MailService mailService,
                          ApplicationConfigData configData) {
        this.faviconIco = faviconIco;
        this.mailService = mailService;
        this.configData = configData;
    }


    @GetMapping("api")
    @ResponseBody
    public String apiTest(Principal principal) {
        return "Hi you are user " + principal.getName();
    }

    @GetMapping(path = {"/home", "/"})
    public String home() {
        return "home";
    }

    @GetMapping("contact")
    public String contact(Model model) {
        model.addAttribute("contact_request", new ContactRequest());
        model.addAttribute("error", "");
        return "contact";
    }

    @PostMapping("contact")
    public String contactPost(@ModelAttribute("contact_request") @Valid ContactRequest contactRequest,
                              BindingResult bindingResult,
                              HttpServletRequest request,
                              Model model) {
        if (bindingResult.hasErrors()) return "contact";
        MailMessage mailMessage = new DefaultTemplateMail(configData.application_contact_mail,
                contactRequest.getEmail(),
                contactRequest.getSubject(),
                contactMessage.replace("{email}", contactRequest.getEmail())
                        .replace("{name}", contactRequest.getName())
                        .concat(contactRequest.getMessage()),
                "",
                "mail/default-mail"
        );
        log.info("New contact request is received from {}", contactRequest.getEmail());
        mailService.publishMail(mailMessage);
        model.addAttribute("message", "Your message has been sent. Thank you!");
        return "contact";
    }


    @GetMapping(path = {"favicon.png", "favicon.ico"}, produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    public Resource faviconIco() {
        try {
            return new InputStreamResource(faviconIco.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
