package br.com.matheusperin.entitybase.exception;

public class EntityExceptionPersist extends EntityException {

    public EntityExceptionPersist(Exception exception) {
        super(exception);
    }

    public EntityExceptionPersist(String mesagem) {
        super(mesagem);
    }

}
