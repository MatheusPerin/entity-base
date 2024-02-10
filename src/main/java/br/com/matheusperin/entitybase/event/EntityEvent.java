package br.com.matheusperin.entitybase.event;

import br.com.matheusperin.entitybase.EntityBase;
import br.com.matheusperin.entitybase.enums.EntityPersistStage;

import java.util.List;

public interface EntityEvent<T extends EntityBase> {

    default List<EntityPersistStage> stage() {
        return List.of(EntityPersistStage.BEFORE, EntityPersistStage.AFTER);
    };

}
