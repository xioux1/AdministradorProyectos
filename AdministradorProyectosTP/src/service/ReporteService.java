package service;

public interface ReporteService {
    class CostoProyecto {
        public final int proyectoId;
        public int horas;
        public int costo;
        public CostoProyecto(int proyectoId, int horas, int costo) {
            this.proyectoId = proyectoId;
            this.horas = horas;
            this.costo = costo;
        }
    }

    java.util.List<CostoProyecto> resumenCostos() throws ServiceException;
}
