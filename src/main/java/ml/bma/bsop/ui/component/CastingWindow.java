/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.ui.component;

import com.vaadin.data.HasValue;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import ml.bma.bsop.backend.data.CastState;
import ml.bma.bsop.backend.data.PerformanceState;
import ml.bma.bsop.backend.data.entity.Ability;
import ml.bma.bsop.backend.data.entity.Account;
import ml.bma.bsop.backend.data.entity.Casting;
import ml.bma.bsop.backend.data.entity.Performance;
import ml.bma.bsop.backend.data.entity.Performer;
import ml.bma.bsop.backend.service.CastingService;
import ml.bma.bsop.ui.utils.AbilityComparator;
import ml.bma.bsop.ui.utils.Utils;
import org.vaadin.spring.i18n.support.Translatable;

/**
 *
 * @author ironman
 */
public class CastingWindow extends Window implements Translatable {
    
    private final Utils utils;
    private final CastingService service;
    private final PerformanceItem performanceItem;
    private final AbilityComparator<Ability> comparator;
    
    private final GridLayout grid;
    private final Button invite;
    private final Button confirm;
    private final Button mail;
    private final Button sms;
    private final Button saveInvited;
    private final Button saveConfirmed;
    private final Button switchMail;
    private final Button switchSms;
    private final Button sendMail;
    private final Button sendSms;
    
    private final TwinColSelect<Ability> invited;
    private final TwinColSelect<Ability> confirmed;
    private final TwinColSelect<Ability> newInvitedMail;
    private final TwinColSelect<Ability> newConfirmedMail;
    private final TwinColSelect<Ability> newInvitedSms;
    private final TwinColSelect<Ability> newConfirmedSms;
    
    private final TextArea message;
    
    private List<Ability> othersList;
    private List<Ability> loggedList;
    private List<Ability> invitedList;
    private List<Ability> confirmedList;
    private Set<Ability> newInvited;
    private Set<Ability> newConfirmed;
    private Set<Ability> remove;
    
    private enum View {
        INVITE, CONFIRME, MAIL, SMS;
    }
    
    private View currentView = View.INVITE;
    private boolean inviteMail = true;
    private boolean inviteSms = true;

