/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.ui.component;

import com.vaadin.icons.VaadinIcons;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import ml.bma.bsop.backend.data.Authority;
import ml.bma.bsop.backend.data.PerformanceState;
import ml.bma.bsop.backend.data.entity.Address;
import ml.bma.bsop.backend.data.entity.Performance;
import ml.bma.bsop.backend.data.entity.Production;
import ml.bma.bsop.security.SecurityUtils;
import org.vaadin.addon.calendar.item.BasicItem;

/**
 *
 * @author ironman
 */
public class PerformanceItem extends BasicItem {
    
    private final Performance performance;

    public PerformanceItem(Performance performance) {
        this.performance = performance;
        Production production = performance.getProduction();
        ZonedDateTime dateTime = performance.getDateTime().atZone(ZoneId.systemDefault());
        super.setCaption(production.getName());
        super.setDescription(production.getDescription());
        super.setStart(dateTime);
        super.setEnd(dateTime.plusMinutes(performance.getDuration()));
        
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
        final PerformanceItem other = (PerformanceItem) obj;
        return Objects.equals(this.performance, other.performance);
    }

    public Performance getPerformance() {
        return performance;
    }
    
    @Override
    public String getStyleName() {
        String state;
        switch(this.performance.getPhase()) {
            case CREATE:
            case CAST:
                state = "empty";
                break;
            case READY:
                state = "planned";
                break;
            case END:
                state = "confirmed";
                break;
            default:
                state = "empty";
                break;
        }
        return "state-" + state;
    }

    @Override
    public int hashCode() {
        return performance.hashCode();
    }

    @Override
    public boolean isAllDay() {
        return false;
    }
    
    @Override
    public boolean isMoveable() {
        if(SecurityUtils.hasRole(Authority.ROLE_USER) || SecurityUtils.hasRole(Authority.ROLE_PERFORMER)) {
            return false;
        }
        return this.performance.getPhase() == PerformanceState.CREATE;
    }
    
    @Override
    public boolean isResizeable() {
        return isMoveable();
    }
    
    @Override
    public void setEnd(ZonedDateTime end) {
        ZonedDateTime start = performance.getDateTime().atZone(ZoneId.systemDefault());
        Duration tmp = Duration.between(start, end);
        performance.setDuration((int)tmp.toMinutes());
    }
    
    @Override
    public void setStart(ZonedDateTime start) {
        performance.setDateTime(start.toLocalDateTime());
    }
    
    @Override
    public String getDateCaptionFormat(){
        return VaadinIcons.CLOCK.getHtml() + " %s - %s";
    }
    
    public String toHtmlString() {
        Production production = performance.getProduction();
        Address address = performance.getAddress();
        String html = "<h2 style='margin: 0; text-align: center;'><b>" + production.getName() + "</b></h2>" +
                      "<p style='margin: 0; font-weight: 400;'>" + production.getDescription() + "</p>" +
                      "<p>" + VaadinIcons.CLOCK.getHtml() + " " + super.getStart().format(DateTimeFormatter.ofPattern("HH:mm")) + " - " + super.getEnd().format(DateTimeFormatter.ofPattern("HH:mm")) +
                      "<ul style='padding: 0; list-style-type: none;'>" + VaadinIcons.BUILDING.getHtml() + " " + address.getName() +
                      "<li>" + address.getStreet() + " " + address.getHouseNumber() + "</li>" +
                      "<li>" + address.getCity() + " " + address.getZipCode() + "</li></ul></p>";
        return html;
    }
    
    @Override
    public String toString() {
        return this.performance.getProduction().getName() + " " + 
                super.getStart().format(DateTimeFormatter.ofPattern("dd.MM. YYYY HH:mm")) + " - " +
                super.getEnd().format(DateTimeFormatter.ofPattern("HH:mm"));
    }
}
