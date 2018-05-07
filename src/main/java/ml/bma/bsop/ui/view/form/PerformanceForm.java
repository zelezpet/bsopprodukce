/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.ui.view.form;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateTimeField;
import com.vaadin.ui.renderers.TextRenderer;
import java.util.List;
import java.util.Locale;
import ml.bma.bsop.backend.data.entity.Address;
import ml.bma.bsop.backend.data.entity.Performance;
import ml.bma.bsop.backend.data.entity.Production;
import ml.bma.bsop.backend.service.PerformanceService;
import ml.bma.bsop.ui.utils.Utils;
import org.vaadin.crudui.form.impl.field.provider.ComboBoxProvider;


/**
 *
 * @author ironman
 */

public class PerformanceForm extends AbstractForm<Performance, PerformanceService> {
   
    
    public PerformanceForm() {
        super(Performance.class);
    }

    @Override
    public void init(Utils utils, PerformanceService service) {
        super.init(utils, service);
        super.grid.setColumns("dateTime","duration","address","production","phase");
        super.grid.getColumn("address").setRenderer(addr -> {
            return ((Address) addr).getName();
        }, new TextRenderer(""));
        super.grid.getColumn("production").setRenderer(prod -> ((Production) prod).getName(), new TextRenderer(""));
        super.crudFormFactory.setVisibleProperties("dateTime","duration","address","production","phase");
        super.crudFormFactory.setFieldType("dateTime", DateTimeField.class);
        super.crudFormFactory.setFieldProvider("dateTime", () -> {
            DateTimeField field = new DateTimeField(utils.getMessage("datetimefield.label", utils.getLocale()));
            return field;
        });
        
        List<Address> addresses = service.findAllPerformanceAddress();
        addresses.sort((Address o1, Address o2) -> o1.toString().compareTo(o2.toString()));
        super.crudFormFactory.setFieldProvider("address", new ComboBoxProvider<>(utils.getMessage("combobox.address"), addresses));
        super.crudFormFactory.setFieldCreationListener("address", field -> {
            ((ComboBox) field).setEmptySelectionAllowed(false);
        });
        
        List<Production> productions = service.findAllProduction();
        productions.sort((Production p1, Production p2) -> p1.toString().compareTo(p2.toString()));
        super.crudFormFactory.setFieldProvider("production", new ComboBoxProvider<>(utils.getMessage("combobox.production"), productions));
        super.crudFormFactory.setFieldCreationListener("production", field -> {
            ((ComboBox) field).setEmptySelectionAllowed(false);
        });
        updateMessageStrings(utils.getLocale());
    }


    @Override
    public void updateMessageStrings(Locale locale) {
        super.grid.getColumn("dateTime").setCaption(super.utils.getMessage("datetimefield.label", locale));
        super.grid.getColumn("duration").setCaption(super.utils.getMessage("textfield.duration", locale));
        super.grid.getColumn("address").setCaption(super.utils.getMessage("combobox.address", locale));
        super.grid.getColumn("production").setCaption(super.utils.getMessage("combobox.production", locale));
        super.grid.getColumn("phase").setCaption(super.utils.getMessage("combobox.phase", locale));
        super.formFactory.setFieldCaptions(
                super.utils.getMessage("datetimefield.label", locale),
                super.utils.getMessage("textfield.duration", locale),
                super.utils.getMessage("combobox.address", locale),
                super.utils.getMessage("combobox.production", locale),
                super.utils.getMessage("combobox.phase", locale)
        );
    }
    
}
