package ml.bma.bsop.ui.view.form;


import java.util.Locale;
import ml.bma.bsop.ui.component.LocaleBar;

import com.jarektoro.responsivelayout.ResponsiveLayout;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import ml.bma.bsop.backend.data.entity.Account;
import ml.bma.bsop.ui.BSOPProductionUI;
import ml.bma.bsop.ui.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.i18n.support.Translatable;


@SpringComponent
@UIScope
public class LoginForm extends ResponsiveLayout implements Translatable {
    
    private final Utils utils;
    
    private final RegistrationForm registrationForm;
    private final LostPasswordForm lostPasswordForm;
    private ResetPasswordForm resetPasswordForm;
    
    private Window registrationWindow;
    private Window lostPasswordWindow;
    private Window resetPasswordWindow;
    
    private final Label title1,title2,title3,title4;
    
    private final PasswordField password;
    private final TextField email;
    
    private final Button loginButton;
    private final Button registrationButton;
    private final Button lostPasswordButton;
    
    private final LocaleBar localeBar;
    
    private final VerticalLayout page;
    private BSOPProductionUI ui;
    
    private LoginCallback callback;
    
    @Autowired
    public LoginForm( Utils utils, 
            RegistrationForm registrationForm, LostPasswordForm lostPasswordForm) {
        
        this.utils = utils;
        this.registrationForm = registrationForm;
        this.lostPasswordForm = lostPasswordForm;
        
        this.page = new VerticalLayout();
        this.localeBar = new LocaleBar();
        
        this.title1 = new Label("Bohemian");
        this.title1.addStyleNames(ValoTheme.LABEL_HUGE,"login-title");
        this.title2 = new Label("Symphony");
        this.title2.addStyleNames(ValoTheme.LABEL_HUGE,"login-title");        
        this.title3 = new Label("Orchestra");
        this.title3.addStyleNames(ValoTheme.LABEL_HUGE,"login-title");
        this.title4 = new Label("Prague");
        this.title4.addStyleNames(ValoTheme.LABEL_HUGE,"login-title");
        
        this.email = new TextField("");
        this.email.setIcon(VaadinIcons.USER);
        this.email.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        this.email.clear();
        this.email.addStyleName("login-email");
        
        this.password = new PasswordField("");
        this.password.setIcon(VaadinIcons.LOCK);
        this.password.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        this.password.clear();
        
        this.loginButton = new Button("", evt -> {
            String pword = password.getValue();
            password.clear();
            callback.login(email.getValue(), pword);
            email.focus();
        });
        this.loginButton.addStyleNames(ValoTheme.BUTTON_PRIMARY,"login-button");
        this.loginButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        
        this.registrationButton = new Button("",evt -> {
            LoginForm login = this;
            this.detach();
            this.registrationForm.setValidator();
            registrationWindow = new Window(utils.getMessage("registration.title")) {
                
                @Override
                public void close() {
                    login.attach();
                    super.close();
                }
            };
            registrationWindow.center();
            this.registrationForm.reset();
            registrationWindow.setContent(this.registrationForm);
            getUI().addWindow(registrationWindow);
        });
        
        this.registrationButton.setIcon(VaadinIcons.EDIT);
        this.registrationButton.addStyleNames(ValoTheme.BUTTON_LINK,ValoTheme.BUTTON_BORDERLESS);
        
        lostPasswordButton = new Button("", evt -> {
            LoginForm login = this;
            login.detach();
            this.lostPasswordForm.setValidator();
            lostPasswordWindow = new Window(utils.getMessage("lostpassword.title")) {
                
                @Override
                public void close() {
                    super.close();
                    login.attach();
                }
            };
            lostPasswordWindow.center();
            this.lostPasswordForm.reset();
            lostPasswordWindow.setContent(this.lostPasswordForm);
            getUI().addWindow(lostPasswordWindow);
        });
        
        lostPasswordButton.setIcon(VaadinIcons.AMBULANCE);
        lostPasswordButton.addStyleNames(ValoTheme.BUTTON_LINK,ValoTheme.BUTTON_BORDERLESS);     
        
        super.setScrollable(true);        
        super.addComponent(page);
        super.setSizeFull();        
        
        this.page.setSizeUndefined();
        this.page.setResponsive(true);
        this.page.addStyleName("login-page");
        this.page.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        this.page.setMargin(false);
        this.page.setHeight("100%");
        
        VerticalLayout loginPanel = new VerticalLayout();
        ResponsiveLayout respLabels = new ResponsiveLayout();
        ResponsiveLayout respInputs = new ResponsiveLayout();
        VerticalLayout credentials = new VerticalLayout();
        VerticalLayout buttons = new VerticalLayout();
        loginPanel.setSizeUndefined();
        respLabels.setSizeUndefined();
        respInputs.setSizeUndefined();        
        credentials.setSizeUndefined();        
        buttons.setSizeUndefined();        
        
        this.page.addComponents(localeBar, loginPanel);
        this.page.setComponentAlignment(localeBar, Alignment.TOP_RIGHT);
        this.page.setComponentAlignment(loginPanel, Alignment.BOTTOM_CENTER);
        loginPanel.setSizeUndefined();
        loginPanel.addComponents(respLabels,respInputs);
        loginPanel.addStyleName("login-panel");
        respLabels.addRow().addComponents(title1,title2,title3,title4);
        
        respInputs.addRow().addComponents(credentials,buttons);
        respInputs.addStyleName("resp-inputs");
        
        credentials.addComponents(email, password);
        
        buttons.addComponents(loginButton, registrationButton, lostPasswordButton);
        buttons.addStyleName("login-buttons");      
        
    }
    
    @Override
    public final void updateMessageStrings(Locale locale) {
        
        setCaption(utils.getMessage("login.title", locale));
        setLocale(locale);
        this.email.setCaption(utils.getMessage("textfield.email", locale));
        this.email.setLocale(locale);
        this.password.setCaption(utils.getMessage("textfield.password", locale));
        this.password.setLocale(locale);
        this.loginButton.setCaption(utils.getMessage("button.login", locale));
        this.registrationButton.setCaption(utils.getMessage("button.registration", locale));
        this.lostPasswordButton.setCaption(utils.getMessage("button.lostpassword", locale));
        if(this.registrationWindow != null) {
            this.registrationWindow.setCaption(utils.getMessage(("registration.title"), locale));
            this.registrationWindow.setLocale(locale);
        }
        if(this.lostPasswordWindow != null) {
            this.lostPasswordWindow.setCaption(utils.getMessage(("lostpassword.title"), locale));
            this.lostPasswordWindow.setLocale(locale);
        }
        if(this.resetPasswordWindow != null) {
            this.resetPasswordWindow.setCaption(utils.getMessage(("lostpassword.title"), locale));
            this.resetPasswordWindow.setLocale(locale);
        }
    }
    
    @FunctionalInterface
    public interface LoginCallback {

        boolean login(String username, String password);
    }
    
    public void init(LoginCallback callback, BSOPProductionUI ui) {
        this.callback = callback;
        this.ui = ui;
    }

    public void initResetPassword(Account account) {
        LoginForm login = this;
        this.resetPasswordForm = new ResetPasswordForm(utils, account);        
        this.resetPasswordWindow = new Window(utils.getMessage("resetpassword.title")) {
            @Override
            public void close() {
                super.close();
                login.attach();
            }            
        };
        resetPasswordWindow.center();
        this.resetPasswordForm.reset();
        this.detach();
        resetPasswordWindow.setContent(resetPasswordForm);
        ui.addWindow(resetPasswordWindow);
    }
    
    
    
}
