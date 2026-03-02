package com.myproject.server.exception;

public class UserNotFoundException extends CertException{
    public UserNotFoundException(String message){
        super(message);
    }

}
