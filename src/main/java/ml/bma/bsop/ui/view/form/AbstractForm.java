/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.ui.view.form;

import java.util.Locale;
import ml.bma.bsop.backend.data.entity.AbstractEntity;
import ml.bma.bsop.backend.service.CrudService;
import ml.bma.bsop.ui.utils.Utils;
import org.vaadin.crudui.crud.CrudOperation;
import org.vaadin.crudui.crud.impl.GridCrud;
import org.vaadin.crudui.form.AbstractAutoGeneratedCrudFormFactory;
import org.vaadin.crudui.layout.impl.WindowBasedCrudLayout;
import org.vaadin.spring.i18n.support.Translatable;

/**
 *
 * @author ironman
 * @param <T>
 * @param <U>
 */


public abstract class AbstractForm<T extends AbstractEntity, U extends CrudService> extends GridCrud<T> implements Translatable{
    
    protected Utils utils;
    protected U service;
    
    protected final WindowBasedCrudLayout formLayout;
    protected final AbstractAutoGeneratedCrudFormFactory formFactory;
    
    public AbstractForm(Class<T> domainType) {
        super(domainType);
        this.formLayout = (WindowBasedCrudLayout) super.crudLayout;
        this.formFactory = (AbstractAutoGeneratedCrudFormFactory) super.crudFormFactory;
        this.formFactory.setUseBeanValidation(true);
    }
    
    public void init(Utils utils, U service) {
        this.utils = utils;
        this.service = service;
        updateMessageString(utils.getLocale());        
        
    }
    
    public void updateMessageString(Locale locale) {
        super.findAllButton.setDescription(utils.getMessage("button.refreshlist.desc", locale));
        super.addButton.setDescription(utils.getMessage("button.add", locale));
        super.deleteButton.setDescription(utils.getMessage("button.delete", locale));
        super.updateButton.setDescription(utils.getMessage("button.update", locale));
        super.savedMessage = utils.getMessage("notification.save", locale);
        super.deletedMessage = utils.getMessage("notification.delete", locale);
        this.formFactory.setButtonCaption(CrudOperation.ADD, utils.getMessage("button.add", locale));
        this.formFactory.setButtonCaption(CrudOperation.UPDATE, utils.getMessage("button.update", locale));
        this.formFactory.setButtonCaption(CrudOperation.DELETE, utils.getMessage("button.delete", locale));
        this.formFactory.setCancelButtonCaption(utils.getMessage("button.cancel", locale));
        this.formLayout.setWindowCaption(CrudOperation.ADD, utils.getMessage("window.add", locale));
        this.formLayout.setWindowCaption(CrudOperation.UPDATE, utils.getMessage("window.update", locale));
        this.formLayout.setWindowCaption(CrudOperation.DELETE, utils.getMessage("window.delete", locale));
    }
    
    public void setRowCountMessage(int count) {
        super.rowCountCaption = count + " " + utils.getMessage("rowcountcaption", utils.getLocale());
    }
    
}