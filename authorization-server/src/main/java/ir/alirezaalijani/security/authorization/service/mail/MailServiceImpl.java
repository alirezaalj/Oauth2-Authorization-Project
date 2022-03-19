package ir.alirezaalijani.security.authorization.service.mail;

import ir.alirezaalijani.security.authorization.service.repository.MailRepository;
import ir.alirezaalijani.security.authorization.service.repository.model.ApplicationMail;
import ir.alirezaalijani.security.authorization.service.mail.model.BasicMailMessage;
import ir.alirezaalijani.security.authorization.service.mail.model.MailMessage;
import ir.alirezaalijani.security.authorization.service.mail.model.TemplateMailMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class MailServiceImpl implements MailService {
    private final MailRepository mailRepository;
    private final ApplicationEventPublisher eventPublisher;

    public MailServiceImpl(MailRepository mailRepository,
                           ApplicationEventPublisher eventPublisher) {
        this.mailRepository = mailRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void mailSend(BasicMailMessage mailMessage, boolean success) {
        var mail = ApplicationMail.builder();
        if (mailMessage != null) {
            mail = mail.fromMail(mailMessage.getFromMail())
                    .toMail(mailMessage.getToMail())
                    .sendAt(new Date())
                    .message(mailMessage.getMessage())
                    .subject(mailMessage.getSubject())
                    .actionUrl(mailMessage.getActionUrl())
                    .isSend(success);
            if (mailMessage instanceof TemplateMailMessage) {
                mail = mail
                        .templateHtml(((TemplateMailMessage) mailMessage).getTemplateHtml());
            }
        }
        var applicationMail = mail.build();
        mailRepository.save(applicationMail);
    }

    @Override
    public void publishMail(MailMessage mailMessage) {
        eventPublisher.publishEvent(new MailSendEvent(mailMessage));
    }
}
