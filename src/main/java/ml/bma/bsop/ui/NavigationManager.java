/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.ui;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.spring.internal.Conventions;
import com.vaadin.spring.navigator.SpringNavigator;
import ml.bma.bsop.ui.view.CalendarView;
import org.springframework.stereotype.Component;


/**
 *
 * @author ironman
 */
@Component
@UIScope
public class NavigationManager extends SpringNavigator {
    

    /**
     * Find the view id (URI fragment) used for a given view class.
     *
     * @param viewClass
     *            the view class to find the id for
     * @return the URI fragment for the view
     */
    public String getViewId(Class<? extends View> viewClass) {
            SpringView springView = viewClass.getAnnotation(SpringView.class);
            if (springView == null) {
                    throw new IllegalArgumentException("The target class must be a @SpringView");
            }

            return Conventions.deriveMappingForView(viewClass, springView);
    }

    /**
     * Navigate to the given view class.
     *
     * @param targetView
     *            the class of the target view, must be annotated using
     *            {@link SpringView @SpringView}
     */
    public void navigateTo(Class<? extends View> targetView) {
        String viewId = getViewId(targetView);
        navigateTo(viewId);
    }

    public void navigateTo(Class<? extends View> targetView, Object parameter) {
        String viewId = getViewId(targetView);
        navigateTo(viewId + "/" + parameter.toString());
    }

    public void navigateToDefaultView() {
        
        navigateTo(CalendarView.class);
    }

    /**
     * Update the parameter of the the current view without firing any
     * navigation events.
     *
     * @param parameter
     *            the new parameter to set, never <code>null</code>,
     *            <code>""</code> to not use any parameter
     */
    public void updateViewParameter(String parameter) {
        String viewName = getViewId(getCurrentView().getClass());
        String parameters;
        if (parameter == null) {
                parameters = "";
        } else {
                parameters = parameter;
        }

        updateNavigationState(new ViewChangeEvent(this, getCurrentView(), getCurrentView(), viewName, parameters));
    }
    
    

}