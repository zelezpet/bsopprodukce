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
public enum CastState implements Serializable {
    
    INVITED, LOGGED, CONFIRMED;
    
    public static CastState getCastStateLike(String castStateLike) {
        CastState[] states = CastState.values();
        for(CastState state: states) {
            if(state.name().toLowerCase().contains(castStateLike.toLowerCase())) {
                return state;
            }
        }
        return null;
    }
    
}
