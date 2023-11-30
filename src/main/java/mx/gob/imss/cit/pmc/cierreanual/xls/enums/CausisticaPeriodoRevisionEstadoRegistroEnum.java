package mx.gob.imss.cit.pmc.cierreanual.xls.enums;

import lombok.Getter;

@Getter
public enum CausisticaPeriodoRevisionEstadoRegistroEnum {

    DELEGACION("delegacion"),
    ANIO("anio"),
    CORRECTOS("correctos"),
    CORRECTOSOTRASDELEGACIONES("correctosOtrasDelegaciones"),
    SUSCEPTIBLESAJUSTE("susceptiblesAjuste"),
    SUSCEPTIBLEAJUSTEOTRASDELEGACIONES("susceptibleAjusteOtrasDelegaciones"),
    TOTAL("total");

    private final String name;

    CausisticaPeriodoRevisionEstadoRegistroEnum(String name){
        this.name = name;
    }

}
