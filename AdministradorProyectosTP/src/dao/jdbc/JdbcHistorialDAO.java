package dao.jdbc;

import dao.DAOException;
import dao.HistorialDAO;
import model.EstadoTarea;
import model.HistorialEstado;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JdbcHistorialDAO implements HistorialDAO {
    private final Connection conn;

    public JdbcHistorialDAO(Connection conn) throws DAOException {
        this.conn = conn;
        try {
            crearTabla();
        } catch (SQLException e) {
            throw new DAOException("Error al inicializar historial", e);
        }
    }

    @Override
    public void registrar(HistorialEstado h) throws DAOException {
        String sql = "INSERT INTO tarea_historial(tarea_id, estado, responsable, fecha) VALUES (?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, h.getTareaId());
            ps.setString(2, h.getEstado().name());
            ps.setString(3, h.getResponsable());
            ps.setTimestamp(4, Timestamp.valueOf(h.getFecha()));
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) h.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new DAOException("Error al registrar historial", e);
        }
    }

    @Override
    public List<HistorialEstado> obtenerPorTarea(int tareaId) throws DAOException {
        List<HistorialEstado> list = new ArrayList<>();
        String sql = "SELECT * FROM tarea_historial WHERE tarea_id=? ORDER BY fecha";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, tareaId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error al listar historial", e);
        }
        return list;
    }

    private void crearTabla() throws SQLException {
        String ddl = """
            CREATE TABLE IF NOT EXISTS tarea_historial (
                id INT AUTO_INCREMENT PRIMARY KEY,
                tarea_id INT,
                estado VARCHAR(20),
                responsable VARCHAR(255),
                fecha TIMESTAMP
            )
        """;
        try (Statement st = conn.createStatement()) {
            st.executeUpdate(ddl);
        }
    }

    private HistorialEstado mapRow(ResultSet rs) throws SQLException {
        return new HistorialEstado(
                rs.getInt("id"),
                rs.getInt("tarea_id"),
                EstadoTarea.valueOf(rs.getString("estado")),
                rs.getString("responsable"),
                rs.getTimestamp("fecha").toLocalDateTime()
        );
    }
}
