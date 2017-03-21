package model;

import javax.persistence.*;

/**
 * Created by alex on 17.18.3.
 */
@Entity
@Table(name = "USER")
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String surname;
    private String personalId;
    private Boolean blacklisted = false;

    public User(){}

    public User(String name, String surname, String personalId){
        this.name = name;
        this.surname = surname;
        this.personalId = personalId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPersonalId() {
        return personalId;
    }

    public void setPersonalId(String personalId) {
        this.personalId = personalId;
    }

    public Boolean isBlacklisted() {
        return blacklisted;
    }

    public void setBlacklisted(Boolean blacklisted) {
        this.blacklisted = blacklisted;
    }

    @Override
    public String toString() {
        return getClass() + "[" + id + ", " + name + ", " + surname+ ", " + personalId + "]";
    }
}
