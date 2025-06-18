package service;

import dao.DAOException;
import dao.TareaDAO;
import model.Tarea;
import validacion.ValidacionException;
import validacion.ValidadorDeErrores;

import java.util.List;

public class TareaServiceImpl implements TareaService {

    private final TareaDAO dao;

    public TareaServiceImpl(TareaDAO dao) {
        this.dao = dao;
    }

    // ------------------------------------ CRUD

    @Override
    public void alta(String titulo, String desc, int hEst, int hReal)
            throws ValidacionException, ServiceException {

        ValidadorDeErrores.validarTarea(titulo, hEst, hReal);
        try {
            dao.crear(new Tarea(titulo, desc, hEst, hReal));
        } catch (DAOException ex) {
            throw new ServiceException("No se pudo guardar la tarea", ex);
        }
    }

    @Override
    public void modificar(int id, String titulo, String desc, int hEst, int hReal)
            throws ValidacionException, ServiceException {

        ValidadorDeErrores.validarTarea(titulo, hEst, hReal);
        try {
            dao.actualizar(new Tarea(id, titulo, desc, hEst, hReal));
        } catch (DAOException ex) {
            throw new ServiceException("No se pudo actualizar la tarea", ex);
        }
    }

    @Override
    public void baja(int id) throws ServiceException {
        try {
            dao.eliminar(id);
        } catch (DAOException ex) {
            throw new ServiceException("No se pudo eliminar la tarea", ex);
        }
    }

    @Override
    public List<Tarea> listado() throws ServiceException {
        try {
            return dao.obtenerTodas();
        } catch (DAOException ex) {
            throw new ServiceException("No se pudo obtener el listado", ex);
        }
    }

    @Override
    public Tarea consulta(int id) throws ServiceException {
        try {
            return dao.obtenerPorId(id).orElse(null);
        } catch (DAOException ex) {
            throw new ServiceException("No se pudo consultar la tarea", ex);
        }
    }
}
