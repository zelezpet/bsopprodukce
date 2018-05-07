/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.ui.component;

import com.jarektoro.responsivelayout.ResponsiveLayout;
import com.jarektoro.responsivelayout.ResponsiveRow;
import com.vaadin.annotations.Theme;
import com.vaadin.server.UserError;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.themes.ValoTheme;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import ml.bma.bsop.backend.data.Country;
import ml.bma.bsop.backend.data.Instrument;
import ml.bma.bsop.backend.data.entity.Ability;
import ml.bma.bsop.backend.data.entity.CellPhone;
import ml.bma.bsop.backend.data.entity.Performer;
import ml.bma.bsop.ui.utils.Utils;
import org.vaadin.spring.i18n.support.Translatable;

/**
 *
 * @author ironman
 */
@Theme("bsop")
public class PersonalPanel extends Panel implements Translatable{
    private final Utils utils;
    
    private final TextField firstName;
    
    private final TextField surname;
    
    private final TextField op;
    
    private final TextField pas;
    
    private final ComboBox<Country> preset;
    private final TextField cellPhone;
    
    private final TwinColSelect<Instrument> instruments;
    
    
    public PersonalPanel(Utils utils) {
        
        this.utils = utils;
        
        this.firstName = new TextField(utils.getMessage("textfield.firstname"));
        this.firstName.setWidth(240, Unit.PIXELS);
        this.surname = new TextField(utils.getMessage("textfield.surname"));
        this.surname.setWidth(240, Unit.PIXELS);
        this.op = new TextField(utils.getMessage("textfield.op"));
        this.op.setWidth(105, Unit.PIXELS);
        this.op.setMaxLength(9);
        this.pas = new TextField(utils.getMessage("textfield.pas"));
        this.pas.setMaxLength(9);
        this.pas.setWidth(105, Unit.PIXELS);
        
        this.preset = new ComboBox<>(utils.getMessage("combobox.preset"));
        this.preset.setItems((String itemCaption, String filterText) -> itemCaption.toLowerCase().contains(filterText.toLowerCase()),
                ("cs".equals(utils.getLocale().getLanguage()) ? Country.getSortValuesByNameCZ() : Country.getSortValuesByNameEN()));
        this.preset.setItemCaptionGenerator((Country country) -> "cs".equals(utils.getLocale().getLanguage())? country.getNameCZ() : country.getNameEN());
        this.preset.setScrollToSelectedItem(true);
        this.preset.setWidth(120, Unit.PIXELS);
        
        this.cellPhone = new TextField(utils.getMessage("textfield.cellphone"));
        this.cellPhone.setMaxLength(10);
        this.cellPhone.setWidth(110,Unit.PIXELS);
        
        this.instruments = new TwinColSelect<>(utils.getMessage("twincolselect.instruments"));
        this.instruments.setItems(Instrument.getCollection());
        this.instruments.setRows(5);
        this.instruments.setWidth(240, Unit.PIXELS);
        
        ArrayList<HorizontalLayout> layouts = new ArrayList<>();
        for(int i = 0; i < 6; i++){
            layouts.add(new HorizontalLayout());
        }
        layouts.get(0).addComponent(firstName);
        layouts.get(1).addComponent(surname);
        layouts.get(2).addComponent(op);
        layouts.get(3).addComponent(pas);
        layouts.get(4).addComponents(preset,cellPhone);
        layouts.get(5).addComponent(instruments);
        
        
        ResponsiveLayout responsive = new ResponsiveLayout();
        ResponsiveRow row = responsive.addRow();
        row.setMargin(true);
        row.setSpacing(true);
        
        for(HorizontalLayout layout: layouts) {
            layout.setMargin(false);
            row.addColumn().withComponent(layout);
        }       
        super.setContent(responsive);
        super.setCaption(utils.getMessage("personal.name"));
        super.addStyleName(ValoTheme.PANEL_WELL);
        
    }
    
    public void init(String firstName, String surname, String op, String pas, Country preset, String cellPhone) {
        this.firstName.setValue(firstName);
        this.surname.setValue(surname);
        this.op.setValue(op);
        this.pas.setValue(pas);
        this.preset.setValue(preset);
        this.cellPhone.setValue(cellPhone);
    }

    @Override
    public void updateMessageStrings(Locale locale) {
        super.setCaption(utils.getMessage("personal.name"));
        this.firstName.setCaption(utils.getMessage("textfield.firstname", locale));
        this.firstName.setLocale(locale);
        this.surname.setCaption(utils.getMessage("textfield.surname", locale));
        this.surname.setLocale(locale);
        this.op.setCaption(utils.getMessage("textfield.op", locale));
        this.op.setLocale(locale);
        this.pas.setCaption(utils.getMessage("textfield.pas", locale));
        this.pas.setLocale(locale);
        this.preset.setCaption(utils.getMessage("combobox.preset"));
        this.preset.setItems("cs".equals(utils.getLocale().getLanguage()) ? Country.getSortValuesByNameCZ() : Country.getSortValuesByNameEN());
        this.preset.setLocale(locale);
        this.cellPhone.setCaption(utils.getMessage("textfield.cellphone", locale));
        this.cellPhone.setLocale(locale);
        this.instruments.setCaption(utils.getMessage("twincolselect.instruments"));
    }

