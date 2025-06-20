package service;

import dao.DAOException;
import dao.TareaDAO;
import dao.HistorialDAO;
import dao.ProyectoDAO;
import dao.EmpleadoDAO;
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
    private final ProyectoDAO proyectoDao;
    private final EmpleadoDAO empleadoDao;

    public TareaServiceImpl(TareaDAO dao, HistorialDAO historialDao,
                            ProyectoDAO proyectoDao, EmpleadoDAO empleadoDao) {
        this.dao = dao;
        this.historialDao = historialDao;
        this.proyectoDao = proyectoDao;
        this.empleadoDao = empleadoDao;
    }

    // ------------------------------------ CRUD

    @Override
    public void alta(String titulo, String desc, int hEst, int hReal,
                     LocalDate inicio, LocalDate fin, model.EstadoTarea estado,
                     int proyectoId, int empleadoId)
            throws ValidacionException, ServiceException {

        ValidadorDeErrores.validarTarea(titulo, hEst, hReal);
        try {
            Proyecto proyecto = proyectoDao.obtenerPorId(proyectoId)
                    .orElseThrow(() -> new ServiceException("Proyecto inexistente"));
            Empleado empleado = empleadoDao.obtenerPorId(empleadoId)
                    .orElseThrow(() -> new ServiceException("Empleado inexistente"));

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
                          int proyectoId, int empleadoId)
            throws ValidacionException, ServiceException {

        ValidadorDeErrores.validarTarea(titulo, hEst, hReal);
        try {
            Tarea previa = dao.obtenerPorId(id).orElse(null);
            Proyecto proyecto = proyectoDao.obtenerPorId(proyectoId)
                    .orElseThrow(() -> new ServiceException("Proyecto inexistente"));
            Empleado empleado = empleadoDao.obtenerPorId(empleadoId)
                    .orElseThrow(() -> new ServiceException("Empleado inexistente"));

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
