package repository;

import model.LoanApplication;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by alex on 17.18.3.
 */
@Repository
public class LoanRepository {

    @PersistenceContext
    EntityManager em;

    @Value("#{systemProperties['timeframe.seconds']}")
    private Integer timeFrameSeconds = 3;

    @Value("#{systemProperties['max.applications']}")
    private Integer maxApplications = 5;

    @Autowired
    UserRepository userRepository;

    public List<LoanApplication> getLoans(){
        List<LoanApplication> loanApplicationList = new LinkedList<>();
        loanApplicationList.addAll(em.createQuery("from LoanApplication l", LoanApplication.class)
                .getResultList());
        return loanApplicationList;
    }

    @Transactional
    public synchronized LoanApplication createLoanApplication(Double amount, String personalId,
                                                              LocalDate term, String name,
                                                              String surname, String countryCode,
                                                              LocalDateTime requestDateTime) throws Exception {

        if(!isLoanApplicationAllowed(countryCode, requestDateTime)){
            throw new Exception("Maximum application rate exceeded");
        }

        User user = getOrCreate(personalId, name, surname);

        if(user.isBlacklisted()){
            throw new Exception("user is blacklisted " + user);
        }

        LoanApplication loanApplication = new LoanApplication(amount, user, term, countryCode, requestDateTime);
        em.merge(loanApplication);

        return loanApplication;
    }


    public List<LoanApplication> byUser(String personalId) throws InterruptedException {
        TypedQuery<LoanApplication> query = em.createQuery("select l from LoanApplication l " +
                "where l.user.personalId=:personalId", LoanApplication.class);
        query.setParameter("personalId", personalId);
        return query.getResultList();
    }


    private User getOrCreate(String personalId, String name, String surname){
        User user;
        try{
            TypedQuery<User> query = em.createQuery("select u from User u " +
                    "where u.personalId=:personalId", User.class);
            query.setParameter("personalId", personalId);
            user = query.getSingleResult();
        }catch (NoResultException nre){
            user = new User(name, surname, personalId);
        }

        return user;
    }


    private boolean isLoanApplicationAllowed(String countryCode, LocalDateTime requestDateTime){
        Query query = em.createQuery("select count(l.id) from LoanApplication l " +
                "where l.countryCode=:countryCode and l.created > :date");
        query.setParameter("countryCode", countryCode);
        query.setParameter("date", requestDateTime.minusSeconds(timeFrameSeconds));

        Long acceptedApps = (Long) query.getSingleResult();
        if(acceptedApps >= maxApplications){
            return false;
        }

        return true;
    }
}
