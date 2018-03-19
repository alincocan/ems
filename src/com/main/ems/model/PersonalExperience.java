package main.ems.model;

import org.hibernate.annotations.GenericGenerator;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "personal_experience")
public class PersonalExperience {

    int id;
    Date from;
    Date to;
    String positionHeld;
    String employer;
    String city;
    String country;
    String acquiredKnowledges;
    CurriculumVitae curriculumVitae;
    String activities;

    @Id
    @GenericGenerator(
            name = "sequence",
            strategy = "sequence",
            parameters = {
                    @org.hibernate.annotations.Parameter(
                            name = "sequence",
                            value = "sequence"
                    )

            })
    @GeneratedValue(generator = "sequence")
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "from_ev")
    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    @Column(name = "to_ev")
    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    @Column(name = "position_hold")
    public String getPositionHeld() {
        return positionHeld;
    }

    public void setPositionHeld(String positionHeld) {
        this.positionHeld = positionHeld;
    }

    @Column(name = "employer")
    public String getEmployer() {
        return employer;
    }

    public void setEmployer(String employer) {
        this.employer = employer;
    }

    @Column(name = "city")
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Column(name = "country")
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Column(name = "acquired_knowledges")
    public String getAcquiredKnowledges() {
        return acquiredKnowledges;
    }

    public void setAcquiredKnowledges(String acquiredKnowledges) {
        this.acquiredKnowledges = acquiredKnowledges;
    }

    @ManyToOne
    @JoinColumn(name = "cv_id")
    public CurriculumVitae getCurriculumVitae() {
        return curriculumVitae;
    }

    public void setCurriculumVitae(CurriculumVitae curriculumVitae) {
        this.curriculumVitae = curriculumVitae;
    }

    @Column(name = "activities")
    public String getActivities() {
        return activities;
    }

    public void setActivities(String activities) {
        this.activities = activities;
    }
}
