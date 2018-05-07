/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.ui.component;

import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;

/**
 *
 * @author ironman
 */
public class BSOPNotification extends Notification {
    
    public BSOPNotification(String caption) {
        super(caption, Type.TRAY_NOTIFICATION);
        super.setDelayMsec(4000);
        super.setPosition(Position.BOTTOM_RIGHT);
        super.setStyleName("dark notification small");
        
    }
    
    public BSOPNotification(String caption, String description) {
        super(caption, description, Type.TRAY_NOTIFICATION);
        super.setStyleName("tray dark small closable");
        super.setPosition(Position.MIDDLE_CENTER);
        super.setDelayMsec(20000);        
    }
    
    public void show() {
        super.show(Page.getCurrent());
    }
    
    
}
