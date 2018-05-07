/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.ui.view.form;

import com.vaadin.data.ValidationResult;
import com.vaadin.data.ValueContext;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.UserError;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.util.Locale;
import ml.bma.bsop.ui.component.BSOPNotification;
import ml.bma.bsop.ui.component.LocaleBar;
import ml.bma.bsop.ui.utils.EmailExistValidator;
import ml.bma.bsop.ui.utils.Utils;
import org.vaadin.spring.i18n.support.Translatable;

/**
 *
 * @author ironman
 */

@UIScope
@SpringComponent
public class LostPasswordForm extends VerticalLayout implements Translatable {
    
    private final Utils utils;
    
    private final TextField email;
    
    private final Button send,reset;
    
    private final LocaleBar localeBar;
    
    private final EmailExistValidator validator;
    
    
    
    public LostPasswordForm(
            Utils utils, EmailExistValidator validator ) {
        
        this.utils = utils;
        
        this.validator = validator;        
        
        this.localeBar = new LocaleBar();
        
        this.email = new TextField();
        this.email.setMaxLength(60);
        this.email.setWidth("100%");
        this.email.setIcon(VaadinIcons.USER);
        this.email.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        this.email.clear();
        this.email.addStyleName("registration-email");
        utils.addValidator(email, validator);
        
        this.send = new Button(utils.getMessage("button.send"), evt -> {
            send();
        });
        this.send.addStyleName(ValoTheme.BUTTON_PRIMARY);
        this.send.setClickShortcut(Button.ClickShortcut.KeyCode.ENTER);
        
        
        this.reset = new Button(utils.getMessage("button.reset"), evt -> {
            reset();
        });
        this.reset.addStyleName(ValoTheme.BUTTON_DANGER);
        
        createContent();
    }
    
    private void createContent() {
        HorizontalLayout buttons = new HorizontalLayout(send, reset);
        buttons.setSizeFull();
        buttons.setComponentAlignment(send, Alignment.MIDDLE_LEFT);
        buttons.setComponentAlignment(reset, Alignment.MIDDLE_RIGHT);
        super.addComponents(localeBar, email,buttons);
        super.setComponentAlignment(localeBar, Alignment.TOP_RIGHT);
        updateMessageStrings(utils.getLocale());
    }
    
    private void send() {
        String emailValue = email.getValue();
        this.validator.setMustExist(true);
        ValidationResult emailResult = validator.apply(emailValue, new ValueContext(email));
        if(emailResult.isError()) {
            email.setComponentError(new UserError(utils.getMessage("usererror.email.notexist")));
        } else {
            getUI().getWindows().iterator().next().close();
            if(utils.sendLostPassMail(emailValue)) {
                BSOPNotification notification = new BSOPNotification(utils.getMessage("notification.sendlinktoresetpass.capt"), utils.getMessage("notification.sendlinktoresetpass.desc"));
                notification.show();
            }
        }
    }

    void reset() {
        this.email.clear();
    }
    

    @Override
    public void updateMessageStrings(Locale locale) {
        setCaption(utils.getMessage("lostpassword.title", locale));
        setLocale(locale);
        this.email.setCaption(utils.getMessage("textfield.email", locale));
        this.email.setLocale(locale);
        this.send.setCaption(utils.getMessage("button.send", locale));
        this.reset.setCaption(utils.getMessage("button.reset", locale));
        
    }
    public void setValidator() {
        this.validator.setMustExist(true);
    }
    
}
