package f.com.panoramics.utils;


/**
 * 
 * @author Fatty
 * 
 * sdcard
 *
 */
public class SDCardUtil {
	
	/**
	 * 检查sdcard是否挂载
	 * 
	 * @return
	 */
	public static  boolean existSDCard() {
		return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED) ? true : false;
	}
}