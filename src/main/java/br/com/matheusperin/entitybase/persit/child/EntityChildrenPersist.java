package br.com.matheusperin.entitybase.persit.child;

import br.com.matheusperin.entitybase.enums.EntityPersistType;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EntityChildrenPersist {

    String mappedBy() default "";
    EntityPersistType[] persistType() default {EntityPersistType.INSERT, EntityPersistType.UPDATE, EntityPersistType.REMOVE};

}
