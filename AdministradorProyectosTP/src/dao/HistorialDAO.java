package dao;

import model.HistorialEstado;
import java.util.List;

public interface HistorialDAO {
    void registrar(HistorialEstado h) throws DAOException;
    List<HistorialEstado> obtenerPorTarea(int tareaId) throws DAOException;
}