    public CastingWindow(Utils utils, CastingService service, PerformanceItem item) {
        super(utils.getMessage("window.casting", utils.getLocale()) + " " + item.toString());
        this.utils = utils;
        this.service = service;
        this.performanceItem = item;
        this.comparator = new AbilityComparator<>();
        
        this.othersList = new ArrayList<>();
        this.loggedList = new ArrayList<>();
        this.invitedList = new ArrayList<>();
        this.confirmedList = new ArrayList<>();
        this.newInvited = new HashSet<>();
        this.newConfirmed = new HashSet<>();
        this.remove = new HashSet<>();
        
        this.grid = new GridLayout(4,4);
        this.grid.setMargin(true);
        this.grid.setSpacing(true);
        this.grid.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        
        this.invite = new Button(utils.getMessage("button.invite"));
        this.invite.addClickListener(this::invite);
        this.invite.setDisableOnClick(true);
        
        this.confirm = new Button(utils.getMessage("button.confirme"));
        this.confirm.addClickListener(this::confirme);
        this.confirm.setDisableOnClick(true);
        
        this.mail = new Button(utils.getMessage("button.mail"));
        this.mail.addClickListener(this::mail);
        this.mail.setDisableOnClick(true);
        
        this.sms = new Button(utils.getMessage("button.sms"));
        this.sms.addClickListener(this::sms);
        this.sms.setDisableOnClick(true);
        
        HorizontalLayout toolBox = new HorizontalLayout(invite,confirm,mail,sms);
        this.grid.addComponent(toolBox, 0, 0, 3, 0);
        
        this.saveInvited = new Button(utils.getMessage("button.save"));
        this.saveInvited.addClickListener(this::saveInvited);
        this.saveInvited.setStyleName(ValoTheme.BUTTON_PRIMARY);
        
        this.saveConfirmed = new Button(utils.getMessage("button.save"));
        this.saveConfirmed.addClickListener(this::saveConfirmed);
        this.saveConfirmed.setStyleName(ValoTheme.BUTTON_PRIMARY);
        
        this.invited = new TwinColSelect<>();
        this.invited.setLeftColumnCaption(utils.getMessage("twincolselect.others"));
        this.invited.setRightColumnCaption(utils.getMessage("twincolselect.invited"));
        this.invited.addValueChangeListener(new HasValue.ValueChangeListener<Set<Ability>>() {
            @Override
            public void valueChange(HasValue.ValueChangeEvent<Set<Ability>> event) {
                Set<Ability> olds = new HashSet<>(event.getOldValue());
                Set<Ability> news = new HashSet<>(event.getValue());
                if(olds.size() > news.size()) {
                    olds.removeAll(news);
                    remove.addAll(olds);
                    newInvited.removeAll(olds);
                } else {
                    news.removeAll(olds);
                    newInvited.addAll(news);
                    remove.removeAll(news);
                }
            }
        });
        
        this.confirmed = new TwinColSelect<>();
        this.confirmed.setLeftColumnCaption(utils.getMessage("twincolselect.logged"));
        this.confirmed.setRightColumnCaption(utils.getMessage("twincolselect.confirmed"));
        this.confirmed.addValueChangeListener(new HasValue.ValueChangeListener<Set<Ability>>() {
            @Override
            public void valueChange(HasValue.ValueChangeEvent<Set<Ability>> event) {
                Set<Ability> olds = new HashSet<>(event.getOldValue());
                Set<Ability> news = new HashSet<>(event.getValue());
                if(olds.size() > news.size()) {
                    olds.removeAll(news);
                    remove.addAll(olds);
                    newConfirmed.removeAll(olds);
                } else {
                    news.removeAll(olds);
                    newConfirmed.addAll(news);
                    remove.removeAll(news);
                }
            }
        });
        
        initLists();
        initCasting();
        
        this.switchMail = new Button(utils.getMessage("button.switch.confirmed"));
        this.switchMail.addClickListener(this::switchMail);
        
        this.switchSms = new Button(utils.getMessage("button.switch.confirmed"));
        this.switchSms.addClickListener(this::switchSms);
        
        this.sendMail = new Button(utils.getMessage("button.send"));
        this.sendMail.addClickListener(this::sendMail);
        this.sendMail.setStyleName(ValoTheme.BUTTON_PRIMARY);
        
        this.sendSms = new Button(utils.getMessage("button.send"));
        this.sendSms.addClickListener(this::sendSms);
        this.sendSms.setStyleName(ValoTheme.BUTTON_PRIMARY);
        
        this.newConfirmedMail = new TwinColSelect<>();
        this.newConfirmedMail.setLeftColumnCaption(utils.getMessage("twincolselect.confirmed"));
        this.newConfirmedMail.setRightColumnCaption(utils.getMessage("twincolselect.send"));
        
        this.newInvitedMail = new TwinColSelect<>();
        this.newInvitedMail.setLeftColumnCaption(utils.getMessage("twincolselect.invited"));
        this.newInvitedMail.setRightColumnCaption(utils.getMessage("twincolselect.send"));
        
        this.newInvitedSms = new TwinColSelect<>();
        this.newInvitedSms.setLeftColumnCaption(utils.getMessage("twincolselect.invited"));
        this.newInvitedSms.setRightColumnCaption(utils.getMessage("twincolselect.send"));
        
        this.newConfirmedSms = new TwinColSelect<>();
        this.newConfirmedSms.setLeftColumnCaption(utils.getMessage("twincolselect.confirmed"));
        this.newConfirmedSms.setRightColumnCaption(utils.getMessage("twincolselect.send"));
        
        this.message = new TextArea(utils.getMessage("textarea.message"));
        this.message.setSizeFull();
        this.message.setRows(3);
        
        super.setContent(this.grid);
        invite.click();
        invite.setEnabled(false);
    }
    
