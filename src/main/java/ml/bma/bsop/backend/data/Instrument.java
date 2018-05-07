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
import java.util.Map;

/**
 *
 * @author ironman
 */

public enum Instrument implements Serializable {
    
    VIOLIN(Type.STRINGS), VIOLA(Type.STRINGS), CELLO(Type.STRINGS), BASS(Type.STRINGS), HARPE(Type.STRINGS),
    
    TRUMPET(Type.BRASS), TROMBONE(Type.BRASS), BARITONE(Type.BRASS), TENOR(Type.BRASS),
    HORN(Type.BRASS), TUBA(Type.BRASS),
    
    FLUTE(Type.WOODS), OBOE(Type.WOODS), ENG_HORN(Type.WOODS), CLARINET(Type.WOODS),
    FAGOT(Type.WOODS), SAXOFON(Type.WOODS),
    
    TIMPANES(Type.DRUMS), PERCUSSION(Type.DRUMS), DRUMS(Type.DRUMS),
    
    PIANO(Type.KEYS), CEMBALO(Type.KEYS),
    
    CONDUCTOR(Type.CONDUCT);
    
    private static final Map<Instrument, String> instruments = new HashMap<>();
    
    static {
        Instrument[] values = Instrument.values();
        for(Instrument value: values) {
            instruments.put(value, value.toString());
        }
    }
            
    private final Type type;
    
    public enum Type {        
        STRINGS, BRASS, WOODS, DRUMS, KEYS, CONDUCT;
    }
    
    
    
    Instrument(Type type) {
        this.type = type;
    }
    
    public boolean isType(Type type) {
        return this.type == type;
    }
    
    public static Collection<Instrument> getCollection() {
        ArrayList<Instrument> list = new ArrayList<>(instruments.keySet());
        Collections.sort(list, (Instrument o1, Instrument o2) -> o1.toString().compareTo(o2.toString()));
        return list;
    }
    
    public static Instrument getInstrumentLike(String instrumentLike) {
        Collection<String> instrs = Instrument.instruments.values();
        for (String instrument: instrs) {
            if(instrument.toLowerCase().contains(instrumentLike.toLowerCase())) {
                return Instrument.valueOf(instrument);
            }
        }
        
        return null;
    }
    
    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
    
}
