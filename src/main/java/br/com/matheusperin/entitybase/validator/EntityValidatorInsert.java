package br.com.matheusperin.entitybase.validator;

import br.com.matheusperin.entitybase.EntityBase;
import br.com.matheusperin.entitybase.exception.EntityRuntimeExceptionValidator;

public interface EntityValidatorInsert<T extends EntityBase> extends EntityValidator<T> {

    void onInsert(T entity) throws EntityRuntimeExceptionValidator;

}
