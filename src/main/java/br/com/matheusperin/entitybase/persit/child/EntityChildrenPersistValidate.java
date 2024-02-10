package br.com.matheusperin.entitybase.persit.child;

import br.com.matheusperin.entitybase.exception.EntityExceptionPersist;
import java.util.Arrays;

public class EntityChildrenPersistValidate {

    public static void validateMappedBy(EntityChildrenPersist childrenPersist, Class<?> childClass, Class<?> parentClass) throws EntityExceptionPersist {
        if (childrenPersist.mappedBy().isEmpty())
            return;

        boolean findField = Arrays
            .stream(childClass.getDeclaredFields())
            .anyMatch(field -> field.getName().equals(childrenPersist.mappedBy()));

        if (!findField)
            throw new EntityExceptionPersist(
                String.format(
                    "Field '%s' in child entity '%s' to reference parent entity '%s' was not found",
                    childrenPersist.mappedBy(),
                    childClass.getSimpleName(),
                    parentClass.getSimpleName()
                )
            );
    }

}
