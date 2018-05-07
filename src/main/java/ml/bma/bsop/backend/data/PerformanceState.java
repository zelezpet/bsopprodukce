/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.backend.data;

import java.io.Serializable;

/**
 *
 * @author ironman
 */
public enum PerformanceState implements Serializable {
    
    CREATE, CAST, READY, END;
    
    public static PerformanceState getPerformanceStateLike(String stateLike) {
        PerformanceState[] states = PerformanceState.values();
        for(PerformanceState state: states) {
            if(state.name().toLowerCase().contains(stateLike)) {
                return state;
            }
        }
        
        return null;
    }
    
}
