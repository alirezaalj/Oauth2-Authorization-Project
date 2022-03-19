package ir.alirezaalijani.security.authorization.service.mail;

import ir.alirezaalijani.security.authorization.service.mail.model.BasicMailMessage;
import ir.alirezaalijani.security.authorization.service.mail.model.MailMessage;

public interface MailService {

    void mailSend(BasicMailMessage mailMessage,boolean success);
    void publishMail(MailMessage mailMessage);
}
