/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.ui.component;

import com.jarektoro.responsivelayout.ResponsiveLayout;
import com.jarektoro.responsivelayout.ResponsiveRow;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.UserError;
import com.vaadin.shared.ui.BorderStyle;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.ComboBox.CaptionFilter;
import com.vaadin.ui.Link;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.util.Locale;
import ml.bma.bsop.backend.data.Bank;
import ml.bma.bsop.backend.data.entity.BankAccount;
import ml.bma.bsop.ui.utils.Utils;
import org.vaadin.spring.i18n.support.Translatable;

/**
 *
 * @author ironman
 */
public class BankPanel extends Panel implements Translatable{
    
    private final Utils utils;
    
    private final TextField prefix;
    
    private final TextField accountNumber;
    
    private final TextField bankCode;
    
    private final ComboBox bankName;
    
    private final TextField iban;
    
    private final Link link;
    
    private final ExternalResource calc = new ExternalResource("https://www.ibancalculator.com/bic_und_iban.html");
    
    
    public BankPanel(Utils utils) {
        
        this.utils = utils;
        
        this.prefix = new TextField(utils.getMessage("textfield.prefix"));
        this.prefix.setMaxLength(6);
        this.prefix.setWidth(75, Unit.PIXELS);
        
        this.accountNumber = new TextField(utils.getMessage("textfield.accountnumber"));
        this.accountNumber.setMaxLength(10);
        this.accountNumber.setWidth(110, Unit.PIXELS);
        
        this.bankCode = new TextField(utils.getMessage("textfield.bankcode"));
        this.bankCode.setEnabled(false);
        this.bankCode.setWidth(60, Unit.PIXELS);

        this.bankName = new ComboBox(utils.getMessage("combobox.bankname"));
        this.bankName.setItems(new CaptionFilter(){
            @Override
            public boolean test(String itemCaption, String filterText) {
                return itemCaption.toLowerCase().contains(filterText.toLowerCase());
            }
        }, Bank.getBankNames());
        this.bankName.addValueChangeListener(evt -> {
            String value = (String) evt.getValue();
            Bank bank = Bank.KB;
            if(value != null) {
                bank = Bank.getBank(value);
            }
            if(bank != null) {
                bankCode.setValue(bank.getCode());
            }
        });
        this.bankName.setScrollToSelectedItem(true);
        this.bankName.setEmptySelectionAllowed(false);
        this.bankName.setWidth(240, Unit.PIXELS);
        
        this.link = new Link(utils.getMessage("link.ibancalc"), calc);
        this.link.setCaptionAsHtml(true);
        this.link.setIcon(VaadinIcons.CALC_BOOK);
        link.setTargetName("_blank");
        link.setTargetBorder(BorderStyle.DEFAULT);
        
        this.iban = new TextField(utils.getMessage("textfield.iban"));
        this.iban.setWidth(240, Unit.PIXELS);
        this.iban.setMaxLength(31);
        
        ResponsiveLayout responsive = new ResponsiveLayout();
        ResponsiveRow row = responsive.addRow();
        row.setMargin(true);
        row.setSpacing(true);
        VerticalLayout lay1 = new VerticalLayout(prefix);
        lay1.setMargin(false);
        VerticalLayout lay2 = new VerticalLayout(accountNumber);
        lay2.setMargin(false);
        VerticalLayout lay3 = new VerticalLayout(bankName);
        lay3.setMargin(false);
        VerticalLayout lay4 = new VerticalLayout(bankCode);
        lay4.setMargin(false);        
        VerticalLayout lay5 = new VerticalLayout(iban,link);
        lay5.setMargin(false);
        row.addColumn().withComponent(lay1);
        row.addColumn().withComponent(lay2);
        row.addColumn().withComponent(lay3);
        row.addColumn().withComponent(lay4);
        row.addColumn().withComponent(lay5);
        
        super.setContent(responsive);
        super.setCaption(utils.getMessage("bank.name"));
        super.addStyleName(ValoTheme.PANEL_WELL);
        
    }

    @Override
    public void updateMessageStrings(Locale locale) {
        super.setCaption(utils.getMessage("bank.name"));
        this.prefix.setCaption(utils.getMessage("textfield.prefix", locale));
        this.prefix.setLocale(locale);
        this.accountNumber.setCaption(utils.getMessage("textfield.accountnumber", locale));
        this.accountNumber.setLocale(locale);
        this.bankCode.setCaption(utils.getMessage("textfield.bankcode", locale));
        this.bankCode.setLocale(locale);
        this.bankName.setCaption(utils.getMessage("combobox.bankname", locale));
        this.bankName.setLocale(locale);
        this.iban.setCaption(utils.getMessage("textfield.iban", locale));
        this.iban.setLocale(locale);
        this.link.setCaption(utils.getMessage("link.ibancalc", locale));
    }

