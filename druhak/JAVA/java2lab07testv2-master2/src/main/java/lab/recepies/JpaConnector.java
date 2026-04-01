package lab.recepies;

import java.util.List;

import jakarta.persistence.*;
import jakarta.persistence.criteria.*;

public class JpaConnector {

    private EntityManager em;

    // Inicializace EntityManagerFactory z persistence.xml (persistence unit "lab")
    public JpaConnector() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("lab");
        this.em = emf.createEntityManager();
    }

    // ===================== Cookware =====================

    public List<Cookware> getAllCookwares() {
        return em.createQuery("SELECT c FROM Cookware c", Cookware.class)
                .getResultList();
    }

    public Cookware save(Cookware entity) {
        em.getTransaction().begin();
        if (entity.getId() == null) {
            em.persist(entity);
        } else {
            entity = em.merge(entity);
        }
        em.getTransaction().commit();
        return entity;
    }

    public void delete(Cookware entity) {
        em.getTransaction().begin();
        Cookware managed = em.contains(entity) ? entity : em.merge(entity);
        em.remove(managed);
        em.getTransaction().commit();
    }

    // ===================== CookingRecipe =====================

    public List<CookingRecipe> getAllCookingRecipies() {
        return em.createQuery("SELECT c FROM CookingRecipe c", CookingRecipe.class)
                .getResultList();
    }

    public CookingRecipe save(CookingRecipe entity) {
        em.getTransaction().begin();
        if (entity.getId() == null) {
            em.persist(entity);
        } else {
            entity = em.merge(entity);
        }
        em.getTransaction().commit();
        return entity;
    }

    // ===================== Ingredient =====================

    public List<Ingredient> getAllIngredients() {
        return em.createQuery("SELECT i FROM Ingredient i", Ingredient.class)
                .getResultList();
    }

    public Ingredient save(Ingredient entity) {
        em.getTransaction().begin();
        if (entity.getId() == null) {
            em.persist(entity);
        } else {
            entity = em.merge(entity);
        }
        em.getTransaction().commit();
        return entity;
    }

    // ===================== Criteria Query =====================

    /**
     * Spustí předané CriteriaQuery a vrátí výsledky
     */
    public List<Cookware> executeCriteriaQuery(CriteriaQuery<Cookware> query) {
        return em.createQuery(query).getResultList();
    }

    /**
     * Vytvoří CriteriaQuery pro nádobí (Cookware) filtrované podle barev.
     * Použití: createCriteriaQueryForColoredCookware("Orange", "White")
     */
    public CriteriaQuery<Cookware> createCriteriaQueryForColoredCookware(String... colors) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Cookware> cq = cb.createQuery(Cookware.class);
        Root<Cookware> root = cq.from(Cookware.class);

        // Sestavíme predikát: color IN ('Orange', 'White', ...)
        Predicate colorPredicate = root.get("color").in((Object[]) colors);

        cq.select(root).where(colorPredicate);
        return cq;
    }

    // ===================== Ostatní =====================

    public void stop() {
        em.close();
    }

    public EntityManager getEntityManager() {
        return em;
    }

    public Cookware find(long id) {
        return em.find(Cookware.class, id);
    }
}