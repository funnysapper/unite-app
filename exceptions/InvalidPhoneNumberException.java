package com.Unite.UniteMobileApp.exceptions;

public class InvalidPhoneNumberException extends RuntimeException{
    public InvalidPhoneNumberException(String message){
        super(message);
    }
}
