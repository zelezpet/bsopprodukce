/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.ui.view.form;

import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.data.ValidationResult;
import com.vaadin.data.Validator;
import com.vaadin.data.ValueContext;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.UserError;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.util.Locale;
import ml.bma.bsop.backend.data.Authority;
import ml.bma.bsop.backend.data.RegistrationEmail;
import ml.bma.bsop.backend.data.TokenType;
import ml.bma.bsop.backend.data.entity.Account;
import ml.bma.bsop.backend.data.entity.Token;
import ml.bma.bsop.ui.component.BSOPNotification;
import ml.bma.bsop.ui.component.LocaleBar;
import ml.bma.bsop.ui.utils.ConfirmPasswordValidator;
import ml.bma.bsop.ui.utils.EmailExistValidator;
import ml.bma.bsop.ui.utils.PasswordValidator;
import ml.bma.bsop.ui.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.i18n.support.Translatable;



/**
 *
 * @author ironman
 */

@UIScope
@SpringComponent
public class RegistrationForm extends VerticalLayout implements Translatable {
    
    private final Utils utils;
    
    private final TextField email;
    
    private final PasswordField password;
    private final PasswordField confirmPassword;
    
    private final TextField pass1;
    private final TextField pass2;
    
    private final Button save;
    private final Button reset;
    
    private final CheckBox checkPass;   
    
    private final PasswordValidator passwordValidator;
    private final PasswordValidator passValidator;
    private final EmailExistValidator emailValidator;    
    private final ConfirmPasswordValidator confirmPasswordValidator;
    private final ConfirmPasswordValidator confirmPassValidator;
    
    private final LocaleBar localeBar;
    
    @Autowired
    public RegistrationForm( 
            Utils utils, EmailExistValidator emailValidator) {
        
        this.utils = utils;
        
        this.emailValidator = emailValidator;
        this.passwordValidator = new PasswordValidator();
        this.passValidator = new PasswordValidator();
        
        this.email = new TextField();
        this.email.setMaxLength(60);
        this.email.setWidth("100%");
        this.email.setIcon(VaadinIcons.USER);
        this.email.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        this.email.clear();
        this.email.addStyleName("registration-email");
        utils.addValidator(email, this.emailValidator);
        
        this.password = new PasswordField();
        this.password.setIcon(VaadinIcons.LOCK);
        this.password.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        this.password.clear();
        utils.addValidator(password, passwordValidator);
        
        this.confirmPasswordValidator = new ConfirmPasswordValidator(password);
        
        this.confirmPassword = new PasswordField();
        this.confirmPassword.setIcon(VaadinIcons.LOCK);
        this.confirmPassword.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        this.confirmPassword.clear();
        utils.addValidator(confirmPassword, confirmPasswordValidator);
        
        
        this.pass1 = new TextField();
        this.pass1.setVisible(false);
        this.pass1.setIcon(VaadinIcons.LOCK);
        this.pass1.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        utils.addValidator(pass1, passValidator);
        
        this.confirmPassValidator = new ConfirmPasswordValidator(pass1);
        
        this.pass2 = new TextField();
        this.pass2.setVisible(false);
        this.pass2.setIcon(VaadinIcons.LOCK);
        this.pass2.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        utils.addValidator(pass2, confirmPassValidator);
        
        this.save = new Button("", (Button.ClickEvent evt) -> {
            save();
        });
        this.save.addStyleName(ValoTheme.BUTTON_PRIMARY);
        this.save.setClickShortcut(Button.ClickShortcut.KeyCode.ENTER);
        
        this.reset = new Button("", (Button.ClickEvent evt) -> {
            reset();
        });
        this.reset.addStyleName(ValoTheme.BUTTON_DANGER);
        
        this.checkPass = new CheckBox();
        checkPass.addValueChangeListener(event -> {
            componentEvent(event);
        });
        checkPass.addStyleName(ValoTheme.CHECKBOX_SMALL);        
        
        this.localeBar = new LocaleBar();        
        
        createContent();
    }    
    
    
    private void createContent() {
        
        updateMessageStrings(utils.getLocale());
        HorizontalLayout buttons = new HorizontalLayout(save, reset);
        buttons.setSizeFull();
        buttons.setComponentAlignment(save, Alignment.MIDDLE_LEFT);
        buttons.setComponentAlignment(reset, Alignment.MIDDLE_RIGHT);
        HorizontalLayout passLayout = new HorizontalLayout(password, pass1, checkPass);
        passLayout.setComponentAlignment(checkPass, Alignment.MIDDLE_RIGHT);
        addComponents(localeBar, email, passLayout, confirmPassword, pass2, new Label(), buttons);
        setComponentAlignment(localeBar, Alignment.TOP_RIGHT);
        setComponentAlignment(buttons, Alignment.MIDDLE_CENTER);
        
    }
    
