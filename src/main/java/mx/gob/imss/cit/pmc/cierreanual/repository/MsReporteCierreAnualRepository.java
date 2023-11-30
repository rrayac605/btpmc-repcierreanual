package mx.gob.imss.cit.pmc.cierreanual.repository;

import com.mongodb.BasicDBObject;
import mx.gob.imss.cit.pmc.cierreanual.constants.CierreAnualBatchConstants;
import mx.gob.imss.cit.pmc.cierreanual.dto.DelegacionOOADDTO;
import mx.gob.imss.cit.pmc.cierreanual.xls.model.ParamModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface MsReporteCierreAnualRepository{


    List<ParamModel> getCausisticaPeriodoRevisionTipoRiesgo(int cicloActual, int ooad, List<Integer> subdelegaciones);

    List<ParamModel> getCausisticaPeriodoRevisionEstadoRegistro(int anioRevision, int ooad,List<Integer> subdelegaciones);

    List<Integer> ciclosGlobales(int anioRevision);

    default List<DelegacionOOADDTO> obtenerCatalogo() {
        return CierreAnualBatchConstants.catalogo;
    }

    default String descripcion(Integer ooad, List<Integer> subdelegaciones){

        List<DelegacionOOADDTO> delegacionOOADDTOS = obtenerCatalogo().stream().filter(del -> del.getClaveDelegacion() == ooad).collect(Collectors.toList());

        if(delegacionOOADDTOS.size() == 1){
            return delegacionOOADDTOS.get(0).getDescripcion();
        }else{
            return delegacionOOADDTOS.stream().filter(del -> del.getClaveSubdelegaciones().stream().allMatch(sub-> subdelegaciones.contains(sub))).findFirst().get().getDescripcion();
        }
    }

    default BasicDBObject validarNull(List<BasicDBObject> basicDBObjects, String ...keys){
        BasicDBObject basicDBObject = new BasicDBObject();
        if(basicDBObjects.isEmpty()){
            for (String s : keys) {
                basicDBObject.append(s,"0");
            }

            return basicDBObject;
        }else{
            return basicDBObjects.get(0);
        }

    }

    default BasicDBObject validarListNull(List<BasicDBObject> basicDBObjects, String ...keys){
        BasicDBObject basicDBObject = new BasicDBObject();
        if(basicDBObjects.isEmpty()){
            for (String s : keys) {
                basicDBObject.append(s,new ArrayList<Integer>());
            }

            return basicDBObject;
        }else{
            return basicDBObjects.get(0);
        }

    }

    List<Integer> getCiclosCorrectos(int anioRevision, int ooad,List<Integer> subdelegaciones);

    List<String> getCicloAnterioresCasosValues(List<Integer> ciclos,int ooad,List<Integer> subdelegaciones);

    List<String> getCicloPosterioresCasosValue(List<Integer> ciclos, int ooads,List<Integer> subdelegaciones);

    List<String> getCicloActualCasosValue(List<Integer> ciclos,int ooad,List<Integer> subdelegaciones);

    List<ParamModel> getCausisticaPeriodoRevisionOtrosEstadosRegistro(int cicloActual, int ooad,List<Integer> subdelegaciones);

    List<ParamModel> getCausisticaOtrosAniosPorTipoRiesgo(List<Integer> ciclosAnteriores, List<Integer> ciclosPosteriores, int ooad,List<Integer> subdelegaciones);

    Map<String, List<ParamModel>> getAccidentesEnfermedadesTrabajoAnioRevisionRegistroPatronal(int ooad, int cicloActual,List<Integer> subdelegaciones);

    List<Integer> getOoad();
}
