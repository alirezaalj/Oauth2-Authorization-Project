package ir.alirezaalijani.security.authorization.service.mail.model;

public abstract class BasicMailMessage {
    private final String toMail;
    private final String fromMail;
    private final String subject;
    private final String message;
    private final String actionUrl;

    public BasicMailMessage(String toMail, String fromMail, String subject,String message,final String actionUrl) {
        this.toMail = toMail;
        this.fromMail = fromMail;
        this.subject = subject;
        this.message=message;
        this.actionUrl=actionUrl;
    }

    public String getToMail() {
        return toMail;
    }

    public String getFromMail() {
        return fromMail;
    }

    public String getSubject() {
        return subject;
    }

    public String getMessage() {
        return message;
    }

    public String getActionUrl() {
        return actionUrl;
    }
}
