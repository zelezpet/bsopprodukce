/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.ui.view;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import ml.bma.bsop.backend.data.entity.AbstractEntity;
import ml.bma.bsop.backend.service.CrudService;
import ml.bma.bsop.ui.utils.Utils;
import ml.bma.bsop.ui.view.form.AbstractForm;
import org.vaadin.spring.i18n.support.Translatable;


/**
 *
 * @author ironman
 * @param <T>
 * @param <V>
 * @param <U>
 */


abstract public class AbstractCrudView<T extends AbstractEntity, V extends CrudService<T>, U extends AbstractForm<T,V>> extends AbstractView implements Translatable {
        
    protected final Utils utils;
    
    protected final V service;
    
    protected U form;
    private final Class<? extends AbstractForm<T,V>> formClass;
    private final Class<T> entityClass;
    private final boolean toolable;
    
    private TextField filterText;
    private Button reset;
    protected final VerticalLayout toolBar;
    
    public AbstractCrudView(Utils utils, V service, Class<T> entityClass, Class<U> formClass, boolean toolable) {
        
        this.utils = utils;
        this.service = service;
        this.formClass = formClass;
        this.entityClass = entityClass;        
        this.toolable = toolable;
        this.filterText = new TextField();
        this.filterText.setPlaceholder("filter");
        this.filterText.addValueChangeListener(evt -> updateList());
        this.filterText.setValueChangeMode(ValueChangeMode.LAZY);
        this.filterText.setIcon(VaadinIcons.SEARCH);
        this.filterText.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        this.reset = new Button(VaadinIcons.CLOSE_SMALL);
        this.reset.addClickListener(evt -> filterText.clear());
        this.reset.addStyleNames(ValoTheme.BUTTON_ICON_ONLY, ValoTheme.BUTTON_BORDERLESS);
        this.reset.setDescription(utils.getMessage("button.resetfilter.desc"));
        
        HorizontalLayout filterTool = new HorizontalLayout(filterText, reset);
        filterTool.setSpacing(false);
        filterTool.setMargin(false);
        this.toolBar =  new VerticalLayout(filterTool);
        this.toolBar.setMargin(false);
        this.form = null;
        try {
            this.form = (U) this.formClass.newInstance();
            this.form.init(utils, service);
            this.form.setUpdateOperation(service::save);
            this.form.setAddOperation(service::save);
            this.form.setDeleteOperation(service::delete);
            this.form.setFindAllOperation(this::update);
            if(toolable) {
                super.content.addComponent(toolBar);
            }
            super.content.addComponent(form);
            super.setFullContent();
        } catch (InstantiationException ex) {
            Logger.getLogger(AbstractCrudView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(AbstractCrudView.class.getName()).log(Level.SEVERE, null, ex);
        }       
    }
    
    protected List<T> update() {
        String filterText = this.filterText.getValue();
        Optional<String> filter = filterText.isEmpty()? Optional.empty(): Optional.of(filterText);
        List<T> list;
        if(toolable) {
            list = limitList(filter);            
        } else {            
            list = service.findAnyMatching(filter);
        }
        this.form.setRowCountMessage(list.size());
        return list;
    }
    
    protected void updateList() {
        Grid<T> grid = this.form.getGrid();
        grid.setItems(update());
    }
    
    @Override
    abstract public void enter(ViewChangeListener.ViewChangeEvent event);

    

    @Override
    abstract public String getViewName();
    
    
    public void updateMessageString(Locale locale) {
        super.title.setCaption(utils.getMessage("view."+getViewName(), locale));
        
    }
    
    abstract public List<T> limitList(Optional<String> filter);
    
}
