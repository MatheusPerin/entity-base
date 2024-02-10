package br.com.matheusperin.entitybase.event;

import br.com.matheusperin.entitybase.EntityBase;
import br.com.matheusperin.entitybase.enums.EntityPersistStage;
import br.com.matheusperin.entitybase.exception.EntityExceptionPersist;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.spi.CDI;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EntityEventController {

    private final List<EntityEvent<? extends EntityBase>> events;

    public static EntityEventController create() {
        return new EntityEventController();
    }

    public EntityEventController() {
        events = new ArrayList<>();
    }

    public EntityEventController add(EntityEvent<? extends EntityBase> event) {
        events.add(event);

        return this;
    }

    public EntityEventController add(Class<? extends EntityEvent<?>> eventClass) throws EntityExceptionPersist {
        try {
            Instance<? extends EntityEvent<?>> eventCDI = CDI.current().select(eventClass);

            if (eventCDI.isUnsatisfied())
                events.add(eventClass.getConstructor().newInstance());
            else
                events.add(eventCDI.get());

            return this;
        } catch (Exception e) {
            throw new EntityExceptionPersist(
                String.format(
                    "It was not possible to find any instance of the event '%s' in the CDI, and it was not possible to create it due to there being no default constructor",
                    eventClass.getSimpleName()
                )
            );
        }
    }

    public void onInsert(EntityBase entity, EntityPersistStage stage) {
        List<EntityEventInsert<EntityBase>> eventsInsert = getEventsByType(EntityEventInsert.class, stage);

        for (EntityEventInsert<EntityBase> eventInsert: eventsInsert)
            eventInsert.onInsert(entity, stage);
    }

    public void onRemove(EntityBase entity, EntityPersistStage stage) {
        List<EntityEventRemove<EntityBase>> eventsRemove = getEventsByType(EntityEventRemove.class, stage);

        for (EntityEventRemove<EntityBase> eventRemove: eventsRemove)
            eventRemove.onRemove(entity, stage);
    }

    public void onUpdate(EntityBase entity, EntityBase oldEntity, EntityPersistStage stage) {
        List<EntityEventUpdate<EntityBase>> eventsUpdate = getEventsByType(EntityEventUpdate.class, stage);

        for (EntityEventUpdate<EntityBase> event: eventsUpdate)
            event.onUpdate(entity, oldEntity, stage);
    }

    @SuppressWarnings("unchecked")
    public <E extends EntityEvent<EntityBase>> List<E> getEventsByType(Class<?> classEvent, EntityPersistStage stage) {
        return (List<E>) events
            .stream()
            .filter(event -> Optional.ofNullable(event.stage()).orElse(List.of()).contains(stage))
            .filter(classEvent::isInstance)
            .map(classEvent::cast)
            .toList();
    }

}
