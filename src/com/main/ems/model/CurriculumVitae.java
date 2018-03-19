package main.ems.model;

import org.hibernate.annotations.GenericGenerator;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "curriculum_vitae")
public class CurriculumVitae {
    int id;
    String firstName;
    String lastName;
    String address;
    String zipCode;
    String city;
    String country;
    String phoneNumber;
    String email;
    Vacancy vacancy;
    Set<EducationAndTraining> educationAndTraining = new HashSet<EducationAndTraining>();
    Set<PersonalExperience> personalExperience = new HashSet<PersonalExperience>();
    Set<PersonalProject> personalProjects = new HashSet<PersonalProject>();
    Set<Knowledge> knowledges = new HashSet<Knowledge>();
    Set<Language> languages = new HashSet<Language>();
    Set<Certificate> certificates = new HashSet<Certificate>();

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

    @Column(name = "first_name")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "last_name")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column(name = "address")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Column(name = "zip_code")
    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
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

    @Column(name = "phone_number")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @ManyToOne
    @JoinColumn(name = "vacancy_id")
    public Vacancy getVacancy() {
        return vacancy;
    }

    public void setVacancy(Vacancy vacancy) {
        this.vacancy = vacancy;
    }

    @OneToMany(mappedBy = "curriculumVitae")
    public Set<EducationAndTraining> getEducationAndTraining() {
        return educationAndTraining;
    }

    public void setEducationAndTraining(Set<EducationAndTraining> educationAndTraining) {
        this.educationAndTraining = educationAndTraining;
    }

    @OneToMany(mappedBy = "curriculumVitae")
    public Set<PersonalExperience> getPersonalExperience() {
        return personalExperience;
    }

    public void setPersonalExperience(Set<PersonalExperience> personalExperience) {
        this.personalExperience = personalExperience;
    }

    @OneToMany(mappedBy = "curriculumVitae")
    public Set<PersonalProject> getPersonalProjects() {
        return personalProjects;
    }

    public void setPersonalProjects(Set<PersonalProject> personalProjects) {
        this.personalProjects = personalProjects;
    }

    @OneToMany(mappedBy = "curriculumVitae")
    public Set<Knowledge> getKnowledges() {
        return knowledges;
    }

    public void setKnowledges(Set<Knowledge> knowledges) {
        this.knowledges = knowledges;
    }

    @OneToMany(mappedBy = "curriculumVitae")
    public Set<Language> getLanguages() {
        return languages;
    }

    public void setLanguages(Set<Language> languages) {
        this.languages = languages;
    }

    @OneToMany(mappedBy = "curriculumVitae")
    public Set<Certificate> getCertificates() {
        return certificates;
    }

    public void setCertificates(Set<Certificate> certificates) {
        this.certificates = certificates;
    }
}
