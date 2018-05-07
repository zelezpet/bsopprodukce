/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.ui.view;

import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.CheckBox;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import ml.bma.bsop.backend.data.entity.Address;
import ml.bma.bsop.backend.service.AddressService;
import ml.bma.bsop.ui.utils.Utils;
import ml.bma.bsop.ui.view.form.AddressForm;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author ironman
 */

@SpringView(name = AddressView.NAME)
public class AddressView extends AbstractCrudView<Address, AddressService, AddressForm> {
    
    public static final String NAME = "address";
    
    private final CheckBox personal;
    

    @Autowired
    public AddressView(Utils utils, AddressService service) {
        super(utils, service, Address.class, AddressForm.class, true);
        
        this.personal = new CheckBox(utils.getMessage("checkbox.personal"), false);
        this.personal.addValueChangeListener(evt -> super.updateList());
        super.toolBar.addComponent(personal, 0);
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
    public List<Address> limitList(Optional<String> filter) {
        if(personal.getValue()) {
            return this.service.findAnyMatching(filter);
        }
        return this.service.findAnyMatchingPerformanceAddress(filter);
    }
    
}
