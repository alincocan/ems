package main.ems.model;

import org.hibernate.annotations.GenericGenerator;

import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "personal_projects")
public class PersonalProject {

    int id;
    Date from;
    Date to;
    String description;
    String acquiredKnowledges;
    CurriculumVitae curriculumVitae;

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

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "aquired_knowledges")
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
}
