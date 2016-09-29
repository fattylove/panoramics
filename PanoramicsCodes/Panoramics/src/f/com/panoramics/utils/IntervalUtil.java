package f.com.panoramics.utils;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class IntervalUtil {  
	
	public static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss" ,Locale.ENGLISH);  
  
    public static String getInterval(long createtime) {
        String result = null; 
		ParsePosition pos = new ParsePosition(0);
		String cTime = IntervalUtil.format.format(new Date(createtime*1000));
		Date d1 = (Date) format.parse(cTime, pos);
		
		long time = new Date().getTime() - d1.getTime();
		long second_x = time / 1000L;
		long minute_x = time / 60000L;
		long hour_x = time   / 3600000L;
		long day_x = time    / 86400000L;
		long month_x = time  / 2592000000L;
		long year_x = time   / 31104000000L;
		
		if ( second_x < 60 && second_x > 0) { // s
			int s = (int) ((time % 60000) / 1000);
			if (s == 1) {
				result = s + " second ago";
			} else {
				result = s + " seconds ago";
			}
		}  else if ( minute_x < 60 && minute_x > 0) { // m
			int m = (int) ((time % 3600000) / 60000);
			if (m == 1) {
				result = m + " minute ago";
			} else {
				result = m + " minutes ago";
			}
		} else if (hour_x < 24 && hour_x >= 0) { // h
			int h = (int) hour_x;
			if (h == 1) {
				result = h + " hour ago";
			} else {
				result = h + " hours ago";
			}
		}else if(day_x < 30 && day_x >= 0){ //d
			int d = (int) day_x;
			if (d == 1) {
				result = d + " day ago";
			} else {
				if(d < 7){
					result = d + " days ago";
				}else if(d >=7 && d < 14){
					result = "Last week";
				}else if(d >=14 && d < 21){
					result = "2 weeks ago";
				}else if(d >=21 && d < 28){
					result = "3 weeks ago";
				}else{
					result = "4 weeks ago";
				}
			}
		}else if(month_x < 30 && time/ month_x >= 0){ //m
			int m = (int) month_x;
			if (m == 1) {
				result = "Last month";
			} else {
				result = m + " months ago";
			}
		}else if(year_x < 12 && year_x >= 0){
			int y = (int) year_x;
			if (y == 1) {
				result = "Last year";
			} else {
				result = y + " years ago";
			}
		}
		return result;
	} 
}  