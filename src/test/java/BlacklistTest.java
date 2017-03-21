import model.User;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by alex on 17.21.3.
 */
public class BlacklistTest extends BaseTest{

    @Test
    @Transactional
    public void testAddToBlacklist()
            throws Exception {

        String personalId = "111";
        User user = new User(null, null, personalId);
        em.merge(user);

        mockMvc.perform(post("/blacklist")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content("personalId=" + personalId))
                .andDo(print())
                .andExpect(status().isOk());

        User updatedUser = userRepository.findByPersonalId(personalId);
        assertTrue(updatedUser.isBlacklisted());
    }

    @Test
    public void testFailNonExistingUser()
            throws Exception {
        mockMvc.perform(post("/blacklist")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content("personalId=non_existing_personalId"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    @Test
    @Transactional
    public void testRemoveFromBlacklist()
            throws Exception {
        String personalId = "999";
        User user = new User(null, null, personalId);
        user.setBlacklisted(true);
        em.merge(user);

        mockMvc.perform(delete("/blacklist?personalId=" + personalId).contentType(MediaType.APPLICATION_FORM_URLENCODED))
               .andDo(print())
               .andExpect(status().isOk());

        User updatedUser = userRepository.findByPersonalId(personalId);
        assertFalse(updatedUser.isBlacklisted());
    }

}
