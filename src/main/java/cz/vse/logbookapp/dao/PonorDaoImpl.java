package cz.vse.logbookapp.dao;

import cz.vse.logbookapp.model.Ponor;
import cz.vse.logbookapp.model.Uzivatel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PonorDaoImpl implements PonorDao {
    private final EntityManagerFactory emf;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public PonorDaoImpl(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public void save(Ponor ponor) {
        logger.debug("Attempting to save Ponor entity with id: {}", ponor.getId());
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(ponor);
            tx.commit();
            logger.info("Saved Ponor entity with id: {}", ponor.getId());
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            logger.error("Error saving Ponor entity with id: {}", ponor.getId(), e);
            throw e;
        } finally {
            em.close();
            logger.debug("EntityManager closed after saving Ponor with id: {}", ponor.getId());
        }
    }

    @Override
    public void update(Ponor ponor) {
        logger.debug("Attempting to update Ponor entity with id: {}", ponor.getId());
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(ponor);
            tx.commit();
            logger.info("Updated Ponor entity with id: {}", ponor.getId());
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            logger.error("Error updating Ponor entity with id: {}", ponor.getId(), e);
            throw e;
        } finally {
            em.close();
            logger.debug("EntityManager closed after updating Ponor with id: {}", ponor.getId());
        }
    }

    @Override
    public void delete(Long id) {
        logger.debug("Attempting to delete Ponor entity with id: {}", id);
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Ponor ponor = em.find(Ponor.class, id);
            if (ponor != null) {
                em.remove(ponor);
                logger.info("Deleted Ponor entity with id: {}", id);
            } else {
                logger.info("No Ponor entity found with id: {} to delete.", id);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            logger.error("Error deleting Ponor entity with id: {}", id, e);
            throw e;
        } finally {
            em.close();
            logger.debug("EntityManager closed after deleting Ponor by id: {}", id);
        }
    }

    @Override
    public List<Ponor> findAllByUser(Uzivatel uzivatel) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            logger.debug("Fetching all Ponor entities for Uzivatel with id: {}", uzivatel.getId());
            tx.begin();
            List<Ponor> ponorList = em.createQuery("SELECT p FROM Ponor p WHERE p.uzivatel.id = :uzivatelId", Ponor.class)
                    .setParameter("uzivatelId", uzivatel.getId())
                    .getResultList();
            tx.commit();
            logger.info("Fetched {} Ponor entities for Uzivatel id: {}", ponorList.size(), uzivatel.getId());
            return ponorList;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            logger.error("Error fetching Ponor entities for Uzivatel id: {}", uzivatel.getId(), e);
            throw e;
        } finally {
            em.close();
            logger.debug("EntityManager closed after fetching Ponor entities for Uzivatel id: {}", uzivatel.getId());
        }
    }
}