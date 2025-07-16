package ar.edu.davinci.excusas.model.excusas;

public enum MotivoExcusa {
    QUEDARSE_DORMIDO("Me quedé dormido"),
    PERDI_TRANSPORTE("Perdí el transporte"),
    PERDIDA_SUMINISTRO("Pérdida de suministro eléctrico"),
    CUIDADO_FAMILIAR("Cuidado familiar"),
    IRRELEVANTE("Motivo irrelevante"),
    INCREIBLE_INVEROSIMIL("Increíble e inverosímil");

    private final String descripcion;

    MotivoExcusa(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
