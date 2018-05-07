/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.ui.component;

import com.vaadin.data.HasValue;
import com.vaadin.icons.VaadinIcons;
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
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import java.util.Locale;
import ml.bma.bsop.backend.data.AccountUpdateEmail;
import ml.bma.bsop.backend.data.TokenType;
import ml.bma.bsop.backend.data.entity.Account;
import ml.bma.bsop.backend.data.entity.Token;
import ml.bma.bsop.ui.BSOPProductionUI;
import ml.bma.bsop.ui.utils.Utils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.vaadin.spring.i18n.support.Translatable;

/**
 *
 * @author ironman
 */

public class EditAccountWindow extends Window implements Translatable{
  
    private final Utils utils;
    
    private final VerticalLayout content;
    
    private final TextField email;
    
    private final PasswordField oldPassword;
    private final PasswordField password;
    private final PasswordField confirmPassword;
    
    private final TextField oldPass;
    private final TextField pass1;
    private final TextField pass2;
    
    private final Button save;
    private final Button reset;
    
    private final CheckBox checkPass;
    
    private final LocaleBar localeBar;
    
    
    public EditAccountWindow(Utils utils) {
        
        this.content = new VerticalLayout();
        this.utils = utils;
        
        this.email = new TextField(utils.getMessage("textfield.email"));
        this.email.setMaxLength(60);
        this.email.setWidth("100%");
        this.email.setIcon(VaadinIcons.USER);
        this.email.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        this.email.clear();
        this.email.setValue(utils.getCurrentAccount().getEmail());
        this.email.addStyleName("registration-email");
        
        this.oldPassword = new PasswordField(utils.getMessage("textfield.oldpassword"));
        this.oldPassword.setIcon(VaadinIcons.LOCK);
        this.oldPassword.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        this.oldPassword.clear();
        
        this.password = new PasswordField(utils.getMessage("textfield.password"));
        this.password.setIcon(VaadinIcons.LOCK);
        this.password.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        this.password.clear();
        
        this.confirmPassword = new PasswordField(utils.getMessage("textfield.confirmpassword"));
        this.confirmPassword.setIcon(VaadinIcons.LOCK);
        this.confirmPassword.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        this.confirmPassword.clear();
        
        this.oldPass = new TextField(utils.getMessage("textfield.oldpassword"));
        this.oldPass.setVisible(false);
        this.oldPass.setIcon(VaadinIcons.LOCK);
        this.oldPass.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        
        
        this.pass1 = new TextField(utils.getMessage("textfield.password"));
        this.pass1.setVisible(false);
        this.pass1.setIcon(VaadinIcons.LOCK);
        this.pass1.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        this.pass2 = new TextField(utils.getMessage("textfield.confirmpassword"));
        this.pass2.setVisible(false);
        this.pass2.setIcon(VaadinIcons.LOCK);
        this.pass2.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
         
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
        
        this.updateMessageStrings(utils.getLocale());
        HorizontalLayout buttons = new HorizontalLayout(save, reset);
        buttons.setSizeFull();
        buttons.setComponentAlignment(save, Alignment.MIDDLE_LEFT);
        buttons.setComponentAlignment(reset, Alignment.MIDDLE_RIGHT);
        HorizontalLayout passLayout = new HorizontalLayout(oldPassword, oldPass, checkPass);
        passLayout.setComponentAlignment(checkPass, Alignment.MIDDLE_RIGHT);
        content.addComponents(localeBar, email, passLayout,password, pass1, confirmPassword, pass2, new Label(), buttons);
        content.setComponentAlignment(localeBar, Alignment.TOP_RIGHT);
        content.setComponentAlignment(buttons, Alignment.MIDDLE_CENTER);
        super.setContent(content);
        super.setCaptionAsHtml(true);
        
    }

