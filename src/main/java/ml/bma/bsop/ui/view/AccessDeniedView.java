/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.ui.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 *
 * @author ironman
 */

@SpringComponent // No SpringView annotation because this view can not be navigated to
@UIScope
public class AccessDeniedView extends VerticalLayout implements View {

    public final static String NAME = "accessdenied";

    public AccessDeniedView() {
        super.setMargin(true);
        Label lbl = new Label("You don't have access to this view.");
        lbl.addStyleName(ValoTheme.LABEL_FAILURE);
        lbl.setSizeUndefined();
        super.addComponent(lbl);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }
}