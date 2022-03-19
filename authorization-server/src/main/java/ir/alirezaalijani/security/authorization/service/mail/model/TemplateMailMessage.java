package ir.alirezaalijani.security.authorization.service.mail.model;

import java.util.Map;

public interface TemplateMailMessage {
    Map<String,Object> getObjectModel();
    String getTemplateHtml();
}
