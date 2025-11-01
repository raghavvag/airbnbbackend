package org.example.airbnbbackend.exceptions;

public class ResourceNotFounfException extends RuntimeException{
    public ResourceNotFounfException(String message){
        super(message);
    }
}
