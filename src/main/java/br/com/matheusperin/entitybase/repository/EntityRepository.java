package br.com.matheusperin.entitybase.repository;

import br.com.matheusperin.entitybase.EntityBase;
import br.com.matheusperin.entitybase.exception.EntityExceptionPersist;
import br.com.matheusperin.entitybase.exception.EntityRuntimeExceptionValidator;
import br.com.matheusperin.entitybase.persit.EntityPersistController;
import br.com.matheusperin.entitybase.utils.GetGenericTypeUtil;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaQuery;

import java.util.List;

public abstract class EntityRepository<T extends EntityBase> {

    private final EntityPersistController persistController;

    public EntityRepository() {
        this.persistController = EntityPersistController.create(CDI.current().select(EntityManager.class).get());
    }

    public Class<T> getEntityClass() {
        return GetGenericTypeUtil.create().forClass(getClass(), 0);
    }

    public T save(T entity) throws EntityRuntimeExceptionValidator, EntityExceptionPersist {
        return persistController.save(entity);
    }

    public T insert(T entity) throws EntityRuntimeExceptionValidator, EntityExceptionPersist {
        return persistController.insert(entity);
    }

    public T update(T entity) throws EntityRuntimeExceptionValidator, EntityExceptionPersist {
        return persistController.update(entity);
    }

    public void remove(T entity) throws EntityRuntimeExceptionValidator, EntityExceptionPersist {
        persistController.remove(entity);
    }

    public void removeById(Long id) throws EntityRuntimeExceptionValidator, EntityExceptionPersist {
        persistController.removeById(getEntityClass(), id);
    }

    public EntityManager getEntityManager() {
        return persistController.getEntityManager();
    }

    public List<T> findAll() {
        CriteriaQuery<T> query = getEntityManager()
            .getCriteriaBuilder()
            .createQuery(getEntityClass());

        return getEntityManager()
            .createQuery(
                query
                    .select(
                        query.from(getEntityClass())
                    )
            ).getResultList();
    }

    public T findById(Long id) {
        return getEntityManager().find(getEntityClass(), id);
    }

}
