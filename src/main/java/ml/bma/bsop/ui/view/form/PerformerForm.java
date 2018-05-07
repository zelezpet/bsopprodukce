/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.ui.view.form;

import com.jarektoro.responsivelayout.ResponsiveLayout;
import com.jarektoro.responsivelayout.ResponsiveRow;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.UserError;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import ml.bma.bsop.backend.data.Authority;
import ml.bma.bsop.backend.data.Instrument;
import ml.bma.bsop.backend.data.entity.Ability;
import ml.bma.bsop.backend.data.entity.Account;
import ml.bma.bsop.backend.data.entity.Address;
import ml.bma.bsop.backend.data.entity.BankAccount;
import ml.bma.bsop.backend.data.entity.CellPhone;
import ml.bma.bsop.backend.data.entity.Performer;
import ml.bma.bsop.backend.service.PerformerService;
import ml.bma.bsop.security.SecurityUtils;
import ml.bma.bsop.ui.BSOPProductionUI;
import ml.bma.bsop.ui.component.AddressPanel;
import ml.bma.bsop.ui.component.BSOPNotification;
import ml.bma.bsop.ui.component.BankPanel;
import ml.bma.bsop.ui.component.PersonalPanel;
import ml.bma.bsop.ui.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.vaadin.spring.i18n.support.Translatable;

/**
 *
 * @author ironman
 */

@SpringComponent
@UIScope
public class PerformerForm extends VerticalLayout implements Translatable {
    
    private final Utils utils;
    private final PerformerService performerService;
    private final AuthenticationManager authentication;
    
    private final PersonalPanel personal;
    
    private final AddressPanel address;
    
    private final BankPanel bank;
    
    private final Button save;
    
    private final CheckBox edit;
    
    
    @Autowired
    public PerformerForm(Utils utils, PerformerService performerService, AuthenticationManager authenticationManager) {
        
        this.utils = utils;
        this.performerService = performerService;
        this.authentication = authenticationManager;
        
        this.personal = new PersonalPanel(utils);
        
        this.address = new AddressPanel(utils);
        
        this.bank = new BankPanel(utils);
        
        this.save = new Button(utils.getMessage("button.save"));
        this.save.addClickListener(evt -> {
            save();
        });
        
        this.edit = new CheckBox(utils.getMessage("checkbox.edit"));
        this.edit.addValueChangeListener(evt -> {
            edit();
        });
        
        ResponsiveLayout responsive = new ResponsiveLayout();
        ResponsiveRow row = responsive.addRow();
        ResponsiveRow buttons = responsive.addRow();
        HorizontalLayout buttonsLayout = new HorizontalLayout(save, edit);
        buttonsLayout.setSizeUndefined();
        buttonsLayout.setMargin(true);
        Panel buttonsPanel = new Panel(buttonsLayout);
        buttonsPanel.addStyleName(ValoTheme.PANEL_WELL);
        row.setMargin(true);
        row.setSpacing(true);
        buttons.setMargin(true);
        buttons.setSpacing(true);
        row.addColumn().withComponent(personal);
        row.addColumn().withComponent(address);
        row.addColumn().withComponent(bank);
        buttons.addColumn().withComponent(buttonsPanel);       
        
        super.addComponents(responsive);
        super.setSizeUndefined();
        super.setMargin(false);
        
    }

    @Override
    public void updateMessageStrings(Locale locale) {
        this.save.setCaption(utils.getMessage("button.save", locale));
        this.edit.setCaption(utils.getMessage("checkbox.edit", locale));
    }

