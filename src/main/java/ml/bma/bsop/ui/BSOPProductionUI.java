/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.ui;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Viewport;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.shared.ui.ui.Transport;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.Notification;
import java.util.Locale;
import ml.bma.bsop.backend.PictureRepository;
import ml.bma.bsop.backend.data.entity.Account;
import ml.bma.bsop.backend.data.entity.Token;
import ml.bma.bsop.backend.data.BSOPProductionException;
import ml.bma.bsop.security.SecurityUtils;
import ml.bma.bsop.ui.component.BSOPNotification;
import ml.bma.bsop.ui.utils.Utils;
import ml.bma.bsop.ui.view.AccessDeniedView;
import ml.bma.bsop.ui.view.ErrorView;
import ml.bma.bsop.ui.view.form.LoginForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.vaadin.spring.i18n.annotation.EnableI18N;
import org.vaadin.spring.i18n.support.Translatable;
import org.vaadin.spring.i18n.support.TranslatableUI;

/**
 *
 * @author ironman
 */

@SpringUI
@Theme("bsop")
@Viewport("width=device-width,initial-scale=1.0,user-scalable=no")
@EnableI18N
public class BSOPProductionUI extends TranslatableUI implements Translatable {
    
    public static final String URL = "https://app-bsopprodukce.193b.starter-ca-central-1.openshiftapps.com:8443";
    
    private final AuthenticationManager authenticationManager;
    private final SpringViewProvider viewProvider;
    private final NavigationManager navigator;
    
    private final Utils utils;
    
    private final BSOPProductionMenu menu;
    private final LoginForm loginForm;
    
    private BSOPNotification tempNotification;
    private Account currentAccount;
    

    @Autowired
    public BSOPProductionUI(AuthenticationManager authenticationManager,
            SpringViewProvider viewProvider, NavigationManager navigator, Utils utils, PictureRepository pictureRepository,
            LoginForm loginForm ) {
        
        this.authenticationManager = authenticationManager;
        this.viewProvider = viewProvider;
        this.navigator = navigator;
        this.utils = utils;
        this.loginForm = loginForm;
        this.menu = new BSOPProductionMenu(viewProvider, navigator, utils);
 
    }    

    @Override
    protected void initUI(VaadinRequest request) {
        
        super.getPage().setTitle(utils.getMessage("application.name"));
        super.setErrorHandler(this::handleError);
        super.addStyleName("bsop-produkce");
        super.setSizeFull();
        loginForm.init(this::login, this);

        String token = request.getParameter("token");
        if (token == null && SecurityUtils.isLoggedIn()) {
            showMain();
        } else if(SecurityUtils.isLoggedIn()) {
            this.logout();
        } else {
            showLogin();
            if(token != null) {
                Token tok = utils.findToken(token);
                if(tok != null) {
                    Account account = tok.getAccount();
                    switch(tok.getType()) {
                        case REGIST :

                            account.setEnable(true);
                            account.setToken(null);
                            this.utils.saveAccount(account);
                            this.tempNotification = new BSOPNotification(utils.getMessage("notification.accountvalidation"));
                            break;

                        case LOSTPASS:

                            this.loginForm.initResetPassword(account);
                            tok = null;
                            break;

                        case UPDATEACCOUNT:

                            this.currentAccount = tok.getAccount();
                            Account newAccount = tok.getTmpAccount();
                            this.currentAccount.setByAccount(newAccount);
                            this.currentAccount.setEnable(true);
                            this.currentAccount.setToken(null);
                            utils.deleteAccount(newAccount.getId());
                            utils.saveAccount(currentAccount);
                            this.tempNotification = new BSOPNotification(utils.getMessage("notification.updateaccount"));
                            break;           
                    }
                } else {
                    this.tempNotification = new BSOPNotification(utils.getMessage("notification.notvalidtoken"));
                }
            }
            if(tempNotification != null) {
            tempNotification.show();
            }
        }
        
    }
    
    @Override
    public void detach() {        
        super.detach();
    }
    
    private void showLogin() {        
        setContent(this.loginForm);
    }
    
    private boolean login(String username, String password) {
        try {
            Authentication token = 
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            if(utils.existsEmail(username)) {
                Account account = utils.getAccount(username);
                if(!account.isEnable()) {
                    throw new BSOPProductionException(BSOPProductionException.ACCOUNT_NOT_VALIDATED);
                }
            }
            // Reinitialize the session to protect against session fixation attacks. This does not work
            // with websocket communication.
            VaadinService.reinitializeSession(VaadinService.getCurrentRequest());
            SecurityContextHolder.getContext().setAuthentication(token);
            showMain();
        } catch (BSOPProductionException ex) {
            tempNotification = new BSOPNotification(utils.getMessage("notification.loginemailvalidation"));
            tempNotification.show();
            return false;
        } catch (AuthenticationException ex) {
            tempNotification = new BSOPNotification(utils.getMessage("notification.loginfail"));
            tempNotification.show();
            return false;
        } 
        return true;
    }
    
    void logout() {         
        getPage().reload();
        getSession().close();
    }   

    private void handleError(com.vaadin.server.ErrorEvent event) {
        Throwable t = DefaultErrorHandler.findRelevantThrowable(event.getThrowable());
        if (t instanceof AccessDeniedException) {
            Notification.show("You do not have permission to perform this operation",
                Notification.Type.WARNING_MESSAGE);
        } else {
            DefaultErrorHandler.doDefault(event);
        }
    }

    private void showMain() {
        // Now when the session is reinitialized, we can enable websocket communication. Or we could have just
        // used WEBSOCKET_XHR and skipped this step completely.
        //getPushConfiguration().setTransport(Transport.WEBSOCKET);
        //getPushConfiguration().setPushMode(PushMode.AUTOMATIC);
        // Show the main UI
        
        
        
        viewProvider.setAccessDeniedViewClass(AccessDeniedView.class);
        navigator.addProvider(viewProvider);
        navigator.setErrorView(ErrorView.class);
        super.setNavigator(navigator);
        Account account = utils.getCurrentAccount();
        this.currentAccount = account;
        menu.init(this, utils.getLocale());
        super.setContent(menu);
    }

    @Override
    public void updateMessageStrings(Locale locale) {
        super.getPage().setTitle(utils.getMessage("application.name", locale));
    }

    public String getCurrentAccountEmail() {
        return this.currentAccount.getEmail();
    }
    
}
