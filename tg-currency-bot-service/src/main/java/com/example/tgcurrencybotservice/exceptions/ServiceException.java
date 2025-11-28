package com.example.tgcurrencybotservice.exceptions;

public class ServiceException extends Exception{
    public ServiceException(String message) {
        super(message); // Передаем сообщение в конструктор Exception
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause); // Передаем сообщение в конструктор Exception
    }

}
