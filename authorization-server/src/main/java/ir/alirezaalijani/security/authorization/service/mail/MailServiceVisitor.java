package ir.alirezaalijani.security.authorization.service.mail;

import ir.alirezaalijani.security.authorization.service.config.ApplicationConfigData;
import ir.alirezaalijani.security.authorization.service.mail.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.retry.RetryOperations;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.HashMap;

@Slf4j
@Service
public class MailServiceVisitor implements MailMessageVisitor {

    private final JavaMailSender javaMailSender;
    private final MailService mailService;
    private final RetryOperations retryOperations;
    private final SpringTemplateEngine thymeleafTemplateEngine;
    private final ApplicationConfigData applicationConfigData;

    public MailServiceVisitor(JavaMailSender javaMailSender,
                              MailService mailService,
                              RetryOperations retryOperations,
                              SpringTemplateEngine thymeleafTemplateEngine,
                              ApplicationConfigData applicationConfigData) {
        this.javaMailSender = javaMailSender;
        this.mailService = mailService;
        this.retryOperations = retryOperations;
        this.thymeleafTemplateEngine = thymeleafTemplateEngine;
        this.applicationConfigData = applicationConfigData;
    }

    @Override
    public void visit(UserVerificationMail o) {
        mailService.mailSend(o, processMail(o, o));
    }

    @Override
    public void visit(PasswordChangeMail o) {
        mailService.mailSend(o, processMail(o, o));
    }

    @Override
    public void visit(NotificationMail o) {
        mailService.mailSend(o, processMail(o, o));
    }

    @Override
    public void visit(DefaultTemplateMail o) {
        mailService.mailSend(o,processMail(o,o));
    }

    @Override
    public void visit(SimpleTextMail o) {
        mailService.mailSend(o, processMail(o, null));
    }

    private boolean processMail(BasicMailMessage mailMessage, TemplateMailMessage templateMailMessage) {
        try {
            return retryOperations.execute(context -> {
                if (context.getRetryCount() == 0) {
                    log.info("Send new Mail From {} To {}", mailMessage.getFromMail(), mailMessage.getToMail());
                } else {
                    log.error("Error at Send Email message: {}", context.getLastThrowable().getMessage());
                    log.info("Retry send email From {} ,To {} for the {} time", mailMessage.getFromMail(), mailMessage.getToMail(), context.getRetryCount());
                }
                if (templateMailMessage != null) {
                    String html = processTemplate(templateMailMessage);
                    if (html != null) {
                        sendHtmlMessage(mailMessage, html);
                    } else {
                        sendBasicMail(mailMessage);
                    }
                } else {
                    sendBasicMail(mailMessage);
                }
                return true;
            });
        } catch (Exception e) {
            log.error("Exit retry email sending message: {}", e.getMessage());
        }
        return false;
    }

    private void sendBasicMail(BasicMailMessage o) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(o.getFromMail());
        message.setTo(o.getToMail());
        message.setSubject(o.getSubject());
        message.setText(o.getMessage());
        javaMailSender.send(message);
    }

    private String processTemplate(TemplateMailMessage templateMailMessage) {
        var thymeleafContext = new Context();
        var templateVariables=new HashMap<>(templateMailMessage.getObjectModel());
        templateVariables.put("application_name",applicationConfigData.application_name);
        templateVariables.put("application_host",applicationConfigData.application_host);
        templateVariables.put("date",new Date());
        thymeleafContext.setVariables(templateVariables);
        try {
            return thymeleafTemplateEngine.process(templateMailMessage.getTemplateHtml(), thymeleafContext);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    private void sendHtmlMessage(BasicMailMessage mailMessage, String htmlBody) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(mailMessage.getFromMail());
        helper.setTo(mailMessage.getToMail());
        helper.setSubject(mailMessage.getSubject());
        helper.setText(htmlBody, true);
        javaMailSender.send(message);
    }
}
