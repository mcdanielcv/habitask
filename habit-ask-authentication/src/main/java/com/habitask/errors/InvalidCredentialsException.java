package com.habitask.errors;

public class InvalidCredentialsException extends RuntimeException{
    public InvalidCredentialsException(String mensaje) {
        super(mensaje);
    }
}
