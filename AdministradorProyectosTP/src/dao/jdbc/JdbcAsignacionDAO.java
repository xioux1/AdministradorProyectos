package dao.jdbc;

import dao.AsignacionDAO;
import dao.DAOException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcAsignacionDAO implements AsignacionDAO {
    private final Connection conn;

    public JdbcAsignacionDAO(Connection conn) throws DAOException {
        this.conn = conn;
        try {
            crearTabla();
        } catch (SQLException e) {
            throw new DAOException("Error al inicializar asignacion", e);
        }
    }

    @Override
    public void asignar(int empleadoId, int proyectoId) throws DAOException {
        String sql = "INSERT INTO empleado_proyecto(empleado_id, proyecto_id) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, empleadoId);
            ps.setInt(2, proyectoId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error al asignar", e);
        }
    }

    @Override
    public void desasignar(int empleadoId, int proyectoId) throws DAOException {
        String sql = "DELETE FROM empleado_proyecto WHERE empleado_id=? AND proyecto_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, empleadoId);
            ps.setInt(2, proyectoId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error al desasignar", e);
        }
    }

    @Override
    public List<Integer> empleadosPorProyecto(int proyectoId) throws DAOException {
        List<Integer> ids = new ArrayList<>();
        String sql = "SELECT empleado_id FROM empleado_proyecto WHERE proyecto_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, proyectoId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) ids.add(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new DAOException("Error al listar asignados", e);
        }
        return ids;
    }

    @Override
    public List<Integer> empleadosLibres() throws DAOException {
        List<Integer> libres = new ArrayList<>();
        String sql = "SELECT id FROM empleado WHERE id NOT IN (SELECT empleado_id FROM empleado_proyecto)";
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) libres.add(rs.getInt(1));
        } catch (SQLException e) {
            throw new DAOException("Error al obtener libres", e);
        }
        return libres;
    }

    private void crearTabla() throws SQLException {
        String ddl = """
            CREATE TABLE IF NOT EXISTS empleado_proyecto (
                empleado_id INT,
                proyecto_id INT,
                PRIMARY KEY (empleado_id, proyecto_id)
            )
        """;
        try (Statement st = conn.createStatement()) {
            st.executeUpdate(ddl);
        }
    }
}
