package main.ems.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "activities")
public class Activitie {

    int id;
    String name;
    PersonalExperience personalExperience;

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

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToOne
    @JoinColumn(name = "pers_exp_id")
    public PersonalExperience getPersonalExperience() {
        return personalExperience;
    }

    public void setPersonalExperience(PersonalExperience personalExperience) {
        this.personalExperience = personalExperience;
    }


}
