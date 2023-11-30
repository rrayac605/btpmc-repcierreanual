package mx.gob.imss.cit.pmc.cierreanual.constants;

import mx.gob.imss.cit.pmc.cierreanual.dto.DelegacionOOADDTO;

import java.util.*;

public class CierreAnualBatchConstants {
    public static List<DelegacionOOADDTO> catalogo = new ArrayList();
    static {
        catalogo.add(DelegacionOOADDTO.builder().claveDelegacion(01).claveSubdelegaciones(Arrays.asList(1, 19)).descripcion("AGUASCALIENTES").build());
        catalogo.add(DelegacionOOADDTO.builder().claveDelegacion(02).claveSubdelegaciones(Arrays.asList(1, 2, 3, 4)).descripcion("BAJA CALIFORNIA NORTE").build());
        catalogo.add(DelegacionOOADDTO.builder().claveDelegacion(03).claveSubdelegaciones(Arrays.asList(1, 8)).descripcion("BAJA CALIFOR SUR").build());
        catalogo.add(DelegacionOOADDTO.builder().claveDelegacion(04).claveSubdelegaciones(Arrays.asList(1, 4)).descripcion("CAMPECHE").build());
        catalogo.add(DelegacionOOADDTO.builder().claveDelegacion(8).claveSubdelegaciones(Arrays.asList(1, 3, 5, 8, 22, 60)).descripcion("CHIHUAHUA").build());
        catalogo.add(DelegacionOOADDTO.builder().claveDelegacion(8).claveSubdelegaciones(Arrays.asList(10)).descripcion("JUAREZ 1").build());
        catalogo.add(DelegacionOOADDTO.builder().claveDelegacion(06).claveSubdelegaciones(Arrays.asList(1, 3, 7)).descripcion("COLIMA").build());
        catalogo.add(DelegacionOOADDTO.builder().claveDelegacion(05).claveSubdelegaciones(Arrays.asList(3, 11, 12, 17, 23)).descripcion("COAHUILA").build());
        catalogo.add(DelegacionOOADDTO.builder().claveDelegacion(07).claveSubdelegaciones(Arrays.asList(1, 2)).descripcion("CHIAPAS").build());
        catalogo.add(DelegacionOOADDTO.builder().claveDelegacion(39).claveSubdelegaciones(Arrays.asList( 11, 16, 54, 56, 57)).descripcion("D.F. NORTE").build());
        catalogo.add(DelegacionOOADDTO.builder().claveDelegacion(40).claveSubdelegaciones(Arrays.asList( 1, 6, 11, 54, 58)).descripcion("D.F.SUR").build());
        catalogo.add(DelegacionOOADDTO.builder().claveDelegacion(10).claveSubdelegaciones(Arrays.asList( 1, 13)).descripcion("DURANGO").build());
        catalogo.add(DelegacionOOADDTO.builder().claveDelegacion(12).claveSubdelegaciones(Arrays.asList( 1, 2, 3, 13)).descripcion("GUERRERO").build());
        catalogo.add(DelegacionOOADDTO.builder().claveDelegacion(11).claveSubdelegaciones(Arrays.asList( 1, 5, 8, 14, 17)).descripcion("GUANAJUATO").build());
        catalogo.add(DelegacionOOADDTO.builder().claveDelegacion(27).claveSubdelegaciones(Arrays.asList( 1)).descripcion("HERMOSILLO").build());
        catalogo.add(DelegacionOOADDTO.builder().claveDelegacion(13).claveSubdelegaciones(Arrays.asList( 1, 5, 7, 10)).descripcion("HIDALGO").build());
        catalogo.add(DelegacionOOADDTO.builder().claveDelegacion(14).claveSubdelegaciones(Arrays.asList( 12, 15, 22, 38, 39, 40, 50)).descripcion("JALISCO").build());
        catalogo.add(DelegacionOOADDTO.builder().claveDelegacion(17).claveSubdelegaciones(Arrays.asList( 3, 9, 13, 17, 27)).descripcion("MICHOACAN").build());
        catalogo.add(DelegacionOOADDTO.builder().claveDelegacion(16).claveSubdelegaciones(Arrays.asList( 1, 5)).descripcion("MEX. PONIENTE").build());
        catalogo.add(DelegacionOOADDTO.builder().claveDelegacion(20).claveSubdelegaciones(Arrays.asList( 6, 8, 31, 32, 33, 34)).descripcion("NUEVO LEON").build());
        catalogo.add(DelegacionOOADDTO.builder().claveDelegacion(18).claveSubdelegaciones(Arrays.asList( 1, 11, 15)).descripcion("MORELOS").build());
        catalogo.add(DelegacionOOADDTO.builder().claveDelegacion(29).claveSubdelegaciones(Arrays.asList( 19)).descripcion("MATAMOROS").build());
        catalogo.add(DelegacionOOADDTO.builder().claveDelegacion(26).claveSubdelegaciones(Arrays.asList( 5)).descripcion("MAZATLAN").build());
        catalogo.add(DelegacionOOADDTO.builder().claveDelegacion(19).claveSubdelegaciones(Arrays.asList( 1)).descripcion("NAYARIT").build());
        catalogo.add(DelegacionOOADDTO.builder().claveDelegacion(21).claveSubdelegaciones(Arrays.asList( 2, 3, 4, 53)).descripcion("OAXACA").build());
        catalogo.add(DelegacionOOADDTO.builder().claveDelegacion(22).claveSubdelegaciones(Arrays.asList( 1, 5, 6, 8, 22)).descripcion("PUEBLA").build());
        catalogo.add(DelegacionOOADDTO.builder().claveDelegacion(24).claveSubdelegaciones(Arrays.asList( 1, 2, 7)).descripcion("QUINTANA ROO").build());
        catalogo.add(DelegacionOOADDTO.builder().claveDelegacion(23).claveSubdelegaciones(Arrays.asList( 1, 3)).descripcion("QUERETARO").build());
        catalogo.add(DelegacionOOADDTO.builder().claveDelegacion(26).claveSubdelegaciones(Arrays.asList( 1, 3, 4)).descripcion("SINALOA").build());
        catalogo.add(DelegacionOOADDTO.builder().claveDelegacion(25).claveSubdelegaciones(Arrays.asList( 1, 3, 5, 60)).descripcion("SAN LUIS POTOSI").build());
        catalogo.add(DelegacionOOADDTO.builder().claveDelegacion(27).claveSubdelegaciones(Arrays.asList( 3, 7, 10, 13, 51, 57, 70)).descripcion("SONORA").build());
        catalogo.add(DelegacionOOADDTO.builder().claveDelegacion(29).claveSubdelegaciones(Arrays.asList( 1, 4, 10, 13, 18)).descripcion("TAMAULIPAS").build());
        catalogo.add(DelegacionOOADDTO.builder().claveDelegacion(28).claveSubdelegaciones(Arrays.asList( 1, 2)).descripcion("TABASCO").build());
        catalogo.add(DelegacionOOADDTO.builder().claveDelegacion(02).claveSubdelegaciones(Arrays.asList(5)).descripcion("TIJUANA").build());
        catalogo.add(DelegacionOOADDTO.builder().claveDelegacion(15).claveSubdelegaciones(Arrays.asList( 6, 54, 80)).descripcion("MEX. ORIENTE").build());
        catalogo.add(DelegacionOOADDTO.builder().claveDelegacion(05).claveSubdelegaciones(Arrays.asList(9)).descripcion("TORREON").build());
        catalogo.add(DelegacionOOADDTO.builder().claveDelegacion(30).claveSubdelegaciones(Arrays.asList( 1)).descripcion("TLAXCALA").build());
        catalogo.add(DelegacionOOADDTO.builder().claveDelegacion(31).claveSubdelegaciones(Arrays.asList( 2, 7, 9, 25)).descripcion("VERACRUZ NORTE").build());
        catalogo.add(DelegacionOOADDTO.builder().claveDelegacion(31).claveSubdelegaciones(Arrays.asList( 12)).descripcion("VERACRUZ PUERTO").build());
        catalogo.add(DelegacionOOADDTO.builder().claveDelegacion(32).claveSubdelegaciones(Arrays.asList( 2, 3, 38, 45)).descripcion("VERACRUZ SUR").build());
        catalogo.add(DelegacionOOADDTO.builder().claveDelegacion(33).claveSubdelegaciones(Arrays.asList( 1, 33)).descripcion("YUCATAN").build());
        catalogo.add(DelegacionOOADDTO.builder().claveDelegacion(34).claveSubdelegaciones(Arrays.asList( 1, 9)).descripcion("ZACATECAS").build());
    }
    public static final String HEADER = "00000000000000000000000000000HEADER";