    public String getPrefix() {
        String prefix = this.prefix.getValue();
        if(prefix.isEmpty()) {
            return null;
        }
        return prefix;
    }

    public String getAccountNumber() {
        String accountNumber = this.accountNumber.getValue();
        if(accountNumber.isEmpty()) {
            return null;
        }
        return accountNumber;
    }

    public String getBankCode() {
        String bankCode = this.bankCode.getValue();
        if(bankCode.isEmpty()) {
            return null;
        }
        return bankCode;
    }

    public String getBankName() {
        String bankName = (String) this.bankName.getValue();        
        return bankName;
    }
    
    public Bank getBank() {
        String bankName = (String) this.bankName.getValue();
        return Bank.getBank(bankName);
    }

    public String getIban() {
        String iban = this.iban.getValue();
        if(iban.isEmpty()) {
            return null;
        }
        return iban;
    }
    
    public BankAccount getBankAccount() {
        return new BankAccount(getPrefix(), getAccountNumber(), Bank.getBank(getBankName()), getIban());
    }
    
    public void init(BankAccount bankAccount) {
        String iban = bankAccount.getIban();
        Bank bank = bankAccount.getBank();
        if(bank != null) {
            this.prefix.setValue(bankAccount.getPre() == null ? "000000" : bankAccount.getPre());
            this.accountNumber.setValue(bankAccount.getAccountNum());            
            this.bankName.setValue(bank.getName());
            this.bankCode.setValue(bank.getCode());
        }
        if(iban != null) {
            this.iban.setValue(bankAccount.getIban());            
        }
    }
    
    public boolean validate() {
        String prefix = this.prefix.getValue();
        String accountNumber = this.accountNumber.getValue();
        String bankName = (String) this.bankName.getValue();
        String iban = this.iban.getValue();
        
        boolean valid = true;        
        
        if(prefix.isEmpty() && iban.isEmpty()) {
            this.prefix.setComponentError(new UserError(utils.getMessage("usererror.accountoribannumberrequired")));
            valid = false;
        } else {
            if(!prefix.isEmpty() && !prefix.matches("[0-9]{6}")) {
                this.prefix.setComponentError(new UserError(utils.getMessage("usererror.match.prefix")));
                valid = false;
            } else {
                this.prefix.setComponentError(null);
            }
        }
        
        if(accountNumber.isEmpty() && iban.isEmpty()) {
            this.accountNumber.setComponentError(new UserError(utils.getMessage("usererror.accountoribannumberrequired")));
            valid = false;
        } else {
            if(!accountNumber.isEmpty() && !accountNumber.matches("[0-9]{10}")) {
                this.accountNumber.setComponentError(new UserError(utils.getMessage("usererror.match.accountnumber")));
                valid = false;
            } else {
                this.accountNumber.setComponentError(null);
            }
        }
        
        if((bankName == null || bankName.isEmpty()) && iban.isEmpty()) {
            this.bankName.setComponentError(new UserError(utils.getMessage("usererror.accountoribannumberrequired")));
            valid = false;
        } else {
            this.bankName.setComponentError(null);
        }
        
        if(iban.isEmpty() && (accountNumber.isEmpty() || bankName == null || bankName.isEmpty())) {
            this.iban.setComponentError(new UserError(utils.getMessage("usererror.accountoribannumberrequired")));
            valid = false;
        } else {
            if(!iban.isEmpty() && !iban.matches("[a-zA-Z]{2}[0-9]{2}[a-zA-Z0-9]{4}[0-9]{7}[a-zA-Z0-9]{0,16}")) {
                this.iban.setComponentError(new UserError(utils.getMessage("usererror.match.iban")));
                valid = false;
            } else {
                this.iban.setComponentError(null);
            }
        }
        
        
        return valid;
    }

    @Override
    public void setEnabled(boolean b) {
        this.prefix.setEnabled(b);
        this.accountNumber.setEnabled(b);
        this.bankName.setEnabled(b);
        this.bankCode.setEnabled(b);
        this.iban.setEnabled(b);
    }
    
}
