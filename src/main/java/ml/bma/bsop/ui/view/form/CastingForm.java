/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.ui.view.form;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.renderers.TextRenderer;
import java.util.List;
import java.util.Locale;
import ml.bma.bsop.backend.data.entity.Ability;
import ml.bma.bsop.backend.data.entity.Casting;
import ml.bma.bsop.backend.data.entity.Performance;
import ml.bma.bsop.backend.service.CastingService;
import ml.bma.bsop.ui.utils.AbilityComparator;
import ml.bma.bsop.ui.utils.Utils;
import org.vaadin.crudui.form.impl.field.provider.ComboBoxProvider;
import org.vaadin.spring.i18n.support.Translatable;

/**
 *
 * @author ironman
 */
public class CastingForm extends AbstractForm<Casting, CastingService> implements Translatable{

    public CastingForm() {
        super(Casting.class);
    }
    
    @Override
    public void init(Utils utils, CastingService castingService) {
        super.init(utils, castingService);
        super.grid.setColumns("ability", "performance", "castState");
        super.grid.getColumn("ability").setRenderer( ability -> ((Ability) ability).toString(), new TextRenderer(""));
        super.grid.getColumn("performance").setRenderer( performance -> ((Performance) performance).toString(), new TextRenderer(""));
        super.crudFormFactory.setVisibleProperties("ability","performance","castState");
        
        List<Ability> abilities = castingService.findAllAbilities();
        abilities.sort(new AbilityComparator<>());
        super.crudFormFactory.setFieldProvider("ability", new ComboBoxProvider(utils.getMessage("combobox.ability"), abilities));
        super.crudFormFactory.setFieldCreationListener("ability", field -> {
            ((ComboBox) field).setEmptySelectionAllowed(false);
        });
        
        List<Performance> performances = castingService.findAllPerformances();
        performances.sort((Performance o1, Performance o2) -> o1.toString().compareTo(o2.toString()));
        super.crudFormFactory.setFieldProvider("performance", new ComboBoxProvider(utils.getMessage("combobox.performance"), performances));
        super.crudFormFactory.setFieldCreationListener("performance", field -> {
            ((ComboBox) field).setEmptySelectionAllowed(false);
        });
        
        super.crudFormFactory.setUseBeanValidation(true);
        updateMessageStrings(utils.getLocale());
    }

    @Override
    public void updateMessageStrings(Locale locale) {
        super.grid.getColumn("ability").setCaption(super.utils.getMessage("combobox.ability", locale));
        super.grid.getColumn("performance").setCaption(super.utils.getMessage("combobox.performance", locale));
        super.grid.getColumn("castState").setCaption(super.utils.getMessage("combobox.caststate", locale));
        super.formFactory.setFieldCaptions(
                super.utils.getMessage("combobox.ability", locale),
                super.utils.getMessage("combobox.performance", locale),
                super.utils.getMessage("combobox.caststate", locale)
        );
    }   
    
}
