package mx.gob.imss.cit.pmc.cierreanual.repository.impl;

import com.mongodb.BasicDBObject;
import mx.gob.imss.cit.pmc.cierreanual.enums.EstadoRiesgoEnum;
import mx.gob.imss.cit.pmc.cierreanual.enums.TipoSituacion;
import mx.gob.imss.cit.pmc.cierreanual.repository.MsReporteCierreAnualRepository;
import mx.gob.imss.cit.pmc.cierreanual.xls.enums.*;
import mx.gob.imss.cit.pmc.cierreanual.xls.model.ParamModel;
import org.apache.poi.ss.usermodel.CellType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Service("real")
@Primary
public class MsReporteCierreAnualRepositoryImpl implements MsReporteCierreAnualRepository {

    public static final String OOAD_REGISTRO_PATROAL = "OOADRegistroPatroal";
    public static final String ANIO_REVISION = "anioRevision";
    public static final String TIPO_RIESGO = "tipoRiesgo";
    public static final String SUB_DEL_REGISTRO_PATRONAL = "subDelRegistroPatronal";
    public static final String DIAS_SUBSIDIADOS = "diasSubsidiados";
    public static final String PORCENTAJE_INCAPACIDAD = "porcentajeIncapacidad";
    public static final String TOTAL = "total";
    public static final String CLAVE_CONSECUENCIA = "claveConsecuencia";
    public static final String TOTAL_DEFUNCIONES = "totalDefunciones";
    public static final String CLAVE_ESTADO_RIESGO = "claveEstadoRiesgo";
    public static final String ANIO_CICLO_CASO = "anioCicloCaso";
    public static final String CICLOS = "ciclos";
    public static final String CLAVE_SITUACION_RIESGO = "claveSituacionRiesgo";
    public static final String RAZON_SOCIAL = "razonSocial";
    public static final String REG_PATRONAL = "regPatronal";

    @Autowired
    private MongoOperations operations;






    @Override
    public List<ParamModel> getCausisticaPeriodoRevisionTipoRiesgo(int cicloActual, int ooad, List<Integer> subdelegaciones) {


        AggregationResults<BasicDBObject> aggregacion1 = operations.aggregate(newAggregation(RespaldoCierreAnualCasuistica.class,
                        match(where(OOAD_REGISTRO_PATROAL).is(ooad).and(ANIO_REVISION).is(cicloActual).and(TIPO_RIESGO).in(
                                Arrays.asList(1, 3)
                        ).and(SUB_DEL_REGISTRO_PATRONAL).in(subdelegaciones)),
                        group()
                                .sum(DIAS_SUBSIDIADOS).as(DIAS_SUBSIDIADOS)
                                .sum(PORCENTAJE_INCAPACIDAD).as(PORCENTAJE_INCAPACIDAD)
                                .count().as(TOTAL)
                                .sum(ConditionalOperators.Cond.newBuilder()
                                        .when(Criteria.where(CLAVE_CONSECUENCIA).is(4)).then(1).otherwise(0)
                                ).as(TOTAL_DEFUNCIONES)
                ), RespaldoCierreAnualCasuistica.class,
                BasicDBObject.class
        );
        List<BasicDBObject> mappedResults = aggregacion1.getMappedResults();
        List<ParamModel> params = new ArrayList<>();
        params.add(addParam(CausisticaPeriodoRevisionTipoRiesgoParamEnum.DELEGACION.getName(), descripcion(ooad, subdelegaciones)));
        params.add(addParam(CausisticaPeriodoRevisionTipoRiesgoParamEnum.ANIO.getName(), cicloActual + ""));
        BasicDBObject basicDBObject = validarNull(mappedResults,TOTAL,DIAS_SUBSIDIADOS,PORCENTAJE_INCAPACIDAD,TOTAL_DEFUNCIONES);
        params.add(addParam(CausisticaPeriodoRevisionTipoRiesgoParamEnum.CASOSAET.getName(), basicDBObject.get(TOTAL).toString()));
        params.add(addParam(CausisticaPeriodoRevisionTipoRiesgoParamEnum.DIASSUBSIDIADOSAET.getName(), basicDBObject.get(DIAS_SUBSIDIADOS).toString()));
        params.add(addParam(CausisticaPeriodoRevisionTipoRiesgoParamEnum.INCAPACIDADESPERMANENTESAET.getName(), basicDBObject.get(PORCENTAJE_INCAPACIDAD).toString()));
        params.add(addParam(CausisticaPeriodoRevisionTipoRiesgoParamEnum.DEFUNCIONESAET.getName(), basicDBObject.get(TOTAL_DEFUNCIONES).toString()));
        AggregationResults<BasicDBObject> aggregacion2 = operations.aggregate(newAggregation(RespaldoCierreAnualCasuistica.class,
                        match(where(OOAD_REGISTRO_PATROAL).is(ooad).and(ANIO_REVISION).is(cicloActual).and(TIPO_RIESGO).in(
                                Arrays.asList(2)
                        ).and(SUB_DEL_REGISTRO_PATRONAL).in(subdelegaciones)),
                        group()
                                .sum(DIAS_SUBSIDIADOS).as(DIAS_SUBSIDIADOS)
                                .sum(PORCENTAJE_INCAPACIDAD).as(PORCENTAJE_INCAPACIDAD)
                                .count().as(TOTAL)
                                .sum(ConditionalOperators.Cond.newBuilder()
                                        .when(Criteria.where(CLAVE_CONSECUENCIA).is(4)).then(1).otherwise(0)
                                ).as(TOTAL_DEFUNCIONES)
                ), RespaldoCierreAnualCasuistica.class,
                BasicDBObject.class
        );
        BasicDBObject basicDBObject1 = validarNull(aggregacion2.getMappedResults(),TOTAL,DIAS_SUBSIDIADOS,PORCENTAJE_INCAPACIDAD,TOTAL_DEFUNCIONES,TOTAL);;
        params.add(addParam(CausisticaPeriodoRevisionTipoRiesgoParamEnum.CASOSAT.getName(), basicDBObject1.get(TOTAL).toString()));
        params.add(addParam(CausisticaPeriodoRevisionTipoRiesgoParamEnum.DIASSUBSIDIADOSAT.getName(), basicDBObject1.get(DIAS_SUBSIDIADOS).toString()));
        params.add(addParam(CausisticaPeriodoRevisionTipoRiesgoParamEnum.INCAPACIDADESPERMANENTESAT.getName(), basicDBObject1.get(PORCENTAJE_INCAPACIDAD).toString()));
        params.add(addParam(CausisticaPeriodoRevisionTipoRiesgoParamEnum.DEFUNCIONESAT.getName(), basicDBObject1.get(TOTAL_DEFUNCIONES).toString()));
        params.add(addParam(CausisticaPeriodoRevisionTipoRiesgoParamEnum.TOTALCASOS.getName(), String.valueOf(Integer.parseInt(basicDBObject1.get(TOTAL).toString()) + Integer.parseInt(basicDBObject.get(TOTAL).toString()))));
        params.add(addParam(CausisticaPeriodoRevisionTipoRiesgoParamEnum.TOTALDIASSUBSIDIADOS.getName(), String.valueOf(Integer.parseInt(basicDBObject1.get(DIAS_SUBSIDIADOS).toString()) + Integer.parseInt(basicDBObject.get(DIAS_SUBSIDIADOS).toString()))));
        params.add(addParam(CausisticaPeriodoRevisionTipoRiesgoParamEnum.TOTALINCAPACIDADESPERMANENTES.getName(), String.valueOf(Integer.parseInt(basicDBObject1.get(PORCENTAJE_INCAPACIDAD).toString()) + Integer.parseInt(basicDBObject.get(PORCENTAJE_INCAPACIDAD).toString()))));
        params.add(addParam(CausisticaPeriodoRevisionTipoRiesgoParamEnum.TOTALDEFUNCIONES.getName(), String.valueOf(Integer.parseInt(basicDBObject1.get(TOTAL_DEFUNCIONES).toString()) + Integer.parseInt(basicDBObject.get(TOTAL_DEFUNCIONES).toString()))));
        return params;
    }

