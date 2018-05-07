/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.backend.data.entity;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import ml.bma.bsop.backend.data.Instrument;

/**
 *
 * @author ironman
 */

@Entity
public class Ability extends AbstractEntity {
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "performer_id")
    private Performer performer;
    
    @Column
    @NotNull
    @Enumerated(EnumType.STRING)
    private Instrument instrument;
    
    @Column
    private String description;
    
    @OneToMany(mappedBy = "ability")
    private List<Casting> casts;
    
    public Ability() {
        //default
    }

    public Ability(Performer performer, Instrument instrument) {
        this.performer = performer;
        this.instrument = instrument;
    }

    public Performer getPerformer() {
        return this.performer;
    }

    public void setPerformer(Performer performer) {
        this.performer = performer;
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public void setInstrument(Instrument instrument) {
        this.instrument = instrument;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Ability other = (Ability) obj;
        if (!this.performer.getId().equals(other.getPerformer().getId())) {
            return false;
        }
        if (this.instrument != other.instrument) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
    
    @Override
    public String toString() {
        return performer.toString() + " " + instrument.toString();
    }
    
    
    
}
