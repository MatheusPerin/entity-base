package br.com.matheusperin.entitybase.persit.child;

import br.com.matheusperin.entitybase.EntityBase;
import br.com.matheusperin.entitybase.enums.EntityPersistType;
import br.com.matheusperin.entitybase.persit.EntityPersistController;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class EntityChildrenCallBackInsertParent implements EntityChildrenCallBack {

    private final EntityPersistController persistController;

    public static EntityChildrenCallBackInsertParent create(EntityPersistController persistController) {
        return new EntityChildrenCallBackInsertParent(persistController);
    }

    public EntityChildrenCallBackInsertParent(EntityPersistController persistController) {
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

        if (Objects.isNull(child.getId()))
            insert(child, childrenPersist);
        else
            update(child, childrenPersist);
    }

    private void insert(EntityBase child, EntityChildrenPersist childrenPersist) throws Exception {
        if (!List.of(childrenPersist.persistType()).contains(EntityPersistType.INSERT))
            return;

        persistController.insert(child);
    }

    private void update(EntityBase child, EntityChildrenPersist childrenPersist) throws Exception {
        if (!List.of(childrenPersist.persistType()).contains(EntityPersistType.UPDATE))
            return;

        persistController.update(child);
    }

}
