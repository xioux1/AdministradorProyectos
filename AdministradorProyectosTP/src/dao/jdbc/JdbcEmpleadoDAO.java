package dao.jdbc;

import dao.DAOException;
import dao.EmpleadoDAO;
import model.Empleado;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcEmpleadoDAO implements EmpleadoDAO {

    private final Connection conn;

    public JdbcEmpleadoDAO(Connection conn) throws DAOException {
        this.conn = conn;
        try {
            crearTablaSiNoExiste();
        } catch (SQLException e) {
            throw new DAOException("Error al inicializar la tabla «empleado»", e);
        }
    }

    @Override
    public void crear(Empleado e) throws DAOException {
        String sql = "INSERT INTO empleado(nombre, costo_hora) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, e.getNombre());
            ps.setInt(2, e.getCostoHora());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    e.setId(rs.getInt(1));
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Error al crear empleado", ex);
        }
    }

    @Override
    public void actualizar(Empleado e) throws DAOException {
        String sql = "UPDATE empleado SET nombre=?, costo_hora=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, e.getNombre());
            ps.setInt(2, e.getCostoHora());
            ps.setInt(3, e.getId());
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new DAOException("Error al actualizar empleado", ex);
        }
    }

    @Override
    public void eliminar(int id) throws DAOException {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM empleado WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new DAOException("Error al eliminar empleado", ex);
        }
    }

    @Override
    public List<Empleado> obtenerTodas() throws DAOException {
        List<Empleado> lista = new ArrayList<>();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM empleado")) {
            while (rs.next()) {
                lista.add(mapRow(rs));
            }
        } catch (SQLException ex) {
            throw new DAOException("Error al listar empleados", ex);
        }
        return lista;
    }

    @Override
    public Optional<Empleado> obtenerPorId(int id) throws DAOException {
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM empleado WHERE id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Error al buscar empleado", ex);
        }
        return Optional.empty();
    }

    private void crearTablaSiNoExiste() throws SQLException {
        String ddl = """
            CREATE TABLE IF NOT EXISTS empleado (
                id INT AUTO_INCREMENT PRIMARY KEY,
                nombre VARCHAR(255) NOT NULL,
                costo_hora INT
            )
        """;
        try (Statement st = conn.createStatement()) {
            st.executeUpdate(ddl);
        }
    }

    private Empleado mapRow(ResultSet rs) throws SQLException {
        return new Empleado(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getInt("costo_hora")
        );
    }
}
