package dao;

import model.Tarea;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public final class InMemoryTareaDAO implements TareaDAO {

    private final List<Tarea> tareas = new CopyOnWriteArrayList<>();
    private final AtomicInteger nextId = new AtomicInteger(1);

    @Override
    public void crear(Tarea tarea) throws DAOException {
        try {
            tarea.setId(nextId.getAndIncrement());
            tareas.add(tarea);
        } catch (Exception e) {
            throw new DAOException("Error al crear tarea en memoria", e);
        }
    }

    @Override
    public void actualizar(Tarea tarea) throws DAOException {
        try {
            tareas.replaceAll(t -> t.getId() == tarea.getId() ? tarea : t);
        } catch (Exception e) {
            throw new DAOException("Error al actualizar tarea en memoria", e);
        }
    }

    @Override
    public void eliminar(int id) throws DAOException {
        try {
            tareas.removeIf(t -> t.getId() == id);
        } catch (Exception e) {
            throw new DAOException("Error al eliminar tarea en memoria", e);
        }
    }

    @Override
    public List<Tarea> obtenerTodas() throws DAOException {
        try {
            return List.copyOf(tareas);
        } catch (Exception e) {
            throw new DAOException("Error al listar tareas en memoria", e);
        }
    }

    @Override
    public Optional<Tarea> obtenerPorId(int id) throws DAOException {
        try {
            return tareas.stream()
                         .filter(t -> t.getId() == id)
                         .findFirst();
        } catch (Exception e) {
            throw new DAOException("Error al buscar tarea en memoria", e);
        }
    }

    @Override
    public void actualizarEstado(int id, model.EstadoTarea estado) throws DAOException {
        try {
            tareas.forEach(t -> { if (t.getId() == id) t.setEstado(estado); });
        } catch (Exception e) {
            throw new DAOException("Error al cambiar estado en memoria", e);
        }
    }
}