    @Override
    public List<ParamModel> getCausisticaPeriodoRevisionEstadoRegistro(int anioRevision, int ooad, List<Integer> subdelegaciones) {

        AggregationResults<BasicDBObject> aggregacion1 = operations.aggregate(newAggregation(RespaldoCierreAnualCasuistica.class,
                        match(where(OOAD_REGISTRO_PATROAL).is(ooad).and(ANIO_REVISION).is(anioRevision).
                                and(CLAVE_ESTADO_RIESGO).is(
                                        1).and(SUB_DEL_REGISTRO_PATRONAL).in(subdelegaciones)

                        ),
                        group()
                                .count()
                                .as(TOTAL)
                ), RespaldoCierreAnualCasuistica.class,
                BasicDBObject.class
        );
        BasicDBObject basicDBObject = null;
        if (aggregacion1.getMappedResults().isEmpty()) {
            basicDBObject = new BasicDBObject();
            basicDBObject.append(TOTAL, 0);
        } else {
            basicDBObject = aggregacion1.getMappedResults().get(0);
        }
        List<ParamModel> params = new ArrayList<>();
        params.add(addParam(CausisticaPeriodoRevisionEstadoRegistroEnum.DELEGACION.getName(), descripcion(ooad, subdelegaciones)));
        params.add(addParam(CausisticaPeriodoRevisionEstadoRegistroEnum.ANIO.getName(), anioRevision + ""));
        params.add(addParam(CausisticaPeriodoRevisionEstadoRegistroEnum.CORRECTOS.getName(), basicDBObject.get(TOTAL).toString()));

        AggregationResults<BasicDBObject> aggregacion2 = operations.aggregate(newAggregation(RespaldoCierreAnualCasuistica.class,
                        match(where(OOAD_REGISTRO_PATROAL).is(ooad).and(ANIO_REVISION).is(anioRevision).
                                and(CLAVE_ESTADO_RIESGO).is(
                                        5).and(SUB_DEL_REGISTRO_PATRONAL).in(subdelegaciones)
                        ),
                        group()
                                .sum(ConditionalOperators.Cond.newBuilder()
                                        .when(Criteria.where("true").is(true)).then(1).otherwise(1)
                                ).as(TOTAL)
                ), RespaldoCierreAnualCasuistica.class,
                BasicDBObject.class
        );

        BasicDBObject basicDBObject1 = null;
        if (aggregacion2.getMappedResults().isEmpty()) {
            basicDBObject1 = new BasicDBObject();
            basicDBObject1.append(TOTAL, 0);
        } else {
            basicDBObject1 = aggregacion2.getMappedResults().get(0);
        }

        params.add(addParam(CausisticaPeriodoRevisionEstadoRegistroEnum.CORRECTOSOTRASDELEGACIONES.getName(), basicDBObject1.get(TOTAL).toString()));

        AggregationResults<BasicDBObject> aggregacion3 = operations.aggregate(newAggregation(RespaldoCierreAnualCasuistica.class,
                        match(where(OOAD_REGISTRO_PATROAL).is(ooad).and(ANIO_REVISION).is(anioRevision).
                                and(CLAVE_ESTADO_RIESGO).is(
                                        4).and(SUB_DEL_REGISTRO_PATRONAL).in(subdelegaciones)

                        ),
                        group()
                                .count()
                                .as(TOTAL)
                ), RespaldoCierreAnualCasuistica.class,
                BasicDBObject.class
        );

        BasicDBObject basicDBObject2 = null;
        if (aggregacion3.getMappedResults().isEmpty()) {
            basicDBObject2 = new BasicDBObject();
            basicDBObject2.append(TOTAL, 0);
        } else {
            basicDBObject2 = aggregacion3.getMappedResults().get(0);
        }

        params.add(addParam(CausisticaPeriodoRevisionEstadoRegistroEnum.SUSCEPTIBLESAJUSTE.getName(), basicDBObject2.get(TOTAL).toString()));

        AggregationResults<BasicDBObject> aggregacion4 = operations.aggregate(newAggregation(RespaldoCierreAnualCasuistica.class,
                        match(where(OOAD_REGISTRO_PATROAL).is(ooad).and(ANIO_REVISION).is(anioRevision).
                                and(CLAVE_ESTADO_RIESGO).is(
                                        8).and(SUB_DEL_REGISTRO_PATRONAL).in(subdelegaciones)

                        ),
                        group()
                                .count()
                                .as(TOTAL)
                ), RespaldoCierreAnualCasuistica.class,
                BasicDBObject.class
        );

        BasicDBObject basicDBObject3 = null;
        if (aggregacion4.getMappedResults().isEmpty()) {
            basicDBObject3 = new BasicDBObject();
            basicDBObject3.append(TOTAL, 0);
        } else {
            basicDBObject3 = aggregacion4.getMappedResults().get(0);
        }

        params.add(addParam(CausisticaPeriodoRevisionEstadoRegistroEnum.SUSCEPTIBLEAJUSTEOTRASDELEGACIONES.getName(), basicDBObject3.get(TOTAL).toString()));
        int total = Integer.parseInt(basicDBObject.get(TOTAL).toString())
                + Integer.parseInt(basicDBObject1.get(TOTAL).toString())
                + Integer.parseInt(basicDBObject2.get(TOTAL).toString())
                + Integer.parseInt(basicDBObject3.get(TOTAL).toString());

        params.add(addParam(CausisticaPeriodoRevisionEstadoRegistroEnum.TOTAL.getName(), total + ""));
        return params;
    }

