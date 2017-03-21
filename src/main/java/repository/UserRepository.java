package repository;

import model.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * Created by alex on 17.18.3.
 */
@Repository
public class UserRepository {

    @PersistenceContext
    EntityManager em;

    @Transactional
    public void addToBlackList(String personalId){
        User user = findByPersonalId(personalId);

        user.setBlacklisted(true);
        em.merge(user);
    }

    @Transactional
    public void removeFromBlackList(String personalId){
        User user = findByPersonalId(personalId);

        user.setBlacklisted(false);
        em.merge(user);
    }


    public User findByPersonalId(String personalId){
        CriteriaBuilder builder = em.getCriteriaBuilder();

        CriteriaQuery<User> searchQuery = builder.createQuery(User.class);
        Root<User> root = searchQuery.from(User.class);

        return em.createQuery(searchQuery.select(root)
                .where(builder.equal(root.get("personalId"), personalId))).getSingleResult();
    }

}
