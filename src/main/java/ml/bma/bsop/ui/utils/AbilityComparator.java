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
public class AbilityComparator<Ability> implements Comparator<Ability> {
        
        private final Collator collator = Collator.getInstance(new Locale("cs","CZ"));
        
        @Override
        public int compare(Ability o1, Ability o2) {
            return this.collator.compare(o1.toString(), o2.toString());
        }        
        
    }
