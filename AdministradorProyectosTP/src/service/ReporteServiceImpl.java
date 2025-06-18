package service;

import dao.DAOException;
import dao.TareaDAO;
import model.Tarea;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReporteServiceImpl implements ReporteService {
    private final TareaDAO tareaDao;

    public ReporteServiceImpl(TareaDAO tareaDao) {
        this.tareaDao = tareaDao;
    }

    @Override
    public List<CostoProyecto> resumenCostos() throws ServiceException {
        try {
            List<Tarea> tareas = tareaDao.obtenerTodas();
            Map<Integer, CostoProyecto> map = new HashMap<>();
            for (Tarea t : tareas) {
                CostoProyecto c = map.computeIfAbsent(t.getProyecto().getId(), k -> new CostoProyecto(k,0,0));
                c.horas += t.getHorasReales();
                c.costo += t.getHorasReales() * t.getEmpleado().getCostoHora();
            }
            return new ArrayList<>(map.values());
        } catch (DAOException e) {
            throw new ServiceException("No se pudo generar reporte", e);
        }
    }
}
