package br.com.matheusperin.entitybase.event;

import br.com.matheusperin.entitybase.EntityBase;
import br.com.matheusperin.entitybase.enums.EntityPersistStage;

public interface EntityEventInsert<T extends EntityBase> extends EntityEvent<T> {

    void onInsert(T entity, EntityPersistStage stage);

}
