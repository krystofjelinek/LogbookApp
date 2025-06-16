package cz.vse.logbookapp.dao;

import cz.vse.logbookapp.model.Lokalita;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class LokalitaDaoImpl implements LokalitaDao {
    private final EntityManagerFactory emf;
    private final Logger logger = LoggerFactory.getLogger(LokalitaDaoImpl.class);

    public LokalitaDaoImpl(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public List<Lokalita> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            logger.debug("Fetching all Lokalita entities.");
            List<Lokalita> lokalitaList = em.createQuery("SELECT l FROM Lokalita l", Lokalita.class).getResultList();
            logger.info("Fetched {} Lokalita entities.", lokalitaList.size());
            return lokalitaList;
        } catch (Exception e) {
            logger.error("Error fetching all Lokalita entities.", e);
            throw e;
        } finally {
            em.close();
            logger.debug("EntityManager closed after fetching all Lokalita entities.");
        }
    }

    @Override
    public Lokalita findByNazev(String nazev) {
        EntityManager em = emf.createEntityManager();
        try {
            logger.debug("Fetching Lokalita by nazev: {}", nazev);
            Lokalita lokalita = em.createQuery("SELECT l FROM Lokalita l WHERE l.nazev = :nazev", Lokalita.class)
                    .setParameter("nazev", nazev)
                    .getSingleResult();
            logger.info("Found Lokalita: {}", lokalita.getNazev());
            return lokalita;
        } catch (NoResultException e) {
            logger.warn("No Lokalita found with nazev: {}", nazev);
            return null;
        } catch (Exception e) {
            logger.error("Error fetching Lokalita by nazev: {}", nazev, e);
            throw e;
        } finally {
            em.close();
            logger.debug("EntityManager closed after fetching Lokalita by nazev: {}", nazev);
        }
    }

    @Override
    public List<String> findAllNazvy() {
        EntityManager em = emf.createEntityManager();
        try {
            logger.debug("Fetching all Lokalita names (nazvy).");
            List<String> nazvy = em.createQuery("SELECT l.nazev FROM Lokalita l", String.class).getResultList();
            logger.info("Fetched {} Lokalita names.", nazvy.size());
            return nazvy;
        } catch (Exception e) {
            logger.error("Error fetching Lokalita names.", e);
            throw e;
        } finally {
            em.close();
            logger.debug("EntityManager closed after fetching Lokalita names.");
        }
    }

    @Override
    public ObservableMap<String, Lokalita> findAllMap() {
        EntityManager em = emf.createEntityManager();
        try {
            logger.debug("Fetching all Lokalita entities for map.");
            List<Lokalita> lokalitaList = em.createQuery("SELECT l FROM Lokalita l", Lokalita.class)
                    .getResultList();
            ObservableMap<String, Lokalita> lokalitaMap = FXCollections.observableHashMap();
            for (Lokalita lokalita : lokalitaList) {
                lokalitaMap.put(lokalita.getNazev(), lokalita);
            }
            logger.info("Fetched {} Lokalita entities for map.", lokalitaMap.size());
            return lokalitaMap;
        } catch (Exception e) {
            logger.error("Error fetching Lokalita map.", e);
            throw e;
        } finally {
            em.close();
            logger.debug("EntityManager closed after fetching Lokalita map.");
        }
    }

    @Override
    public void save(Lokalita lokalita) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            logger.debug("Starting transaction to save Lokalita: {}", lokalita);
            transaction.begin();
            em.persist(lokalita);
            transaction.commit();
            logger.info("Lokalita saved successfully: {}", lokalita);
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
                logger.warn("Transaction rolled back due to error while saving Lokalita: {}", lokalita, e);
            }
            logger.error("Error saving Lokalita: {}", lokalita, e);
            throw e;
        } finally {
            em.close();
            logger.debug("EntityManager closed after saving Lokalita: {}", lokalita);
        }
    }
}