    @Override
    public List<Integer> ciclosGlobales(int anioRevision) {
        AggregationResults<BasicDBObject> aggregacion4 = operations.aggregate(newAggregation(RespaldosRiesgosDescartados.class,
                        match(where(ANIO_REVISION).is(anioRevision)),
                        group()
                                .addToSet(ANIO_CICLO_CASO).as(CICLOS)
                ), RespaldosRiesgosDescartados.class,
                BasicDBObject.class
        );


        List<Integer> ciclos = (List<Integer>) aggregacion4.getMappedResults().get(0).get(CICLOS);
        ciclos.sort((a, b) -> a.compareTo(b));
        return ciclos;
    }

    @Override
    public List<Integer> getCiclosCorrectos(int anioRevision, int ooad, List<Integer> subdelegaciones) {
        AggregationResults<BasicDBObject> aggregacion4 = operations.aggregate(newAggregation(RespaldosRiesgosDescartados.class,
                        match(where(OOAD_REGISTRO_PATROAL).is(ooad).and(ANIO_REVISION).is(anioRevision).and(SUB_DEL_REGISTRO_PATRONAL).in(subdelegaciones)

                        ),
                        group()
                                .addToSet(ANIO_CICLO_CASO).as(CICLOS)
                ), RespaldosRiesgosDescartados.class,
                BasicDBObject.class
        );


        List<Integer> ciclos = (List<Integer>) validarListNull(aggregacion4.getMappedResults(),CICLOS).get(CICLOS);
        ciclos.sort((a, b) -> a.compareTo(b));
        return ciclos;
    }

    @Override
    public List<String> getCicloAnterioresCasosValues(List<Integer> ciclos, int ooad, List<Integer> subdelegaciones) {
        List<String> valores = new ArrayList<>();
        ciclos.stream().forEach(c -> {
            AggregationResults<BasicDBObject> aggregacion4 = operations.aggregate(newAggregation(RespaldosRiesgosDescartados.class,
                            match(where(OOAD_REGISTRO_PATROAL).is(ooad).and(ANIO_CICLO_CASO).is(c).and(SUB_DEL_REGISTRO_PATRONAL).in(subdelegaciones)

                            ),
                            group()
                                    .count().as(TOTAL)
                    ), RespaldosRiesgosDescartados.class,
                    BasicDBObject.class
            );
            List<BasicDBObject> mappedResults = aggregacion4.getMappedResults();
            Object total = null;
            if (!mappedResults.isEmpty()) {
                total = mappedResults.get(0).get(TOTAL);
            } else {
                total = "0";
            }


            valores.add(String.valueOf(total));
        });
        return valores;
    }

