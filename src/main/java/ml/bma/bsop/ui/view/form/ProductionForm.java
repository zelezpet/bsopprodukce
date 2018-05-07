/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.ui.view.form;

import com.jarektoro.responsivelayout.ResponsiveLayout;
import com.jarektoro.responsivelayout.ResponsiveRow;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.UserError;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import ml.bma.bsop.backend.data.entity.Composition;
import ml.bma.bsop.backend.data.entity.Performance;
import ml.bma.bsop.backend.data.entity.Picture;
import ml.bma.bsop.backend.data.entity.Production;
import ml.bma.bsop.backend.service.PictureService;
import ml.bma.bsop.backend.service.ProductionService;
import ml.bma.bsop.ui.component.BSOPNotification;
import ml.bma.bsop.ui.utils.ImageReciever;
import ml.bma.bsop.ui.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.i18n.support.Translatable;

/**
 *
 * @author ironman
 */

@SpringComponent
@UIScope
public class ProductionForm extends VerticalLayout implements Translatable {
    
    private final Utils utils;
    private final PictureService pictureService;
    private final ProductionService productionService;
    private final ImageReciever reciever;
    
    private final ComboBox<String> images;
    
    private final TextField title;
    
    private final TextArea desc;
    
    private final Panel picturePanel;
    
    private final Panel basicPanel;
    
    private final Image defaultImage;
    
    private final Upload upload;
    
    private final TwinColSelect<Composition> compositions;
    
    private final Button save;
    private final Button delete;
    
    private Production current;
    
    
    
    @Autowired
    public ProductionForm(Utils utils, PictureService pictureService, ProductionService productionService) {
        this.utils = utils;
        this.pictureService = pictureService;
        this.productionService = productionService;
        this.reciever = new ImageReciever();
        
        this.upload = new Upload(null,reciever);
        this.upload.setLocale(utils.getLocale());
        this.upload.setImmediateMode(true);
        this.upload.setButtonCaption(utils.getMessage("upload.image.button"));
        this.upload.setWidth(240, Unit.PIXELS);
        this.upload.addStartedListener(evt -> {
           String mime = evt.getMIMEType();
           if(!mime.contains("image")) {
               upload.interruptUpload();
               BSOPNotification notify = new BSOPNotification(utils.getMessage("notification.upload.notimage"));
               notify.show();
               return;
           }
           if(evt.getContentLength() > 5000000) {
               evt.getUpload().interruptUpload();
               BSOPNotification notify = new BSOPNotification(utils.getMessage("notification.upload.large"));
               notify.show();
               return;
           }
           upload.setEnabled(false);
        });
        this.upload.addSucceededListener(evt -> {
            upload.setEnabled(true);
            uploadSucceeded();
        });
        
        this.defaultImage = pictureService.getImage(Picture.DEFAULT);
        this.defaultImage.setWidth(237, Unit.PIXELS);
        this.defaultImage.setHeight(145, Unit.PIXELS);
        
        this.picturePanel = new Panel();
        this.picturePanel.setHeight(147, Unit.PIXELS);
        this.picturePanel.setWidth(239, Unit.PIXELS);
        this.picturePanel.setContent(defaultImage);        
        
        this.images = new ComboBox<>(utils.getMessage("combobox.images"));
        this.images.setItems((String itemCaption, String filterText) -> itemCaption.toLowerCase().contains(filterText.toLowerCase()), pictureService.getNameList());
        this.images.setScrollToSelectedItem(true);
        this.images.setWidth(240, Unit.PIXELS);
        this.images.setEmptySelectionAllowed(false);
        this.images.addValueChangeListener(evt -> {
            String name = (String) evt.getValue();
            if(name != null && !name.equals(evt.getOldValue())) {
                Image image = pictureService.getImage(name);
                image.setWidth(237, Unit.PIXELS);
                image.setHeight(145, Unit.PIXELS);
                picturePanel.setContent(image);
            }
        });
        this.images.setSelectedItem(Picture.DEFAULT);       
        
        this.title = new TextField(utils.getMessage("textfield.title"));
        this.title.setWidth(240, Unit.PIXELS);        
        
        this.desc = new TextArea(utils.getMessage("textarea.description"));
        this.desc.setMaxLength(255);
        this.desc.setWidth(240, Unit.PIXELS);
        this.desc.setHeight(175, Unit.PIXELS);
        this.desc.setWordWrap(true);
        
        this.compositions = new TwinColSelect<>(utils.getMessage("twincolselect.compositions"));
        this.compositions.setRows(5);
        this.compositions.setWidth(240, Unit.PIXELS);

        this.save = new Button(utils.getMessage("button.save"), (Button.ClickEvent event) -> save());
        this.save.addStyleName(ValoTheme.BUTTON_PRIMARY);
        this.save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        
        this.delete = new Button(utils.getMessage("button.delete"), (Button.ClickEvent event) -> delete());
        this.delete.addStyleName(ValoTheme.BUTTON_DANGER);
        
        ResponsiveLayout responsive = new ResponsiveLayout();
        ResponsiveRow row = responsive.addRow();
        row.setMargin(true);
        row.setSpacing(true);        
        
        VerticalLayout lay1 = new VerticalLayout(title, desc);
        lay1.setMargin(false);
        VerticalLayout lay2 = new VerticalLayout(images, picturePanel, upload);
        lay2.setMargin(false);
        VerticalLayout lay3 = new VerticalLayout(compositions);
        lay3.setMargin(false);
        VerticalLayout lay4 = new VerticalLayout(save, delete);
        lay4.setMargin(false);
        lay4.setSizeFull();
        lay4.setComponentAlignment(save, Alignment.BOTTOM_CENTER);
        
        
        row.addColumn().withComponent(lay1);
        row.addColumn().withComponent(lay2);
        row.addColumn().withComponent(lay3);
        row.addColumn().withComponent(lay4 );
        
        this.basicPanel = new Panel(responsive);
        this.basicPanel.setCaption(utils.getMessage("production.basics"));
        this.basicPanel.addStyleName(ValoTheme.PANEL_WELL);
        
        super.addComponent(basicPanel);
        super.setSizeUndefined();
        
    }
    