    public static final String REF_TSR = "TSR";

    public static final String REF_CASUI99 = "CASUI99";

    public static final String FILLER3 = "000";

    public static final String FILLER73 = "0000000000000000000000000000000000000000000000000000000000000000000000000";

    public static final String FILLER7 = "0000000";

    public static final String REF_0TRR = "0RTT";

    public static final String FILLER14 = "00000000000000";

    public static final String PATTERN_DDMMYYYY_TIME = "ddMMyyyy HH:mm:ss";

    public static final Integer DELEGACION = 99;

    public static final String HEADER_ID = "HEADER_ID";

    public static final String NOMBRE_ARCHIVO = "nombreArchivo";

    public static final Integer CHUNK_SIZE = 500;

    public static final String BATCH_SIZE = "batchSize";

    public static final String MANUAL = "MN";

    public static final Integer POOL_SIZE = 64;

    public static final String MISSING_CURP = "000000000000000000";

    public static final String MISSING_NSS_RP = "0000000000";

    public static final String RTT = "RTT";

    public static final Object[] CRITERIA_ESTADO_REGISTRO_CORRECTO = { 1, 5 };

    public static final Object[] CRITERIA_ESTADO_REGISTRO_SUSCEPTIBLE = { 4, 8 };

    public static final Object[] CRITERIA_CONSECUENCIA = { null, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };

