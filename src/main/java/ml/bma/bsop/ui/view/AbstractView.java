/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.ui.view;

import com.jarektoro.responsivelayout.ResponsiveColumn;
import com.jarektoro.responsivelayout.ResponsiveLayout;
import com.jarektoro.responsivelayout.ResponsiveRow;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author ironman
 */

abstract class AbstractView extends ResponsiveLayout implements View {
    
    protected final Label title;
    
    protected final Panel panel;
    
    protected final VerticalLayout content;
    
    protected final ResponsiveColumn column;
    
    protected final ResponsiveRow row;
    
    public AbstractView() {
        
        HorizontalLayout top = new HorizontalLayout();        
        top.setMargin(false);
        this.title = new Label();
        this.title.setCaptionAsHtml(true);
        top.addComponent(title);
        top.setComponentAlignment(title, Alignment.MIDDLE_CENTER);        
        this.content = new VerticalLayout(top);
        this.content.setComponentAlignment(top, Alignment.TOP_CENTER);
        this.panel = new Panel(content);
        this.row = super.addRow();
        this.row.setMargin(true);
        this.row.setGrow(true);
        this.column = row.addColumn().withComponent(panel);
        this.column.setGrow(true);
        super.setScrollable(true);
    }
    
    public void setFullContent() {
        this.column.setSizeFull();
    }

    @Override
    abstract public void enter(ViewChangeListener.ViewChangeEvent event);
    

    abstract public String getViewName();
    
}
