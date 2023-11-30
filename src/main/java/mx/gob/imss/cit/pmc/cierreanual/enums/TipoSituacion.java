package mx.gob.imss.cit.pmc.cierreanual.enums;

import lombok.Getter;

@Getter
public enum TipoSituacion
{

    APROBADO(1),
    PENDIENTE_APROBAR(2),
    RECHAZADO(3);
    int valor;
    private TipoSituacion(int valor){
        this.valor = valor;
    }



}
