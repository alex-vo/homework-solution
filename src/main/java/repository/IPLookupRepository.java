package repository;

import javax.persistence.TypedQuery;
import model.IPLookup;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;


/**
 * Created by alex on 17.19.3.
 */
@Repository
public class IPLookupRepository {

    @PersistenceContext
    EntityManager em;

    public IPLookup getByIP(String ip){
        TypedQuery<IPLookup> query = em.createQuery("select i from IPLookup i " +
                "where i.ip=:ip", IPLookup.class);
        query.setParameter("ip", ip);
        return query.getSingleResult();
    }


    @Transactional
    public void saveIPLookup(String ip, String countryCode){
        em.merge(new IPLookup(ip, countryCode));
    }

}
