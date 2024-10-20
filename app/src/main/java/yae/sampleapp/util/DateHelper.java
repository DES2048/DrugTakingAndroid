package yae.sampleapp.util;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {
    public static String prettyDate(Date date) {
        return date != null ? new PrettyTime().format(date) : "";
    }
    public static String formatDate(Date date) {
        SimpleDateFormat formatter  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(date);
    }
}