    private void save() {
        if(!edit.getValue()) return;
        boolean validate = false;
        validate = this.personal.validate();
        validate = this.address.validate() && validate;
        validate = this.bank.validate() && validate;
        if(validate) {
            Window agreeWindow = new Window(utils.getMessage("window.agreement"));
            Label describe = new Label(utils.getMessage("label.agreement.describe"));
            describe.setWidth(340, Unit.PIXELS);
            describe.setContentMode(ContentMode.HTML);
            Responsive.makeResponsive(describe);
            PasswordField password = new PasswordField(utils.getMessage("passwordfield.agreement"));
            password.setIcon(VaadinIcons.LOCK);
            password.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
            password.clear();
            Button agree = new Button(utils.getMessage("button.agree"));
            agree.setClickShortcut(ShortcutAction.KeyCode.ENTER);
            agree.addClickListener(evt -> {
                Account current = utils.getCurrentAccount();
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                String pass = password.getValue();
                if(encoder.matches(pass, current.getPassword())) {
                    
                    Address address = this.address.getAddress();
                    address.setPersonal(true);
                    BankAccount bankAccount = this.bank.getBankAccount();
                    CellPhone cellPhone = personal.getCellPhone();
                    Performer performer = new Performer(current, personal.getFirstName(), personal.getSurname(), cellPhone, address, bankAccount);
                    performer.setOp(personal.getOp());
                    performer.setPas(personal.getPas());
                    
                    if(SecurityUtils.hasRole(Authority.ROLE_USER)) {
                        
                        performerService.save(performer);
                        createAbilities(this.personal.getInstruments(), performer);
                        performerService.saveAbilities(performer.getAbilities());
                        current.setAuthority(Authority.ROLE_PERFORMER);
                        utils.saveAccount(current);
                        SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
                        Authentication authentication = this.authentication.authenticate(new UsernamePasswordAuthenticationToken(current.getEmail(), pass));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        Page.getCurrent().setLocation(BSOPProductionUI.URL);
                        
                    } else {
                        Performer oldPerformer = performerService.findPerformer(current);
                        List<Ability> savedAbilities = oldPerformer.getAbilities();
                        oldPerformer.setAbilities(null);
                        createAbilities(this.personal.getInstruments(), oldPerformer);
                        BSOPNotification notify;
                        if(performerService.save(oldPerformer, performer, savedAbilities)) {
                            notify = new BSOPNotification(utils.getMessage("notification.performer.update"));
                        } else {
                            notify = new BSOPNotification(utils.getMessage("notification.performer.noupdate"));
                        }
                        notify.show();
                    }
                    
                    getUI().getWindows().iterator().next().close();
                    
                } else {
                    password.setComponentError(new UserError("usererror.passwordfail"));
                }
            });
            HorizontalLayout tmpLayout = new HorizontalLayout(password);
            tmpLayout.setSizeUndefined();
            VerticalLayout content = new VerticalLayout(describe, tmpLayout, agree);
            content.setComponentAlignment(describe, Alignment.MIDDLE_CENTER);
            content.setComponentAlignment(tmpLayout, Alignment.MIDDLE_CENTER);
            content.setComponentAlignment(agree, Alignment.MIDDLE_CENTER);
            content.setMargin(true);
            agreeWindow.setContent(content);
            agreeWindow.setModal(true);
            getUI().addWindow(agreeWindow);
            
        }
        
        
    }

    private void edit() {
        if(this.edit.getValue()) {
            this.personal.setEnabled(true);
            this.address.setEnabled(true);
            this.bank.setEnabled(true);
            this.save.setEnabled(true);
        } else {
            this.personal.setEnabled(false);
            this.address.setEnabled(false);
            this.bank.setEnabled(false);
            this.save.setEnabled(false);
        }
    }
    
    public void init(Performer performer) {
        if(performer == null) {
            this.edit.setValue(true);
            this.edit.setVisible(false);
        } else if(SecurityUtils.hasRole(Authority.ROLE_PERFORMER)) {
                    this.personal.init(performer);
                    this.address.init(performer.getAddress());
                    this.bank.init(performer.getBank());
                    this.edit.setVisible(true);
                    this.edit.setValue(false);
                    this.edit();
        }
    }
    
    private void createAbilities(Set<Instrument> instruments, Performer performer) {
        
        if(performer.getAbilities() == null) {
            performer.setAbilities(new ArrayList<>());
        }
        for(Instrument instrument: instruments) {            
            Ability ability = new Ability(performer, instrument);
            performer.getAbilities().add(ability);            
        }
    }
    
}
