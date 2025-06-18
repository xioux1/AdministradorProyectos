package dao;

import model.Empleado;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public final class InMemoryEmpleadoDAO implements EmpleadoDAO {

    private final List<Empleado> empleados = new CopyOnWriteArrayList<>();
    private final AtomicInteger nextId = new AtomicInteger(1);

    @Override
    public void crear(Empleado empleado) throws DAOException {
        try {
            empleado.setId(nextId.getAndIncrement());
            empleados.add(empleado);
        } catch (Exception e) {
            throw new DAOException("Error al crear empleado en memoria", e);
        }
    }

    @Override
    public void actualizar(Empleado empleado) throws DAOException {
        try {
            empleados.replaceAll(p -> p.getId() == empleado.getId() ? empleado : p);
        } catch (Exception e) {
            throw new DAOException("Error al actualizar empleado en memoria", e);
        }
    }

    @Override
    public void eliminar(int id) throws DAOException {
        try {
            empleados.removeIf(p -> p.getId() == id);
        } catch (Exception e) {
            throw new DAOException("Error al eliminar empleado en memoria", e);
        }
    }

    @Override
    public List<Empleado> obtenerTodas() throws DAOException {
        try {
            return List.copyOf(empleados);
        } catch (Exception e) {
            throw new DAOException("Error al listar empleados en memoria", e);
        }
    }

    @Override
    public Optional<Empleado> obtenerPorId(int id) throws DAOException {
        try {
            return empleados.stream().filter(p -> p.getId() == id).findFirst();
        } catch (Exception e) {
            throw new DAOException("Error al buscar empleado en memoria", e);
        }
    }
}
