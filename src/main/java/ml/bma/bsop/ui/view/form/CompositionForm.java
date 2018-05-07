/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.ui.view.form;

import java.util.Locale;
import ml.bma.bsop.backend.data.entity.Composition;
import ml.bma.bsop.backend.service.CompositionService;
import ml.bma.bsop.ui.utils.Utils;

/**
 *
 * @author ironman
 */
public class CompositionForm extends AbstractForm<Composition, CompositionService>{
    

    public CompositionForm() {
        super(Composition.class);
    }
    
    @Override
    public void init(Utils utils, CompositionService service) {
        super.init(utils, service);
        super.grid.setColumns("name", "author");
        /*Grid.Column<Composition, ListSelect<Instrument>> parts = super.grid.addComponentColumn((Composition source) -> {
            ListSelect<Instrument> list = new ListSelect<>();
            if(source != null) {
                list.setItems(source.getInstrumentList());
            }
            list.setReadOnly(true);            
            return list;
        })*/
        super.formFactory.setVisibleProperties("name", "author");
        
        updateMessageStrings(utils.getLocale());
    }

    @Override
    public void updateMessageStrings(Locale locale) {
        super.grid.getColumn("name").setCaption(super.utils.getMessage("textfield.compositionname", locale));
        super.grid.getColumn("author").setCaption(super.utils.getMessage("textfield.author", locale));
        //super.grid.getColumn("parts").setCaption(super.utils.getMessage("listbox.parts", locale));
        super.formFactory.setFieldCaptions(
                super.utils.getMessage("textfield.compositionname"),
                super.utils.getMessage("textfield.author")
                //super.utils.getMessage("listbox.parts")
        );
    }
    
}