    public String getFirstName() {
        String firstName= this.firstName.getValue();
        if(firstName.isEmpty()) {
            return null;
        }
        return firstName;
    }

    public String getSurname() {
        String surname= this.surname.getValue();
        if(surname.isEmpty()) {
            return null;
        }
        return surname;
    }

    public String getOp() {
        String op = this.op.getValue();
        if(op.isEmpty()) {
            return null;
        }        
        return op;
    }

    public String getPas() {
        String pas = this.pas.getValue();
        if(pas.isEmpty()) {
            return null;
        }     
        return pas;
    }
    
    public CellPhone getCellPhone() {
        Country country = preset.getValue();
        if(country == null) {
            return null;
        }        
        return new CellPhone(this.cellPhone.getValue(), country);
    }
    
    public Set<Instrument> getInstruments() {
        return this.instruments.getSelectedItems();
    }

    public void init(Performer performer) {
        this.firstName.setValue(performer.getFirstname());
        this.surname.setValue(performer.getSurname());
        String op = performer.getOp();
        if(op != null) {
            this.op.setValue(performer.getOp());
        } else {
            this.pas.setValue(performer.getPas());
        }       
        CellPhone cell = performer.getCellPhone();
        Country tmp = cell.getCountry();
        this.preset.setValue(tmp);
        this.cellPhone.setValue(cell.getNumber());
        List<Ability> abilities = performer.getAbilities();
        for(Ability ability: performer.getAbilities()) {
            this.instruments.select(ability.getInstrument());
        }
    }
    
    public boolean validate() {
        String firstName = this.firstName.getValue();
        String surname = this.surname.getValue();
        String op = this.op.getValue();
        String pas = this.pas.getValue();
        Country preset = this.preset.getValue();
        String cellPhone = this.cellPhone.getValue();
        Set<Instrument> instruments = this.instruments.getSelectedItems();
        
        boolean valid = true;
        
        if(firstName.isEmpty()) {
            this.firstName.setComponentError(new UserError(utils.getMessage("usererror.required")));
            valid = false;
        } else {
            if(!firstName.matches("\\p{Lu}\\p{L}+")) {
                this.firstName.setComponentError(new UserError(utils.getMessage("usererror.match.firstname")));
                valid = false;
            } else {
                this.firstName.setComponentError(null);
            }            
        }
        
        if(surname.isEmpty()) {
            this.surname.setComponentError(new UserError(utils.getMessage("usererror.required")));
            valid = false;
        } else {
            if(!surname.matches("(\\p{Lu}\\p{L}+)*(\\p{Lu}'\\p{L}+)*([ \\-]\\p{Lu}\\p{L}+)*")) {
                this.surname.setComponentError(new UserError(utils.getMessage("usererror.match.surname")));
                valid = false;
            } else {
                this.surname.setComponentError(null);
            }
        }
        
        if(op.isEmpty() && pas.isEmpty()) {
            this.op.setComponentError(new UserError(utils.getMessage("usererror.oporpasrequired")));
            this.pas.setComponentError(new UserError(utils.getMessage("usererror.oporpasrequired")));
            valid = false;
        } else {
            
            if(!op.isEmpty() && !op.matches("[A-Z0-9]{9}")) {
                this.op.setComponentError(new UserError(utils.getMessage("usererror.match.op")));
                valid = false;
            } else {
                this.op.setComponentError(null);
            }
            if(!pas.isEmpty() && !pas.matches("[A-Z0-9]{9}")) {
                this.pas.setComponentError(new UserError(utils.getMessage("usererror.match.pas")));
                valid = false;
            } else {
                this.pas.setComponentError(null);
            }
        }
        
        if(preset == null) {
            this.preset.setComponentError(new UserError(utils.getMessage("usererror.required")));
            valid = false;
        } else {
            this.preset.setComponentError(null);
        }
        
        if(cellPhone.isEmpty()) {
            this.cellPhone.setComponentError(new UserError(utils.getMessage("usererror.required")));
            valid = false;
        } else {
            if(!cellPhone.matches("[0-9]{4,10}")) {
                this.cellPhone.setComponentError(new UserError(utils.getMessage("usererror.match.cellphone")));
                valid = false;
            } else {
                this.cellPhone.setComponentError(null);
            }
        }
        if(instruments.isEmpty()) {
            this.instruments.setComponentError(new UserError(utils.getMessage("usererror.instrumentrequired")));
            valid = false;
        } else {
            this.instruments.setComponentError(null);
        }
        
        
        return valid;
    }
    
}
