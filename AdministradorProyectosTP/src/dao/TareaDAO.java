package dao;

import model.Tarea;
import java.util.List;
import java.util.Optional;

public interface TareaDAO {

    void crear(Tarea tarea)                   throws DAOException;
    void actualizar(Tarea tarea)              throws DAOException;
    void eliminar(int id)                     throws DAOException;
    List<Tarea> obtenerTodas()                throws DAOException;
    Optional<Tarea> obtenerPorId(int id)      throws DAOException;
}
