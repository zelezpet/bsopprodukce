/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.backend.service;

import org.hibernate.dialect.MySQL57InnoDBDialect;

/**
 *
 * @author ironman
 */
public class MySQL5InnoDBDialect extends MySQL57InnoDBDialect {

    @Override
    public String getTableTypeString() {
        return "ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_czech_ci";
    }
    
    
    
}
