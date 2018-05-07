/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.ui.component;

import com.vaadin.server.ThemeResource;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.util.Locale;

/**
 *
 * @author ironman
 */


@UIScope
public class LocaleBar extends HorizontalLayout {
    
    private final Button en,cs,de;

    public LocaleBar() {
        super.addStyleNames("locale-bar");
        super.setSizeUndefined();
        
        this.cs = new Button();
        cs.setIcon(new ThemeResource("img/cz.png"));
        cs.setPrimaryStyleName(ValoTheme.BUTTON_ICON_ONLY);
        cs.addClickListener((Button.ClickEvent event) -> {
            getUI().setLocale(new Locale("cs"));
            
        });
        super.addComponent(cs);
        
        this.en = new Button();
        en.setIcon(new ThemeResource("img/en.png"));
        en.setPrimaryStyleName(ValoTheme.BUTTON_ICON_ONLY);
        en.addClickListener((Button.ClickEvent event) -> {
            getUI().setLocale(new Locale("en"));
            
        });
        super.addComponent(en);
        
        this.de = new Button();
        de.setIcon(new ThemeResource("img/de.png"));
        de.setPrimaryStyleName(ValoTheme.BUTTON_ICON_ONLY);
        de.addClickListener((Button.ClickEvent event) -> {
            getUI().setLocale(new Locale("de"));
            
            
        });
        super.addComponent(de);
    }
    
    
}