    public static final Object CRITERIA_SITUACION_REGISTRO = 1;

    public static final Object CRITERIA_CASO_REGISTRO_OPORTUNO = 1;

    public static final Object CRITERIA_CASO_REGISTRO_EXTEMPORANEO = 2;

    public static final Object JANUARY = 1;

    public static final Integer MARCH = 3;

    public static final Integer FIRST = 1;

    public static final Integer FIFTEEN = 15;

    public static final Integer ZERO = 0;

    public static final String ZERO_STRING = "0";

    public static final String FIRST_DAY = "01";

    public static final String BEGIN_HOURS = " 00:00:00";

    public static final String END_HOURS = " 18:59:59";

    public static final String ISO_TIMEZONE = "UTC";

    public static final Integer HOURS_TO_ADD = 6;

    public static final Integer TEN = 10;

    public static final String EMPTY = "";

//    public static final String ORACLE_URL = "oracleUrl";
//
//    public static final String ORACLE_USERNAME = "oracleUsername";
//
//    public static final String ORACLE_PASSWORD = "oraclePassword";

//    public static final String ORACLE_DRIVER = "oracle.jdbc.driver.OracleDriver";

    public static final String MOVEMENT_DTO_PACKAGE_CLASS = "mx.gob.imss.cit.pmc.rtt.dto.MovementDTO";



//    public static final String DETAIL_WRITER_SQL_STATEMENT = "INSERT INTO MGCARGA1.MCT_PMC_DETALLE_RTT " +
//            "(OBJECT_ID, REF_CVE_ID_PMC_ENCABEZADO_RTT, NUM_NSS, REF_REGISTRO_PATRONAL, CVE_ID_CONSECUENCIA," +
//            "FEC_INICIO, NUM_DIAS_SUBSIDIADOS, FEC_TERMINO, CVE_ID_TIPO_RIESGO, NUM_PORCENTAJE_INCAPACIDAD," +
//            "NOM_NOMBRE_ASEGURADO, NOM_APELLIDO_PAT, NOM_APELLIDO_MAT, REF_FILLER1, REF_CURP, REF_FILLER2," +
//            "CVE_DELEGACION_ASEGURADO, CVE_SUBDELEGACION_ASEGURADO, REF_FILLER3, IND_PROCESADO, FEC_ALTA," +
//            "CVE_ID_PMC_DETALLE_RTT)" +
//            "VALUES" +
//            "(:mongoObjectId, :cveIdPmcRttEncabezado, :numNss, :refRegistroPatronal, :cveIdConsecuencia," +
//            ":fecInicio, :numDiasSubsidiados, :fecTermino, :cveTipoRiesgo, :numPorcentajeIncapacidad," +
//            ":nombre, :apellidoPat, :apellidoMat, :refFiller1, :curp, :refFiller2," +
//            ":cveDelegacionAsegurado, :cveSubdelegacionAsegurado, :refFiller3, :indProcesado, :fecAlta," +
//            "MGCARGA1.SEQ_PMCDETALLERTT.NEXTVAL)";


}
