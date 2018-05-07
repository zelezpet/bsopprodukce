/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.ui.utils;

import com.vaadin.data.ValidationResult;
import com.vaadin.data.Validator;
import com.vaadin.data.ValueContext;
import com.vaadin.server.UserError;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Notification;
import java.io.Serializable;
import java.util.Locale;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import ml.bma.bsop.backend.TokenRepository;
import ml.bma.bsop.backend.data.Email;
import ml.bma.bsop.backend.data.LostPasswordEmail;
import ml.bma.bsop.backend.data.TokenType;
import ml.bma.bsop.backend.data.entity.Account;
import ml.bma.bsop.backend.data.entity.Performer;
import ml.bma.bsop.backend.data.entity.Token;
import ml.bma.bsop.backend.service.AccountService;
import ml.bma.bsop.backend.service.PerformerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.vaadin.spring.i18n.I18N;

/**
 *
 * @author ironman
 */
@Component
@UIScope
public class Utils implements Serializable {
    
    
    private final I18N i18n;
    private final TokenRepository tokenRepository;
    private final AccountService accountService;
    private final PerformerService performerService;
    private final JavaMailSender mailSender;
    
    @Autowired
    public Utils(I18N i18n, TokenRepository tokenRepository, AccountService accountService,
            PerformerService performerService, JavaMailSender mailSender) {
        
        this.i18n = i18n;
        this.tokenRepository = tokenRepository;
        this.accountService = accountService;
        this.performerService = performerService;
        this.mailSender = mailSender;
        
    }    
    
    public void addValidator(AbstractField field, Validator validator) {
        field.addValueChangeListener(event -> {
            ValidationResult result = validator.apply(event.getValue(), new ValueContext(field));
            
            if (result.isError()) {
                UserError error = new UserError(i18n.get(result.getErrorMessage()));
                field.setComponentError(error);
            } else {
                field.setComponentError(null);
            }
        });
    }
    
    public boolean sendMail(String recepient, String subject, String message) {
        
        try {
            MimeMessage mail = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mail,false);
            helper.setFrom(((JavaMailSenderImpl)mailSender).getUsername());
            helper.setSubject(subject);
            helper.setText(message, true);
            helper.addTo(recepient);
            mailSender.send(mail);
        } catch (MessagingException | MailSendException ex) {
            String caption = i18n.get("notification.error.invalidmailaddress.capt");
            String description = i18n.get("notification.error.invalidmailaddress.desc");
            Notification.show(caption, description, Notification.Type.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
    
    public boolean sendMail(String recipient, Email email) {
        return sendMail(recipient, email.getSubject(), email.getMessage());
    }
    
    public boolean sendLostPassMail(String recipient) {
        Account account = accountService.findOneByEmail(recipient);
        Token token = account.getToken();
        Long id = null;
        if( token == null) {
            token = new Token(TokenType.LOSTPASS, account);
            account.setToken(token);
        } else {
            if(token.getTmpAccount() != null) {
                id = token.getTmpAccount().getId();
                token.setTmpAccount(null);
                token.setType(TokenType.LOSTPASS);
            }
        }
        saveAccount(account);
        Email email = new LostPasswordEmail(i18n.getLocale(), token);
        if(id != null) {
            deleteAccount(id);
        }
        return sendMail(recipient, email);
    }    
    
    public void saveAccount(Account account) {
        accountService.save(account);
    }
    
    public void saveToken(Token token) {
        tokenRepository.save(token);
    }
    
    public Token findToken(String token) {
        return tokenRepository.findOneByToken(token);
    }
    
    public void deleteToken(Token token) {
        tokenRepository.delete(token);
        tokenRepository.flush();
    }

    public String getMessage(String codeText, Locale locale) {
        return this.i18n.get(codeText, locale);
    }
    
    public String getMessage(String codeText) {
        return i18n.get(codeText, i18n.getLocale());
    }
    
    public Account getCurrentAccount() {
        UserDetails details = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return accountService.findOneByEmail(details.getUsername());
    }   

    public boolean existsEmail(String email) {
        return accountService.existsEmail(email);
    }

    public Account getAccount(String email) {
        return accountService.findOneByEmail(email);
    }
    
    public Locale getLocale() {
        return i18n.getLocale();
    }

    public void deleteAccount(Long account_id) {
        accountService.delete(account_id);
    }
    
    public Performer getPerformer(Account account) {
        return performerService.findPerformer(account);
    }

}
