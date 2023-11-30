package mx.gob.imss.cit.pmc.cierreanual.repository.impl;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document("MCB_RESPALDO_RIESGOS_DESCARTADOS_IMSS")
public class RespaldosRiesgosDescartadosRFC {
    private String _id;
    private String objectIdOrigen;
    private String nss;
    private String ooadNSS;
    private String subDelegacionNss;
    private String umfNSS;
    private String registroPatronal;
    private String ooadRegistroPatronal;
    private String subDelRegistroPatronal;
    private String claveConsecuencia;
    private String descripcionConsecuencia;
    private String fechaInicioAccidente;
    private String fechaFinTermino;
    private String tipoRiesgo;
    private String porcentajeIncapacidad;
    private String nombreAsegurado;
    private String curpAsegurado;
    private String anioEjecucionRespaldo;
    private String claveEstadoRiesgo;
    private String descripcionEstadoRiesgo;
    private String origen;
    private String anioRevision;
    private String anioCicloCaso;
    private  int diasSubsidiados;
    private String key;

}
