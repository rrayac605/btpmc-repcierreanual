package mx.gob.imss.cit.pmc.cierreanual.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

import mx.gob.imss.cit.pmc.cierreanual.constants.CierreAnualBatchConstants;

public class DateUtils {

    public static String getCurrentMexicoDateString() {
        TimeZone tz = TimeZone.getTimeZone("America/Mexico_City");
        DateFormat df = new SimpleDateFormat(CierreAnualBatchConstants.PATTERN_DDMMYYYY_TIME);
        df.setTimeZone(tz);
        return df.format(new Date());
    }

    public static Date getCurrentMexicoDate() {
        try {
            return new SimpleDateFormat(CierreAnualBatchConstants.PATTERN_DDMMYYYY_TIME).parse(getCurrentMexicoDateString());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getCurrentYear() {
        String actualDate = getCurrentMexicoDateString();
        return actualDate.substring(4, 8);
    }

    private static String addYearToDate(String date) {
        StringBuilder stringBuilder = new StringBuilder(date);
        stringBuilder.insert(4, getCurrentYear());
        return stringBuilder.toString();
    }

    public static String getMonth(String date) {
        return date.substring(2,4);
    }

    public static String getCurrentDay() {
        return getCurrentMexicoDateString().substring(0,2);
    }

    public static String getCurrentMonth() {
        return getMonth(getCurrentMexicoDateString());
    }

    private static String validateLeapYear(String date) {
        GregorianCalendar calendar = new GregorianCalendar();
        if (calendar.isLeapYear(Integer.parseInt(getCurrentYear()))) {
            StringBuilder stringBuilder = new StringBuilder(date);
            stringBuilder.replace(2, 4, "29");
            return stringBuilder.toString();
        }
        return date;
    }

    public static Date getCycleDate(String date) {
        try {
            date = addYearToDate(date);
            String month = getMonth(date);
            if (month.equals("2")) {
                date = validateLeapYear(date);
            }
            return new SimpleDateFormat(CierreAnualBatchConstants.PATTERN_DDMMYYYY_TIME).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getFormattedDay(String day, String lastDayOfMonth) {
        return day != null && !day.equals("null") ? Integer.parseInt(day) < 10 ? CierreAnualBatchConstants.ZERO_STRING.concat(day) : day
                : lastDayOfMonth != null ? lastDayOfMonth : CierreAnualBatchConstants.FIRST_DAY;
    }

    private static String getFormattedMonth(String month) {
        return month.length() < 2 ? CierreAnualBatchConstants.ZERO_STRING.concat(month) : month;
    }

    /**
     * Utileria que genera la fecha con las horas adecuadas para realizar la
     * consulta de movimientos, se agregan 6 horas a esta fecha dado que al momento
     * de realizar la busqueda mongodb agrega 6 horas y con estas se compenza la
     * busqueda de la misma
     */
    public static Date calculateBeginDate(String year, String month, String day) {
        String stringDate = getFormattedDay(day, null).concat(getFormattedMonth(month)).concat(year)
                .concat(CierreAnualBatchConstants.BEGIN_HOURS);
        TimeZone tz = TimeZone.getTimeZone(CierreAnualBatchConstants.ISO_TIMEZONE);
        DateFormat df = new SimpleDateFormat(CierreAnualBatchConstants.PATTERN_DDMMYYYY_TIME);
        df.setTimeZone(tz);
        try {
            Date formattedDate = df.parse(stringDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(formattedDate);
            calendar.add(Calendar.HOUR, CierreAnualBatchConstants.HOURS_TO_ADD);
            return calendar.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Utileria que genera la fecha con las horas adecuadas para realizar la
     * consulta de movimientos, se agregan 6 horas a esta fecha dado que al momento
     * de realizar la busqueda mongodb agrega 6 horas y con estas se compenza la
     * busqueda de la misma
     */
    public static Date calculateEndDate(String year, String month, String day) {
        LocalDateTime initial = LocalDateTime.of(Integer.parseInt(year), Integer.parseInt(month), CierreAnualBatchConstants.TEN,
                CierreAnualBatchConstants.FIRST, CierreAnualBatchConstants.ZERO, CierreAnualBatchConstants.ZERO, CierreAnualBatchConstants.ZERO);
        initial = initial.with(TemporalAdjusters.lastDayOfMonth());
        String stringDate = getFormattedDay(day, String.valueOf(initial.getDayOfMonth()))
                .concat(getFormattedMonth(month)).concat(year).concat(CierreAnualBatchConstants.END_HOURS);
        TimeZone tz = TimeZone.getTimeZone(CierreAnualBatchConstants.ISO_TIMEZONE);
        DateFormat df = new SimpleDateFormat(CierreAnualBatchConstants.PATTERN_DDMMYYYY_TIME);
        df.setTimeZone(tz);
        try {
            Date formattedDate = df.parse(stringDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(formattedDate);
            calendar.add(Calendar.HOUR, CierreAnualBatchConstants.HOURS_TO_ADD);
            return calendar.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date orElse(Date principal, Date secondary) {
        return principal != null ? principal : secondary;
    }

    public static Date orElse(Date principal, Date secondary, Date third) {
        return principal != null ? principal : secondary != null ? secondary : third;
    }

    public static List<String> calculateCurrentCycle() {
        List<String> cycles = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Objects.requireNonNull(getCurrentMexicoDate()));
        cycles.add(String.valueOf(calendar.get(Calendar.YEAR)));
        if (calendar.get(Calendar.MONTH) < Calendar.MARCH || (calendar.get(Calendar.DATE) <= CierreAnualBatchConstants.FIFTEEN) &&
                Integer.valueOf(Calendar.MARCH).equals(calendar.get(Calendar.MONTH))) {
            cycles.add(String.valueOf(calendar.get(Calendar.YEAR) - CierreAnualBatchConstants.FIRST));
        }
        return cycles;
    }

    public static List<Integer> calculateCurrentCycleInt() {
        List<Integer> cycles = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Objects.requireNonNull(getCurrentMexicoDate()));
        cycles.add(calendar.get(Calendar.YEAR));
        if (calendar.get(Calendar.MONTH) < Calendar.MARCH || (calendar.get(Calendar.DATE) <= CierreAnualBatchConstants.FIFTEEN) &&
                Integer.valueOf(Calendar.MARCH).equals(calendar.get(Calendar.MONTH))) {
            cycles.add(calendar.get(Calendar.YEAR) - CierreAnualBatchConstants.FIRST);
        }
        return cycles;
    }

}
