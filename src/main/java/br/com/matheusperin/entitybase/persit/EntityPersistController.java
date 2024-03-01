package br.com.matheusperin.entitybase.persit;

import br.com.matheusperin.entitybase.EntityBase;
import br.com.matheusperin.entitybase.enums.EntityPersistStage;
import br.com.matheusperin.entitybase.exception.EntityExceptionPersist;
import br.com.matheusperin.entitybase.exception.EntityRuntimeExceptionValidator;
import br.com.matheusperin.entitybase.persit.child.*;
import jakarta.persistence.EntityManager;

import java.util.Objects;

public class EntityPersistController {

    private final EntityManager em;
    private final EntityChildrenPersistProcess childrenProcess;

    public static EntityPersistController create(EntityManager em) {
        return new EntityPersistController(em);
    }

    public EntityPersistController(EntityManager em) {
        this.em = em;
        this.childrenProcess = EntityChildrenPersistProcess.create();
    }

    public EntityManager getEntityManager() {
        return em;
    }

    public <T extends EntityBase> T save(T entity) throws EntityExceptionPersist, EntityRuntimeExceptionValidator {
        if (Objects.nonNull(entity.getId()))
            return update(entity);

        return insert(entity);
    }

    public <T extends EntityBase> T insert(T entity) throws EntityExceptionPersist {
        EntityPersistValidate.validateNullEntity(entity);

        try {
            entity.eventsController().onInsert(entity, EntityPersistStage.BEFORE);
            entity.validatorsController().onInsert(entity);
            em.persist(entity);
            em.flush();
            childrenProcess.process(entity, EntityChildrenCallBackSetParent.create());
            childrenProcess.process(entity, EntityChildrenCallBackInsertParent.create(this));
            entity.eventsController().onInsert(entity, EntityPersistStage.AFTER);
        } catch (EntityRuntimeExceptionValidator exceptionValidator) {
            throw exceptionValidator;
        } catch (Exception e) {
            throw new EntityExceptionPersist(e);
        }

        return entity;
    }

    public <T extends EntityBase> T update(T entity) throws EntityExceptionPersist {
        EntityPersistValidate.validateNonNullEntityId(entity);

        return update(entity, findById(entity.getClass(), entity.getId()));
    }

    public <T extends EntityBase> T update(T entity, T oldEntity) throws EntityExceptionPersist {
        EntityPersistValidate.validateNonNullEntityId(entity);

        try {
            entity.eventsController().onUpdate(entity, oldEntity, EntityPersistStage.BEFORE);
            entity.validatorsController().onUpdate(entity, oldEntity);
            em.detach(oldEntity);
            em.merge(entity);
            em.flush();
            childrenProcess.process(entity, EntityChildrenCallBackSetParent.create());
            childrenProcess.process(entity, EntityChildrenCallBackUpdateParent.create(oldEntity, this));
            entity.eventsController().onUpdate(entity, oldEntity, EntityPersistStage.AFTER);
        } catch (EntityRuntimeExceptionValidator exceptionValidator) {
            throw exceptionValidator;
        } catch (Exception e) {
            throw new EntityExceptionPersist(e);
        }

        return entity;
    }

    public <T extends EntityBase> void remove(T entity) throws EntityExceptionPersist {
        EntityPersistValidate.validateNonNullEntityId(entity);

        try {
            childrenProcess.process(entity, EntityChildrenCallBackRemoveParent.create(this));

            entity.eventsController().onRemove(entity, EntityPersistStage.BEFORE);
            entity.validatorsController().onRemove(entity);
            em.remove(entity);
            em.flush();
            entity.eventsController().onRemove(entity, EntityPersistStage.AFTER);
        } catch (EntityRuntimeExceptionValidator exceptionValidator) {
            throw exceptionValidator;
        } catch (Exception e) {
            throw new EntityExceptionPersist(e);
        }
    }

    public <T extends EntityBase> void removeById(Class<T> classEntity, Long id) throws EntityRuntimeExceptionValidator, EntityExceptionPersist {
        remove(findById(classEntity, id));
    }

    @SuppressWarnings("unchecked")
    public <T extends EntityBase> T findById(Class<?> classEntity, Long id) {
        return (T) em.find(classEntity, id);
    }

}