    public void init(Production production) {
        this.current = production;
        this.compositions.setItems(productionService.getCompositionsList());
        if(production != null) {
            Set<Composition> playlist = new HashSet<>(production.getCompositions());
            this.title.setValue(production.getName());
            this.desc.setValue(production.getDescription());
            String name = production.getPicture().getName();
            this.images.setSelectedItem(name);
            Image image = production.getImage();
            image.setWidth(237, Unit.PIXELS);
            image.setHeight(145, Unit.PIXELS);
            this.picturePanel.setContent(image);
            this.compositions.setValue(playlist);
        }
        updateMessageStrings(utils.getLocale());
        
    }
    

    @Override
    public void updateMessageStrings(Locale locale) {
        
        this.images.setCaption(utils.getMessage("combobox.images", locale));
        this.title.setCaption(utils.getMessage("textfield.title", locale));
        this.title.setLocale(locale);
        this.desc.setCaption(utils.getMessage("textarea.description", locale));
        this.desc.setLocale(locale);
        this.basicPanel.setCaption(utils.getMessage("production.basics", locale));
        this.upload.setButtonCaption(utils.getMessage("upload.image.button", locale));
        this.upload.setLocale(locale);
        this.compositions.setCaption(utils.getMessage("twincolselect.compositions",locale));
        this.compositions.setLocale(locale);
        this.save.setCaption(utils.getMessage("button.save", locale));
        this.delete.setCaption(utils.getMessage("button.delete", locale));
    }

    private void uploadSucceeded() {
        Picture picture = this.reciever.getPicture();
        if(picture != null) {
            picture = this.pictureService.save(picture);
            if(picture == null) {
                BSOPNotification notify = new BSOPNotification(utils.getMessage("notification.image.alreadyexists"));
                notify.show();
            } else {
                this.images.setItems(pictureService.getNameList());
                this.images.setSelectedItem(picture.getName());
                Image image = picture.getImage();
                image.setWidth(237, Unit.PIXELS);
                image.setHeight(145, Unit.PIXELS);
                this.picturePanel.setContent(image);
            }
        } else {
            BSOPNotification notify = new BSOPNotification(utils.getMessage("notification.upload.image.fail"));
            notify.show();
        }
    }
    
