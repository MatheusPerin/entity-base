package br.com.matheusperin.entitybase.validator;

import br.com.matheusperin.entitybase.EntityBase;
import br.com.matheusperin.entitybase.exception.EntityRuntimeExceptionValidator;

public interface EntityValidatorUpdate<T extends EntityBase> extends EntityValidator<T> {

    void onUpdate(T entity, T oldEntity) throws EntityRuntimeExceptionValidator;

}