    private void hideView() {
        
        
        switch(currentView) {
            case INVITE:
                this.grid.removeComponent(0, 1);
                this.grid.removeComponent(3, 2);
                this.invite.setEnabled(true);
                break;
            case CONFIRME:
                this.grid.removeComponent(0, 1);
                this.grid.removeComponent(3, 2);
                this.confirm.setEnabled(true);
                break;
            case MAIL:
                this.grid.removeComponent(0, 1);
                this.grid.removeComponent(0, 2);
                this.grid.removeComponent(0, 3);
                this.grid.removeComponent(3, 3);
                this.mail.setEnabled(true);
                break;
            case SMS:
                this.grid.removeComponent(0, 1);
                this.grid.removeComponent(0, 2);
                this.grid.removeComponent(0, 3);
                this.grid.removeComponent(3, 3);
                this.sms.setEnabled(true);
                break;
        }
    }
    
    
    private void confirme(ClickEvent event) {
        hideView();
        this.currentView = View.CONFIRME;
        this.grid.addComponent(confirmed, 0, 1, 3, 1);
        this.grid.addComponent(saveConfirmed, 3, 2);
        this.grid.setComponentAlignment(saveConfirmed, Alignment.MIDDLE_RIGHT);
        
    }
    
    private void initLists() {
        Performance performance = this.performanceItem.getPerformance();
        List<Casting> tmp = this.service.findAllConfirmed(performance);
        for(Casting casting: tmp) {
            this.confirmedList.add(casting.getAbility());
        }
        this.confirmedList.sort(comparator);
        
        tmp = this.service.findAllInvited(performance);
        for(Casting casting: tmp) {
            this.invitedList.add(casting.getAbility());
        }
        this.invitedList.sort(comparator);
        
        tmp = this.service.findAllLogged(performance);
        for(Casting casting: tmp) {
            this.loggedList.add(casting.getAbility());
        }
        this.loggedList.sort(comparator);
        
        List<Ability> abilities = this.service.findAllAbilities();
        for(Ability ability: abilities) {
            if(!this.confirmedList.contains(ability) && !this.invitedList.contains(ability) && !this.loggedList.contains(ability)) {
                this.othersList.add(ability);
            }
        }
        this.othersList.sort(comparator);
        
    }
    
    private void initCasting() {
        List<Ability> tmpList = new ArrayList<>(othersList);
        tmpList.addAll(invitedList);
        tmpList.sort(comparator);
        Set<Ability> tmpSet = new HashSet<>(invitedList);
        this.invited.setItems(tmpList);
        this.invited.setValue(tmpSet);
        
        tmpList = new ArrayList<>(confirmedList);
        tmpList.addAll(loggedList);
        tmpList.sort(comparator);
        tmpSet = new HashSet<>(confirmedList);
        this.confirmed.setItems(tmpList);
        this.confirmed.setValue(tmpSet);
        
    }
    
    private void initMail() {
        
        List<Ability> tmp = new ArrayList<>(newInvited);
        tmp.sort(comparator);
        this.newInvitedMail.setItems(tmp);
        tmp = new ArrayList<>(newConfirmed);
        tmp.sort(comparator);
        this.newConfirmedMail.setItems(tmp);
    }
    
    private void initSms() {
        List<Ability> tmp = new ArrayList<>(newInvited);
        tmp.sort(comparator);
        this.newInvitedSms.setItems(tmp);
        tmp = new ArrayList<>(newConfirmed);
        tmp.sort(comparator);
        this.newConfirmedSms.setItems(tmp);
    }
    
    private void invite(ClickEvent event) {
        hideView();
        this.currentView = View.INVITE;
        this.grid.addComponent(invited, 0, 1, 3, 1);
        this.grid.addComponent(saveInvited, 3, 2);
        this.grid.setComponentAlignment(saveInvited, Alignment.MIDDLE_RIGHT);
        
    }
    
    private void mail(ClickEvent event) {
        hideView();
        initMail();
        this.currentView = View.MAIL;
        if(inviteMail) {
            this.grid.addComponent(newInvitedMail, 0, 1, 3, 1);            
        } else {
            this.grid.addComponent(newConfirmedMail, 0, 1, 3, 1);
        }
        this.message.setValue(textMessage());
        this.grid.addComponent(message, 0, 2, 3, 2);
        this.grid.addComponent(switchMail, 0, 3);
        this.grid.setComponentAlignment(switchMail, Alignment.MIDDLE_LEFT);
        this.grid.addComponent(sendMail, 3, 3);
        this.grid.setComponentAlignment(sendMail, Alignment.MIDDLE_RIGHT);
    }
    
