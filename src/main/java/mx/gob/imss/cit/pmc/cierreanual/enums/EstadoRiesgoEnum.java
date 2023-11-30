package mx.gob.imss.cit.pmc.cierreanual.enums;

import lombok.Getter;

@Getter
public enum EstadoRiesgoEnum {

    ERRONEOS(2),
    SUSCEPTIBLES_DE_AJUSTE_OTRAS_DELEGACIONES(8),
    BAJA_OTRAS_DELEGACIONES(11),
    CORRECTOS(1),
    SUSCEPTIBLES_DE_AJUSTE(4),
    DUPLICADOS_OTRAS_DELEGACIONES(7),
    BAJA(10),
    CORRECTOS_OTRAS_DELEGACIONES(5),
    ERRONEOS_OTRAS_DELEGACIONES(6),
    DUPLICADOS(3);
    int valor;
    private EstadoRiesgoEnum(int valor){
        this.valor = valor;
    }
}
