package dao;

public class DAOException extends Exception {
    public DAOException(String mensaje)             { super(mensaje); }
    public DAOException(String mensaje, Throwable c){ super(mensaje, c); }
}
