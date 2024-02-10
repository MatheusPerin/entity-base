package br.com.matheusperin.entitybase.validator;

import br.com.matheusperin.entitybase.EntityBase;
import br.com.matheusperin.entitybase.event.EntityEventUpdate;
import br.com.matheusperin.entitybase.exception.EntityExceptionPersist;
import br.com.matheusperin.entitybase.exception.EntityExceptionValidator;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.spi.CDI;

import java.util.ArrayList;
import java.util.List;

public class EntityValidatorController {

    private final List<EntityValidator<?>> validators;

    public static EntityValidatorController create() {
        return new EntityValidatorController();
    }

    public EntityValidatorController() {
        validators = new ArrayList<>();
    }

    public EntityValidatorController add(EntityValidator<? extends EntityBase> validator) {
        validators.add(validator);

        return this;
    }

    public EntityValidatorController add(Class<? extends EntityValidator<?>> validatorClass) throws EntityExceptionPersist {
        try {
            Instance<? extends EntityValidator<?>> validatorCDI = CDI.current().select(validatorClass);

            if (validatorCDI.isUnsatisfied())
                validators.add(validatorClass.getConstructor().newInstance());
            else
                validators.add(validatorCDI.get());

            return this;
        } catch (Exception e) {
            throw new EntityExceptionPersist(
                String.format(
                    "It was not possible to find any instance of the validator '%s' in the CDI, and it was not possible to create it due to there being no default constructor",
                    validatorClass.getSimpleName()
                )
            );
        }
    }

    public void onInsert(EntityBase entity) throws EntityExceptionValidator {
        List<EntityValidatorInsert<EntityBase>> validatorsInsert = getValidatorsByType(EntityValidatorInsert.class);

        for (EntityValidatorInsert<EntityBase> validator: validatorsInsert)
            validator.onInsert(entity);
    }

    public void onRemove(EntityBase entity) throws EntityExceptionValidator {
        List<EntityValidatorRemove<EntityBase>> eventsRemove = getValidatorsByType(EntityValidatorRemove.class);

        for (EntityValidatorRemove<EntityBase> validator: eventsRemove)
            validator.onRemove(entity);
    }

    public void onUpdate(EntityBase entity, EntityBase oldEntity) throws EntityExceptionValidator {
        List<EntityValidatorUpdate<EntityBase>> eventsUpdate = getValidatorsByType(EntityEventUpdate.class);

        for (EntityValidatorUpdate<EntityBase> validator: eventsUpdate)
            validator.onUpdate(entity, oldEntity);
    }

    @SuppressWarnings("unchecked")
    public <E extends EntityValidator<EntityBase>> List<E> getValidatorsByType(Class<?> classEvent) {
        return (List<E>) validators
            .stream()
            .filter(classEvent::isInstance)
            .map(classEvent::cast)
            .toList();
    }

}
