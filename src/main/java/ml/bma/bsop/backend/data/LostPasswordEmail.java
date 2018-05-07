/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.backend.data;

import java.util.Locale;
import ml.bma.bsop.backend.data.entity.Token;
import ml.bma.bsop.ui.BSOPProductionUI;

/**
 *
 * @author ironman
 */
public class LostPasswordEmail implements Email{
    
    private final Locale locale;
    
    private static final String SUBJECT_CS = "Obnovení hesla k aplikaci BSOP Produkce";
    
    private static final String SUBJECT_EN = "Restore account password for BSOP Production application";
    
    private static final String SUBJECT_DE = "Passwort für BSOP-Produktion Webanwendung wiederherstellen";
    
    private String link;
    
    private String message;
    
    public LostPasswordEmail(Locale locale, Token token) {
        this.locale = locale;
        this.link = BSOPProductionUI.URL + "?token=" + token.getToken();
    }

    @Override
    public String getMessage() {
        
        switch (locale.getLanguage()) {
            case "cs":
                message = "<h2>Dobrý den,</h2><br/><br/>" +
                        "<p>pro obnovení hesla prosím klikněte <a href='" + link + "'>sem</a>.</p><br/><br/>" +
                        "<p>S pozdravem</p><p>Aplikace <font color='blue'>BSOP Produkce</p>";
                break;
            case "en":
                message = "<h2>Hello,</h2><br/><br/>" +
                        "<p>To reset the password, please click <a href='" + link + "'>here</a>.</p><br/><br/>" +
                        "<p>Regards</p><p><font color='blue'>BSOP Production</font> application<p>";
                break;
            case "de":
                message = "<h2>Hallo,</h2><br/><br/>" +
                        "<p>Sie haben sich gerade bei BSOP Produktion registriert.</p>" +
                        "<p>Zurücksetzen des Passworts, klicken Sie bitte <a href='" + link + "'>hier</a>.</p><br/><br/>" +
                        "<p>Mit freundlichen Grüßen</p><p>Ihre <font color='blue'>BSOP Produktion</font> Webanwendung</p>";
                break;
            default: message = " Hi, click <a href='" + link + "'>here</a>.</p><br/><br/>";
                break;
        }
        return message;
    }
    
    @Override
    public String getSubject() {
        switch(locale.getLanguage()) {
            case "cs":
                return SUBJECT_CS;      
            case "de":
                return SUBJECT_DE;
            default:
                return SUBJECT_EN;
        }
    }  
    
    
}
