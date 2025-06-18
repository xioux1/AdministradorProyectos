package service;

import model.Empleado;

import java.util.List;

public interface AsignacionService {
    void asignar(int empleadoId, int proyectoId) throws ServiceException;
    void desasignar(int empleadoId, int proyectoId) throws ServiceException;
    List<Empleado> empleadosDelProyecto(int proyectoId) throws ServiceException;
    List<Empleado> empleadosLibres() throws ServiceException;
}
