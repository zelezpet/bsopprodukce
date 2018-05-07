/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.ui.view;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import ml.bma.bsop.backend.data.entity.Ability;
import ml.bma.bsop.backend.data.entity.Account;
import ml.bma.bsop.backend.data.entity.Casting;
import ml.bma.bsop.backend.data.entity.Performance;
import ml.bma.bsop.backend.data.entity.Performer;
import ml.bma.bsop.backend.service.CastingService;
import ml.bma.bsop.backend.service.PerformanceService;
import ml.bma.bsop.ui.component.BSOPNotification;
import ml.bma.bsop.ui.component.ContractWindow;
import ml.bma.bsop.ui.utils.AbilityComparator;
import ml.bma.bsop.ui.utils.Utils;
import ml.bma.bsop.ui.view.form.CastingForm;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author ironman
 */

@SpringView(name = CastingView.NAME)
public class CastingView extends AbstractCrudView<Casting, CastingService, CastingForm> {
    
    public static final String NAME = "casting";
    
    private final ComboBox<Performance> performances;
    
    private final PerformanceService performanceService;
    
    private final Button message;
    private final Button contract;
    
    @Autowired
    public CastingView(Utils utils, CastingService service, PerformanceService performanceService) {
        super(utils, service, Casting.class, CastingForm.class, true);
        this.performanceService = performanceService;
        this.performances = new ComboBox<>(utils.getMessage("combobox.performance"), super.service.findAllPerformances());
        this.performances.setEmptySelectionAllowed(true);
        this.performances.setWidth(240, Unit.PIXELS);
        this.performances.addValueChangeListener(evt -> {
            super.updateList();
        });
        
        this.message = new Button(VaadinIcons.ENVELOPE_O);
        this.message.setStyleName(ValoTheme.BUTTON_ICON_ONLY);
        this.message.addClickListener(this::message);
        this.message.setDescription(utils.getMessage("button.message.desc"));
        
        this.contract = new Button(VaadinIcons.USER_CARD);
        this.contract.setStyleName(ValoTheme.BUTTON_ICON_ONLY);
        this.contract.setDescription(utils.getMessage("button.contract.desc"));
        this.contract.addClickListener(this::contract);
        
        super.toolBar.addComponent(performances, 0);
        HorizontalLayout buttons = new HorizontalLayout(message, contract);
        super.toolBar.addComponent(buttons);
        
    }
    
    private void contract(ClickEvent event) {
        ContractWindow window = new ContractWindow(super.utils, super.service, super.update());
        window.setModal(true);
        window.setResponsive(true);
        UI.getCurrent().addWindow(window);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        updateMessageStrings(super.utils.getLocale());
    }

    @Override
    public String getViewName() {
        return NAME;
    }
    
    private void message(ClickEvent event) {
        AbilityComparator comparator = new AbilityComparator();
        List<Ability> abilities = super.service.findAllAbilities();
        abilities.sort(comparator);
        List<Casting> castings = super.update();
        
        GridLayout grid = new GridLayout(2, 3);
        grid.setMargin(true);
        grid.setSpacing(true);
        TwinColSelect<Ability> twinColAbility = new TwinColSelect<Ability>();
        twinColAbility.setLeftColumnCaption(super.utils.getMessage("twincolselect.others"));
        twinColAbility.setRightColumnCaption(super.utils.getMessage("twincolselect.send"));
        twinColAbility.setItems(abilities);
        
        grid.addComponent(twinColAbility, 0, 0, 1, 0);
        grid.setComponentAlignment(twinColAbility, Alignment.MIDDLE_CENTER);
        
        TextArea message = new TextArea(super.utils.getMessage("textarea.message"));
        message.setSizeFull();
        message.setRows(5);
        
        grid.addComponent(message, 0, 1, 1, 1);
        grid.setComponentAlignment(message, Alignment.MIDDLE_CENTER);
        
        Button sendSms = new Button(super.utils.getMessage("button.sendsms"));
        sendSms.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                Set<Ability> send = twinColAbility.getValue();
                if(send.isEmpty()) {
                    BSOPNotification notify = new BSOPNotification(utils.getMessage("notification.emptysendlist"));
                    notify.show();
                    return;
                }
                int count = 0;
                for(Ability ability: send) {
                    String recipient = "bsopprodukce@seznam.cz";
                    Performer performer = ability.getPerformer();            
                    String subject = "<t:: "+ performer.getCellPhone().toString() + ">";
                    String mess = message.getValue();
                    if(utils.sendMail(recipient, subject, mess)) {
                        count++;
                    }
            
                }
                BSOPNotification notify = new BSOPNotification(count + " " + utils.getMessage("notification.sentsms"));
                notify.show();
            }
            
        });
        sendSms.setStyleName(ValoTheme.BUTTON_PRIMARY);        
        
        grid.addComponent(sendSms, 0, 2);
        grid.setComponentAlignment(sendSms, Alignment.MIDDLE_LEFT);
        
        Button sendMail = new Button(super.utils.getMessage("button.sendmail"));
        sendMail.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                Set<Ability> send = twinColAbility.getValue();
                if(send.isEmpty()) {
                    BSOPNotification notify = new BSOPNotification(utils.getMessage("notification.emptysendlist"));
                    notify.show();
                    return;
                }
                int count = 0;
                for(Ability ability: send) {
                    Account account = ability.getPerformer().getAccount();
                    String recipient = account.getEmail();
                    String subject = "BSOP " + utils.getMessage("textarea.message");
                    String mess = message.getValue();
                    if(utils.sendMail(recipient, subject, mess)) count++;
                }
                BSOPNotification notify = new BSOPNotification(count + " " + utils.getMessage("notification.sentmail"));
                notify.show();
            }
            
        });
        sendMail.setStyleName(ValoTheme.BUTTON_PRIMARY);
        
        grid.addComponent(sendMail, 1, 2);
        grid.setComponentAlignment(sendMail, Alignment.MIDDLE_RIGHT);
        
        Window window = new Window(super.utils.getMessage("window.sendmessage"), grid);
        window.setModal(true);
        UI.getCurrent().addWindow(window);
    }

    @Override
    public List<Casting> limitList(Optional<String> filter) {
        Performance temp = this.performances.getValue();
        if(temp == null) {
            return super.service.findAnyMatching(filter);
        } else {
            return super.service.findByPerformanceAndAnyMatching(temp, filter);
        }
    }

    @Override
    public void updateMessageStrings(Locale locale) {
        super.updateMessageString(locale);
    }
    
}
