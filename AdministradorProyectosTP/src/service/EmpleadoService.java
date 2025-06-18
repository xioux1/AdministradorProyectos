package service;

import model.Empleado;
import validacion.ValidacionException;
import java.util.List;

public interface EmpleadoService {
    void alta(String nombre) throws ValidacionException, ServiceException;
    void modificar(int id, String nombre) throws ValidacionException, ServiceException;
    void baja(int id) throws ServiceException;
    List<Empleado> listado() throws ServiceException;
    Empleado consulta(int id) throws ServiceException;
}
