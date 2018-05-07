/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.ui.view;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import java.util.Locale;
import ml.bma.bsop.backend.data.Authority;
import ml.bma.bsop.backend.data.entity.Account;
import ml.bma.bsop.backend.service.PerformerService;
import ml.bma.bsop.security.SecurityUtils;
import ml.bma.bsop.ui.utils.Utils;
import ml.bma.bsop.ui.view.form.PerformerForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.i18n.support.Translatable;

/**
 *
 * @author ironman
 */

@UIScope
@SpringView(name = PerformerView.NAME)
@Theme("bsop")
public class PerformerView extends AbstractView implements Translatable{
    
    public static final String NAME = "performer";
    
    private final Utils utils;
    
    private final PerformerService performerService;
    
    private final PerformerForm form;
    
    @Autowired
    public PerformerView(Utils utils, PerformerService performerService, PerformerForm form) {
        this.utils = utils;
        this.performerService = performerService;
        this.form = form;
        
        super.title.setCaption(utils.getMessage("view."+NAME));
        super.content.addComponent(form);
        super.content.setComponentAlignment(form, Alignment.MIDDLE_CENTER);
    }
    

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        if(SecurityUtils.hasRole(Authority.ROLE_USER)) {
            this.form.init(null);
        } else if(SecurityUtils.hasRole(Authority.ROLE_PERFORMER)) {
            Account current = this.utils.getCurrentAccount();
            this.form.init(performerService.findPerformer(current));
        }
        
    }


    @Override
    public String getViewName() {
        return NAME;
    }

    @Override
    public void updateMessageStrings(Locale locale) {
        super.title.setCaption(utils.getMessage("view."+NAME, locale));
    }
    
    
}
