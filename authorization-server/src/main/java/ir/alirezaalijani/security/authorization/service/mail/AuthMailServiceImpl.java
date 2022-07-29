package ir.alirezaalijani.security.authorization.service.mail;

import ir.alirezaalijani.security.authorization.service.repository.MailRepository;
import ir.alirezaalijani.security.authorization.service.repository.model.ApplicationMail;
import ir.alirezaalijani.spring.mail.module.mail.MailService;
import ir.alirezaalijani.spring.mail.module.mail.model.BasicMailMessage;
import ir.alirezaalijani.spring.mail.module.mail.model.HtmlMail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Primary
@Service
public class AuthMailServiceImpl extends MailService {

    private final MailRepository mailRepository;

    public AuthMailServiceImpl(MailRepository mailRepository) {
        this.mailRepository = mailRepository;
    }

    @Override
    public void mailSend(BasicMailMessage mailMessage, boolean success) {
        log.info("save mail to data base");
        var mail = ApplicationMail.builder();
        if (mailMessage != null) {
            mail = mail.fromMail(mailMessage.getFromMail())
                    .toMail(mailMessage.getToMail())
                    .sendAt(new Date())
                    .message(mailMessage.getMessage())
                    .subject(mailMessage.getSubject())
                    .actionUrl(mailMessage.getActionUrl())
                    .isSend(success);
            if (mailMessage instanceof HtmlMail) {
                mail = mail.templateHtml(((HtmlMail) mailMessage).getTemplateHtml());
            }
        }
        var applicationMail = mail.build();
        mailRepository.save(applicationMail);
    }
}
