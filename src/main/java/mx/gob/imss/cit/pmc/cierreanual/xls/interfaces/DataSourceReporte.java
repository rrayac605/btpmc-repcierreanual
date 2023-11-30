package mx.gob.imss.cit.pmc.cierreanual.xls.interfaces;


import mx.gob.imss.cit.pmc.cierreanual.xls.model.ReporteCierreAnualRequestModel;

public interface DataSourceReporte <O> {

    O fillData(ReporteCierreAnualRequestModel request);


}
