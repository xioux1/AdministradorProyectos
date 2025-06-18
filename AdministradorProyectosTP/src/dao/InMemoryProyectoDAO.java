package dao;

import model.Proyecto;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public final class InMemoryProyectoDAO implements ProyectoDAO {

    private final List<Proyecto> proyectos = new CopyOnWriteArrayList<>();
    private final AtomicInteger nextId = new AtomicInteger(1);

    @Override
    public void crear(Proyecto proyecto) throws DAOException {
        try {
            proyecto.setId(nextId.getAndIncrement());
            proyectos.add(proyecto);
        } catch (Exception e) {
            throw new DAOException("Error al crear proyecto en memoria", e);
        }
    }

    @Override
    public void actualizar(Proyecto proyecto) throws DAOException {
        try {
            proyectos.replaceAll(p -> p.getId() == proyecto.getId() ? proyecto : p);
        } catch (Exception e) {
            throw new DAOException("Error al actualizar proyecto en memoria", e);
        }
    }

    @Override
    public void eliminar(int id) throws DAOException {
        try {
            proyectos.removeIf(p -> p.getId() == id);
        } catch (Exception e) {
            throw new DAOException("Error al eliminar proyecto en memoria", e);
        }
    }

    @Override
    public List<Proyecto> obtenerTodas() throws DAOException {
        try {
            return List.copyOf(proyectos);
        } catch (Exception e) {
            throw new DAOException("Error al listar proyectos en memoria", e);
        }
    }

    @Override
    public Optional<Proyecto> obtenerPorId(int id) throws DAOException {
        try {
            return proyectos.stream().filter(p -> p.getId() == id).findFirst();
        } catch (Exception e) {
            throw new DAOException("Error al buscar proyecto en memoria", e);
        }
    }
}
