package config;

import model.LoanApplication;
import model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

/**
 * Created by alex on 17.18.3.
 */
@Component
public class TestDataInitializer {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    public void init() throws Exception {
        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        User userJohn = new User("John", "Doe", "123456-78910");
        session.persist(userJohn);

        LoanApplication loanApplication1 = new LoanApplication(300., userJohn,
                LocalDate.of(2017, Month.JUNE, 1), "LV", LocalDateTime.now());
        session.persist(loanApplication1);

        transaction.commit();
    }
}
