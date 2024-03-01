package br.com.matheusperin.entitybase;

import br.com.matheusperin.entitybase.event.EntityEventController;
import br.com.matheusperin.entitybase.validator.EntityValidatorController;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@MappedSuperclass
@Getter @Setter
public abstract class EntityBase {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    protected Long id;

    @Override
    public EntityBase clone() throws CloneNotSupportedException {
        return (EntityBase) super.clone();
    }

    @Override
    public boolean equals(Object obj) {
        if (Objects.isNull(obj))
            return false;

        if (obj instanceof EntityBase)
            return Objects.equals(getId(), ((EntityBase) obj).getId());

        return false;
    }

    public EntityValidatorController validatorsController() {
        return EntityValidatorController.create();
    }

    public EntityEventController eventsController() {
        return EntityEventController.create();
    }

}
