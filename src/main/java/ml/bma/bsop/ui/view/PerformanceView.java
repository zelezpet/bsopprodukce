/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.ui.view;

import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.ComboBox;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import ml.bma.bsop.backend.data.entity.Performance;
import ml.bma.bsop.backend.data.entity.Production;
import ml.bma.bsop.backend.service.PerformanceService;
import ml.bma.bsop.backend.service.ProductionService;
import ml.bma.bsop.ui.utils.Utils;
import ml.bma.bsop.ui.view.form.PerformanceForm;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author ironman
 */


@SpringView(name = PerformanceView.NAME)
public class PerformanceView extends AbstractCrudView<Performance, PerformanceService, PerformanceForm> {
    public static final String NAME = "performance";
    
    private final PerformanceService performanceService;
    private final ProductionService productionService;
    private final ComboBox<Production> productions;
    
            
    @Autowired
    public PerformanceView(Utils utils, PerformanceService service, ProductionService productionService) {
        super(utils, service, Performance.class, PerformanceForm.class, true);
        this.performanceService = service;
        this.productionService = productionService;
        
        this.productions = new ComboBox<>(utils.getMessage("combobox.production"), this.productionService.findAll());
        this.productions.setEmptySelectionAllowed(true);
        this.productions.setWidth(240, Unit.PIXELS);
        this.productions.addValueChangeListener(evt -> {
            super.updateList();
        });
        super.toolBar.addComponent(productions, 0);
    }
    
    

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        updateMessageStrings(super.utils.getLocale());
    }

    @Override
    public String getViewName() {
        return NAME;
    }

    @Override
    public void updateMessageStrings(Locale locale) {
        super.updateMessageString(locale);
    }

    @Override
    public List<Performance> limitList(Optional<String> filter) {
        Production temp = this.productions.getValue();
        String productionName = (temp == null)? "" : temp.getName();
        return this.performanceService.findByProductionAndFilter(productionName, filter);
    }
    
}
