package mx.gob.imss.cit.pmc.cierreanual.utils;

import mx.gob.imss.cit.pmc.cierreanual.constants.CierreAnualBatchConstants;

public class StringUtils {

    public static String safeValidate(String str) {
        return isNotEmpty(str) ? str : CierreAnualBatchConstants.EMPTY;
    }

    public static String safeValidateCurp(String curp) {
        return isNotEmpty(curp) ? curp : CierreAnualBatchConstants.MISSING_CURP;
    }

    public static Boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static Boolean isNotEmpty(String str) { return !isEmpty(str); }

    public static String safeSubString(String str, Integer length) {
        if (isNotEmpty(str)) {
            return str.length() <= length ? str : str.substring(CierreAnualBatchConstants.ZERO, length);
        }
        return CierreAnualBatchConstants.MISSING_NSS_RP;
    }

}
