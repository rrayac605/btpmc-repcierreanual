package mx.gob.imss.cit.pmc.cierreanual.xls.enums;

import lombok.Getter;

@Getter
public enum CausisticaOtrosAniosRevisionTIpoRiesgoEnum {

    DELEGACION("delegacion"),
    CASOSPA("casosPA"),
    DIASSUBSIDIADOSPA("diasSubsidiadosPA"),
    INCAPACIDADESPERMANENTESPA("incapacidadesPermanentesPA"),
    DEFUNCIONESPA("defuncionesPA"),
    CASOSPP("casosPP"),
    DIASSUBSIDIADOSPP("diasSubsidiadosPP"),
    INCAPACIDADESPERMANENTESPP("incapacidadesPermanentesPP"),
    DEFUNCIONESPP("defuncionesPP"),
    CASOSTOTAL("casosTotal"),
    DIASSUBSIDIADOSTOTAL("diasSubsidiadosTotal"),
    INCAPACIDADESPERMANENTESTOTAL("incapacidadesPermanentesTotal"),
    DEFUNCIONESTOTAL("defuncionesTotal");

    private final String name;

    CausisticaOtrosAniosRevisionTIpoRiesgoEnum(String name){
        this.name = name;
    }

}
