package f.com.panoramics.utils;

import java.util.Date;

public class DateUtil {

	/**
	 * 
	 * @param timestamp
	 * @return
	 */
	public static String getDate(long timestamp){
		return IntervalUtil.format.format(new Date(timestamp*1000));
	}

}
