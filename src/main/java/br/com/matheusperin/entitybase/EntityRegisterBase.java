package br.com.matheusperin.entitybase;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@MappedSuperclass
@Getter @Setter
public abstract class EntityRegisterBase extends EntityBase {

    @Column(name = "name", nullable = false)
    protected String name;

    @Override
    public String toString() {
        if (Objects.isNull(getId()) || Objects.isNull(getName()))
            return super.toString();

        return getId().toString().concat(" - ").concat(getName());
    }

}
