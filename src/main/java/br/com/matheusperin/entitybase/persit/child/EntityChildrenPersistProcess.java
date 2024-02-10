package br.com.matheusperin.entitybase.persit.child;

import br.com.matheusperin.entitybase.EntityBase;
import br.com.matheusperin.entitybase.utils.GetGenericTypeUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Default;
import org.hibernate.Hibernate;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class EntityChildrenPersistProcess {

    public static EntityChildrenPersistProcess create() {
        return new EntityChildrenPersistProcess();
    }

    @SuppressWarnings("unchecked")
    public void process(EntityBase entity, EntityChildrenCallBack callBack) throws Exception {
        Class<?> classEntity = entity.getClass();

        for (Field fieldParent: classEntity.getDeclaredFields()) {
            if (!fieldParent.isAnnotationPresent(EntityChildrenPersist.class))
                continue;

            fieldParent.setAccessible(true);

            EntityChildrenPersist childrenPersist = fieldParent.getAnnotation(EntityChildrenPersist.class);

            Hibernate.initialize(fieldParent.get(entity));

            if (Collection.class.isAssignableFrom(fieldParent.getType())) {
                if (!EntityBase.class.isAssignableFrom(GetGenericTypeUtil.create().forField(fieldParent, 0)))
                    continue;

                callBack.onChildren(childrenPersist, entity, fieldParent, Optional.ofNullable((Collection<EntityBase>) fieldParent.get(entity)).orElse(List.of()));
            } else {
                if (!EntityBase.class.isAssignableFrom(fieldParent.getType()))
                    continue;

                callBack.onChild(childrenPersist, entity, fieldParent, (EntityBase) fieldParent.get(entity));
            }
        }
    }

}
