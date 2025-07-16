package service;

import dao.DAOException;
import dao.TareaDAO;
import dao.HistorialDAO;
import model.Tarea;
import model.Proyecto;
import model.Empleado;
import model.HistorialEstado;
import validacion.ValidacionException;
import validacion.ValidadorDeErrores;

import java.time.LocalDate;
import java.util.List;

public class TareaServiceImpl implements TareaService {

    private final TareaDAO dao;
    private final HistorialDAO historialDao;

    public TareaServiceImpl(TareaDAO dao, HistorialDAO historialDao) {
        this.dao = dao;
        this.historialDao = historialDao;
    }

    // ------------------------------------ CRUD

    @Override
    public void alta(String titulo, String desc, int hEst, int hReal,
                     LocalDate inicio, LocalDate fin, model.EstadoTarea estado,
                     model.Proyecto proyecto, model.Empleado empleado)
            throws ValidacionException, ServiceException {

        ValidadorDeErrores.validarTarea(titulo, hEst, hReal);
        try {
            Tarea t = new Tarea(0, titulo, desc, hEst, hReal,
                                inicio, fin, estado,
                                proyecto, empleado);
            dao.crear(t);
            if(estado != null)
                historialDao.registrar(new HistorialEstado(t.getId(), estado, "creacion", java.time.LocalDateTime.now()));
        } catch (DAOException ex) {
            throw new ServiceException("No se pudo guardar la tarea", ex);
        }
    }

    @Override
    public void modificar(int id, String titulo, String desc, int hEst, int hReal,
                          LocalDate inicio, LocalDate fin, model.EstadoTarea estado,
                          model.Proyecto proyecto, model.Empleado empleado)
            throws ValidacionException, ServiceException {

        ValidadorDeErrores.validarTarea(titulo, hEst, hReal);
        try {
            Tarea previa = dao.obtenerPorId(id).orElse(null);

            dao.actualizar(new Tarea(id, titulo, desc, hEst, hReal,
                                     inicio, fin, estado,
                                     proyecto, empleado));
            if(previa != null && previa.getEstado() != estado)
                historialDao.registrar(new HistorialEstado(id, estado, "modificacion", java.time.LocalDateTime.now()));
        } catch (DAOException ex) {
            throw new ServiceException("No se pudo actualizar la tarea", ex);
        }
    }

    @Override
    public void cambiarEstado(int id, model.EstadoTarea estado) throws ServiceException {
        try {
            dao.actualizarEstado(id, estado);
            historialDao.registrar(new HistorialEstado(id, estado, "cambio", java.time.LocalDateTime.now()));
        } catch (DAOException ex) {
            throw new ServiceException("No se pudo cambiar el estado", ex);
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

    @Override
    public List<HistorialEstado> historial(int tareaId) throws ServiceException {
        try {
            return historialDao.obtenerPorTarea(tareaId);
        } catch (DAOException ex) {
            throw new ServiceException("No se pudo obtener historial", ex);
        }
    }
}
