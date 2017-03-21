package model;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by alex on 17.18.3.
 */
@Entity
@Table(name = "LOAN_APPLICATION")
public class LoanApplication {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private Double amount;
    private LocalDate term;
    private String countryCode;
    private LocalDateTime created;

    @ManyToOne(cascade = CascadeType.ALL)
    private User user;

    public LoanApplication(){}

    public LoanApplication(Double amount, User user, LocalDate term, String countryCode, LocalDateTime created){
        this.amount = amount;
        this.user = user;
        this.term = term;
        this.countryCode = countryCode;
        this.created = created;
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public Double getAmount() {
        return amount;
    }


    public void setAmount(Double amount) {
        this.amount = amount;
    }


    public LocalDate getTerm() {
        return term;
    }


    public void setTerm(LocalDate term) {
        this.term = term;
    }


    public String getCountryCode() {
        return countryCode;
    }


    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }


    public LocalDateTime getCreated() {
        return created;
    }


    public void setCreated(LocalDateTime created) {
        this.created = created;
    }


    public User getUser() {
        return user;
    }


    public void setUser(User user) {
        this.user = user;
    }


    @Override
    public String toString() {
        return getClass() + "[" + id + ", " + amount + ", " + countryCode + ", " + user + "]";
    }

}
