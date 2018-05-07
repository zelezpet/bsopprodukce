/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.ui.view.form;

import com.vaadin.data.HasValue;
import com.vaadin.ui.ComboBox;
import java.util.Locale;
import ml.bma.bsop.backend.data.Country;
import ml.bma.bsop.backend.data.entity.Address;
import ml.bma.bsop.backend.service.AddressService;
import ml.bma.bsop.ui.utils.Utils;
import org.vaadin.spring.i18n.support.Translatable;

/**
 *
 * @author ironman
 */


public class AddressForm extends AbstractForm<Address, AddressService> implements Translatable{
    
    public AddressForm() {
        super(Address.class);
    }

    @Override
    public void init(Utils utils, AddressService service) {
        super.init(utils, service);
        super.grid.setColumns("name", "street", "houseNumber", "city", "zipCode", "country");
        super.formFactory.setVisibleProperties("name", "street", "houseNumber", "city", "zipCode", "country");
        super.crudFormFactory.setFieldType("country", ComboBox.class);
        super.formFactory.setFieldCreationListener("country", (HasValue field) -> {
            ((ComboBox)field).setItems((String itemCaption, String filterText) -> itemCaption.toLowerCase().contains(filterText.toLowerCase()), "cs".equals(utils.getLocale().getLanguage())? Country.getSortValuesByNameCZ() : Country.getSortValuesByNameEN());
            ((ComboBox)field).setItemCaptionGenerator(item -> {
                return "cs".equals(utils.getLocale().getLanguage())? ((Country)item).getNameCZ() : ((Country)item).getNameEN();
            });
        });
        updateMessageStrings(utils.getLocale());
    }

    @Override
    public void updateMessageStrings(Locale locale) {
        super.grid.getColumn("name").setCaption(super.utils.getMessage("textfield.addressname", locale));
        super.grid.getColumn("street").setCaption(super.utils.getMessage("textfield.street", locale));
        super.grid.getColumn("houseNumber").setCaption(super.utils.getMessage("textfield.housenumber", locale));
        super.grid.getColumn("city").setCaption(super.utils.getMessage("textfield.city", locale));
        super.grid.getColumn("zipCode").setCaption(super.utils.getMessage("textfield.zipcode", locale));
        super.grid.getColumn("country").setCaption(super.utils.getMessage("combobox.country", locale));
        super.formFactory.setFieldCaptions(
                super.utils.getMessage("textfield.addressname"),
                super.utils.getMessage("textfield.street"),
                super.utils.getMessage("textfield.housenumber"),
                super.utils.getMessage("textfield.city"),
                super.utils.getMessage("textfield.zipcode"),
                super.utils.getMessage("combobox.country")        
        );
    }
    
}
