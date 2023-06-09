/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atividadecruddao.DAO;

import atividadecruddao.entities.AbstractEntity;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author santo
 */
@SuppressWarnings("unchecked")
public class GenericDAO<T extends AbstractEntity> {
    protected static EntityManager entityManager;
    static {
        EntityManagerFactory fac = Persistence.createEntityManagerFactory("atividade_persistence");
        entityManager = fac.createEntityManager();
    }
    public T getById(Long id) {
        return (T) entityManager.find(getTypeClass(), id);
    }
    public void save(T entity) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(entity);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            entityManager.getTransaction().rollback();
        }
    }
    public void update(T entity) {
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(entity);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            entityManager.getTransaction().rollback();
        }
    }
    public void delete(T entity) {
        try {
            entity = getById(entity.getId());
            entityManager.getTransaction().begin();
            entityManager.remove(entity);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            entityManager.getTransaction().rollback();
        }
    }
    public List<T> findAll() {
        return entityManager.createQuery(("From " + getTypeClass().getName())).getResultList();
    }
    private Class<?> getTypeClass() {
        Class<?> clazz = (Class<?>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return clazz;
    }
}