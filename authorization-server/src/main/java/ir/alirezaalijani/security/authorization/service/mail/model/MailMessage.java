package ir.alirezaalijani.security.authorization.service.mail.model;

import ir.alirezaalijani.security.authorization.service.mail.MailMessageVisitor;

public interface MailMessage {
    void accept(MailMessageVisitor visitor);
}
