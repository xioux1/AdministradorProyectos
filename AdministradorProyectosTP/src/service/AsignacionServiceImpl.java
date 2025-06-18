package service;

import dao.AsignacionDAO;
import dao.DAOException;
import dao.EmpleadoDAO;
import model.Empleado;

import java.util.ArrayList;
import java.util.List;

public class AsignacionServiceImpl implements AsignacionService {
    private final AsignacionDAO dao;
    private final EmpleadoDAO empleadoDao;

    public AsignacionServiceImpl(AsignacionDAO dao, EmpleadoDAO empleadoDao) {
        this.dao = dao;
        this.empleadoDao = empleadoDao;
    }

    @Override
    public void asignar(int empleadoId, int proyectoId) throws ServiceException {
        try {
            dao.asignar(empleadoId, proyectoId);
        } catch (DAOException e) {
            throw new ServiceException("No se pudo asignar", e);
        }
    }

    @Override
    public void desasignar(int empleadoId, int proyectoId) throws ServiceException {
        try {
            dao.desasignar(empleadoId, proyectoId);
        } catch (DAOException e) {
            throw new ServiceException("No se pudo desasignar", e);
        }
    }

    @Override
    public List<Empleado> empleadosDelProyecto(int proyectoId) throws ServiceException {
        try {
            List<Integer> ids = dao.empleadosPorProyecto(proyectoId);
            List<Empleado> res = new ArrayList<>();
            for (int id : ids) {
                empleadoDao.obtenerPorId(id).ifPresent(res::add);
            }
            return res;
        } catch (DAOException e) {
            throw new ServiceException("No se pudo obtener lista", e);
        }
    }

    @Override
    public List<Empleado> empleadosLibres() throws ServiceException {
        try {
            List<Integer> ids = dao.empleadosLibres();
            List<Empleado> res = new ArrayList<>();
            for (int id : ids) {
                empleadoDao.obtenerPorId(id).ifPresent(res::add);
            }
            return res;
        } catch (DAOException e) {
            throw new ServiceException("No se pudo obtener libres", e);
        }
    }
}
