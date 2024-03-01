package br.com.matheusperin.entitybase.validator;

import br.com.matheusperin.entitybase.EntityBase;
import br.com.matheusperin.entitybase.exception.EntityRuntimeExceptionValidator;

public interface EntityValidatorRemove<T extends EntityBase> extends EntityValidator<T> {

    void onRemove(T entity) throws EntityRuntimeExceptionValidator;

}
