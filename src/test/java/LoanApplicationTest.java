import config.Config;
import java.time.LocalDate;
import java.time.LocalDateTime;
import model.IPLookup;
import model.LoanApplication;
import model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import repository.LoanRepository;
import repository.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by alex on 17.21.3.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes={Config.class})
public class LoanApplicationTest {

    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    LoanRepository loanRepository;

    @Autowired
    private WebApplicationContext wac;

    @PersistenceContext
    EntityManager em;

    @Before
    public void init()  {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void testApplyForLoanEmptyUser() throws Exception {
        String personalId = "222";
        Double amount = 200.;

        mockMvc.perform(post("/loan")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content("personalId=" + personalId + "&amount=" + amount +
                        "&term=01-06-2017&name=John&surname=Doe"))
                .andDo(print())
                .andExpect(status().isCreated());

        User user = userRepository.findByPersonalId(personalId);
        Assert.assertNotNull(user);

        List<LoanApplication> list = loanRepository.byUser(personalId);
        assertEquals(list.size(), 1);
        assert(Math.abs(list.get(0).getAmount() - amount) < 0.1);
    }

    @Test
    @Transactional
    public void testApplyForLoanExistingUser() throws Exception {
        String personalId = "333";
        String name = "John";
        String surname = "Doe";
        Double amount = 150.;
        User user = new User(name, surname, personalId);
        em.merge(user);

        mockMvc.perform(post("/loan")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content("personalId=" + personalId + "&amount=" + amount +
                        "&term=01-06-2017&name=" + name + "&surname=" + surname))
                .andDo(print())
                .andExpect(status().isCreated());

        List<LoanApplication> list = loanRepository.byUser(personalId);
        assertEquals(list.size(), 1);
        assert(Math.abs(list.get(0).getAmount() - amount) < 0.1);
    }

    @Test
    @Transactional
    public void testFailBlacklistedUser() throws Exception {
        String personalId = "444";
        User user = new User(null, null, personalId);
        user.setBlacklisted(true);
        em.merge(user);

        mockMvc.perform(post("/loan")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content("personalId=" + personalId + "&amount=100.0" +
                        "&term=01-06-2017&name=&surname="))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testFailTooManyRequests() throws Exception {
        try {
            for (int i = 0; i < 5; i++) {
                mockMvc.perform(post("/loan")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("personalId=555&amount=100.0" +
                                "&term=01-06-2017&name=John&surname=Doe"));
            }
        }catch (Throwable t){}

        mockMvc.perform(post("/loan")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content("personalId=555&amount=100.0" +
                        "&term=01-06-2017&name=John&surname=Doe"))
                .andDo(print())
                .andExpect(status().isBadRequest());

        //wait for a complete timeframe to avoid further blocking
        Thread.sleep(3100);

    }


    @Test
    @Transactional
    public void testGetLoanApplicationsByUser()
            throws Exception {
        String personalId = "666";

        User user = new User(null, null, personalId);
        user.setBlacklisted(true);
        LoanApplication loanApplication = new LoanApplication(10., user, LocalDate.of(2017, 6, 1), "LV", LocalDateTime.now());

        em.merge(loanApplication);

        mockMvc.perform(get("/loan?personalId=" + personalId))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(content().contentType("application/json;charset=UTF-8"))
               .andExpect(jsonPath("$[0].amount").value("10.0"));
    }


    @Test
    @Transactional
    public void testGetAllLoanApplications()
            throws Exception {

        String personalId = "777";
        Double amount = 152.5;
        em.createQuery("delete from LoanApplication").executeUpdate();

        User user = new User(null, null, personalId);
        LoanApplication loanApplication = new LoanApplication(amount, user, LocalDate.of(2017, 6, 1), "LV", LocalDateTime.now());
        em.merge(loanApplication);

        mockMvc.perform(get("/loan/all"))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(content().contentType("application/json;charset=UTF-8"))
               .andExpect(jsonPath("$[0].amount").value(amount))
               .andExpect(jsonPath("$[0].user.personalId").value(personalId));
    }


    @Test
    @Transactional
    public void testApplyForLoanFromSavedIP()
            throws Exception {

        String personalId = "888";
        String countryCode = "DE";

        IPLookup ipLookup = new IPLookup("127.0.0.1", countryCode);
        em.merge(ipLookup);

        mockMvc.perform(post("/loan").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                     .content("personalId=" + personalId
                                                      +"&amount=150.0" + "&term=01-06-2017&name=John&surname=Doe"))
               .andDo(print())
               .andExpect(status().isCreated());

        mockMvc.perform(get("/loan?personalId=" + personalId))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(content().contentType("application/json;charset=UTF-8"))
               .andExpect(jsonPath("$[0].countryCode").value(countryCode));
    }

}
