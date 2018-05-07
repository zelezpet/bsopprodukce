/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.ui.component;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.server.UserError;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Link;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import ml.bma.bsop.backend.data.CastState;
import ml.bma.bsop.backend.data.entity.Casting;
import ml.bma.bsop.backend.data.entity.Performance;
import ml.bma.bsop.backend.data.entity.Performer;
import ml.bma.bsop.backend.service.CastingService;
import ml.bma.bsop.ui.utils.MyPdfSource;
import ml.bma.bsop.ui.utils.Utils;

/**
 *
 * @author ironman
 */
public class ContractWindow extends Window {
    
    public static final String CONTRACT_SRC = "static/smlouva.pdf";
    public static final String CONTRACT_DEST = "smlouva%d.pdf";
    
    private final Utils utils;
    private final CastingService service;    
    
    private final GridLayout grid;
    private final ListSelect<Casting> castings;
    private final TextField feeText;
    private final ComboBox<String> currency;
    private final Button create;
    
    private boolean large = false;
    private int count = 0;

    public ContractWindow(Utils utils, CastingService service, List<Casting> castList) {
        super(utils.getMessage("window.contract"));
        this.utils = utils;
        this.service = service;
        
        this.castings = new ListSelect<>(utils.getMessage("listselect.casting"));
        this.castings.setSizeFull();
        this.castings.setResponsive(true);
        initListSelect(castList);
        
        this.feeText = new TextField(utils.getMessage("textfield.feetext"));
        this.feeText.setWidth(80f, Unit.PIXELS);
        
        this.currency = new ComboBox<>(utils.getMessage("combobox.currency"));
        this.currency.setItems("CZK","EUR");
        this.currency.setEmptySelectionAllowed(false);
        this.currency.setWidth(100f, Unit.PIXELS);
        
        this.create = new Button(utils.getMessage("button.create"));
        this.create.addClickListener(this::create);
        this.create.setStyleName(ValoTheme.BUTTON_PRIMARY);
        
        this.grid = new GridLayout(2, 4);
        this.grid.addLayoutClickListener(event -> {
           Component tmp = event.getClickedComponent();
           if(tmp != null) return;
           if(large) {
               large = false;
               castings.setSizeFull();
           } else {
               large = true;
               castings.setSizeUndefined();
           }
        });
        this.grid.setMargin(true);
        this.grid.setSpacing(true);
        this.grid.addComponent(feeText, 0, 0);
        this.grid.addComponent(currency, 1, 0);
        this.grid.addComponent(castings, 0, 1, 1, 1);
        this.grid.addComponent(create, 0, 2, 1, 2);
        this.grid.setComponentAlignment(create, Alignment.MIDDLE_CENTER);
        this.grid.setResponsive(true);
        super.setContent(this.grid);
    }
    
    private void create(ClickEvent event) {
        Set<Casting> castSet = castings.getValue();
        boolean again = false;
        if(castSet.isEmpty()) {
            BSOPNotification notify = new BSOPNotification(this.utils.getMessage("notification.emptysendlist"));
            notify.show();
            again = true;
        }
        if(feeText.isEmpty()) {
            this.feeText.setComponentError(new UserError(this.utils.getMessage("usererror.required")));
            again = true;
        } else {
            this.feeText.setComponentError(null);
        }
        if(currency.isEmpty()) {
            this.currency.setComponentError(new UserError(this.utils.getMessage("usererror.required")));
            again = true;
        } else {
            this.currency.setComponentError(null);
        }
        if(again) return;
        
        this.grid.removeComponent(0, 3);
        VerticalLayout links = new VerticalLayout();
        links.setMargin(false);
        links.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        for(Casting casting: castSet) {
            this.count++;
            String dest = String.format(CONTRACT_DEST, this.count);
            ByteArrayOutputStream baos = editContract(casting, dest);
            StreamSource ss = new MyPdfSource(baos.toByteArray());
            Resource res = new StreamResource(ss, dest);
            Link pdf = new Link(casting.toString(), res);
            pdf.setTargetName("_blank");
            pdf.setTargetBorder(BorderStyle.DEFAULT);
            links.addComponent(pdf);
            
        }
        this.grid.addComponent(links, 0, 3, 1, 3);
    }
    
    private ByteArrayOutputStream editContract(Casting casting, String dest) {
        Performer performer = casting.getAbility().getPerformer();
        Performance per = casting.getPerformance();
        String fullname = performer.getFirstname() + " " + performer.getSurname();
        String id = performer.getOp() != null ? "OP " + performer.getOp(): "Pas " + performer.getPas();
        String address = performer.getAddress().allToString();
        String bankNum = performer.getBank().toString();
        String phone = performer.getCellPhone().toString();
        String performance = per.getDateTime().format(DateTimeFormatter.ofPattern("dd.MM. YYYY")).toString() + " " + per.getAddress().toString();
        String fee = this.feeText.getValue() + ",- " + this.currency.getValue() + " za výkon";
        String placeTime = per.getAddress().getCity() + " dne " + per.getDateTime().format(DateTimeFormatter.ofPattern("dd.MM. YYYY"));
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            BaseFont fontBold = BaseFont.createFont(BaseFont.TIMES_BOLD, BaseFont.CP1250, BaseFont.EMBEDDED);
            BaseFont fontNormal = BaseFont.createFont(BaseFont.TIMES_ROMAN, BaseFont.CP1250, BaseFont.EMBEDDED);
            PdfReader reader = new PdfReader(CONTRACT_SRC);            
            
            PdfStamper stamper = new PdfStamper(reader, baos);
            PdfContentByte over = stamper.getOverContent(1);               

            over.beginText();
            over.setFontAndSize(fontBold, 10f);
            over.setTextMatrix(150f, 690f);
            over.showText(fullname);
            over.setFontAndSize(fontNormal, 10f);
            over.setTextMatrix(150f,673);
            over.showText(id);
            over.setTextMatrix(150f, 656);
            over.showText(address);
            over.setTextMatrix(150f, 639f);
            over.showText(bankNum);
            over.setFontAndSize(fontBold, 10f);
            over.setTextMatrix(150f, 621f);
            over.showText(phone);
            over.setTextMatrix(262f, 420f);
            over.showText(performance);
            over.endText();

            over = stamper.getOverContent(2);
            over.beginText();
            over.setFontAndSize(fontBold, 10f);
            over.setTextMatrix(107f, 760f);
            over.showText(fee);
            over.setTextMatrix(37f, 260f);
            over.showText(placeTime);
            over.endText();

            stamper.close();
            
        } catch (DocumentException ex) {
            Logger.getLogger(ContractWindow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ContractWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        return baos;
    }
    
    private void initListSelect(List<Casting> castList) {
        List<Casting> tmp = new ArrayList<>();
        for(Casting casting: castList) {
            if(casting.getCastState() == CastState.CONFIRMED) {
                tmp.add(casting);
            }
        }
        
        this.castings.setItems(tmp);
    }
    
    
}
