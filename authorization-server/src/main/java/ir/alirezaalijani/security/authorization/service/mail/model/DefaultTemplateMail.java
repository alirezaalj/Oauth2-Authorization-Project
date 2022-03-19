package ir.alirezaalijani.security.authorization.service.mail.model;

import ir.alirezaalijani.security.authorization.service.mail.MailMessageVisitor;

import java.util.Map;

public class DefaultTemplateMail extends BasicMailMessage implements TemplateMailMessage,MailMessage{

    private final String templateHtml;
    private final Map<String, Object> messageModel;

    public DefaultTemplateMail(String toMail, String fromMail, String subject, String message, String actionUrl, String templateHtml) {
        super(toMail, fromMail, subject, message, actionUrl);
        this.templateHtml = templateHtml;
        this.messageModel = Map.of(
                "toMail",toMail,
                "fromMail",fromMail,
                "subject", getSubject(),
                "from",fromMail,
                "actionUrl", actionUrl,
                "message", message
        );
    }

    @Override
    public void accept(MailMessageVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public Map<String, Object> getObjectModel() {
        return this.messageModel;
    }

    @Override
    public String getTemplateHtml() {
        return this.templateHtml;
    }
}
