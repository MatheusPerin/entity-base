package br.com.matheusperin.entitybase.persit.child;

import br.com.matheusperin.entitybase.EntityBase;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Objects;

public class EntityChildrenCallBackSetParent implements EntityChildrenCallBack {

    public static EntityChildrenCallBackSetParent create() {
        return new EntityChildrenCallBackSetParent();
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

        if (childrenPersist.mappedBy().isBlank())
            return;

        EntityChildrenPersistValidate.validateMappedBy(childrenPersist, child.getClass(), parent.getClass());

        Field fieldChild = child.getClass().getDeclaredField(childrenPersist.mappedBy());

        fieldChild.setAccessible(true);
        fieldChild.set(child, parent);
    }
}
