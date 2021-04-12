package utils;
import java.util.Calendar;
/**
 *
 */
public class TanawarCalendar {
    /**
     *
     */
    private static TanawarCalendar instance = null ;
    /**
     *
     */
    private TanawarCalendar(){}
    /**
     *
     * @return
     */
    public static TanawarCalendar getInstance(){
        if(instance == null)
            instance = new TanawarCalendar();
        return instance;
    }
    /**
     *
     * @param cal
     * @return
     */
    public static String getDateTimeAsString(Calendar cal){
        String res="";
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        month++;
        int year = cal.get(Calendar.YEAR);
        int hour = cal.get(Calendar.HOUR);
        int minutes = cal.get(Calendar.MINUTE);
        int seconds = cal.get(Calendar.SECOND);
        String dayStr = ""+day;
        if(day>0 && day<10){
            dayStr = "0"+day;
        }
        String monthStr = ""+month;
        if(month>0 && month<10){
            monthStr = "0"+month;
        }
        String hourStr = ""+hour;
        if(hour>=0 && hour<10){
            hourStr = "0"+hour;
        }
        String minutesStr = ""+minutes;
        if(minutes>=0 && minutes<10){
            minutesStr = "0"+minutes;
        }
        String secondsStr = ""+seconds;
        if(seconds>=0 && seconds<10){
            secondsStr = "0"+seconds;
        }
        res = dayStr+"/"+monthStr+"/"+year+"AAA"+hourStr+":"+minutesStr+":"+secondsStr;
        return res;
    }
    /**
     *
     * @param cal
     * @return
     */
    public static String getDateTimeAsString(Calendar cal, String regex){
        String res="";
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        month++;
        int year = cal.get(Calendar.YEAR);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minutes = cal.get(Calendar.MINUTE);
        int seconds = cal.get(Calendar.SECOND);
        String dayStr = ""+day;
        if(day>0 && day<10){
            dayStr = "0"+day;
        }
        String monthStr = ""+month;
        if(month>0 && month<10){
            monthStr = "0"+month;
        }
        String hourStr = ""+hour;
        if(hour>=0 && hour<10){
            hourStr = "0"+hour;
        }
        String minutesStr = ""+minutes;
        if(minutes>=0 && minutes<10){
            minutesStr = "0"+minutes;
        }
        String secondsStr = ""+seconds;
        if(seconds>=0 && seconds<10){
            secondsStr = "0"+seconds;
        }
        res = dayStr+"/"+monthStr+"/"+year + regex + hourStr+":"+minutesStr+":"+secondsStr;
        return res;
    }
    /**
     *
     * @param cal
     * @return
     */
    public static String getDateAsString(Calendar cal){
        String res="";
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        month++;
        int year = cal.get(Calendar.YEAR);
        String dayStr = ""+day;
        if(day>0 && day<10){
            dayStr = "0"+day;
        }
        String monthStr = ""+month;
        if(month>0 && month<10){
            monthStr = "0"+month;
        }
        res = dayStr+"/"+monthStr+"/"+year;
        return res;
    }
    /**
     *
     * @param cal
     * @return
     */
    public static String getTimeAsString(Calendar cal){
        String res="";
        int hour = cal.get(Calendar.HOUR);
        int minutes = cal.get(Calendar.MINUTE);
        int seconds = cal.get(Calendar.SECOND);
        String hourStr = ""+hour;
        if(hour>=0 && hour<10){
            hourStr = "0"+hour;
        }
        String minutesStr = ""+minutes;
        if(minutes>=0 && minutes<10){
            minutesStr = "0"+minutes;
        }
        String secondsStr = ""+seconds;
        if(seconds>=0 && seconds<10){
            secondsStr = "0"+seconds;
        }
        res = hourStr+":"+minutesStr+":"+secondsStr;
        return res;
    }
    /**
     *
     * @param dateAsString DD/MM/YYYY   HH:MM:SS
     * @return
     */
    public static Calendar getDateTimeAsCalendar(String dateAsString){
        Calendar res = Calendar.getInstance();
        String[] temp = dateAsString.split("AAA");
        String[] dateStr = temp[0].split("/");
        String[] timeStr = temp[1].split(":");
        res.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateStr[0]));
        res.set(Calendar.MONTH, Integer.parseInt(dateStr[1]));
        res.set(Calendar.YEAR, Integer.parseInt(dateStr[2]));
        res.set(Calendar.HOUR, Integer.parseInt(timeStr[0]));
        res.set(Calendar.MINUTE, Integer.parseInt(timeStr[1]));
        res.set(Calendar.SECOND, Integer.parseInt(timeStr[2]));
        return res;
    }
    /**
     *
     * @param dateAsString DD/MM/YYYY
     * @return
     */
    public static Calendar getDateAsCalendar(String dateAsString){
        Calendar res = Calendar.getInstance();
        String[] dateStr = dateAsString.split("/");
        res.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateStr[0]));
        res.set(Calendar.MONTH, Integer.parseInt(dateStr[1]));
        res.set(Calendar.YEAR, Integer.parseInt(dateStr[2]));
        return res;
    }
    /**
     *
     * @param timeAsString HH:MM:SS
     * @return
     */
    public static Calendar getTimeAsCalendar(String timeAsString){
        Calendar res = Calendar.getInstance();
        String[] dateStr = timeAsString.split(":");
        res.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateStr[0]));
        res.set(Calendar.MONTH, Integer.parseInt(dateStr[1]));
        res.set(Calendar.YEAR, Integer.parseInt(dateStr[2]));
        return res;
    }
    /**
     *
     * @param cal
     * @return
     */
    public static String dateTimeAsString(Calendar cal){
        String res="";
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        month++;
        int year = cal.get(Calendar.YEAR);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minutes = cal.get(Calendar.MINUTE);
        int seconds = cal.get(Calendar.SECOND);
        String dayStr = ""+day;
        if(day>0 && day<10){
            dayStr = "0"+day;
        }
        String monthStr = ""+month;
        if(month>0 && month<10){
            monthStr = "0"+month;
        }
        String hourStr = ""+hour;
        if(hour>=0 && hour<10){
            hourStr = "0"+hour;
        }
        String minutesStr = ""+minutes;
        if(minutes>=0 && minutes<10){
            minutesStr = "0"+minutes;
        }
        String secondsStr = ""+seconds;
        if(seconds>=0 && seconds<10){
            secondsStr = "0"+seconds;
        }
        res = dayStr+"/"+monthStr+"/"+year+"  "+hourStr+":"+minutesStr+":"+secondsStr;
        return res;
    }
    /**
     *
     * @param cal
     * @return
     */
    public static Calendar dateTimeAsCalendar(String cal){
        if(cal == null)
            return Calendar.getInstance();
        String[] temp=cal.split("  ");
        String date = temp[0];
        String time = temp[1];
        String[] d = date.split("/");
        String[] t = time.split(":");
        try{
            int day = Integer.parseInt(d[0]);
            int month = Integer.parseInt(d[1]);
            month--;
            int year = Integer.parseInt(d[2]);
            int hour = Integer.parseInt(t[0]);
            int minutes = Integer.parseInt(t[1]);
            int seconds = Integer.parseInt(t[2]);
            Calendar res = Calendar.getInstance();
            res.set(Calendar.DAY_OF_MONTH, day);
            res.set(Calendar.MONTH, month);
            res.set(Calendar.YEAR, year);
            res.set(Calendar.HOUR_OF_DAY, hour);
            res.set(Calendar.MINUTE, minutes);
            res.set(Calendar.SECOND, seconds);
            return res;
        }catch(NumberFormatException npe){

        }
        return Calendar.getInstance();
    }
}
