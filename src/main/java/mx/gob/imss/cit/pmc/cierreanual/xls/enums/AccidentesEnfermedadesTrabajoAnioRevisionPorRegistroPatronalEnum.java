package mx.gob.imss.cit.pmc.cierreanual.xls.enums;

import lombok.Getter;

@Getter
public enum AccidentesEnfermedadesTrabajoAnioRevisionPorRegistroPatronalEnum {

    DELEGACION("delegacion"),
    ANIO("anio"),
    REGISTROPATRONAL("registroPatronal"),
    RAZONSOCIAL("razonSocial"),
    CASOS("casos"),
    DIASSUBSIDIADOS("diasSubsidiados"),
    INCAPACIDADESPERMANENTES("incapacidadesPermanentes"),
    DEFUNCIONES("defunciones");

    private final String name;

    AccidentesEnfermedadesTrabajoAnioRevisionPorRegistroPatronalEnum(String name){
        this.name = name;
    }

}
