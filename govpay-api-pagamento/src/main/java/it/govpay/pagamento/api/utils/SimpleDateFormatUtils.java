package it.govpay.pagamento.api.utils;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class SimpleDateFormatUtils {

        private static final String PATTERN_DATA_JSON_YYYY_MM_DD_T_HH_MM_SS_SSS = "yyyy-MM-dd'T'HH:mm:ss.SSS";
        public static SimpleDateFormat newSimpleDateFormat() {
                SimpleDateFormat sdf = new SimpleDateFormat(SimpleDateFormatUtils.PATTERN_DATA_JSON_YYYY_MM_DD_T_HH_MM_SS_SSS);
                sdf.setTimeZone(TimeZone.getTimeZone("CET"));
                sdf.setLenient(false);
                return sdf;
        }

        private static final String PATTERN_DATA_JSON_YYYY_MM_DD = "yyyy-MM-dd";
        public static SimpleDateFormat newSimpleDateFormatSoloData() {
                SimpleDateFormat sdf = new SimpleDateFormat(SimpleDateFormatUtils.PATTERN_DATA_JSON_YYYY_MM_DD);
                sdf.setTimeZone(TimeZone.getTimeZone("CET"));
                sdf.setLenient(false);
                return sdf;
        }
}
