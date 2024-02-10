package br.com.matheusperin.entitybase.exception;

public class EntityException extends Exception {

    public EntityException(Exception exception) {
        super(exception);
    }

    public EntityException(String message) {
        super(message);
    }

}