    @Override
    public List<String> getCicloPosterioresCasosValue(List<Integer> ciclos, int ooad, List<Integer> subdelegaciones) {
        List<String> valores = new ArrayList<>();
        ciclos.stream().forEach(c -> {
            AggregationResults<BasicDBObject> aggregacion4 = operations.aggregate(newAggregation(RespaldosRiesgosDescartados.class,
                            match(where(OOAD_REGISTRO_PATROAL).is(ooad).and(ANIO_CICLO_CASO).is(c).and(SUB_DEL_REGISTRO_PATRONAL).in(subdelegaciones)

                            ),
                            group()
                                    .count().as(TOTAL)
                    ), RespaldosRiesgosDescartados.class,
                    BasicDBObject.class
            );
            List<BasicDBObject> mappedResults = aggregacion4.getMappedResults();
            Object total = null;
            if (!mappedResults.isEmpty()) {
                total = mappedResults.get(0).get(TOTAL);
            } else {
                total = "0";
            }


            valores.add(String.valueOf(total));
        });
        return valores;
    }

    @Override
    public List<String> getCicloActualCasosValue(List<Integer> ciclos, int ooad, List<Integer> subdelegaciones) {
        List<String> valores = new ArrayList<>();
        ciclos.stream().forEach(c -> {
            AggregationResults<BasicDBObject> aggregacion4 = operations.aggregate(newAggregation(RespaldosRiesgosDescartados.class,
                            match(where(OOAD_REGISTRO_PATROAL).is(ooad).and(ANIO_CICLO_CASO).is(c).and(SUB_DEL_REGISTRO_PATRONAL).in(subdelegaciones)

                            ),
                            group()
                                    .count().as(TOTAL)
                    ), RespaldosRiesgosDescartados.class,
                    BasicDBObject.class
            );
            List<BasicDBObject> mappedResults = aggregacion4.getMappedResults();
            Object total = null;
            if (!mappedResults.isEmpty()) {
                total = mappedResults.get(0).get(TOTAL);
            } else {
                total = "0";
            }


            valores.add(String.valueOf(total));
        });
        return valores;
    }

