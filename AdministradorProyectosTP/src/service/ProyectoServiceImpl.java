package service;

import dao.DAOException;
import dao.ProyectoDAO;
import model.Proyecto;
import validacion.ValidacionException;
import validacion.ValidadorDeErrores;

import java.util.List;

public class ProyectoServiceImpl implements ProyectoService {

    private final ProyectoDAO dao;

    public ProyectoServiceImpl(ProyectoDAO dao) {
        this.dao = dao;
    }

    @Override
    public void alta(String nombre) throws ValidacionException, ServiceException {
        ValidadorDeErrores.textoNoVacio(nombre, "Nombre");
        try {
            dao.crear(new Proyecto(nombre));
        } catch (DAOException e) {
            throw new ServiceException("No se pudo guardar el proyecto", e);
        }
    }

    @Override
    public void modificar(int id, String nombre) throws ValidacionException, ServiceException {
        ValidadorDeErrores.textoNoVacio(nombre, "Nombre");
        try {
            dao.actualizar(new Proyecto(id, nombre));
        } catch (DAOException e) {
            throw new ServiceException("No se pudo actualizar el proyecto", e);
        }
    }

    @Override
    public void baja(int id) throws ServiceException {
        try {
            dao.eliminar(id);
        } catch (DAOException e) {
            throw new ServiceException("No se pudo eliminar el proyecto", e);
        }
    }

    @Override
    public List<Proyecto> listado() throws ServiceException {
        try {
            return dao.obtenerTodas();
        } catch (DAOException e) {
            throw new ServiceException("No se pudo obtener el listado", e);
        }
    }

    @Override
    public Proyecto consulta(int id) throws ServiceException {
        try {
            return dao.obtenerPorId(id).orElse(null);
        } catch (DAOException e) {
            throw new ServiceException("No se pudo consultar el proyecto", e);
        }
    }
}
