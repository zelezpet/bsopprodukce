/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.backend.data;

import java.io.Serializable;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import ml.bma.bsop.ui.utils.CzechComparator;

/**
 *
 * @author ironman
 */


public enum Country implements Serializable{
    
    BEL("Belgium","Belgie","+32"), GRC("Greece","Řecko","+30"), LTU("Lithuania","Litva","+370"),
    PRT("Portugal","Portugalsko","+351"), BGR("Bulgaria","Bulharsko","+359"),
    ESP("Spain","Španělsko","+34"), LUX("Luxembourg","Lucembursko","+352"), ROU("Romania","Rumunsko","+40"),
    CZE("Czech Republic","Česká republika","+420"), FRA("France","Francie","+33"), HUN("Hungary","Maďarsko","+36"),
    SVN("Slovenia","Slovinsko","+386"), DNK("Denmark","Dánsko","+45"), HRV("Croatia","Chorvatsko","+385"),
    MLT("Malta","Malta","+356"), SVK("Slovakia","Slovensko","+421"), DEU("Germany","Německo","+49"),
    ITA("Italy","Itálie","+39"), NLD("Netherlands","Nizozemsko","+31"), FIN("Finland","Finsko","+358"),
    EST("Estonia","Estonsko","+372"), CYP("Cyprus","Kypr","+357"), AUT("Austria","Rakousko","+43"),
    SWE("Sweden","Švédsko","+46"), IRL("Ireland","Irsko","+353"), LVA("Latvia","Lotyšsko","+371"),
    POL("Poland","Polsko","+48"), GBR("United Kingdom","Spojené království","+44");
    
    private static final Map<String, Country> statesEN = new HashMap<>();
    private static final Map<String, Country> statesCZ = new HashMap<>();
    
    private final String nameEN;
    private final String nameCZ;
    private final String preset;
    
    static {
        Country[] values = Country.values();
        for(Country state: values) {
            statesEN.put(state.nameEN, state);
            statesCZ.put(state.nameCZ, state);
        }
    }    
    
    
    Country(String nameEN, String nameCZ, String preset) {
        this.nameEN = nameEN;
        this.nameCZ = nameCZ;
        this.preset = preset;
    }

    public String getNameEN() {
        return this.nameEN;
    }
    
    public String getNameCZ() {
        return nameCZ;
    }

    public String getPreset() {
        return preset;
    }
    
    public static Country getCountry(String name) {
        Country state = statesEN.get(name);
        if(state == null) {
            state = statesCZ.get(name);
        }
        return state;
    }
    
    public static Collection<String> getCountriesEN() {
        List<String> list = new ArrayList<>(statesEN.keySet());
        Collections.sort(list) ;
        return list;
    }

    public static Collection<String> getCountriesCZ() {
        List<String> list = new ArrayList<>(statesCZ.keySet());
        Collections.sort(list, new CzechComparator());
        return list;
    }
   
    public static List<Country> getSortValuesByNameCZ() {
        List<Country> countries = Arrays.asList(Country.values());
        countries.sort(new Comparator<Country>() {
            
            private final Collator collator = Collator.getInstance(new Locale("cs","CZ"));
            
            @Override
            public int compare(Country o1, Country o2) {
                return collator.compare(o1.getNameCZ(), o2.getNameCZ());
            }
            
        });
        return countries;
    }
    
    public static List<Country> getSortValuesByNameEN() {
        List<Country> countries = Arrays.asList(Country.values());
        countries.sort((Country o1, Country o2) -> o1.getNameEN().compareTo(o2.getNameEN()));
        return countries;
    }
}
