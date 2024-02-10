package br.com.matheusperin.entitybase.event;

import br.com.matheusperin.entitybase.EntityBase;
import br.com.matheusperin.entitybase.enums.EntityPersistStage;

public interface EntityEventRemove<T extends EntityBase> extends EntityEvent<T> {

    void onRemove(T entity, EntityPersistStage stage);

}
