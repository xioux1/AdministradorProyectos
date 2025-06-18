package dao.jdbc;

import dao.DAOException;
import dao.TareaDAO;
import model.Tarea;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcTareaDAO implements TareaDAO {

    private final Connection conn;

    public JdbcTareaDAO(Connection conn) throws DAOException {
        this.conn = conn;
        try {
            crearTablaSiNoExiste();
        } catch (SQLException e) {
            throw new DAOException("Error al inicializar la tabla «tarea»", e);
        }
    }

    @Override
    public void crear(Tarea t) throws DAOException {
        String sql = "INSERT INTO tarea(titulo, descripcion, horas_est, horas_real, inicio_sprint, fin_sprint, estado, proyecto_id, empleado_id, costo_hora) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, t.getTitulo());
            ps.setString(2, t.getDescripcion());
            ps.setInt(3, t.getHorasEstimadas());
            ps.setInt(4, t.getHorasReales());
            if (t.getInicioSprint() != null)
                ps.setDate(5, Date.valueOf(t.getInicioSprint()));
            else
                ps.setNull(5, Types.DATE);
            if (t.getFinSprint() != null)
                ps.setDate(6, Date.valueOf(t.getFinSprint()));
            else
                ps.setNull(6, Types.DATE);
            ps.setString(7, t.getEstado() != null ? t.getEstado().name() : null);
            ps.setInt(8, t.getProyectoId());
            ps.setInt(9, t.getEmpleadoId());
            ps.setInt(10, t.getCostoHora());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    t.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error al crear tarea", e);
        }
    }

    @Override
    public void actualizar(Tarea t) throws DAOException {
        String sql = "UPDATE tarea SET titulo=?, descripcion=?, horas_est=?, horas_real=?, inicio_sprint=?, fin_sprint=?, estado=?, proyecto_id=?, empleado_id=?, costo_hora=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, t.getTitulo());
            ps.setString(2, t.getDescripcion());
            ps.setInt(3, t.getHorasEstimadas());
            ps.setInt(4, t.getHorasReales());
            if (t.getInicioSprint() != null)
                ps.setDate(5, Date.valueOf(t.getInicioSprint()));
            else
                ps.setNull(5, Types.DATE);
            if (t.getFinSprint() != null)
                ps.setDate(6, Date.valueOf(t.getFinSprint()));
            else
                ps.setNull(6, Types.DATE);
            ps.setString(7, t.getEstado() != null ? t.getEstado().name() : null);
            ps.setInt(8, t.getProyectoId());
            ps.setInt(9, t.getEmpleadoId());
            ps.setInt(10, t.getCostoHora());
            ps.setInt(11, t.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error al actualizar tarea", e);
        }
    }

    @Override
    public void actualizarEstado(int id, model.EstadoTarea estado) throws DAOException {
        String sql = "UPDATE tarea SET estado=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, estado.name());
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error al actualizar estado", e);
        }
    }

    @Override
    public void eliminar(int id) throws DAOException {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM tarea WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error al eliminar tarea", e);
        }
    }

    @Override
    public List<Tarea> obtenerTodas() throws DAOException {
        List<Tarea> lista = new ArrayList<>();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM tarea")) {
            while (rs.next()) {
                lista.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new DAOException("Error al listar tareas", e);
        }
        return lista;
    }

    @Override
    public Optional<Tarea> obtenerPorId(int id) throws DAOException {
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM tarea WHERE id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error al buscar tarea", e);
        }
        return Optional.empty();
    }

    private void crearTablaSiNoExiste() throws SQLException {
        String ddl = """
            CREATE TABLE IF NOT EXISTS tarea (
                id INT AUTO_INCREMENT PRIMARY KEY,
                titulo VARCHAR(255) NOT NULL,
                descripcion VARCHAR(1024),
                horas_est INT,
                horas_real INT,
                inicio_sprint DATE,
                fin_sprint DATE,
                estado VARCHAR(20),
                proyecto_id INT,
                empleado_id INT,
                costo_hora INT
            )
        """;
        try (Statement st = conn.createStatement()) {
            st.executeUpdate(ddl);
        }
    }

    private Tarea mapRow(ResultSet rs) throws SQLException {
        return new Tarea(
                rs.getInt("id"),
                rs.getString("titulo"),
                rs.getString("descripcion"),
                rs.getInt("horas_est"),
                rs.getInt("horas_real"),
                rs.getDate("inicio_sprint") != null ? rs.getDate("inicio_sprint").toLocalDate() : null,
                rs.getDate("fin_sprint") != null ? rs.getDate("fin_sprint").toLocalDate() : null,
                rs.getString("estado") != null ? model.EstadoTarea.valueOf(rs.getString("estado")) : null,
                rs.getInt("proyecto_id"),
                rs.getInt("empleado_id"),
                rs.getInt("costo_hora")
        );
    }
}
