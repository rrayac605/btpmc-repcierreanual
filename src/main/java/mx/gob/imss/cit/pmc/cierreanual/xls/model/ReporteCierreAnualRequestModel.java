package mx.gob.imss.cit.pmc.cierreanual.xls.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReporteCierreAnualRequestModel {

    private int ooad;
    private List<Integer> subDelegacion;
    private int cicloActual;
    private String tipoReporte;

    private List<ReporteOOADModel> datosDelegaciones;

}
