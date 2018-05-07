/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.ui.component;

import com.vaadin.server.UserError;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import ml.bma.bsop.backend.data.Authority;
import ml.bma.bsop.backend.data.CastState;
import ml.bma.bsop.backend.data.PerformanceState;
import ml.bma.bsop.backend.data.entity.Ability;
import ml.bma.bsop.backend.data.entity.Account;
import ml.bma.bsop.backend.data.entity.Address;
import ml.bma.bsop.backend.data.entity.Casting;
import ml.bma.bsop.backend.data.entity.Performance;
import ml.bma.bsop.backend.data.entity.Performer;
import ml.bma.bsop.backend.data.entity.Production;
import ml.bma.bsop.backend.service.CastingService;
import ml.bma.bsop.backend.service.PerformanceService;
import ml.bma.bsop.security.SecurityUtils;
import ml.bma.bsop.ui.utils.Utils;
import org.vaadin.addon.calendar.Calendar;
import org.vaadin.addon.calendar.item.BasicItemProvider;
import org.vaadin.addon.calendar.ui.CalendarComponentEvents.ItemClickEvent;
import org.vaadin.addon.calendar.ui.CalendarComponentEvents.ItemMoveEvent;
import org.vaadin.addon.calendar.ui.CalendarComponentEvents.ItemResizeEvent;
import org.vaadin.addon.calendar.ui.CalendarComponentEvents.RangeSelectEvent;

/**
 *
 * @author ironman
 */
public class PerformanceCalendar extends CustomComponent {
    
    private final Utils utils;    
    private final PerformanceService performanceService;
    private final CastingService castingService;
    
    private final PerformanceDataProvider provider;
    
    private final Panel panel;    
    private final Calendar<PerformanceItem> calendar;
        
    
    public PerformanceCalendar(Utils utils,PerformanceService performanceService, CastingService castingService) {
        this.utils = utils;
        this.performanceService = performanceService;
        this.castingService = castingService;
        this.provider = new PerformanceDataProvider();
        this.calendar = new Calendar<>(this.provider);
        this.calendar.setSizeFull();
        this.calendar.setItemCaptionAsHtml(true);
        this.calendar.setContentMode(ContentMode.HTML);        
        this.calendar.setHandler(this::onCalendarClick);
        this.calendar.setHandler(this::onCalendarRangeSelect);
        this.calendar.setHandler(this::onItemMove);
        this.calendar.setHandler(this::onItemResize);
        
        this.panel = new Panel(this.calendar);
        this.panel.setSizeFull();
        
        VerticalLayout layout = new VerticalLayout(this.panel);
        layout.setSizeFull();
        
        setId("meeting-meetings");
        setCompositionRoot(layout);
        
    }
    
    
    private void onCalendarRangeSelect(RangeSelectEvent event) {
        if(SecurityUtils.hasRole(Authority.ROLE_USER) || SecurityUtils.hasRole(Authority.ROLE_PERFORMER)) return;
        ComboBox<Production> productions = new ComboBox<>(utils.getMessage("combobox.production"), performanceService.findAllProduction());
        productions.setEmptySelectionAllowed(false);
        ComboBox<Address> addresses = new ComboBox<>(utils.getMessage("combobox.address"), performanceService.findAllPerformanceAddress());
        addresses.setEmptySelectionAllowed(false);
        Button save = new Button(utils.getMessage("button.save"), evt -> {
            Address address = addresses.getValue();
            Production production = productions.getValue();
            if(address != null && production != null) {
                LocalDateTime dateTime = event.getStart().toLocalDateTime();
                Duration duration = Duration.between(event.getStart(), event.getEnd());
                Performance performance =  new Performance();
                performance.setAddress(address);
                performance.setProduction(production);
                performance.setPhase(PerformanceState.CREATE);
                performance.setDateTime(dateTime);
                performance.setDuration((int)duration.toMinutes());
                this.performanceService.save(performance);
                UI.getCurrent().getWindows().iterator().next().close();
                this.provider.addItem(new PerformanceItem(performance));
            } else {
                if(production == null) {
                    productions.setComponentError(new UserError(utils.getMessage("usererror.required")) );
                }
                if(address == null) {
                    addresses.setComponentError(new UserError(utils.getMessage("usererror.required")) );
                }
            }
        });
        
        VerticalLayout content = new VerticalLayout(productions, addresses, save);
        Window window = new Window(utils.getMessage("window.performance"), content);
        window.setModal(true);
        UI.getCurrent().addWindow(window);
    }
    
