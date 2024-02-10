package br.com.matheusperin.entitybase.event;

import br.com.matheusperin.entitybase.EntityBase;
import br.com.matheusperin.entitybase.enums.EntityPersistStage;

public interface EntityEventUpdate<T extends EntityBase> extends EntityEvent<T> {

    void onUpdate(T entity, T oldEntity, EntityPersistStage stage);

}
