package dao;

import model.Empleado;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class InMemoryAsignacionDAO implements AsignacionDAO {

    private final Map<Integer, Set<Integer>> asignaciones = new ConcurrentHashMap<>();
    private final EmpleadoDAO empleadoDao;

    public InMemoryAsignacionDAO(EmpleadoDAO empleadoDao) {
        this.empleadoDao = empleadoDao;
    }

    @Override
    public void asignar(int empleadoId, int proyectoId) throws DAOException {
        try {
            asignaciones
                .computeIfAbsent(proyectoId, k -> ConcurrentHashMap.newKeySet())
                .add(empleadoId);
        } catch (Exception e) {
            throw new DAOException("Error al asignar en memoria", e);
        }
    }

    @Override
    public void desasignar(int empleadoId, int proyectoId) throws DAOException {
        try {
            Set<Integer> set = asignaciones.get(proyectoId);
            if (set != null) {
                set.remove(empleadoId);
                if (set.isEmpty()) {
                    asignaciones.remove(proyectoId);
                }
            }
        } catch (Exception e) {
            throw new DAOException("Error al desasignar en memoria", e);
        }
    }

    @Override
    public List<Integer> empleadosPorProyecto(int proyectoId) throws DAOException {
        try {
            Set<Integer> set = asignaciones.get(proyectoId);
            if (set == null) return List.of();
            return new ArrayList<>(set);
        } catch (Exception e) {
            throw new DAOException("Error al listar asignados en memoria", e);
        }
    }

    @Override
    public List<Integer> empleadosLibres() throws DAOException {
        try {
            List<Integer> todos = empleadoDao.obtenerTodas().stream()
                    .map(Empleado::getId)
                    .toList();
            Set<Integer> asignados = asignaciones.values().stream()
                    .flatMap(Set::stream)
                    .collect(java.util.stream.Collectors.toSet());
            return todos.stream()
                    .filter(id -> !asignados.contains(id))
                    .toList();
        } catch (Exception e) {
            throw new DAOException("Error al obtener libres en memoria", e);
        }
    }
}
