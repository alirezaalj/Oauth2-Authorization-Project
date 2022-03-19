package ir.alirezaalijani.security.authorization.service.mail.model;

import ir.alirezaalijani.security.authorization.service.mail.MailMessageVisitor;

import java.util.Map;

public class UserVerificationMail extends BasicMailMessage implements TemplateMailMessage,MailMessage{

    private final String hostUrl;
    private final String templateHtml;
    private final Map<String, Object> messageModel;

    public UserVerificationMail(String toMail,
                                String fromMail,
                                String subject,
                                String hostUrl,
                                String actionUrl,
                                String message,
                                String templateHtml) {
        super(toMail, fromMail, subject,message,actionUrl);
        this.hostUrl = hostUrl;
        this.templateHtml=templateHtml;
        this.messageModel = Map.of(
                "subject", getSubject(),
                "hostUrl", hostUrl,
                "actionUrl", actionUrl,
                "message", message
        );
    }

    @Override
    public void accept(MailMessageVisitor visitor) {
         visitor.visit(this);
    }

    public String getHostUrl() {
        return hostUrl;
    }

    @Override
    public String getTemplateHtml() {
        return templateHtml;
    }

    @Override
    public Map<String, Object> getObjectModel() {
        return this.messageModel;
    }
}
