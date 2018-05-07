/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.ui.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.VerticalLayout;
import java.util.Locale;
import ml.bma.bsop.backend.service.CastingService;
import ml.bma.bsop.backend.service.PerformanceService;
import ml.bma.bsop.ui.component.PerformanceCalendar;
import ml.bma.bsop.ui.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.i18n.support.Translatable;

/**
 *
 * @author ironman
 */

@SpringView(name = CalendarView.NAME)
public class CalendarView extends VerticalLayout implements View, Translatable {
    
    public static final String NAME = "calendar";
    
    private final Utils utils;
    private final PerformanceService performanceService;
    private final CastingService castingService;

    private final PerformanceCalendar calendar;
    
    

    @Autowired
    public CalendarView(Utils utils, PerformanceService performanceService, CastingService castingService) {
        
        this.utils = utils;
        this.performanceService = performanceService;
        this.castingService = castingService;
        
        this.calendar = new PerformanceCalendar(utils, performanceService, castingService);
        this.calendar.setSizeFull();
        this.calendar.setResponsive(true);
        
        setSizeFull();
        setMargin(false);
        setSpacing(false);
        addComponents(this.calendar);
        
        updateMessageStrings(utils.getLocale());
        
    }   

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        Page.getCurrent().addBrowserWindowResizeListener(new Page.BrowserWindowResizeListener() {
            @Override
            public void browserWindowResized(Page.BrowserWindowResizeEvent event) {
                int width = Page.getCurrent().getBrowserWindowWidth();
                int height = Page.getCurrent().getBrowserWindowHeight();
                if(width < 700) {
                    calendar.setDayView();
                }
                if(height < 700) {
                    calendar.setSmallHeight();
                    
                }
            }
        });
        
    }

    public String getViewName() {
        return NAME;
    }

    @Override
    public final void updateMessageStrings(Locale locale) {
        
    }
    
}
