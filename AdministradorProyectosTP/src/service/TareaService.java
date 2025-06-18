package service;

import model.Tarea;
import validacion.ValidacionException;

import java.time.LocalDate;
import java.util.List;

public interface TareaService {

    void alta(String titulo, String desc, int hEst, int hReal,
              LocalDate inicio, LocalDate fin, model.EstadoTarea estado,
              int proyectoId, int empleadoId, int costoHora)
            throws ValidacionException, ServiceException;

    void modificar(int id, String titulo, String desc, int hEst, int hReal,
                   LocalDate inicio, LocalDate fin, model.EstadoTarea estado,
                   int proyectoId, int empleadoId, int costoHora)
            throws ValidacionException, ServiceException;

    void cambiarEstado(int id, model.EstadoTarea estado) throws ServiceException;

    void baja(int id) throws ServiceException;
    List<Tarea> listado() throws ServiceException;
    Tarea consulta(int id) throws ServiceException;
    java.util.List<model.HistorialEstado> historial(int tareaId) throws ServiceException;
}
