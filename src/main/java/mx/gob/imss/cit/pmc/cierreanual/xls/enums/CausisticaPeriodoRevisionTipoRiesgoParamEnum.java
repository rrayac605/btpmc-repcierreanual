package mx.gob.imss.cit.pmc.cierreanual.xls.enums;

import lombok.Getter;

@Getter
public enum CausisticaPeriodoRevisionTipoRiesgoParamEnum {


    DELEGACION("delegacion"),
    ANIO("anio"),
    CASOSAET("casosAET"),
    DIASSUBSIDIADOSAET("diasSubsidiadosAET"),
    INCAPACIDADESPERMANENTESAET("incapacidadesPermanentesAET"),
    DEFUNCIONESAET("defuncionesAET"),
    CASOSAT("casosAT"),
    DIASSUBSIDIADOSAT("diasSubsidiadosAT"),
    INCAPACIDADESPERMANENTESAT("incapacidadesPermanentesAT"),
    DEFUNCIONESAT("defuncionesAT"),
    TOTALCASOS("totalCasos"),
    TOTALDIASSUBSIDIADOS("totalDiasSubsidiados"),
    TOTALINCAPACIDADESPERMANENTES("totalIncapacidadesPermanentes"),
    TOTALDEFUNCIONES("totalDefunciones");

    private final String name;

    CausisticaPeriodoRevisionTipoRiesgoParamEnum(String name){
        this.name = name;
    }

}