    @Override
    public List<ParamModel> getCausisticaPeriodoRevisionOtrosEstadosRegistro(int cicloActual, int ooad, List<Integer> subdelegaciones) {
        List<ParamModel> params = new ArrayList<>();


        params.add(addParam(CausisticaPeriodoRevisionOtrosEstadosEnum.DELEGACION.getName(), descripcion(ooad, subdelegaciones)));
        params.add(addParam(CausisticaPeriodoRevisionOtrosEstadosEnum.ANIO.getName(), cicloActual + ""));

        AggregationResults<BasicDBObject> aggregacion1 = operations.aggregate(newAggregation(RespaldosRiesgosDescartados.class,
                        match(where(OOAD_REGISTRO_PATROAL).is(ooad).and(ANIO_CICLO_CASO).is(cicloActual).
                                and(CLAVE_ESTADO_RIESGO).is(
                                        EstadoRiesgoEnum.CORRECTOS.getValor()).and(SUB_DEL_REGISTRO_PATRONAL).in(subdelegaciones)
                                .orOperator(where(CLAVE_SITUACION_RIESGO).is(TipoSituacion.RECHAZADO.getValor()),
                                        where(CLAVE_SITUACION_RIESGO).is(TipoSituacion.PENDIENTE_APROBAR.getValor()))
                        ),
                        group()
                                .count()
                                .as(TOTAL)
                ), RespaldosRiesgosDescartados.class,
                BasicDBObject.class
        );
        BasicDBObject basicDBObject = null;
        if (aggregacion1.getMappedResults().isEmpty()) {
            basicDBObject = new BasicDBObject();
            basicDBObject.append(TOTAL, 0);
        } else {
            basicDBObject = aggregacion1.getMappedResults().get(0);
        }

        params.add(addParam(CausisticaPeriodoRevisionOtrosEstadosEnum.CORRECTOS.getName(), basicDBObject.get(TOTAL).toString()));

        AggregationResults<BasicDBObject> aggregacion2 = operations.aggregate(newAggregation(RespaldosRiesgosDescartados.class,
                        match(where(OOAD_REGISTRO_PATROAL).is(ooad).and(ANIO_CICLO_CASO).is(cicloActual).
                                and(CLAVE_ESTADO_RIESGO).is(
                                        EstadoRiesgoEnum.CORRECTOS_OTRAS_DELEGACIONES.getValor()).and(SUB_DEL_REGISTRO_PATRONAL).in(subdelegaciones)
                                .orOperator(where(CLAVE_SITUACION_RIESGO).is(TipoSituacion.RECHAZADO.getValor()),
                                        where(CLAVE_SITUACION_RIESGO).is(TipoSituacion.PENDIENTE_APROBAR.getValor()))
                        ),
                        group()
                                .count()
                                .as(TOTAL)
                ), RespaldosRiesgosDescartados.class,
                BasicDBObject.class
        );
        BasicDBObject basicDBObject2 = null;
        if (aggregacion2.getMappedResults().isEmpty()) {
            basicDBObject2 = new BasicDBObject();
            basicDBObject2.append(TOTAL, 0);
        } else {
            basicDBObject2 = aggregacion2.getMappedResults().get(0);
        }

        params.add(addParam(CausisticaPeriodoRevisionOtrosEstadosEnum.CORRECTOSOTRASDELEGACIONES.getName(), basicDBObject2.get(TOTAL).toString()));

        AggregationResults<BasicDBObject> aggregacion3 = operations.aggregate(newAggregation(RespaldosRiesgosDescartados.class,
                        match(where(OOAD_REGISTRO_PATROAL).is(ooad).and(ANIO_CICLO_CASO).is(cicloActual).
                                and(CLAVE_ESTADO_RIESGO).is(
                                        EstadoRiesgoEnum.SUSCEPTIBLES_DE_AJUSTE.getValor()).and(SUB_DEL_REGISTRO_PATRONAL).in(subdelegaciones)
                                .and(CLAVE_SITUACION_RIESGO).in(Arrays.asList(TipoSituacion.RECHAZADO.getValor(), TipoSituacion.PENDIENTE_APROBAR.getValor()))
                        ),
                        group()
                                .count()
                                .as(TOTAL)
                ), RespaldosRiesgosDescartados.class,
                BasicDBObject.class
        );
        BasicDBObject basicDBObject3 = null;
        if (aggregacion3.getMappedResults().isEmpty()) {
            basicDBObject3 = new BasicDBObject();
            basicDBObject3.append(TOTAL, 0);
        } else {
            basicDBObject3 = aggregacion3.getMappedResults().get(0);
        }

        params.add(addParam(CausisticaPeriodoRevisionOtrosEstadosEnum.SUSCEPTIBLEAJUSTE.getName(), basicDBObject3.get(TOTAL).toString()));

        AggregationResults<BasicDBObject> aggregacion4 = operations.aggregate(newAggregation(RespaldosRiesgosDescartados.class,
                        match(where(OOAD_REGISTRO_PATROAL).is(ooad).and(ANIO_CICLO_CASO).is(cicloActual).
                                and(CLAVE_ESTADO_RIESGO).is(
                                        EstadoRiesgoEnum.SUSCEPTIBLES_DE_AJUSTE_OTRAS_DELEGACIONES.getValor()).and(SUB_DEL_REGISTRO_PATRONAL).in(subdelegaciones)
                                .and(CLAVE_SITUACION_RIESGO).in(Arrays.asList(TipoSituacion.RECHAZADO.getValor(), TipoSituacion.PENDIENTE_APROBAR.getValor()))
                        ),
                        group()
                                .count()
                                .as(TOTAL)
                ), RespaldosRiesgosDescartados.class,
                BasicDBObject.class
        );
        BasicDBObject basicDBObject4 = null;
        if (aggregacion4.getMappedResults().isEmpty()) {
            basicDBObject4 = new BasicDBObject();
            basicDBObject4.append(TOTAL, 0);
        } else {
            basicDBObject4 = aggregacion4.getMappedResults().get(0);
        }


        params.add(addParam(CausisticaPeriodoRevisionOtrosEstadosEnum.SUSCEPTIBLEAJUSTEOTRASDELEGACIONES.getName(), basicDBObject4.get(TOTAL).toString()));

        AggregationResults<BasicDBObject> aggregacion5 = operations.aggregate(newAggregation(RespaldosRiesgosDescartados.class,
                        match(where(OOAD_REGISTRO_PATROAL).is(ooad).and(ANIO_CICLO_CASO).is(cicloActual).
                                and(CLAVE_ESTADO_RIESGO).is(
                                        EstadoRiesgoEnum.ERRONEOS.getValor()).and(SUB_DEL_REGISTRO_PATRONAL).in(subdelegaciones)
                                .orOperator(where(CLAVE_SITUACION_RIESGO).is(TipoSituacion.PENDIENTE_APROBAR.getValor()), where(CLAVE_SITUACION_RIESGO).exists(false))

                        ),
                        group()
                                .count()
                                .as(TOTAL)
                ), RespaldosRiesgosDescartados.class,
                BasicDBObject.class
        );
        BasicDBObject basicDBObject5 = null;
        if (aggregacion5.getMappedResults().isEmpty()) {
            basicDBObject5 = new BasicDBObject();
            basicDBObject5.append(TOTAL, 0);
        } else {
            basicDBObject5 = aggregacion5.getMappedResults().get(0);
        }

        params.add(addParam(CausisticaPeriodoRevisionOtrosEstadosEnum.ERRONEOS.getName(), basicDBObject5.get(TOTAL).toString()));

        AggregationResults<BasicDBObject> aggregacion6 = operations.aggregate(newAggregation(RespaldosRiesgosDescartados.class,
                        match(where(OOAD_REGISTRO_PATROAL).is(ooad).and(ANIO_CICLO_CASO).is(cicloActual).
                                and(CLAVE_ESTADO_RIESGO).is(
                                        EstadoRiesgoEnum.ERRONEOS_OTRAS_DELEGACIONES.getValor()).and(SUB_DEL_REGISTRO_PATRONAL).in(subdelegaciones)
                                .orOperator(where(CLAVE_SITUACION_RIESGO).is(TipoSituacion.PENDIENTE_APROBAR.getValor()), where(CLAVE_SITUACION_RIESGO).exists(false))
                        ),
                        group()
                                .count()
                                .as(TOTAL)
                ), RespaldosRiesgosDescartados.class,
                BasicDBObject.class
        );
        BasicDBObject basicDBObject6 = null;
        if (aggregacion6.getMappedResults().isEmpty()) {
            basicDBObject6 = new BasicDBObject();
            basicDBObject6.append(TOTAL, 0);
        } else {
            basicDBObject6 = aggregacion6.getMappedResults().get(0);
        }

        params.add(addParam(CausisticaPeriodoRevisionOtrosEstadosEnum.ERRONEOSOTRASDELEGACIONES.getName(), basicDBObject6.get(TOTAL).toString()));

        AggregationResults<BasicDBObject> aggregacion7 = operations.aggregate(newAggregation(RespaldosRiesgosDescartados.class,
                        match(where(OOAD_REGISTRO_PATROAL).is(ooad).and(ANIO_CICLO_CASO).is(cicloActual).
                                and(CLAVE_ESTADO_RIESGO).is(
                                        EstadoRiesgoEnum.BAJA.getValor()).and(SUB_DEL_REGISTRO_PATRONAL).in(subdelegaciones)
                                .orOperator(where(CLAVE_SITUACION_RIESGO).is(TipoSituacion.PENDIENTE_APROBAR.getValor()),where(CLAVE_SITUACION_RIESGO).is(TipoSituacion.APROBADO.getValor()), where(CLAVE_SITUACION_RIESGO).exists(false))
                        ),
                        group()
                                .count()
                                .as(TOTAL)
                ), RespaldosRiesgosDescartados.class,
                BasicDBObject.class
        );
        BasicDBObject basicDBObject7 = null;
        if (aggregacion7.getMappedResults().isEmpty()) {
            basicDBObject7 = new BasicDBObject();
            basicDBObject7.append(TOTAL, 0);
        } else {
            basicDBObject7 = aggregacion7.getMappedResults().get(0);
        }

        params.add(addParam(CausisticaPeriodoRevisionOtrosEstadosEnum.BAJAS.getName(), basicDBObject7.get(TOTAL).toString()));

        AggregationResults<BasicDBObject> aggregacion8 = operations.aggregate(newAggregation(RespaldosRiesgosDescartados.class,
                        match(where(OOAD_REGISTRO_PATROAL).is(ooad).and(ANIO_CICLO_CASO).is(cicloActual).
                                and(CLAVE_ESTADO_RIESGO).is(
                                        EstadoRiesgoEnum.BAJA_OTRAS_DELEGACIONES.getValor()
                                ).and(SUB_DEL_REGISTRO_PATRONAL).in(subdelegaciones)
                                .orOperator(where(CLAVE_SITUACION_RIESGO).is(TipoSituacion.PENDIENTE_APROBAR.getValor()),where(CLAVE_SITUACION_RIESGO).is(TipoSituacion.APROBADO.getValor()), where(CLAVE_SITUACION_RIESGO).exists(false))
                        ),
                        group()
                                .count()
                                .as(TOTAL)
                ), RespaldosRiesgosDescartados.class,
                BasicDBObject.class
        );
        BasicDBObject basicDBObject8 = null;
        if (aggregacion8.getMappedResults().isEmpty()) {
            basicDBObject8 = new BasicDBObject();
            basicDBObject8.append(TOTAL, 0);
        } else {
            basicDBObject8 = aggregacion8.getMappedResults().get(0);
        }

        params.add(addParam(CausisticaPeriodoRevisionOtrosEstadosEnum.BAJASOTRASDELEGACIONES.getName(), basicDBObject8.get(TOTAL).toString()));

        AggregationResults<BasicDBObject> aggregacion9 = operations.aggregate(newAggregation(RespaldosRiesgosDescartados.class,
                        match(where(OOAD_REGISTRO_PATROAL).is(ooad).and(ANIO_CICLO_CASO).is(cicloActual).
                                and(CLAVE_ESTADO_RIESGO).is(
                                        EstadoRiesgoEnum.DUPLICADOS.getValor()).and(SUB_DEL_REGISTRO_PATRONAL).in(subdelegaciones)
                                .orOperator(where(CLAVE_SITUACION_RIESGO).is(TipoSituacion.PENDIENTE_APROBAR.getValor()), where(CLAVE_SITUACION_RIESGO).exists(false))
                        ),
                        group()
                                .count()
                                .as(TOTAL)
                ), RespaldosRiesgosDescartados.class,
                BasicDBObject.class
        );
        BasicDBObject basicDBObject9 = null;
        if (aggregacion9.getMappedResults().isEmpty()) {
            basicDBObject9 = new BasicDBObject();
            basicDBObject9.append(TOTAL, 0);
        } else {
            basicDBObject9 = aggregacion9.getMappedResults().get(0);
        }


        params.add(addParam(CausisticaPeriodoRevisionOtrosEstadosEnum.DUPLICADOS.getName(), basicDBObject9.get(TOTAL).toString()));

        AggregationResults<BasicDBObject> aggregacion10 = operations.aggregate(newAggregation(RespaldosRiesgosDescartados.class,
                        match(where(OOAD_REGISTRO_PATROAL).is(ooad).and(ANIO_CICLO_CASO).is(cicloActual).
                                and(CLAVE_ESTADO_RIESGO).is(
                                        EstadoRiesgoEnum.DUPLICADOS_OTRAS_DELEGACIONES.getValor()).and(SUB_DEL_REGISTRO_PATRONAL).in(subdelegaciones)
                                .orOperator(where(CLAVE_SITUACION_RIESGO).is(TipoSituacion.PENDIENTE_APROBAR.getValor()), where(CLAVE_SITUACION_RIESGO).exists(false))
                        ),
                        group()
                                .count()
                                .as(TOTAL)
                ), RespaldosRiesgosDescartados.class,
                BasicDBObject.class
        );
        BasicDBObject basicDBObject10 = null;
        if (aggregacion10.getMappedResults().isEmpty()) {
            basicDBObject10 = new BasicDBObject();
            basicDBObject10.append(TOTAL, 0);
        } else {
            basicDBObject10 = aggregacion10.getMappedResults().get(0);
        }
        params.add(addParam(CausisticaPeriodoRevisionOtrosEstadosEnum.DUPLICADOSOTRASDELEGACIONES.getName(), basicDBObject10.get(TOTAL).toString()));

        int total = getTotal.apply(basicDBObject) +
                getTotal.apply(basicDBObject2) +
                getTotal.apply(basicDBObject3) +
                getTotal.apply(basicDBObject4) +
                getTotal.apply(basicDBObject5) +
                getTotal.apply(basicDBObject6) +
                getTotal.apply(basicDBObject7) +
                getTotal.apply(basicDBObject8) +
                getTotal.apply(basicDBObject9) +
                getTotal.apply(basicDBObject10);

        params.add(addParam(CausisticaPeriodoRevisionOtrosEstadosEnum.TOTAL.getName(), total + ""));
        return params;
    }

