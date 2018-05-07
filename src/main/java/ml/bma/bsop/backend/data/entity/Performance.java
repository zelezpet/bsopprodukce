/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.backend.data.entity;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import ml.bma.bsop.backend.data.PerformanceState;
import ml.bma.bsop.backend.service.LocalDateTimeAttributeConverter;


/**
 *
 * @author ironman
 */

@Entity
public class Performance extends AbstractEntity {
    
    @Column(columnDefinition = "timestamp")
    @NotNull
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime dateTime;
    
    @Column
    @NotNull
    private int duration;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private PerformanceState phase;
    
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "production_id")    
    private Production production;
    
    @NotNull
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "address_id")
    private Address address;
    
    @OneToMany(mappedBy = "performance")
    private List<Casting> casts;

    public Performance() {
        
        this.dateTime = null;
        this.address = null;
        this.casts = null;
        this.phase = null;
        this.production = null;
    }

    public Performance( LocalDateTime dateTime, Address address) {
        
        this.dateTime = dateTime;
        this.address = address;
    }

   

    public LocalDateTime getDateTime() {
        return this.dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Casting> getCasts() {
        return this.casts;
    }

    public void setCasts(List<Casting> casts) {
        this.casts = casts;
    }

    public Production getProduction() {
        return production;
    }

    public void setProduction(Production production) {
        this.production = production;
    }

    public PerformanceState getPhase() {
        return phase;
    }

    public void setPhase(PerformanceState phase) {
        this.phase = phase;
    }
    
    @Override
    public String toString() {
        return this.dateTime.toLocalDate().toString() +" " + this.production.getName();
    }
    
}
