package service;

import dao.DAOException;
import dao.EmpleadoDAO;
import model.Empleado;
import validacion.ValidacionException;
import validacion.ValidadorDeErrores;

import java.util.List;

public class EmpleadoServiceImpl implements EmpleadoService {

    private final EmpleadoDAO dao;

    public EmpleadoServiceImpl(EmpleadoDAO dao) {
        this.dao = dao;
    }

    @Override
    public void alta(String nombre, int costoHora) throws ValidacionException, ServiceException {
        ValidadorDeErrores.textoNoVacio(nombre, "Nombre");
        try {
            dao.crear(new Empleado(nombre, costoHora));
        } catch (DAOException e) {
            throw new ServiceException("No se pudo guardar el empleado", e);
        }
    }

    @Override
    public void modificar(int id, String nombre, int costoHora) throws ValidacionException, ServiceException {
        ValidadorDeErrores.textoNoVacio(nombre, "Nombre");
        try {
            dao.actualizar(new Empleado(id, nombre, costoHora));
        } catch (DAOException e) {
            throw new ServiceException("No se pudo actualizar el empleado", e);
        }
    }

    @Override
    public void baja(int id) throws ServiceException {
        try {
            dao.eliminar(id);
        } catch (DAOException e) {
            throw new ServiceException("No se pudo eliminar el empleado", e);
        }
    }

    @Override
    public List<Empleado> listado() throws ServiceException {
        try {
            return dao.obtenerTodas();
        } catch (DAOException e) {
            throw new ServiceException("No se pudo obtener el listado", e);
        }
    }

    @Override
    public Empleado consulta(int id) throws ServiceException {
        try {
            return dao.obtenerPorId(id).orElse(null);
        } catch (DAOException e) {
            throw new ServiceException("No se pudo consultar el empleado", e);
        }
    }
}