    Function<BasicDBObject, Integer> getTotal = (a) -> Integer.parseInt(a.get(TOTAL).toString());

    @Override
    public List<ParamModel> getCausisticaOtrosAniosPorTipoRiesgo(List<Integer> ciclosAnteriores, List<Integer> ciclosPosteriores, int ooad, List<Integer> subdelegaciones) {
        List<ParamModel> params = new ArrayList<>();

        params.add(addParam(CausisticaOtrosAniosRevisionTIpoRiesgoEnum.DELEGACION.getName(), descripcion(ooad, subdelegaciones)));

        AggregationResults<BasicDBObject> aggregationResultsAnteriores = operations.aggregate(newAggregation(RespaldosRiesgosDescartados.class,
                        match(where(OOAD_REGISTRO_PATROAL).is(ooad).and(ANIO_CICLO_CASO).in(ciclosAnteriores).and(SUB_DEL_REGISTRO_PATRONAL).in(subdelegaciones)

                        ),
                        group()
                                .sum(DIAS_SUBSIDIADOS).as(DIAS_SUBSIDIADOS)
                                .sum(PORCENTAJE_INCAPACIDAD).as(PORCENTAJE_INCAPACIDAD)
                                .count().as(TOTAL)
                                .sum(ConditionalOperators.Cond.newBuilder()
                                        .when(Criteria.where(CLAVE_CONSECUENCIA).is(4)).then(1).otherwise(0)
                                ).as(TOTAL_DEFUNCIONES)
                ), RespaldosRiesgosDescartados.class,
                BasicDBObject.class
        );

        BasicDBObject basicDBObject = validarNull(aggregationResultsAnteriores.getMappedResults(),TOTAL,DIAS_SUBSIDIADOS,PORCENTAJE_INCAPACIDAD,TOTAL_DEFUNCIONES);

        params.add(addParam(CausisticaOtrosAniosRevisionTIpoRiesgoEnum.CASOSPA.getName(), basicDBObject.get(TOTAL).toString()));
        params.add(addParam(CausisticaOtrosAniosRevisionTIpoRiesgoEnum.DIASSUBSIDIADOSPA.getName(), basicDBObject.get(DIAS_SUBSIDIADOS).toString()));
        params.add(addParam(CausisticaOtrosAniosRevisionTIpoRiesgoEnum.INCAPACIDADESPERMANENTESPA.getName(), basicDBObject.get(PORCENTAJE_INCAPACIDAD).toString()));
        params.add(addParam(CausisticaOtrosAniosRevisionTIpoRiesgoEnum.DEFUNCIONESPA.getName(), basicDBObject.get(TOTAL_DEFUNCIONES).toString()));

        AggregationResults<BasicDBObject> aggregationResultsPosteriores = operations.aggregate(newAggregation(RespaldosRiesgosDescartados.class,
                        match(where(OOAD_REGISTRO_PATROAL).is(ooad).and(ANIO_CICLO_CASO).in(ciclosPosteriores).and(SUB_DEL_REGISTRO_PATRONAL).in(subdelegaciones)

                        ),
                        group()
                                .sum(DIAS_SUBSIDIADOS).as(DIAS_SUBSIDIADOS)
                                .sum(PORCENTAJE_INCAPACIDAD).as(PORCENTAJE_INCAPACIDAD)
                                .count().as(TOTAL)
                                .sum(ConditionalOperators.Cond.newBuilder()
                                        .when(Criteria.where(CLAVE_CONSECUENCIA).is(4)).then(1).otherwise(0)
                                ).as(TOTAL_DEFUNCIONES)
                ), RespaldosRiesgosDescartados.class,
                BasicDBObject.class
        );
        BasicDBObject basicDBObject1 = null;
        if (!aggregationResultsPosteriores.getMappedResults().isEmpty()) {

            basicDBObject1 = aggregationResultsPosteriores.getMappedResults().get(0);
        } else {
            basicDBObject1 = new BasicDBObject();
            basicDBObject1.append(TOTAL, 0);
            basicDBObject1.append(DIAS_SUBSIDIADOS, 0);
            basicDBObject1.append(PORCENTAJE_INCAPACIDAD, 0);
            basicDBObject1.append(TOTAL_DEFUNCIONES, 0);
        }

        params.add(addParam(CausisticaOtrosAniosRevisionTIpoRiesgoEnum.CASOSPP.getName(), basicDBObject1.get(TOTAL).toString()));
        params.add(addParam(CausisticaOtrosAniosRevisionTIpoRiesgoEnum.DIASSUBSIDIADOSPP.getName(), basicDBObject1.get(DIAS_SUBSIDIADOS).toString()));
        params.add(addParam(CausisticaOtrosAniosRevisionTIpoRiesgoEnum.INCAPACIDADESPERMANENTESPP.getName(), basicDBObject1.get(PORCENTAJE_INCAPACIDAD).toString()));
        params.add(addParam(CausisticaOtrosAniosRevisionTIpoRiesgoEnum.DEFUNCIONESPP.getName(), basicDBObject1.get(TOTAL_DEFUNCIONES).toString()));

        int casosTotales = getTotal.apply(basicDBObject) + getTotal.apply(basicDBObject1);
        params.add(addParam(CausisticaOtrosAniosRevisionTIpoRiesgoEnum.CASOSTOTAL.getName(), casosTotales + ""));
        int diasSubsidiadosTotales = Integer.parseInt(basicDBObject.get(DIAS_SUBSIDIADOS).toString()) + Integer.parseInt(basicDBObject1.get(DIAS_SUBSIDIADOS).toString());
        params.add(addParam(CausisticaOtrosAniosRevisionTIpoRiesgoEnum.DIASSUBSIDIADOSTOTAL.getName(), diasSubsidiadosTotales + ""));
        int incapacidadesTotales = Integer.parseInt(basicDBObject.get(PORCENTAJE_INCAPACIDAD).toString()) + Integer.parseInt(basicDBObject1.get(PORCENTAJE_INCAPACIDAD).toString());
        params.add(addParam(CausisticaOtrosAniosRevisionTIpoRiesgoEnum.INCAPACIDADESPERMANENTESTOTAL.getName(), incapacidadesTotales + ""));
        int defuncionesTotales = Integer.parseInt(basicDBObject.get(TOTAL_DEFUNCIONES).toString()) + Integer.parseInt(basicDBObject1.get(TOTAL_DEFUNCIONES).toString());
        params.add(addParam(CausisticaOtrosAniosRevisionTIpoRiesgoEnum.DEFUNCIONESTOTAL.getName(), defuncionesTotales + ""));
        return params;
    }


