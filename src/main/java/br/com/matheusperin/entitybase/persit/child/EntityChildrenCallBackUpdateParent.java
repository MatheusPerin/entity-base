package br.com.matheusperin.entitybase.persit.child;

import br.com.matheusperin.entitybase.EntityBase;
import br.com.matheusperin.entitybase.enums.EntityPersistType;
import br.com.matheusperin.entitybase.persit.EntityPersistController;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class EntityChildrenCallBackUpdateParent implements EntityChildrenCallBack {

    private final EntityBase oldParent;
    private final EntityPersistController persistController;

    public static EntityChildrenCallBackUpdateParent create(EntityBase oldEntity, EntityPersistController persistController) {
        return new EntityChildrenCallBackUpdateParent(oldEntity, persistController);
    }

    public EntityChildrenCallBackUpdateParent(EntityBase oldParent, EntityPersistController persistController) {
        this.oldParent = oldParent;
        this.persistController = persistController;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onChildren(EntityChildrenPersist childrenPersist, EntityBase parent, Field fieldParent, Collection<EntityBase> children) throws Exception {
        Collection<EntityBase> oldChildren = Optional.ofNullable((Collection<EntityBase>) fieldParent.get(oldParent)).orElse(List.of());

        if (children.isEmpty() && oldChildren.isEmpty()) {
            return;
        }

        removeChildren(childrenPersist, parent, fieldParent, children, oldChildren);

        insertChildren(childrenPersist, parent, fieldParent, children);

        updateChildren(childrenPersist, children, oldChildren);
    }

    @Override
    public void onChild(EntityChildrenPersist childrenPersist, EntityBase parent, Field fieldParent, EntityBase child) throws Exception {
        EntityBase oldChild = (EntityBase) fieldParent.get(oldParent);

        if (Objects.isNull(child) && Objects.isNull(oldChild))
            return;

        if (Objects.isNull(child)) {
            EntityChildrenCallBackRemoveParent
                .create(persistController)
                .onChild(
                    childrenPersist,
                    parent,
                    fieldParent,
                    oldChild
                );

            return;
        }

        if (Objects.isNull(child.getId())) {
            EntityChildrenCallBackInsertParent
                .create(persistController)
                .onChild(
                    childrenPersist,
                    parent,
                    fieldParent,
                    child
                );

            return;
        }

        if (!checkCanUpdate(childrenPersist))
            return;

        update(child, oldChild);
    }

    private void updateChildren(EntityChildrenPersist childrenPersist, Collection<EntityBase> children, Collection<EntityBase> oldChildren) throws Exception {
        if (!checkCanUpdate(childrenPersist))
            return;

        List<EntityBase> childrenToUpdate = children
            .stream()
            .filter(child -> Objects.nonNull(child.getId()))
            .toList();

        for (EntityBase child: childrenToUpdate) {
            update(
                child,
                oldChildren
                    .stream()
                    .filter(oldChild -> Objects.equals(oldChild.getId(), child.getId()))
                    .findFirst()
                    .orElse(null)
            );
        }
    }

    private void insertChildren(EntityChildrenPersist childrenPersist, EntityBase parent, Field fieldParent, Collection<EntityBase> children) throws Exception {
        EntityChildrenCallBackInsertParent
            .create(persistController)
            .onChildren(
                childrenPersist,
                parent,
                fieldParent,
                children
                    .stream()
                    .filter(e -> Objects.isNull(e.getId()))
                    .toList()
            );
    }

    private void removeChildren(EntityChildrenPersist childrenPersist, EntityBase parent, Field fieldParent, Collection<EntityBase> children, Collection<EntityBase> oldChildren) throws Exception {
        EntityChildrenCallBackRemoveParent
            .create(persistController)
            .onChildren(
                childrenPersist,
                parent,
                fieldParent,
                oldChildren
                    .stream()
                    .filter(oldChild ->
                        children
                            .stream()
                            .noneMatch(child -> Objects.equals(oldChild.getId(), child.getId()))
                    ).toList()
            );
    }

    private void update(EntityBase child, EntityBase oldChild) throws Exception {
        if (Objects.nonNull(oldChild))
            persistController.update(child, oldChild);
        else
            persistController.update(child);
    }

    private boolean checkCanUpdate(EntityChildrenPersist childrenPersist) {
        return List.of(childrenPersist.persistType()).contains(EntityPersistType.UPDATE);
    }

}
