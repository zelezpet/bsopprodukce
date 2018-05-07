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
public class ErrorView extends VerticalLayout implements View {

    public final static String NAME = "error";

    private final Label errorLabel;

    public ErrorView() {
        super.setMargin(true);
        errorLabel = new Label();
        errorLabel.addStyleName(ValoTheme.LABEL_FAILURE);
        errorLabel.setSizeUndefined();
        super.addComponent(errorLabel);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        errorLabel.setValue(String.format("No such view: %s", event.getViewName()));
    }
}