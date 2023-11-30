package mx.gob.imss.cit.pmc.cierreanual.utils;

import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;
import mx.gob.imss.cit.pmc.cierreanual.constants.CierreAnualBatchConstants;

public class CycleYearUtils {

//    public static String calculateCCC() {
//        AtomicReference<String> cccResponse = new AtomicReference<>();
//        Date actualDate = DateUtils.getCurrentMexicoDate();
//        if (actualDate != null) {
//            CierreAnualBatchConstants.cycleFrom.keySet().forEach(key -> {
//                if(actualDate.after(DateUtils.getCycleDate(CierreAnualBatchConstants.cycleFrom.get(key)))
//                        && actualDate.before(DateUtils.getCycleDate(CierreAnualBatchConstants.cycleTo.get(key)))) {
//                    cccResponse.set(key);
//                }
//            });
//        }
//        return cccResponse.get();
//    }

    public static String calculateAA() {
        int year = Integer.parseInt(DateUtils.getCurrentYear());
        String month = DateUtils.getMonth(DateUtils.getCurrentMexicoDateString());
        String day = DateUtils.getCurrentDay();
        if (month.compareTo("03") < CierreAnualBatchConstants.ZERO ||
                (month.equals("03") && day.compareTo("16") < CierreAnualBatchConstants.ZERO)) {
            --year;
        }
        return Integer.toString(year).substring(2);
    }

    public static Integer calculateAAAA() {
        int year = Integer.parseInt(DateUtils.getCurrentYear());
        String month = DateUtils.getMonth(DateUtils.getCurrentMexicoDateString());
        String day = DateUtils.getCurrentDay();
        if (month.compareTo("03") < CierreAnualBatchConstants.ZERO ||
                (month.equals("03") && day.compareTo("16") < CierreAnualBatchConstants.ZERO)) {
            --year;
        }
        return year;
    }

}
