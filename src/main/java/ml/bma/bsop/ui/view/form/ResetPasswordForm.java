/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.ui.view.form;



import com.vaadin.data.HasValue;
import com.vaadin.data.ValidationResult;
import com.vaadin.data.Validator;
import com.vaadin.data.ValueContext;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.server.UserError;
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
import ml.bma.bsop.backend.data.entity.Account;
import ml.bma.bsop.backend.data.entity.Token;
import ml.bma.bsop.ui.BSOPProductionUI;
import ml.bma.bsop.ui.component.BSOPNotification;
import ml.bma.bsop.ui.component.LocaleBar;
import ml.bma.bsop.ui.utils.ConfirmPasswordValidator;
import ml.bma.bsop.ui.utils.PasswordValidator;
import ml.bma.bsop.ui.utils.Utils;
import org.vaadin.spring.i18n.support.Translatable;

/**
 *
 * @author ironman
 */


public class ResetPasswordForm extends VerticalLayout implements Translatable{
    
    private final Utils utils;
    private final Account account;
    
    private final PasswordField password, confirmPassword;
    
    private final TextField pass,confPass;
    
    private final PasswordValidator passwordValidator;
    
    private final ConfirmPasswordValidator confirmPasswordValidator, confPassValidator;
    
    private final Button save, reset;
    
    private final CheckBox checkPass;
    
    private final LocaleBar localeBar;
    
    

    
    public ResetPasswordForm(Utils utils, Account account) {
        
        this.utils = utils;       
        this.account = account;
        this.passwordValidator = new PasswordValidator();
        
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
        
        
        this.pass = new TextField();
        this.pass.setVisible(false);
        this.pass.setIcon(VaadinIcons.LOCK);
        this.pass.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        utils.addValidator(pass, passwordValidator);
        
        this.confPassValidator = new ConfirmPasswordValidator(pass);
        
        this.confPass = new TextField();
        this.confPass.setVisible(false);
        this.confPass.setIcon(VaadinIcons.LOCK);
        this.confPass.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        utils.addValidator(confPass, confPassValidator);
        
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
        HorizontalLayout passLayout = new HorizontalLayout(password, pass, checkPass);
        passLayout.setComponentAlignment(checkPass, Alignment.MIDDLE_RIGHT);
        addComponents(localeBar, passLayout, confirmPassword, confPass, new Label(), buttons);
        setComponentAlignment(localeBar, Alignment.TOP_RIGHT);
        setComponentAlignment(buttons, Alignment.MIDDLE_CENTER);
        
    }

    @Override
    public void updateMessageStrings(Locale locale) {
        
        setCaption(utils.getMessage("resetpassword.title", locale));
        setLocale(locale);
        this.password.setCaption(utils.getMessage("textfield.password", locale));
        this.password.setLocale(locale);
        this.pass.setCaption(utils.getMessage("textfield.password", locale));
        this.pass.setLocale(locale);
        this.confirmPassword.setCaption(utils.getMessage("textfield.confirmpassword", locale));
        this.confirmPassword.setLocale(locale);
        this.confPass.setCaption(utils.getMessage("textfield.confirmpassword", locale));
        this.confPass.setLocale(locale);
        this.save.setCaption(utils.getMessage("button.save", locale));
        this.reset.setCaption(utils.getMessage("button.reset", locale));
        
    }
    
    void reset() {
        
        this.password.clear();
        this.confirmPassword.clear();
        this.pass.clear();
        this.confPass.clear();
        
    }   

    private void save() {
        
        AbstractField tempPass, tempConf;
        Validator tmpValidator;
        if(checkPass.getValue()) {
            tempPass = pass;
            tempConf = confPass;
            tmpValidator = confPassValidator;
        } else {
            tempPass = password;
            tempConf = confirmPassword;
            tmpValidator = confirmPasswordValidator;
        }
        
        ValidationResult passwordResult = passwordValidator.apply((String)tempPass.getValue(), new ValueContext(tempPass));
        ValidationResult confPassResult = tmpValidator.apply(tempConf.getValue(), new ValueContext(tempConf));
        if( passwordResult.isError() || confPassResult.isError()) {
            save.setComponentError(new UserError(utils.getMessage("usererror.save.validation")));
        } else {
            
            BSOPProductionUI ui = (BSOPProductionUI) getUI();
            
            account.setPassword(password.getValue());
            account.setEnable(true);
            Token token = account.getToken();
            account.setToken(null);
            utils.saveAccount(account);
            BSOPNotification notification = new BSOPNotification(utils.getMessage("notification.resetpassword"));
            notification.show();            
            ui.getWindows().iterator().next().close();
        }
    }

    private void componentEvent(HasValue.ValueChangeEvent<Boolean> event) {
        
        if(checkPass.getValue()) {
            pass.setValue(password.getValue());
            confPass.setValue(confirmPassword.getValue());
            password.setVisible(false);
            pass.setVisible(true);
            confirmPassword.setVisible(false);
            confPass.setVisible(true);
        } else {
            password.setValue(pass.getValue());
            confirmPassword.setValue(confPass.getValue());
            pass.setVisible(false);
            password.setVisible(true);
            confPass.setVisible(false);
            confirmPassword.setVisible(true);
            
        }
    }      
    
    
}
