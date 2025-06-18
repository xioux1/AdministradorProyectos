package service;

public class ServiceException extends Exception {
    public ServiceException(String mensaje)              { super(mensaje); }
    public ServiceException(String mensaje, Throwable c) { super(mensaje, c); }
}
