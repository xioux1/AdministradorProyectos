package dao;

import model.HistorialEstado;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public final class InMemoryHistorialDAO implements HistorialDAO {

    private final List<HistorialEstado> historiales = new CopyOnWriteArrayList<>();
    private final AtomicInteger nextId = new AtomicInteger(1);

    @Override
    public void registrar(HistorialEstado h) throws DAOException {
        try {
            h.setId(nextId.getAndIncrement());
            historiales.add(h);
        } catch (Exception e) {
            throw new DAOException("Error al registrar historial en memoria", e);
        }
    }

    @Override
    public List<HistorialEstado> obtenerPorTarea(int tareaId) throws DAOException {
        try {
            return historiales.stream()
                    .filter(h -> h.getTareaId() == tareaId)
                    .sorted(Comparator.comparing(HistorialEstado::getFecha))
                    .toList();
        } catch (Exception e) {
            throw new DAOException("Error al listar historial en memoria", e);
        }
    }
}
