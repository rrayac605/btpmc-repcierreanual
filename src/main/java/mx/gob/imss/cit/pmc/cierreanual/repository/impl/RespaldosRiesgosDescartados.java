package mx.gob.imss.cit.pmc.cierreanual.repository.impl;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document("MCB_RESPALDO_RIESGOS_DESCARTADOS")
public class RespaldosRiesgosDescartados {
    private String _id;
    private String objectIdOrigen;
    private String nss;
    private String OOADNss;
    private String subDelegacionNss;
    private String UMFNss;
    private String regPatronal;
    private String OOADRegistroPatroal;
    private String subDelRegistroPatronal;
    private String claveConsecuencia;
    private String descripcionConsecuencia;
    private String fechaInicioAccidente;
    private String fechaFinTermino;
    private String tipoRiesgo;
    private String porcentajeIncapacidad;
    private String nombreAsegurado;
    private String CURPAsegurado;
    private String anioEjecucionRespaldo;
    private String claveEstadoRiesgo;
    private String descripcionEstadoRiesgo;
    private String origen;
    private String anioRevision;
    private String anioCicloCaso;
    private  int diasSubsidiados;
    private String key;

}
