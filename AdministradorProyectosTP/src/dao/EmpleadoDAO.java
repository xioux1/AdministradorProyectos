package dao;

import model.Empleado;
import java.util.List;
import java.util.Optional;

public interface EmpleadoDAO {
    void crear(Empleado empleado) throws DAOException;
    void actualizar(Empleado empleado) throws DAOException;
    void eliminar(int id) throws DAOException;
    List<Empleado> obtenerTodas() throws DAOException;
    Optional<Empleado> obtenerPorId(int id) throws DAOException;
}
