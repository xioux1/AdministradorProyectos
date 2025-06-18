package dao;

import java.util.List;

public interface AsignacionDAO {
    void asignar(int empleadoId, int proyectoId) throws DAOException;
    void desasignar(int empleadoId, int proyectoId) throws DAOException;
    List<Integer> empleadosPorProyecto(int proyectoId) throws DAOException;
    List<Integer> empleadosLibres() throws DAOException;
}
