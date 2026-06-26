package dev.anamika.journalApp.exception;

public class InvalidRoleOperationException extends RuntimeException{

    public InvalidRoleOperationException(String message){
        super(message);
    }
}
