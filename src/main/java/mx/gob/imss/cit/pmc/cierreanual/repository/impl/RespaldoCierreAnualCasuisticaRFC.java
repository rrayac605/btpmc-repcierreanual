package mx.gob.imss.cit.pmc.cierreanual.repository.impl;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@Document("MCB_RESPALDO_CIERRE_ANUAL_CASUISTICA_IMSS")
public class RespaldoCierreAnualCasuisticaRFC {
    @Id
    private String oid;
    private String nss;
    private int OOADNSS;
    private Integer[] subDelegacionNSS;
    private int UMFNSS;
    private String registroPatronal;
    private int OOADRegistroPatronal;
    private String regPatronal;
    private Integer[] subDelRegistroPatronal;
    private int cveConsecuencia;
    private String descripcionConsecuencia;
    private Date fechaInicioAccidente;
    private int diasSubsidiados;
    private Date fechaFinTermino;
    private int tipoRiesgo;
    private int porcentajeIncapacidad;
    private String nombreAsegurado;
    private String CURPAsegurado;
    private int anioEjecucionRespaldo;
    private int claveEstadoRiesgo;
    private String descripcionEstadoRiesgo;
    private int claveSituacionRiesgo;
    private String descripcionSituacionRiesgo;
    private String origen;
    private int anioRevision;
    private int anioCicloCaso;
    private String razonSocial;
}
