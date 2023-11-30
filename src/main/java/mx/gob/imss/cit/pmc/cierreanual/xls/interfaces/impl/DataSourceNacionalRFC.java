package mx.gob.imss.cit.pmc.cierreanual.xls.interfaces.impl;


import mx.gob.imss.cit.pmc.cierreanual.constants.CierreAnualBatchConstants;
import mx.gob.imss.cit.pmc.cierreanual.repository.MsReporteCierreAnualRepository;
import mx.gob.imss.cit.pmc.cierreanual.xls.interfaces.DataSourceReporte;
import mx.gob.imss.cit.pmc.cierreanual.xls.model.ReporteCierreAnualRequestModel;
import mx.gob.imss.cit.pmc.cierreanual.xls.model.ReporteOOADModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DataSourceNacionalRFC implements DataSourceReporte {

    @Autowired
    @Qualifier("realRFC")
    private MsReporteCierreAnualRepository msReporteCierreAnualRepository;


    @Override
    public List<ReporteOOADModel> fillData(ReporteCierreAnualRequestModel request) {

        List<ReporteOOADModel> data = new ArrayList<>();
        Set<Integer> aniosGlobal = new HashSet<>();
        int cicloActual = request.getCicloActual();
        List<Integer> ciclosCorrectos = msReporteCierreAnualRepository.ciclosGlobales(cicloActual);

        request.getDatosDelegaciones().forEach(del->{
            int ooad = del.getOoad();
            List<Integer> subdelegaciones = del.getListaDelegaciones();
            List<Integer> ciclosAnteriores = ciclosCorrectos.stream().filter(ciclo -> ciclo < cicloActual).collect(Collectors.toList());
            List<Integer> ciclosPosteriores = ciclosCorrectos.stream().filter(ciclo -> ciclo > cicloActual).collect(Collectors.toList());
            del.setCicloAnteriores(ciclosAnteriores);
            del.setCicloPosteriores(ciclosPosteriores);
            del.setCicloActual(cicloActual);
            del.setCicloPosterioresCasosValue(msReporteCierreAnualRepository.getCicloPosterioresCasosValue(ciclosPosteriores,ooad,subdelegaciones));
            del.setCicloAnterioresCasosValues(msReporteCierreAnualRepository.getCicloAnterioresCasosValues(ciclosAnteriores,ooad,subdelegaciones));
            del.setCicloActualCasosValue(msReporteCierreAnualRepository.getCicloActualCasosValue(Collections.singletonList(cicloActual),ooad,subdelegaciones));
            data.add(del);
        });

        return data;
    }
}
