package f.com.panoramics.utils;

import android.util.Log;

/**
 * 
 * @author Fatty
 *
 */
public class L {

	private static final int DEBUG_E = 1;
	private static final int DEBUG_D = 2;
	private static final int DEBUG_I = 3;
	private static final int DEBUG_V = 4;

	private static int debug_e = Integer.valueOf(PropertyUtil.getValue("debugstate"));

	/**
	 * debug
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void debug(String tag , String msg) {
		switch (debug_e) {
		case DEBUG_E:
			Log.e(tag, msg);
			break;
		case DEBUG_D:
			Log.d(tag, msg);
			break;
		case DEBUG_I:
			Log.i(tag, msg);
			break;
		case DEBUG_V:
			Log.v(tag, msg);
			break;
		default:
			break;
		}
	}
}
