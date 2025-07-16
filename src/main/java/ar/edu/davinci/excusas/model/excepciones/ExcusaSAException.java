package ar.edu.davinci.excusas.model.excepciones;

public class ExcusaSAException extends RuntimeException {
    
    public ExcusaSAException(String mensaje) {
        super(mensaje);
    }
    
    public ExcusaSAException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