    private void removeCasting() {
        
        for(Ability ability: remove) {
            Casting casting = this.service.findByAbility(ability);
            if(casting != null) {
                this.service.delete(casting);
            }
        }
    }
    
    
    
    private void saveConfirmed(ClickEvent event) {
        int count = 0;
        Performance performance = this.performanceItem.getPerformance();
        for(Ability ability: newConfirmed) {
            if(service.existCastingLogged(ability, performance)) {
                Casting casting = service.findByAbility(ability);
                casting.setCastState(CastState.CONFIRMED);
                service.save(casting);
                count++;
            } else {
                BSOPNotification notify = new BSOPNotification(ability + " " + utils.getMessage("notification.castingdoesntexist"));
                notify.show();
            }
        }
        
        BSOPNotification notify = new BSOPNotification(count + " " + utils.getMessage("notification.confirmedcasting"));
        notify.show();
        
        removeCasting();
    }
    
    private void saveInvited(ClickEvent event) {
        int count = 0;
        Performance performance = this.performanceItem.getPerformance();
        for(Ability ability: newInvited) {
            if(!service.existCasting(ability, performance)) {
                Casting casting = new Casting(ability, performanceItem.getPerformance(), CastState.INVITED);
                service.save(casting);
                count++;
            } else {
                BSOPNotification notify = new BSOPNotification(ability + " " + utils.getMessage("notification.castingexist"));
                notify.show();
            }
        }
        
        BSOPNotification notify = new BSOPNotification(count + " " + utils.getMessage("notification.savedcasting"));
        notify.show();
        
        removeCasting();
    }
    
    private void sendMail(ClickEvent event) {
        Set<Ability> sendList;
        if(inviteMail) {
            sendList = newInvitedMail.getValue();
            this.newInvitedMail.clear();
        } else {
            sendList = newConfirmedMail.getValue();
            this.newConfirmedMail.clear();
        }
        if(sendList.isEmpty()) {
            BSOPNotification not = new BSOPNotification(utils.getMessage("notification.emptysendlist"));
            not.show();
            return;
        }
        int count = 0;
        for(Ability ability: sendList) {
            Account account = ability.getPerformer().getAccount();
            String recipient = account.getEmail();
            String subject = inviteMail? utils.getMessage("subject.invite") : utils.getMessage("subject.confirm");
            String message = this.message.getValue();
            if(utils.sendMail(recipient, subject, message)) count++;
        }
        BSOPNotification notify = new BSOPNotification(count + " " + utils.getMessage("notification.sentmail"));
        notify.show();
    }
    
    private void sendSms(ClickEvent event) {
        Set<Ability> sendList;
        if(inviteSms) {
            sendList = newInvitedSms.getValue();
            this.newInvitedSms.clear();
        } else {
            sendList = newConfirmedSms.getValue();
            this.newConfirmedSms.clear();
        }
        if(sendList.isEmpty()) {
            BSOPNotification not = new BSOPNotification(utils.getMessage("notification.emptysendlist"));
            not.show();
            return;
        }
        
        
        int count = 0;
        for(Ability ability: sendList) {
            String recipient = "bsopprodukce@seznam.cz";
            Performer performer = ability.getPerformer();            
            String subject = "<t:: "+ performer.getCellPhone().toString() + ">";
            String message = this.message.getValue();
            if(utils.sendMail(recipient, subject, message)) {
                count++;
            }
            
        }
        BSOPNotification notify = new BSOPNotification(count + " " + utils.getMessage("notification.sentsms"));
        notify.show();
    }
    
    private void sms(ClickEvent event) {
        hideView();
        initSms();
        this.currentView = View.SMS;
        if(inviteSms) {
            this.grid.addComponent(newInvitedSms, 0, 1, 3, 1);
        } else {
            this.grid.addComponent(newConfirmedSms, 0, 1, 3, 1);
        }
        this.message.setValue(textMessage());
        this.grid.addComponent(message, 0, 2, 3, 2);
        this.grid.addComponent(switchSms, 0, 3);
        this.grid.setComponentAlignment(switchSms, Alignment.MIDDLE_LEFT);
        this.grid.addComponent(sendSms, 3, 3);
        this.grid.setComponentAlignment(sendSms, Alignment.MIDDLE_RIGHT);
    }
    
