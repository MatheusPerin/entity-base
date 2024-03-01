package br.com.matheusperin.entitybase.exception;

public class EntityRuntimeException extends RuntimeException {

    public EntityRuntimeException(Exception exception) {
        super(exception);
    }

    public EntityRuntimeException(String message) {
        super(message);
    }

}
