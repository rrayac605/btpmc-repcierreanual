package mx.gob.imss.cit.pmc.cierreanual.xls.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class ReporteOOADModel {


    private List<ParamModel> causisticaPeriodoRevisionTipoRiesgo;

    private List<ParamModel> causisticaPeriodoRevisionEstadoRegistro;

    private int cicloActual;

    private int ooad;

    private List<Integer> listaDelegaciones;

    private List<Integer> cicloAnteriores;

    private List<Integer> cicloPosteriores;

    private List<String> cicloAnterioresCasosValues;

    private List<String> cicloPosterioresCasosValue;

    private List<String> cicloActualCasosValue;

    private List<ParamModel> causisticaGlobalAnioRevision;

    private List<ParamModel> causisticaPeriodoRevisionOtrosEstadosRegistro;

    private List<ParamModel> causisticaOtrosAniosPorTipoRiesgo;

    private Map<String,List<ParamModel>> accidentesEnfermedadesTrabajoAnioRevisionRegistroPatronal;




}
