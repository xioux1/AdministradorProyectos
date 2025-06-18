package dao;

import model.Proyecto;
import java.util.List;
import java.util.Optional;

public interface ProyectoDAO {
    void crear(Proyecto proyecto) throws DAOException;
    void actualizar(Proyecto proyecto) throws DAOException;
    void eliminar(int id) throws DAOException;
    List<Proyecto> obtenerTodas() throws DAOException;
    Optional<Proyecto> obtenerPorId(int id) throws DAOException;
}
