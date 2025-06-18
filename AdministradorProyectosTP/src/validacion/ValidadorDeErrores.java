package validacion;

public final class ValidadorDeErrores {

    private ValidadorDeErrores() { }         

    public static void textoNoVacio(String txt, String campo) throws ValidacionException {
        if (txt == null || txt.isBlank()) {
            throw new ValidacionException("El campo «" + campo + "» no puede estar vacío.");
        }
    }

    public static void numeroNoNegativo(int n, String campo) throws ValidacionException {
        if (n < 0) {
            throw new ValidacionException("El campo «" + campo + "» no puede ser negativo.");
        }
    }

    public static void numeroEnRango(int n, int min, int max, String campo) throws ValidacionException {
        if (n < min || n > max) {
            throw new ValidacionException(
                "El campo «" + campo + "» debe estar entre " + min + " y " + max + "."
            );
        }
    }

    public static void validarTarea(String titulo,
                                    int horasEstimadas,
                                    int horasReales) throws ValidacionException {

        textoNoVacio(titulo, "Título");
        numeroNoNegativo(horasEstimadas, "Horas estimadas");
        numeroNoNegativo(horasReales, "Horas reales");
        if (horasReales > horasEstimadas) {
            throw new ValidacionException(
                "Las horas reales (" + horasReales + ") no pueden superar las estimadas (" + horasEstimadas + ").");
        }
    }
}
