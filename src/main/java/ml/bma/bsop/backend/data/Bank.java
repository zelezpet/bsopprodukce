/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.backend.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ml.bma.bsop.ui.utils.CzechComparator;


/**
 *
 * @author ironman
 */

public enum Bank implements Serializable {
    KB("0100","Komerční banka, a.s."), COB("0300","Československá obchodní banka, a.s."),
    MMB("0600","MONETA Money Bank, a.s."), CNB("0710","Česká národní banka"),
    CS("0800","Česká spořitelna, a.s."), FIO("2010","FIO banka, a.s."),
    BTM("2020","Bank of Tokyo-Mitsubishi UFJ (Holland) N. V. Pratur Branch, organizační složka"),
    CUD("2030","Československé úvěrní družstvo"), CIT("2060","Citfin, spořitelní družstvo"),
    MPU("2070","Moravský Peněžní Ústav - spořitelní družstvo"), HYP("2100","Hypoteční banka, a.s."),
    PED("2200","Peněžní dům, spořitelní družstvo"), ART("2220","Artesa, spořitelní družstvo"),
    POB("2240","Poštová banka, a.s., pobočka Česká republika"),
    ZCD("2250","Záložna CREDITAS, spořitelní družstvo"),
    NEY("2260","NEY spořitelní družstvo"), CBE("2600","Citibank Europe plc"),
    UNI("2700","UniCredit Bank Czech Republic and Slovakia, a.s."),
    AIR("3030","Air Bank a.s."), BNP("3050","BNP Paribas Personal Finance SA, odštěpný závod"),
    PKO("3060","PKO BP S.A., Czech Branch"), ING("3500","ING Bank N.V."),
    EXP("4000","Expobank CZ, a.s."),
    CZR("4300","Českomoravská záruční a rozvojová banka, a.s."),
    RFB("5500","Raiffeisenbank, a.s."), JTB("5800","J & T Banka, a.s."),
    PPF("6000","PPF banka, a.s."), EQB("6100","Equa bank a.s."),
    COM("6200","COMMERZBANK AG, pobočka Praha"),
    MBK("6210","mBank S.A., organizační složka"),
    BCZ("6300","BNP Paribas S.A., pobočka Česká republika"),
    VUB("6700","Všeobecná úverová banka, a.s., pobočka Praha"),
    SB("6800","Sberbank CZ, a.s."), DEB("7910","Deutsche Bank A.G., Filiale Prag"),
    WSB("7940","Waldviertler Sparkasse Bank AG"),
    RSS("7950","Raiffeisen stavební spořitelna, a.s."),
    CSS("7960","Českomoravská stavební spořitelna, a.s."),
    WSS("7970","Wustenrot stavební spořitena, a.s."),
    WHB("7980","Wustenrot hypoteční banka, a.s., se sídlem v Praze"),
    MSS("7990","Modrá pyramida stavební spořitelna, a.s."),
    VRN("8030","Volksbank Raiffeisenbank Nordoberpfalz eG pobočka Cheb"),
    OBK("8040","Oberbank AG pobočka Česká republika"),
    SSCS("8060","Stavební spořitelna České spořitelny, a.s."),
    CEB("8090","Česká exportní banka, a.s."), HSB("8150","HSBC Bank plc - pobočka Praha"),
    ICB("8265","Industrial and Commercial Bank of China Limited Prague Branch, odštěpný závod");
    
    
    
    private final String code;
    
    private final String name;
    
    private static final Map<String, Bank> bankNames = new HashMap<>();
    
    static {
        Bank[] values = Bank.values();
        for (Bank value : values) {            
            bankNames.put(value.name,value);
        }
    }
    
    
    Bank(String code, String name) {
        this.code = code;
        this.name = name;
    }   

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
    
    public static Bank getBank(String bankName) {
        return bankNames.get(bankName);
    }
    
    public static Collection<String> getBankNames() {
        List<String> list = new ArrayList<>(bankNames.keySet());
        Collections.sort(list,new CzechComparator());
        return list;
    }
    
}
