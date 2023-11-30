package mx.gob.imss.cit.pmc.cierreanual.task;

import lombok.extern.log4j.Log4j2;
import mx.gob.imss.cit.pmc.cierreanual.constants.CierreAnualBatchConstants;
import mx.gob.imss.cit.pmc.cierreanual.dto.DelegacionOOADDTO;
import mx.gob.imss.cit.pmc.cierreanual.utils.DateUtils;
import mx.gob.imss.cit.pmc.cierreanual.xls.ReporteBuilder;
import mx.gob.imss.cit.pmc.cierreanual.xls.interfaces.DataSourceReporte;
import mx.gob.imss.cit.pmc.cierreanual.xls.interfaces.ReporteInterface;
import mx.gob.imss.cit.pmc.cierreanual.xls.model.ReporteCierreAnualRequestModel;
import mx.gob.imss.cit.pmc.cierreanual.xls.model.ReporteOOADModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
@StepScope
@Log4j2
public class GeneraReporteAnualOOAD implements Tasklet {

    @Value("#{stepExecution}")
    private StepExecution stepExecution;

    Logger logger = LoggerFactory.getLogger("GenerarReportesOOAD");

    @Autowired
    private ReporteInterface reporteOOAD;

    @Autowired
    @Qualifier("reporteNacional")
    private ReporteInterface reporteNacional;

    @Autowired
    @Qualifier("dataSourceReporteByOOAD")
    private DataSourceReporte dataSourceReporteByOOAD;

    @Qualifier("dataSourceReporteByOOADIMSS")
    @Autowired
    private DataSourceReporte dataSourceReporteByOOADIMSS;

    @Autowired
    @Qualifier("dataSourceNacional")
    private DataSourceReporte dataSourceReporteNacional;

    @Autowired
    @Qualifier("dataSourceNacionalRFC")
    private DataSourceReporte dataSourceReporteNacionalIMSS;



    private List<ReporteOOADModel> listaDatosOOAD = new ArrayList<>();
    private List<ReporteOOADModel> listaDatosOOADRFCIMSS = new ArrayList<>();

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        listaDatosOOAD = new ArrayList<>();
        listaDatosOOADRFCIMSS = new ArrayList<>();
        Integer cicloActual = DateUtils.calculateCurrentCycleInt().get(0)-1;
        generarPorOOAD(cicloActual);
        generarGlobal(cicloActual);
        return RepeatStatus.FINISHED;
    }

    private  void generarGlobal(int cicloActual) throws Exception {
        String nombre = cicloActual + "_" +"GLOBAL";
        String nombreRFC = cicloActual + "_" +"GLOBAL_RFC";

        logger.info(MessageFormat.format("Inicio de proceso de generacion {0} ", nombre));
        ReporteBuilder reporteBuilder = null;
        ReporteCierreAnualRequestModel reporteCierreAnualRequestModel = new ReporteCierreAnualRequestModel();

        reporteCierreAnualRequestModel.setCicloActual(cicloActual);
        reporteBuilder = new ReporteBuilder(reporteNacional, dataSourceReporteNacional,reporteCierreAnualRequestModel, nombre +".xlsx", false);
        reporteCierreAnualRequestModel.setDatosDelegaciones(this.listaDatosOOAD);
        reporteBuilder.generarReporte();



        reporteBuilder = new ReporteBuilder(reporteNacional,dataSourceReporteNacionalIMSS, reporteCierreAnualRequestModel, nombreRFC+".xlsx", true);
        reporteCierreAnualRequestModel.setDatosDelegaciones(this.listaDatosOOADRFCIMSS);
        reporteBuilder.generarReporte();


        logger.info(MessageFormat.format("Finalizacion de proceso de generacion {0} ", nombre));
    }


    private  void generarPorOOAD(int cicloActual) throws Exception {

        List<DelegacionOOADDTO> catalogo = CierreAnualBatchConstants.catalogo;
        List<ReporteBuilder> reportes = new ArrayList<>();
        ExecutorService service = Executors.newFixedThreadPool(42);
        for (DelegacionOOADDTO delegacionOOADDTO : catalogo) {
            Integer ooad = delegacionOOADDTO.getClaveDelegacion();
            String llave = delegacionOOADDTO.getDescripcion();
            String nombre = cicloActual + "_" + ooad + llave;
            String nombreRFC = cicloActual + "_" + ooad + llave+"_RFC";
            logger.info(MessageFormat.format("Inicio de proceso de generacion {0} ", nombre));
            ReporteBuilder reporteBuilder = null;
            ReporteCierreAnualRequestModel reporteCierreAnualRequestModel = new ReporteCierreAnualRequestModel();
            reporteCierreAnualRequestModel.setOoad(ooad);
            reporteCierreAnualRequestModel.setCicloActual(cicloActual);
            reporteCierreAnualRequestModel.setSubDelegacion(delegacionOOADDTO.getClaveSubdelegaciones());
            reporteCierreAnualRequestModel.setDatosDelegaciones(this.listaDatosOOAD);
            reporteBuilder = new ReporteBuilder(reporteOOAD, dataSourceReporteByOOAD , reporteCierreAnualRequestModel, nombre +".xlsx",false);
            reporteBuilder.setName(delegacionOOADDTO.getDescripcion());
            reportes.add(reporteBuilder);
            service.execute(reporteBuilder);

            reporteCierreAnualRequestModel.setDatosDelegaciones(this.listaDatosOOADRFCIMSS);
            reporteBuilder = new ReporteBuilder(reporteOOAD,dataSourceReporteByOOADIMSS,reporteCierreAnualRequestModel, nombreRFC+".xlsx",true);
            reporteBuilder.setName(delegacionOOADDTO.getDescripcion());
            reportes.add(reporteBuilder);
            service.execute(reporteBuilder);
            logger.info(MessageFormat.format("Finalizacion de proceso de generacion {0} ", nombre));
        }
        try {
            service.shutdown();
            boolean b = service.awaitTermination(2, TimeUnit.HOURS);
            reportes.forEach(reporte -> {
                if(reporte.isRfc()){
                    listaDatosOOADRFCIMSS.add(reporte.getReporteOOADModel());
                }else{
                    listaDatosOOAD.add(reporte.getReporteOOADModel());
                }
            });
            log.info("Termino proceso " + b);
        } catch (InterruptedException e) {
            log.info(e.getMessage());
            throw new Exception(e.getMessage());

        }


    }
}
