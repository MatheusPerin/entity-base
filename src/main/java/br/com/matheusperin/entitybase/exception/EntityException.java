package br.com.matheusperin.entitybase.exception;

public class EntityException extends Exception {

    public EntityException(String mensage) {
        super(mensage);
    }

    public EntityException(Throwable throwable) {
        super(throwable);
    }

}
