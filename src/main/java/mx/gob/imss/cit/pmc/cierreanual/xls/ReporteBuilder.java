package mx.gob.imss.cit.pmc.cierreanual.xls;


import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import mx.gob.imss.cit.pmc.cierreanual.xls.interfaces.DataSourceReporte;
import mx.gob.imss.cit.pmc.cierreanual.xls.interfaces.ReporteInterface;
import mx.gob.imss.cit.pmc.cierreanual.xls.model.ReporteCierreAnualRequestModel;
import mx.gob.imss.cit.pmc.cierreanual.xls.model.ReporteOOADModel;
import org.springframework.core.io.InputStreamResource;

import java.io.IOException;
import java.util.List;

@Log4j2
public class ReporteBuilder extends Thread {

    private ReporteInterface reporteInterface;

    private DataSourceReporte dataSourceReporte;

    private ReporteCierreAnualRequestModel requestModel;

    private String nombre;

    @Getter
    private boolean rfc;


    @Getter
    private ReporteOOADModel reporteOOADModel;

    public ReporteBuilder(ReporteInterface reporteInterface,DataSourceReporte repository, ReporteCierreAnualRequestModel requestModel, String nombre, boolean rfc){
        this.reporteInterface = reporteInterface;
        this.dataSourceReporte = repository;
        this.requestModel = requestModel;
        this.nombre = nombre;
        this.rfc = rfc;
    }

    public InputStreamResource generarReporte() throws Exception {

        Object input = dataSourceReporte.fillData(requestModel);
        if(input instanceof  ReporteOOADModel){
            this.reporteOOADModel = (ReporteOOADModel) input;
            this.reporteOOADModel.setOoad(requestModel.getOoad());
            this.reporteOOADModel.setListaDelegaciones(requestModel.getSubDelegacion());
        }

        return  this.reporteInterface.create(input,nombre,rfc);
    }

    @Override
    public void run() {
        try {
            generarReporte();
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error(e);
            e.printStackTrace();
        }
    }
}
