package br.com.matheusperin.entitybase.validator;

import br.com.matheusperin.entitybase.EntityBase;
import br.com.matheusperin.entitybase.exception.EntityExceptionValidator;

public interface EntityValidatorUpdate<T extends EntityBase> extends EntityValidator<T> {

    void onUpdate(T entity, T oldEntity) throws EntityExceptionValidator;

}
