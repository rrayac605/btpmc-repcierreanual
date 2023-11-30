package mx.gob.imss.cit.pmc.cierreanual.xls.interfaces.impl;


import mx.gob.imss.cit.pmc.cierreanual.repository.MsReporteCierreAnualRepository;
import mx.gob.imss.cit.pmc.cierreanual.xls.interfaces.DataSourceReporte;
import mx.gob.imss.cit.pmc.cierreanual.xls.model.ReporteCierreAnualRequestModel;
import mx.gob.imss.cit.pmc.cierreanual.xls.model.ReporteOOADModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DataSourceReporteByOOADIMSS implements DataSourceReporte {

    @Autowired
    @Qualifier("realRFC")
    private MsReporteCierreAnualRepository msReporteCierreAnualRepository;

    @Override
    public ReporteOOADModel fillData(ReporteCierreAnualRequestModel request) {
        int cicloActual = request.getCicloActual();
        int ooad = request.getOoad();
        List<Integer> subdelegaciones = request.getSubDelegacion();
        ReporteOOADModel input = new ReporteOOADModel();
        input.setCausisticaPeriodoRevisionTipoRiesgo(msReporteCierreAnualRepository.getCausisticaPeriodoRevisionTipoRiesgo(cicloActual,ooad,subdelegaciones));
        input.setCausisticaPeriodoRevisionEstadoRegistro(msReporteCierreAnualRepository.getCausisticaPeriodoRevisionEstadoRegistro(cicloActual, ooad,subdelegaciones));
        List<Integer> ciclosCorrectos = msReporteCierreAnualRepository.getCiclosCorrectos(cicloActual, ooad,subdelegaciones);
        List<Integer> ciclosAnteriores = ciclosCorrectos.stream().filter(ciclo -> ciclo < cicloActual).collect(Collectors.toList());
        List<Integer> ciclosPosteriores = ciclosCorrectos.stream().filter(ciclo -> ciclo > cicloActual).collect(Collectors.toList());
        input.setCicloAnteriores(ciclosAnteriores);
        input.setCicloPosteriores(ciclosPosteriores);
        input.setCicloActual(cicloActual);
        input.setCicloPosterioresCasosValue(msReporteCierreAnualRepository.getCicloPosterioresCasosValue(ciclosPosteriores,ooad,subdelegaciones));
        input.setCicloAnterioresCasosValues(msReporteCierreAnualRepository.getCicloAnterioresCasosValues(ciclosAnteriores,ooad,subdelegaciones));
        input.setCicloActualCasosValue(msReporteCierreAnualRepository.getCicloActualCasosValue(Collections.singletonList(cicloActual),ooad,subdelegaciones));
        input.setCausisticaPeriodoRevisionOtrosEstadosRegistro(msReporteCierreAnualRepository.getCausisticaPeriodoRevisionOtrosEstadosRegistro(cicloActual,ooad,subdelegaciones));
        input.setCausisticaOtrosAniosPorTipoRiesgo(msReporteCierreAnualRepository.getCausisticaOtrosAniosPorTipoRiesgo(ciclosAnteriores,ciclosPosteriores,ooad,subdelegaciones));
        input.setAccidentesEnfermedadesTrabajoAnioRevisionRegistroPatronal(msReporteCierreAnualRepository.getAccidentesEnfermedadesTrabajoAnioRevisionRegistroPatronal(ooad,cicloActual,subdelegaciones));
        return input;
    }
}
