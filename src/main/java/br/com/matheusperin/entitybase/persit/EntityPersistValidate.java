package br.com.matheusperin.entitybase.persit;

import br.com.matheusperin.entitybase.EntityBase;
import br.com.matheusperin.entitybase.exception.EntityExceptionPersist;

import java.util.Objects;

public class EntityPersistValidate {

    public static <T extends EntityBase> void validateNullEntity(T entity) throws EntityExceptionPersist {
        if (Objects.isNull(entity))
            throw new EntityExceptionPersist("Entity cannot be null");
    }

    public static <T extends EntityBase> void validateNonNullEntityId(T entity) throws EntityExceptionPersist {
        validateNullEntity(entity);

        if (Objects.isNull(entity.getId()))
            throw new EntityExceptionPersist("Entity ID must be non-null");
    }

}
