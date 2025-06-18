package service;

import model.Proyecto;
import validacion.ValidacionException;
import java.util.List;

public interface ProyectoService {
    void alta(String nombre) throws ValidacionException, ServiceException;
    void modificar(int id, String nombre) throws ValidacionException, ServiceException;
    void baja(int id) throws ServiceException;
    List<Proyecto> listado() throws ServiceException;
    Proyecto consulta(int id) throws ServiceException;
}
