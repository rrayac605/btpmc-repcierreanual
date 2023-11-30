package mx.gob.imss.cit.pmc.cierreanual.utils;

import mx.gob.imss.cit.pmc.cierreanual.constants.CierreAnualBatchConstants;

import java.math.BigDecimal;

public class NumberUtils {

    public static Integer safeValidateInteger(Integer integer) {
        return isNotNullInteger(integer) ? integer : CierreAnualBatchConstants.ZERO;
    }

    public static Integer safetyParseBigDecimal(BigDecimal bigDecimal) {
        return bigDecimal != null ? bigDecimal.intValue() : CierreAnualBatchConstants.ZERO;
    }

    public static Boolean isNullInteger(Integer integer) { return integer == null; }

    public static Boolean isNotNullInteger(Integer integer) { return !isNullInteger(integer); }

//    public static Integer processConsequence(Integer consequence, Integer subsidizedDays) {
//        if (consequence != null) {
//            return consequence == 0 || consequence > 9
//                    ? CierreAnualBatchConstants.CONSEQUENCE_EQUIVALENCE.get(consequence)
//                    : consequence;
//        } else {
//            return CierreAnualBatchConstants.FIRST;
//        }
//    }

}