    private void save() {
        String title = this.title.getValue();
        String desc = this.desc.getValue();
        String pictureName = (String) this.images.getValue();
        List<Composition> compositions = new ArrayList<>(this.compositions.getValue());
        boolean valid = true;
        if(title.isEmpty()){
            this.title.setComponentError(new UserError(utils.getMessage("usererror.required")));
            valid = false;
        } else {
            if((current == null && productionService.getNameList().contains(title)) ||
                    (current != null && productionService.getNameList().contains(title) && !title.equalsIgnoreCase(current.getName()))) {
                
                this.title.setComponentError(new UserError(utils.getMessage("usererror.title.unique")));
                valid = false;
            } else {
                this.title.setComponentError(null);
            }
        }
        if(desc.isEmpty()) {
            this.desc.setComponentError(new UserError(utils.getMessage("usererror.required")));
            valid = false;
        } else {
            this.desc.setComponentError(null);
        }
        if(pictureName == null || pictureName.isEmpty()) {
            this.images.setComponentError(new UserError(utils.getMessage("usererror.required")));
            valid = false;
        } else {
            this.images.setComponentError(null);
        }
        if(compositions == null || compositions.isEmpty()) {
            this.compositions.setComponentError(new UserError(utils.getMessage("usererror.required")));
            valid = false;
        } else {
            this.compositions.setComponentError(null);
        }
        if(valid) {
            this.save.setComponentError(null);
            Production production;
            if(current != null) {
                if(title.equalsIgnoreCase(current.getName()) && 
                   desc.equalsIgnoreCase(current.getDescription()) &&
                   pictureName.equalsIgnoreCase(current.getPicture().getName()) &&
                   compositions.containsAll(current.getCompositions())) {
                    BSOPNotification notify = new BSOPNotification(utils.getMessage("notification.noupdate"));
                    notify.show();
                    return;
                }
                production = current;
                production.setName(title);
                production.setDescription(desc);
                production.setPicture(pictureService.getPicture(pictureName));
                production.setCompositions(compositions);
                
            } else {
                production = new Production(title, desc, pictureService.getPicture(pictureName), compositions);
            }
            VerticalLayout content = (VerticalLayout) getParent();
            HorizontalLayout tool = (HorizontalLayout) content.getComponent(1);
            ComboBox<String> productions = (ComboBox<String>) tool.getComponent(1);
            productionService.save(production);
            productions.setItems(productionService.getNameList());
            productions.setSelectedItem(title);
            
            BSOPNotification notify = new BSOPNotification(utils.getMessage("notification.save"));
            notify.show();
            
        } else {
            this.save.setComponentError(new UserError(utils.getMessage("usererror.required")));
        }
    }

    public void clear() {
        this.title.clear();
        this.title.setComponentError(null);
        this.desc.clear();
        this.desc.setComponentError(null);
        this.images.setSelectedItem(Picture.DEFAULT);
        this.images.setComponentError(null);
        this.picturePanel.setContent(defaultImage);
        this.compositions.clear();
        this.compositions.setComponentError(null);
        this.compositions.setItems(productionService.getCompositionsList());
        this.save.setComponentError(null);
    }

    private void delete() {
        if(current == null) {
            clear();
            BSOPNotification notify = new BSOPNotification(utils.getMessage("notification.newproduction"));
            notify.show();
        } else {
            List<Performance> performances = current.getPerformances();
            if(performances == null || performances.isEmpty()) {
                clear();
                this.productionService.delete(current);
                VerticalLayout content = (VerticalLayout) getParent();
                HorizontalLayout tool = (HorizontalLayout) content.getComponent(1);
                ComboBox<String> productions = (ComboBox<String>) tool.getComponent(1);
                productions.clear();
                productions.setItems(productionService.getNameList());
                BSOPNotification notify = new BSOPNotification(utils.getMessage("notification.delete.production"));
                notify.show();
                this.current = null;
            } else {
                BSOPNotification notify = new BSOPNotification(utils.getMessage("notification.delete.production.fail"));
                notify.show();
            }
        }
    }
    
    
}
