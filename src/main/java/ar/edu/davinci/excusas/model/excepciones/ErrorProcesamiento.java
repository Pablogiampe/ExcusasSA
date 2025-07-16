package ar.edu.davinci.excusas.model.excepciones;

public class ErrorProcesamiento extends ExcusaSAException {
    
    public ErrorProcesamiento(String mensaje) {
        super(mensaje);
    }
    
    public ErrorProcesamiento(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