    private AggregationResults<BasicDBObject> patrones = null;


    @Override
    public Map<String, List<ParamModel>> getAccidentesEnfermedadesTrabajoAnioRevisionRegistroPatronal(int ooad, int cicloActual, List<Integer> subdelegaciones) {

        AggregationResults<BasicDBObject> aggregacion = operations.aggregate(newAggregation(RespaldoCierreAnualCasuistica.class,
                        match(where(OOAD_REGISTRO_PATROAL).is(ooad).and(ANIO_REVISION).is(cicloActual).and(TIPO_RIESGO).in(
                                Arrays.asList(1, 3)
                        ).and(SUB_DEL_REGISTRO_PATRONAL).in(subdelegaciones)),
                        group(REG_PATRONAL)
                                .sum(DIAS_SUBSIDIADOS).as(DIAS_SUBSIDIADOS)
                                .sum(PORCENTAJE_INCAPACIDAD).as(PORCENTAJE_INCAPACIDAD)
                                .count().as(TOTAL)
                                .sum(ConditionalOperators.Cond.newBuilder()
                                        .when(Criteria.where(CLAVE_CONSECUENCIA).is(4)).then(1).otherwise(0)
                                ).as(TOTAL_DEFUNCIONES)
                                .first(RAZON_SOCIAL)
                                .as(RAZON_SOCIAL)
                ), RespaldoCierreAnualCasuistica.class,
                BasicDBObject.class
        );

        Map<String, List<ParamModel>> datos = new HashMap<>();

        AtomicInteger i = new AtomicInteger();
        aggregacion.getMappedResults().stream()
                //.limit(100)
                .forEach(registro -> {

            String razonSocial = registro.get(RAZON_SOCIAL).toString();


            List<ParamModel> params = new ArrayList<>();
            params.add(addParam(AccidentesEnfermedadesTrabajoAnioRevisionPorRegistroPatronalEnum.DELEGACION.getName(), descripcion(ooad, subdelegaciones)));
            params.add(addParam(AccidentesEnfermedadesTrabajoAnioRevisionPorRegistroPatronalEnum.ANIO.getName(), cicloActual + ""));
            params.add(addParam(AccidentesEnfermedadesTrabajoAnioRevisionPorRegistroPatronalEnum.REGISTROPATRONAL.getName(), registro.get("_id").toString()));
            params.add(addParam(AccidentesEnfermedadesTrabajoAnioRevisionPorRegistroPatronalEnum.RAZONSOCIAL.getName(), razonSocial));
            params.add(addParam(AccidentesEnfermedadesTrabajoAnioRevisionPorRegistroPatronalEnum.CASOS.getName(), registro.get(TOTAL).toString()));
            params.add(addParam(AccidentesEnfermedadesTrabajoAnioRevisionPorRegistroPatronalEnum.DIASSUBSIDIADOS.getName(), registro.get(DIAS_SUBSIDIADOS).toString()));
            params.add(addParam(AccidentesEnfermedadesTrabajoAnioRevisionPorRegistroPatronalEnum.INCAPACIDADESPERMANENTES.getName(), registro.get(PORCENTAJE_INCAPACIDAD).toString()));
            params.add(addParam(AccidentesEnfermedadesTrabajoAnioRevisionPorRegistroPatronalEnum.DEFUNCIONES.getName(), registro.get(TOTAL_DEFUNCIONES).toString()));
            datos.put(registro.get("_id").toString() + i.get(), params);
            i.getAndIncrement();
        });

        return datos;
    }

    @Override
    public List<Integer> getOoad() {
        return null;
    }

    private ParamModel addParam(String name, String value) {
        ParamModel param = new ParamModel();
        param.setName(name);
        param.setValue(value);
        param.setType(CellType.STRING);
        return param;

    }
}
