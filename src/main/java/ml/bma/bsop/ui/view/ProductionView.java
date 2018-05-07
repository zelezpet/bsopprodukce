/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.ui.view;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.util.Locale;
import ml.bma.bsop.backend.data.entity.Production;
import ml.bma.bsop.backend.service.ProductionService;
import ml.bma.bsop.ui.component.BSOPNotification;
import ml.bma.bsop.ui.utils.Utils;
import ml.bma.bsop.ui.view.form.ProductionForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.i18n.support.Translatable;

/**
 *
 * @author ironman
 */

@SpringView(name = ProductionView.NAME)
@UIScope
public class ProductionView extends AbstractView implements Translatable {
    
    public static final String NAME = "production";
    
    private final Utils utils;
    private final ProductionService productionService;

    private final ProductionForm form;
    
    private final ComboBox<String> productions;
    private final Button newButton;
    
    
    @Autowired
    public ProductionView(Utils utils, ProductionService productionService, ProductionForm localForm) {
        
        this.utils = utils;
        this.productionService = productionService;
        this.form = localForm;
        
        super.title.setCaption(utils.getMessage("view."+NAME));
        
        this.productions = new ComboBox(utils.getMessage("combobox.production"));
        this.productions.setItems(productionService.getNameList());
        this.productions.setWidth(191, Unit.PIXELS);
        this.productions.setEmptySelectionAllowed(true);
        this.productions.setEmptySelectionCaption("");
        this.productions.addValueChangeListener(evt -> {
            String name = (String) evt.getValue();
            if(name != null && !name.isEmpty()) {
                Production production = productionService.findProduction(name);
                form.init(production);
            } else {
                newProduction();
            }
            
        });
        
        this.newButton = new Button();
        this.newButton.setIcon(VaadinIcons.PLUS);
        this.newButton.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        this.newButton.addClickListener((evt) -> newProduction());
                 
        
        HorizontalLayout wrapper = new HorizontalLayout(newButton, productions);
        wrapper.addStyleName("edit-wrapper");
        wrapper.setComponentAlignment(newButton, Alignment.BOTTOM_CENTER);
        super.content.addComponents(wrapper, localForm);
        super.content.setComponentAlignment(wrapper, Alignment.MIDDLE_LEFT);
        super.content.setComponentAlignment(localForm, Alignment.MIDDLE_LEFT);
    }

    private void newProduction() {
        BSOPNotification notify = new BSOPNotification(utils.getMessage("notification.newproduction"));
        notify.show();
        form.clear();
        this.form.init(null);
        this.productions.setSelectedItem(null);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        this.form.init(null);
    }

    @Override
    public String getViewName() {
        return ProductionView.NAME;
    }

    @Override
    public void updateMessageStrings(Locale locale) {
        super.title.setCaption(utils.getMessage("view."+NAME, locale));
        this.productions.setCaption(utils.getMessage("combobox.production", locale));
    }
    
}
