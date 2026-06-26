package dev.anamika.journalApp.exception;

public class MethodAccessDeniedException extends RuntimeException{

    public MethodAccessDeniedException(String message){
          super(message);
    }
}
