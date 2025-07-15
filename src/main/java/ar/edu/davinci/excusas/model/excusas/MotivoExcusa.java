package ar.edu.davinci.excusas.model.excusas;

public enum MotivoExcusa {
    QUEDARSE_DORMIDO("Me quedé dormido"),
    PERDI_TRANSPORTE("Perdí el transporte"),
    OLVIDE_ALGO("Olvidé algo importante"),
    CUIDADO_FAMILIAR("Cuidado familiar"),
    PERDIDA_SUMINISTRO("Pérdida de suministro"),
    INCREIBLE_INVEROSIMIL("Excusa increíble e inverosímil"),
    COMPLEJA_ELABORADA("Excusa compleja y elaborada");
    
    private final String descripcion;
    
    MotivoExcusa(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    @Override
    public String toString() {
        return descripcion;
    }
}
