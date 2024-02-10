package br.com.matheusperin.entitybase.persit.child;

import br.com.matheusperin.entitybase.EntityBase;
import br.com.matheusperin.entitybase.enums.EntityPersistType;
import br.com.matheusperin.entitybase.persit.EntityPersistController;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class EntityChildrenCallBackRemoveParent implements EntityChildrenCallBack{

    private final EntityPersistController persistController;

    public static EntityChildrenCallBackRemoveParent create(EntityPersistController persistController) {
        return new EntityChildrenCallBackRemoveParent(persistController);
    }

    public EntityChildrenCallBackRemoveParent(EntityPersistController persistController) {
        this.persistController = persistController;
    }

    @Override
    public void onChildren(EntityChildrenPersist childrenPersist, EntityBase parent, Field fieldParent, Collection<EntityBase> children) throws Exception {
        for (EntityBase child: children)
            onChild(childrenPersist, parent, fieldParent, child);
    }

    @Override
    public void onChild(EntityChildrenPersist childrenPersist, EntityBase parent, Field fieldParent, EntityBase child) throws Exception {
        if (Objects.isNull(child))
            return;

        if (checkCanRemove(childrenPersist))
            persistController.remove(child);
    }

    private boolean checkCanRemove(EntityChildrenPersist childrenPersist) {
        return !childrenPersist.mappedBy().isBlank() ||
               List.of(childrenPersist.persistType()).contains(EntityPersistType.REMOVE);
    }

}
