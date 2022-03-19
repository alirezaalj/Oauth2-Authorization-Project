package ir.alirezaalijani.security.authorization.service.mail;

import ir.alirezaalijani.security.authorization.service.mail.model.MailMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Async
@Component
public class MailSendListener implements ApplicationListener<MailSendEvent> {

    private final MailMessageVisitor mailService;

    public MailSendListener(MailMessageVisitor mailService) {
        this.mailService = mailService;
    }

    @Override
    public void onApplicationEvent(final MailSendEvent mailSendEvent) {
        if (mailSendEvent.getSource() != null){
            final MailMessage mailMessage=mailSendEvent.getMailMessage();
            mailMessage.accept(mailService);
        }else {
            log.error("Mail event is null");
        }
    }

}
