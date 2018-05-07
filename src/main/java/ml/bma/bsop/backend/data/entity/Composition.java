/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.backend.data.entity;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 *
 * @author ironman
 */

@Entity
public class Composition extends AbstractEntity {
    
    
    @Column
    @NotNull
    @Pattern(regexp = "[1-9\\p{Lu}][0-9\\p{L}\\.]*([ \\p{Lu}\\p{L}])*")
    private String name;
    
    @Column
    @NotNull
    @Pattern(regexp = "([\\p{Lu}][\\p{L}]+)+(\\p{Lu}\\p{L}+)*(\\p{Lu}'\\p{L}+)*([ \\-]\\p{Lu}\\p{L}+)*")
    private String author;
    
    @ManyToMany(mappedBy = "compositions")
    private List<Production> productions;
    

    public Composition() {
    }

    public Composition(String name, String author) {
        this.name = name;
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }


    public List<Production> getProductions() {
        return productions;
    }

    public void setProductions(List<Production> productions) {
        this.productions = productions;
    }
    
        
    @Override
    public String toString() {
        return this.name + " " + this.author;
    }
    
}
