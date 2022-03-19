package ir.alirezaalijani.security.authorization.service.mail.model;

import ir.alirezaalijani.security.authorization.service.mail.MailMessageVisitor;

public class SimpleTextMail extends BasicMailMessage implements MailMessage {
    public SimpleTextMail(String toMail, String fromMail, String subject, String message,String actionUrl) {
        super(toMail, fromMail, subject, message,actionUrl);
    }

    @Override
    public void accept(MailMessageVisitor visitor) {
         visitor.visit(this);
    }
}
