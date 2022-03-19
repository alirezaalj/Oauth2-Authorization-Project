package ir.alirezaalijani.security.authorization.service.mail.model;

import ir.alirezaalijani.security.authorization.service.mail.MailMessageVisitor;

import java.util.Date;
import java.util.Map;

public class NotificationMail extends BasicMailMessage implements MailMessage , TemplateMailMessage{

    private final String hostUrl;
    private final String username;
    private final String notMeUrl;
    private final Date date;
    private final String templateHtml;
    private final Map<String, Object> messageModel;

    public NotificationMail(String toMail,
                            String fromMail,
                            String subject,
                            String hostUrl,
                            String actionUrl,
                            String username,
                            String message,
                            String notMeUrl,
                            Date date,
                            String templateHtml) {
        super(toMail, fromMail, subject,message,actionUrl);
        this.hostUrl = hostUrl;
        this.username = username;
        this.notMeUrl = notMeUrl;
        this.date = date;
        this.templateHtml=templateHtml;
        this.messageModel=Map.of(
                "subject", getSubject(),
                "hostUrl", hostUrl,
                "actionUrl", actionUrl,
                "message", message,
                "username",username,
                "notMeUrl",notMeUrl,
                "date",date
        );
    }

    @Override
    public void accept(MailMessageVisitor visitor) {
         visitor.visit(this);
    }

    public String getHostUrl() {
        return hostUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getNotMeUrl() {
        return notMeUrl;
    }

    public Date getDate() {
        return date;
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