    private void onCalendarClick(ItemClickEvent event) {
        
        PerformanceItem item = (PerformanceItem)event.getCalendarItem();
        Performance performance = item.getPerformance();
        PerformanceState phase = performance.getPhase();
        
        Image img = item.getPerformance().getProduction().getImage();
        img.setWidth(100.0f, Unit.PERCENTAGE);
        img.setCaption("");
        Label desc = new Label(item.toHtmlString(), ContentMode.HTML);
        desc.setWidth(300.0f, Unit.PIXELS);
        VerticalLayout content = new VerticalLayout(img,desc);        
        
        if(SecurityUtils.hasRole(Authority.ROLE_PERFORMER)) {
            switch(phase) {
                case CREATE:
                case CAST:
                    Account account = utils.getCurrentAccount();
                    Performer performer = utils.getPerformer(account);
                    ComboBox<Ability> abilities = new ComboBox<>(utils.getMessage("combobox.ability"), performer.getAbilities());
                    abilities.setEmptySelectionAllowed(false);
                    abilities.addValueChangeListener(evt -> {
                        if(evt.getValue() != null) {
                            abilities.setComponentError(null);
                        }
                    });
                    Button login = new Button(utils.getMessage("button.login"), evt -> {
                        List<Casting> castings = castingService.findByPerformance(performance);
                        Ability ability = abilities.getValue();
                        if(ability == null) {
                            abilities.setComponentError(new UserError(utils.getMessage("usererror.required")));
                            return;
                        }
                        boolean create = true;
                        String message = "ok";
                        for(Casting casting: castings) {
                            if(ability.equals(casting.getAbility())) {
                                create = false;
                                CastState state = casting.getCastState();
                                switch(state) {
                                    case INVITED: 
                                        casting.setCastState(CastState.CONFIRMED);
                                        castingService.save(casting);
                                        message = utils.getMessage("notification.casting.newconfirmed", utils.getLocale());
                                        break;
                                    case CONFIRMED:
                                        message = utils.getMessage("notification.casting.confirmed", utils.getLocale());
                                        break;
                                    case LOGGED:
                                        message = utils.getMessage("notification.casting.logged", utils.getLocale());
                                }
                            }
                        }
                        if(create) {
                            Casting casting = new Casting(ability, item.getPerformance(), CastState.LOGGED);
                            castingService.save(casting);
                            message = utils.getMessage("notification.casting.newlogged");
                            if(performance.getPhase() == PerformanceState.CREATE) {
                                performance.setPhase(PerformanceState.CAST);
                                castingService.savePerformance(performance);
                            }
                        }
                        UI.getCurrent().getWindows().iterator().next().close();
                        BSOPNotification notification = new BSOPNotification(message);
                        notification.show();
                    });
                    HorizontalLayout abilityLogin = new HorizontalLayout(abilities,login);
                    abilityLogin.setComponentAlignment(login, Alignment.BOTTOM_RIGHT);
                    content.addComponent(abilityLogin);
                    break;
                case READY:
                case END: 
            }
            
        } else if(SecurityUtils.hasRole(Authority.ROLE_ADMIN)) {
            switch(phase) {
                case CREATE:
                case CAST:
                    Button cast = new Button(utils.getMessage("button.cast"), evt -> {
                        UI.getCurrent().getWindows().iterator().next().close();
                        casting(item);
                    });
                    content.addComponent(cast);
                    content.setComponentAlignment(cast, Alignment.MIDDLE_CENTER);
                    break;
                case READY:
                case END: 
            }
        }
        
        Window window = new Window(this.utils.getMessage("window.performance") +" "+ item.getStart().getDayOfMonth() + "." + item.getStart().getMonthValue() + ".", content);
        window.setModal(true);
        UI.getCurrent().addWindow(window);
    }
    
    private void onItemMove(ItemMoveEvent event) {
        PerformanceItem item = (PerformanceItem) event.getCalendarItem();
        if(item.isMoveable()) {
            item.setStart(event.getNewStart());
            performanceService.save(item.getPerformance());
        }
    }
    
    private void onItemResize(ItemResizeEvent event) {
        PerformanceItem item = (PerformanceItem) event.getCalendarItem();
        if(item.isResizeable()) {
            item.setStart(event.getNewStart());
            item.setEnd(event.getNewEnd());
            performanceService.save(item.getPerformance());
        }
    }

    private void casting(PerformanceItem item) {
        
        CastingWindow window= new CastingWindow(utils, castingService, item);
        window.center();
        UI.getCurrent().addWindow(window);
    }
    
    public void setDayView() {
        this.calendar.withDay(ZonedDateTime.now());
    }
    
    public void setSmallHeight() {
        this.calendar.setHeight(700.0f, Unit.PIXELS);
    }
    
    
    private final class PerformanceDataProvider extends BasicItemProvider<PerformanceItem> {
        
        @Override
        public List<PerformanceItem> getItems(ZonedDateTime start, ZonedDateTime end) {
            List<Performance> performances = performanceService.findAll();
            List<PerformanceItem> items = new ArrayList<>();
            performances.forEach((performance) -> {
                ZonedDateTime dateTime = performance.getDateTime().atZone(ZoneId.systemDefault());
                Duration startEnd = Duration.between(start, end);
                Duration startPerformance = Duration.between(start, dateTime);
                if (!startPerformance.isNegative() && startEnd.compareTo(startPerformance) > 0) {
                    if(SecurityUtils.hasRole(Authority.ROLE_ADMIN) || SecurityUtils.hasRole(Authority.ROLE_PERFORMER)) {
                        PerformanceItem item = new PerformanceItem(performance);
                        items.add(item);
                    } else {
                        PerformanceState phase = performance.getPhase();
                        if(phase == PerformanceState.READY || phase == PerformanceState.END) {
                            PerformanceItem item = new PerformanceItem(performance);
                            item.setStyleName(item.getStyleName());
                            items.add(item);
                        }
                    }                   
                }
            });

            return items;
        }
        
    }
}
