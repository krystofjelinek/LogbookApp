package cz.vse.logbookapp.dao;

import cz.vse.logbookapp.model.Uzivatel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UzivatelDaoImpl implements UzivatelDao {
    private final EntityManagerFactory emf;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public UzivatelDaoImpl(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Uzivatel findByEmail(String email) {
        EntityManager em = emf.createEntityManager();
        try {
            logger.debug("Fetching Uzivatel by email: {}", email);
            List<Uzivatel> result = em.createQuery("SELECT u FROM Uzivatel u WHERE u.email = :email", Uzivatel.class)
                    .setParameter("email", email)
                    .getResultList();
            if (result.isEmpty()) {
                logger.info("No Uzivatel found with email: {}", email);
                return null;
            } else {
                logger.info("Found Uzivatel with email: {}", email);
                return result.getFirst();
            }
        } catch (Exception e) {
            logger.error("Error fetching Uzivatel by email: {}", email, e);
            throw e;
        } finally {
            em.close();
            logger.debug("EntityManager closed after fetching Uzivatel by email: {}", email);
        }
    }

    @Override
    public List<Uzivatel> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            logger.debug("Fetching all Uzivatel entities.");
            List<Uzivatel> uzivatelList = em.createQuery("SELECT u FROM Uzivatel u", Uzivatel.class).getResultList();
            logger.info("Fetched {} Uzivatel entities.", uzivatelList.size());
            return uzivatelList;
        } catch (Exception e) {
            logger.error("Error fetching all Uzivatel entities.", e);
            throw e;
        } finally {
            em.close();
            logger.debug("EntityManager closed after fetching all Uzivatel entities.");
        }
    }
}