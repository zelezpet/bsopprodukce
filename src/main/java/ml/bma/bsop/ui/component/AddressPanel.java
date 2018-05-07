/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.ui.component;

import com.jarektoro.responsivelayout.ResponsiveLayout;
import com.jarektoro.responsivelayout.ResponsiveRow;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.UserError;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Link;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.util.Locale;
import ml.bma.bsop.backend.data.Country;
import ml.bma.bsop.backend.data.entity.Address;
import ml.bma.bsop.ui.utils.Utils;
import org.vaadin.spring.i18n.support.Translatable;

/**
 *
 * @author ironman
 */

public class AddressPanel extends Panel implements Translatable {
    
    private final Utils utils;
    
    private final TextField street;
    
    private final TextField number;
    
    private final TextField city;
    
    private final TextField zipCode;
    
    private final ComboBox<Country> country;
    
    private final Link link;
    
    private final ExternalResource href = new ExternalResource("https://mapanet.eu/en");
    
    
    public AddressPanel(Utils utils) {
        
        this.utils = utils;
        
        this.street = new TextField(utils.getMessage("textfield.street"));
        this.street.setWidth(240, Unit.PIXELS);
        
        this.number = new TextField(utils.getMessage("textfield.housenumber"));
        this.number.setMaxLength(11);
        this.number.setWidth(120, Unit.PIXELS);
        
        this.city = new TextField(utils.getMessage("textfield.city"));
        this.city.setWidth(240, Unit.PIXELS);

        this.zipCode = new TextField(utils.getMessage("textfield.zipcode"));
        this.zipCode.setMaxLength(5);
        this.zipCode.setWidth(70, Unit.PIXELS);
        
        this.link = new Link(utils.getMessage("link.zipcode"), href);
        this.link.setCaptionAsHtml(true);
        this.link.setIcon(VaadinIcons.SEARCH);
        this.link.setTargetName("_blank");
        this.link.setTargetBorder(BorderStyle.DEFAULT);
        
        this.country = new ComboBox<>(utils.getMessage("combobox.country"));
        this.country.setItems((String itemCaption, String filterText) -> itemCaption.toLowerCase().contains(filterText.toLowerCase()), "cs".equals(utils.getLocale().getLanguage())? Country.getSortValuesByNameCZ() : Country.getSortValuesByNameEN());
        this.country.setItemCaptionGenerator((Country item) -> "cs".equals(utils.getLocale().getLanguage())? item.getNameCZ() : item.getNameEN());
        this.country.setScrollToSelectedItem(true);
        this.country.setWidth(240, Unit.PIXELS);
        
        ResponsiveLayout responsive = new ResponsiveLayout();
        ResponsiveRow row = responsive.addRow();
        row.setMargin(true);
        row.setSpacing(true);
        
        VerticalLayout lay1 = new VerticalLayout(street);
        lay1.setMargin(false);
        VerticalLayout lay2 = new VerticalLayout(number);
        lay2.setMargin(false);
        VerticalLayout lay3 = new VerticalLayout(city);
        lay3.setMargin(false);
        VerticalLayout lay4 = new VerticalLayout(zipCode,link);
        lay4.setMargin(false);
        VerticalLayout lay5 = new VerticalLayout(country);
        lay5.setMargin(false);
        row.addColumn().withComponent(lay1);
        row.addColumn().withComponent(lay2);
        row.addColumn().withComponent(lay3);
        row.addColumn().withComponent(lay4);
        row.addColumn().withComponent(lay5);
        
        super.setContent(responsive);
        super.setCaption(utils.getMessage("address.name"));
        super.addStyleName(ValoTheme.PANEL_WELL);
        
    }

    @Override
    public void updateMessageStrings(Locale locale) {
        super.setCaption(utils.getMessage("address.name"));
        this.street.setCaption(utils.getMessage("textfield.street", locale));
        this.street.setLocale(locale);
        this.number.setCaption(utils.getMessage("textfield.housenumber", locale));
        this.number.setLocale(locale);
        this.city.setCaption(utils.getMessage("textfield.city", locale));
        this.city.setLocale(locale);
        this.zipCode.setCaption(utils.getMessage("textfield.zipcode", locale));
        this.zipCode.setLocale(locale);
        this.link.setCaption(utils.getMessage("link.zipcode", locale));
        this.country.setCaption(utils.getMessage("combobox.country", locale));
        this.country.setLocale(locale);
    }

    public String getStreet() {
        return this.street.getValue();
    }

    public String getNumber() {
        return this.number.getValue();
    }

    public String getCity() {
        return this.city.getValue();
    }

    public String getZipCode() {
        return this.zipCode.getValue();
    }

    public Country getCountry() {
        return country.getValue();
    }
    
    public Address getAddress() {
        return new Address(getStreet(),getNumber(),getCity(),getZipCode(),getCountry());
    }
    
    public void init(Address address) {
        this.street.setValue(address.getStreet());
        this.number.setValue(address.getHouseNumber());
        this.city.setValue(address.getCity());
        this.zipCode.setValue(address.getZipCode());
        this.country.setValue(address.getCountry());
    }
    
    public boolean validate() {
        String street = this.street.getValue();
        String number = this.number.getValue();
        String city = this.city.getValue();
        String zipCode = this.zipCode.getValue();
        Country country = this.country.getValue();
        
        boolean valid = true;
        
        if(street.isEmpty()) {
            this.street.setComponentError(new UserError(utils.getMessage("usererror.required")));
            valid = false;
        } else {
            if(!street.matches("[1-9\\p{Lu}][0-9\\p{L}\\.]*([ \\-\\/][\\p{L}1-9][\\p{L}0-9\\.]*)*")) {
                this.street.setComponentError(new UserError(utils.getMessage("usererror.match.street")));
                valid = false;
            } else {
                this.street.setComponentError(null);
            }            
        }
        
        if(number.isEmpty()) {
            this.number.setComponentError(new UserError(utils.getMessage("usererror.required")));
            valid = false;
        } else {
            if(!number.matches("([0-9]{1,4}[A-Za-z]?)(\\/[0-9]{1,4}[A-Za-z]?)*")) {
                this.number.setComponentError(new UserError(utils.getMessage("usererror.match.housenumber")));
                valid = false;
            } else {
                this.number.setComponentError(null);
            }
        }
        
        if(city.isEmpty()) {
            this.city.setComponentError(new UserError(utils.getMessage("usererror.required")));
            valid = false;
        } else {
            if(!city.matches("[\\p{Lu}1-9][\\p{L}0-9\\.]*([ \\-\\/][\\p{L}1-9][\\p{L}0-9\\.]*)*")) {
                this.city.setComponentError(new UserError(utils.getMessage("usererror.match.city")));
                valid = false;
            } else {
                this.city.setComponentError(null);
            }
        }
        
        if(zipCode.isEmpty()) {
            this.zipCode.setComponentError(new UserError(utils.getMessage("usererror.required")));
            valid = false;
        } else {
            if(!zipCode.matches("[0-9]{5}")) {
                this.zipCode.setComponentError(new UserError(utils.getMessage("usererror.match.zipcode")));
                valid = false;
            } else {
                this.zipCode.setComponentError(null);
            }
        }
        
        if(country == null) {
            this.country.setComponentError(new UserError(utils.getMessage("usererror.required")));
            valid = false;
        } else {
            this.country.setComponentError(null);
        }
        return valid;
    }

    @Override
    public void setEnabled(boolean b) {
        this.street.setEnabled(b);
        this.number.setEnabled(b);
        this.city.setEnabled(b);
        this.zipCode.setEnabled(b);
        this.country.setEnabled(b);
    }
    
    
}
