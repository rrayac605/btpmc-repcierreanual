package mx.gob.imss.cit.pmc.cierreanual.xls.enums;

import lombok.Getter;

@Getter
public enum CausisticaPeriodoRevisionOtrosEstadosEnum {

    DELEGACION("delegacion"),
    ANIO("anio"),
    CORRECTOS("correctos"),
    CORRECTOSOTRASDELEGACIONES("correctosOtrasDelegaciones"),
    SUSCEPTIBLEAJUSTE("susceptibleAjuste"),
    SUSCEPTIBLEAJUSTEOTRASDELEGACIONES("susceptibleAjusteOtrasDelegaciones"),
    ERRONEOS("erroneos"),
    ERRONEOSOTRASDELEGACIONES("erroneosOtrasDelegaciones"),
    BAJAS("bajas"),
    BAJASOTRASDELEGACIONES("bajasOtrasDelegaciones"),
    DUPLICADOS("duplicados"),
    DUPLICADOSOTRASDELEGACIONES("duplicadosOtrasDelegaciones"),
    TOTAL("total");

    private final String name;

    CausisticaPeriodoRevisionOtrosEstadosEnum(String name){
        this.name = name;
    }
}