    private void switchMail(ClickEvent event) {
        if(inviteMail) {
            this.inviteMail = false;
            this.grid.removeComponent(0, 1);
            this.grid.addComponent(newConfirmedMail, 0, 1, 3, 1);
            this.message.setValue(textMessage());
            this.switchMail.setCaption(utils.getMessage("button.switch.invited"));
            
        } else {
            this.inviteMail = true;
            this.grid.removeComponent(0, 1);
            this.grid.addComponent(newInvitedMail, 0, 1, 3, 1);
            this.message.setValue(textMessage());
            this.switchMail.setCaption(utils.getMessage("button.switch.confirmed"));
            
        }
    }
    
    private void switchSms(ClickEvent event) {
        if(inviteSms) {
            inviteSms = false;
            this.grid.removeComponent(0, 1);
            this.grid.addComponent(newConfirmedSms, 0, 1, 3, 1);
            this.message.setValue(textMessage());
            this.switchSms.setCaption(utils.getMessage("button.switch.invited"));
        } else {
            inviteSms = false;
            this.grid.removeComponent(0, 1);
            this.grid.addComponent(newInvitedSms, 0, 1, 3, 1);
            this.message.setValue(textMessage());
            this.switchSms.setCaption(utils.getMessage("button.switch.confirmed"));
        }
    }
    
    private String textMessage() {
        if((currentView == View.MAIL && inviteMail)||(currentView == View.SMS && inviteSms)) {
            return utils.getMessage("message.invite") + " " + this.performanceItem.getPerformance().toString();
        } else {
            return utils.getMessage("message.confirm") + " " + this.performanceItem.getPerformance().toString();
        }
    }
    
    @Override
    public void updateMessageStrings(Locale locale) {
        setCaption(utils.getMessage("window.casting", utils.getLocale()) + " " + this.performanceItem.toString());
        this.invite.setCaption(utils.getMessage("button.invite", locale));
        this.confirm.setCaption(utils.getMessage("button.confirme", locale));
        this.mail.setCaption(utils.getMessage("button.mail", locale));
        this.sms.setCaption(utils.getMessage("button.sms", locale));
        this.saveInvited.setCaption(utils.getMessage("button.save", locale));
        this.saveConfirmed.setCaption(utils.getMessage("button.save", locale));
        this.sendMail.setCaption(utils.getMessage("button.send", locale));
        this.sendSms.setCaption(utils.getMessage("button.send", locale));
        if(inviteMail) {
            this.switchMail.setCaption(utils.getMessage("button.switch.invited", locale));
        } else {
            this.switchMail.setCaption(utils.getMessage("button.switch.confirmed", locale));
        }
        if(inviteSms) {
            this.switchSms.setCaption(utils.getMessage("button.switch.invited", locale));
        } else {
            this.switchSms.setCaption(utils.getMessage("button.switch.confirmed", locale));
        }
        this.invited.setLeftColumnCaption(utils.getMessage("twincolselect.others", locale));
        this.invited.setRightColumnCaption(utils.getMessage("twincolselect.invited", locale));
        this.confirmed.setLeftColumnCaption(utils.getMessage("twincolselect.logged", locale));
        this.confirmed.setRightColumnCaption(utils.getMessage("twincolselect.confirmed", locale));
        this.newConfirmedMail.setLeftColumnCaption(utils.getMessage("twincolselect.logged", locale));
        this.newConfirmedMail.setRightColumnCaption(utils.getMessage("twincolselect.confirmed", locale));
        this.newInvitedMail.setLeftColumnCaption(utils.getMessage("twincolselect.others", locale));
        this.newInvitedMail.setRightColumnCaption(utils.getMessage("twincolselect.invited", locale));
        this.newInvitedSms.setLeftColumnCaption(utils.getMessage("twincolselect.others", locale));
        this.newInvitedSms.setRightColumnCaption(utils.getMessage("twincolselect.invited", locale));
        this.newConfirmedSms.setLeftColumnCaption(utils.getMessage("twincolselect.logged", locale));
        this.newConfirmedSms.setRightColumnCaption(utils.getMessage("twincolselect.confirmed", locale));
        this.message.setCaption(utils.getMessage("textarea.message"));
        this.message.setValue(textMessage());
    } 
    
    
}
