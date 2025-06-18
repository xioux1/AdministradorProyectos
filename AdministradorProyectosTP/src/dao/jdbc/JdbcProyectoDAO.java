package dao.jdbc;

import dao.DAOException;
import dao.ProyectoDAO;
import model.Proyecto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcProyectoDAO implements ProyectoDAO {

    private final Connection conn;

    public JdbcProyectoDAO(Connection conn) throws DAOException {
        this.conn = conn;
        try {
            crearTablaSiNoExiste();
        } catch (SQLException e) {
            throw new DAOException("Error al inicializar la tabla «proyecto»", e);
        }
    }

    @Override
    public void crear(Proyecto p) throws DAOException {
        String sql = "INSERT INTO proyecto(nombre) VALUES (?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getNombre());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    p.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error al crear proyecto", e);
        }
    }

    @Override
    public void actualizar(Proyecto p) throws DAOException {
        String sql = "UPDATE proyecto SET nombre=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getNombre());
            ps.setInt(2, p.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error al actualizar proyecto", e);
        }
    }

    @Override
    public void eliminar(int id) throws DAOException {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM proyecto WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error al eliminar proyecto", e);
        }
    }

    @Override
    public List<Proyecto> obtenerTodas() throws DAOException {
        List<Proyecto> lista = new ArrayList<>();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM proyecto")) {
            while (rs.next()) {
                lista.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new DAOException("Error al listar proyectos", e);
        }
        return lista;
    }

    @Override
    public Optional<Proyecto> obtenerPorId(int id) throws DAOException {
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM proyecto WHERE id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error al buscar proyecto", e);
        }
        return Optional.empty();
    }

    private void crearTablaSiNoExiste() throws SQLException {
        String ddl = """
            CREATE TABLE IF NOT EXISTS proyecto (
                id INT AUTO_INCREMENT PRIMARY KEY,
                nombre VARCHAR(255) NOT NULL
            )
        """;
        try (Statement st = conn.createStatement()) {
            st.executeUpdate(ddl);
        }
    }

    private Proyecto mapRow(ResultSet rs) throws SQLException {
        return new Proyecto(
                rs.getInt("id"),
                rs.getString("nombre")
        );
    }
}