    private void save() {
        
        AbstractField tempPass, tempConf, tempOld;
        BSOPProductionUI ui = (BSOPProductionUI) getUI();
        if(checkPass.getValue()) {
            tempOld = oldPass;
            tempPass = pass1;
            tempConf = pass2;
        } else {
            tempOld = oldPassword;
            tempPass = password;
            tempConf = confirmPassword;
        }
        String emailValue = email.getValue().toLowerCase();
        String currentEmail = ui.getCurrentAccountEmail();
        String oldPassValue = (String) tempOld.getValue();
        String passValue = (String) tempPass.getValue();
        String confPassValue = (String) tempConf.getValue();
        
        boolean[] validPass = this.validPassword(oldPassValue, passValue, confPassValue);
        Account current = utils.getCurrentAccount();
        Token tok = current.getToken();
        if(tok != null) {
            utils.deleteToken(tok);
            tok = null;
        }
        Account tmpAccount;
        if(validPass[1]) {
            BSOPNotification notify = null;
            if(!emailValue.isEmpty() && !emailValue.equalsIgnoreCase(currentEmail)) {
                if(emailValue.matches("^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$")) {
                    tmpAccount = new Account();
                    tmpAccount.setByAccount(current);
                    if(validPass[0]) {
                        // update password
                        tmpAccount.setPassword(passValue);
                        notify = new BSOPNotification(utils.getMessage("notification.updateaccount.full.capt"),utils.getMessage("notification.updateaccount.full.desc"));
                    } else {
                        notify = new BSOPNotification(utils.getMessage("notification.updateaccount.email.capt"),utils.getMessage("notification.updateaccount.email.desc"));
                    }
                    // update mail
                    tmpAccount.setEmail(emailValue);
                    tmpAccount.setEnable(false);
                    Token token = new Token(TokenType.UPDATEACCOUNT, current, tmpAccount);                    
                    if(utils.sendMail(emailValue, new AccountUpdateEmail(utils.getLocale(),token))) {
                        current.setEnable(false);
                        current.setToken(token);
                        utils.saveAccount(tmpAccount);
                    } else {
                        return;
                    }
                    
                } else {
                    this.save.setComponentError(new UserError(utils.getMessage("usererror.email.badvalue")));
                    return;
                }
            } else {
                if(validPass[0]) {
                    // update password
                    current.setPassword(passValue);
                } else {
                    if(passValue.length() < 8) {
                        this.save.setComponentError(new UserError(utils.getMessage("usererror.password.short")));
                    } else {
                        this.save.setComponentError(new UserError(utils.getMessage("usererror.password.confirm")));
                    }
                    return;
                }
            }
            utils.saveAccount(current);
            if(notify == null) {
                notify = new BSOPNotification(utils.getMessage("notification.resetpassword"));
            }
            notify.show();            
            
        } else {
            
            if((emailValue.isEmpty() || emailValue.equalsIgnoreCase(currentEmail)) && passValue.isEmpty()) {
                BSOPNotification notify = new BSOPNotification(utils.getMessage("notification.noupdate"));
                notify.show();
            } else {
                oldPassword.setComponentError(new UserError(utils.getMessage("usererror.oldpassword")));
                return;
            }
        }
        ui.getWindows().iterator().next().close();
    }
    
    private boolean[] validPassword(String oldPass, String pass, String confPass) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String currentPass = utils.getCurrentAccount().getPassword();
        if(encoder.matches(oldPass, currentPass)) {
            if(pass.length() >= 8 && confPass.equals(pass)) {
                return new boolean[]{true,true};
            }
            return new boolean[]{false,true};
        }
        return new boolean[]{false, false};
    }

    private void reset() {
        
        this.email.clear();
        this.oldPassword.clear();
        this.oldPass.clear();
        this.password.clear();
        this.confirmPassword.clear();
        this.pass1.clear();
        this.pass2.clear();
    }

    public void componentEvent(HasValue.ValueChangeEvent event) {
        
        if(checkPass.getValue()) {
            oldPass.setValue(oldPassword.getValue());
            pass1.setValue(password.getValue());
            pass2.setValue(confirmPassword.getValue());
            oldPassword.setVisible(false);
            oldPass.setVisible(true);
            password.setVisible(false);
            pass1.setVisible(true);
            confirmPassword.setVisible(false);
            pass2.setVisible(true);
        } else {
            oldPassword.setValue(oldPass.getValue());
            password.setValue(pass1.getValue());
            confirmPassword.setValue(pass2.getValue());
            oldPass.setVisible(false);
            oldPassword.setVisible(true);
            pass1.setVisible(false);
            password.setVisible(true);
            pass2.setVisible(false);
            confirmPassword.setVisible(true);
            
        }
    }
    
    @Override
    public void updateMessageStrings(Locale locale) {
        
        setCaption(utils.getMessage("window.editaccount", locale));
        setLocale(locale);
        this.email.setCaption(utils.getMessage("textfield.email", locale));
        this.email.setLocale(locale);
        this.oldPassword.setCaption(utils.getMessage("textfield.oldpassword"));
        this.oldPassword.setLocale(locale);
        this.oldPass.setCaption(utils.getMessage("textfield.oldpassword"));
        this.oldPass.setLocale(locale);
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
    
    
}
