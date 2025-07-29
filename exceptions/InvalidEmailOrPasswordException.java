package com.Unite.UniteMobileApp.exceptions;

public class InvalidEmailOrPasswordException extends RuntimeException{
    public InvalidEmailOrPasswordException(String message){
        super(message);
    }
}
