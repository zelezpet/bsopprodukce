/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.ui.utils;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

/**
 *
 * @author ironman
 */


public class CzechComparator<Object> implements Comparator<Object> {
    
    private final Collator collator = Collator.getInstance(new Locale("cs","CZ"));

    @Override
    public int compare(Object o1, Object o2) {
        return collator.compare(o1, o2);
    }
    
}
