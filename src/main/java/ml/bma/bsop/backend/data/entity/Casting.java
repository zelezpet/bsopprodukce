/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.backend.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import ml.bma.bsop.backend.data.CastState;

/**
 *
 * @author ironman
 */

@Entity
public class Casting extends AbstractEntity {
    
    
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ability_id")
    private Ability ability;
    
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "performance_id")
    private Performance performance;
    
    @Column
    @NotNull
    @Enumerated(EnumType.STRING)
    private CastState castState;

    public Casting() {
        //default
    }

    public Casting(Ability ability, Performance performance, CastState castState) {
        this.ability = ability;
        this.performance = performance;
        this.castState = castState;
    }

    public Ability getAbility() {
        return ability;
    }

    public void setAbility(Ability ability) {
        this.ability = ability;
    }

    public Performance getPerformance() {
        return performance;
    }

    public void setPerformance(Performance performance) {
        this.performance = performance;
    }

    public CastState getCastState() {
        return castState;
    }

    public void setCastState(CastState castState) {
        this.castState = castState;
    }
    
    public String toString() {
        return this.ability + " " + this.performance;
    }
    
    
    
    
    
}
