/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.ui;


import com.github.appreciated.app.layout.behaviour.AppLayout;
import com.github.appreciated.app.layout.behaviour.Behaviour;
import com.github.appreciated.app.layout.builder.AppLayoutBuilder;
import com.github.appreciated.app.layout.builder.AppLayoutBuilder.Position;
import com.github.appreciated.app.layout.builder.design.AppBarDesign;
import com.github.appreciated.app.layout.builder.elements.SubmenuBuilder;
import com.github.appreciated.app.layout.builder.elements.SubmenuNavigationElement;
import com.github.appreciated.app.layout.component.MenuHeader;
import com.vaadin.annotations.Theme;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ThemeResource;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.HorizontalLayout;
import java.util.Locale;
import ml.bma.bsop.backend.data.Authority;
import ml.bma.bsop.backend.data.entity.Performer;
import ml.bma.bsop.security.SecurityUtils;
import ml.bma.bsop.ui.component.EditAccountWindow;
import ml.bma.bsop.ui.component.LocaleBar;
import ml.bma.bsop.ui.utils.Utils;
import ml.bma.bsop.ui.view.AddressView;
import ml.bma.bsop.ui.view.CalendarView;
import ml.bma.bsop.ui.view.CastingView;
import ml.bma.bsop.ui.view.CompositionView;
import ml.bma.bsop.ui.view.PerformanceView;
import ml.bma.bsop.ui.view.PerformerView;
import ml.bma.bsop.ui.view.ProductionView;
import org.vaadin.spring.i18n.support.Translatable;

/**
 *
 * @author ironman
 */

@SpringComponent
@UIScope
@Theme("bsop")
public class BSOPProductionMenu extends HorizontalLayout implements Translatable {
    
    private final SpringViewProvider viewProvider;
    private final NavigationManager navigator;
    private final Utils utils;
    
    private BSOPProductionUI ui;
    
    private AppLayout menu;
    private SubmenuNavigationElement home;
    private SubmenuNavigationElement edit;
    
    private final LocaleBar localeBar;
    
    private final MenuHeader header;
    private final ThemeResource avatar;
    
    private String currentView;
    
    public BSOPProductionMenu(SpringViewProvider viewProvider, NavigationManager navigator, Utils utils) {
        
        this.viewProvider = viewProvider;
        this.navigator = navigator;
        this.utils = utils;
        this.localeBar = new LocaleBar();
        this.avatar = new ThemeResource("img/avatar.png");
        this.header = new MenuHeader(this.avatar);
        this.header.addStyleName("avatar-header");
        this.currentView = CalendarView.NAME;
        
    }
    
    public void init(BSOPProductionUI ui, Locale locale) {
        this.ui = ui;
        super.setSizeFull();
        
        initHome(locale);
        AppLayoutBuilder menuBuilder = AppLayoutBuilder.get(Behaviour.LEFT_RESPONSIVE)                
                                                    .withNavigatorProducer(components -> {                    
                                                        navigator.init(ui, components);                
                                                        return navigator;
                                                    })
                                                    .withDefaultNavigationView(viewProvider.getView(currentView))
                                                    .withTitle(utils.getMessage("application.name", locale))
                                                    .addToAppBar(localeBar)
                                                    .withDesign(AppBarDesign.DEFAULT)
                                                    .add(header, Position.HEADER)
                                                    .add(home);
        if(SecurityUtils.hasRole(Authority.ROLE_ADMIN)) {
            initEdit(locale);
            menuBuilder.add(edit);
        }
        menuBuilder.add(utils.getMessage("viewbutton.calendar", locale), VaadinIcons.CALENDAR_CLOCK, viewProvider.getView(CalendarView.NAME));
        this.menu = menuBuilder.build();
        this.navigator.navigateTo(currentView);
        super.addComponent(menu);
    }
    
    
    private void initEdit(Locale locale) {
        this.edit = SubmenuBuilder.get(utils.getMessage("submenu.edit.title", locale), VaadinIcons.EDIT)
                    .add(utils.getMessage("viewbutton.production", locale), ProductionView.NAME, VaadinIcons.PLUS, viewProvider.getView(ProductionView.NAME))
                    .add(utils.getMessage("viewbutton.performance", locale), PerformanceView.NAME, VaadinIcons.PLUS,viewProvider.getView(PerformanceView.NAME))
                    .add(utils.getMessage("viewbutton.casting", locale), CastingView.NAME, VaadinIcons.PLUS, viewProvider.getView(CastingView.NAME))
                    .add(utils.getMessage("viewbutton.address", locale), AddressView.NAME, VaadinIcons.PLUS, viewProvider.getView(AddressView.NAME))
                    .add(utils.getMessage("viewbutton.composition", locale), CompositionView.NAME, VaadinIcons.PLUS, viewProvider.getView(CompositionView.NAME))
                    .build();
        
    }    
    
    private void initHome(Locale locale) {
        SubmenuBuilder builder = SubmenuBuilder.get(getName(), VaadinIcons.HOME)
                                    .addClickable(utils.getMessage("button.editaccount", locale), VaadinIcons.WRENCH, evt -> {
                                        EditAccountWindow editWindow = new EditAccountWindow(utils);
                                        editWindow.setModal(true);
                                        ui.addWindow(editWindow);
                                    });
        
        if(SecurityUtils.hasRole(Authority.ROLE_USER)) {            
            builder.add(utils.getMessage("viewbutton.performer.user", locale), PerformerView.NAME, VaadinIcons.EDIT, viewProvider.getView(PerformerView.NAME));
        } else if(SecurityUtils.hasRole(Authority.ROLE_PERFORMER)) {
                builder.add(utils.getMessage("viewbutton.performer", locale), PerformerView.NAME, VaadinIcons.EDIT, viewProvider.getView(PerformerView.NAME));
        } else if(SecurityUtils.hasRole(Authority.ROLE_ADMIN)) {
            
        }
        
        this.home = builder.addClickable(utils.getMessage("logout", locale), VaadinIcons.EXIT, evt -> ui.logout()).build();
    }
    
    private String getName() {
        String name = "HOME";
        if(SecurityUtils.hasRole(Authority.ROLE_USER)) {
            name = this.ui.getCurrentAccountEmail();
        }
        if(SecurityUtils.hasRole(Authority.ROLE_PERFORMER)) {
            Performer performer = this.utils.getPerformer(this.utils.getCurrentAccount());
            name = performer.getFirstname() + " " + performer.getSurname();
        }
        if(SecurityUtils.hasRole(Authority.ROLE_ADMIN)) {
            name = "Admin-" + this.ui.getCurrentAccountEmail();
        }
        if(name.length() > 19) {
            name = name.substring(0, 19) + "...";
        }
        return name;
    }
    

    @Override
    public void updateMessageStrings(Locale locale) {
        super.removeAllComponents();
        this.currentView = this.navigator.getState();
        init(ui, locale);
        
    }
    
}

    