    private void save() {
        
        AbstractField tempPass, tempConf;
        Validator tmpValidator;
        if(checkPass.getValue()) {
            tempPass = pass1;
            tempConf = pass2;
            tmpValidator = confirmPassValidator;
        } else {
            tempPass = password;
            tempConf = confirmPassword;
            tmpValidator = confirmPasswordValidator;
        }
        String emailValue = email.getValue().toLowerCase();
        this.emailValidator.setMustExist(false);
        ValidationResult emailResult = emailValidator.apply(emailValue, new ValueContext(email));
        ValidationResult passwordResult = passwordValidator.apply((String)tempPass.getValue(), new ValueContext(tempPass));
        ValidationResult confPassResult = tmpValidator.apply(tempConf.getValue(), new ValueContext(tempConf));
        if(emailResult.isError() || passwordResult.isError() || confPassResult.isError()) {
            save.setComponentError(new UserError(utils.getMessage("usererror.save.validation")));
        } else {
            
            if(emailValidator.isResending()) {
                
                Token tok = utils.getAccount(emailValue).getToken();
                if(utils.sendMail(emailValue, new RegistrationEmail(utils.getLocale(), tok))) {
                    BSOPNotification notification = new BSOPNotification(utils.getMessage("notification.save.resendvalidation.capt"), utils.getMessage("notification.save.resendvalidation.desc"));
                    notification.show();
                }
            } else {
                Account account = new Account(emailValue, "", Authority.ROLE_USER);
                account.setPassword((String) tempPass.getValue());
                Token token = new Token(TokenType.REGIST, account);


                if(utils.sendMail(emailValue, new RegistrationEmail(utils.getLocale(), token))){                
                    
                    account.setToken(token);
                    utils.saveAccount(account);

                    BSOPNotification notification = new BSOPNotification(utils.getMessage("notification.saveregistration.capt"), utils.getMessage("notification.saveregistration.desc"));
                    notification.show();
                }
            }
            getUI().getWindows().iterator().next().close();
        }
            

    }
    
    void reset() {
        
        this.email.clear();
        this.password.clear();
        this.confirmPassword.clear();
        this.pass1.clear();
        this.pass2.clear();
        
    }
    
    @Override
    public void updateMessageStrings(Locale locale) {
        setCaption(utils.getMessage("registration.title", locale));
        setLocale(locale);
        this.email.setCaption(utils.getMessage("textfield.email", locale));
        this.email.setLocale(locale);
        this.password.setCaption(utils.getMessage("textfield.password", locale));
        this.password.setLocale(locale);
        this.pass1.setCaption(utils.getMessage("textfield.password", locale));
        this.pass1.setLocale(locale);
        this.confirmPassword.setCaption(utils.getMessage("textfield.confirmpassword", locale));
        this.confirmPassword.setLocale(locale);
        this.pass2.setCaption(utils.getMessage("textfield.confirmpassword", locale));
        this.pass2.setLocale(locale);
        this.save.setCaption(utils.getMessage("button.save", locale));
        this.reset.setCaption(utils.getMessage("button.reset", locale));
    }

 
    public void componentEvent(ValueChangeEvent event) {
        
        if(checkPass.getValue()) {
            pass1.setValue(password.getValue());
            pass1.setComponentError(password.getComponentError());
            pass2.setValue(confirmPassword.getValue());
            pass2.setComponentError(confirmPassword.getComponentError());
            password.setVisible(false);
            pass1.setVisible(true);
            confirmPassword.setVisible(false);
            pass2.setVisible(true);
        } else {
            password.setValue(pass1.getValue());
            password.setComponentError(pass1.getComponentError());
            confirmPassword.setValue(pass2.getValue());
            confirmPassword.setComponentError(pass2.getComponentError());
            pass1.setVisible(false);
            password.setVisible(true);
            pass2.setVisible(false);
            confirmPassword.setVisible(true);
            
        }
        
    }
    
    public void setValidator() {
        this.emailValidator.setMustExist(false);
    }
    
    
}
