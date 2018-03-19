package main.ems.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "Settings")
public class Settings {

    public final static int BILUNAR = 1;
    public final static int FIRST_DAY_OF_PAYMENT = 2;
    public final static int SECOND_DAY_OF_PAYMENT = 3;
    public final static int DAY_OF_PAYMENT = 4;

    int id;
    String name;
    String value;
    String type;

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

    @Column(name = "value")
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Column(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


}
