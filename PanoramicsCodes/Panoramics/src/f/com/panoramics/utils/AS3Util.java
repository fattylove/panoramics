package f.com.panoramics.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;

import f.com.panoramics.constant.Constant;

/**
 * 
 * @author Fatty
 * 
 * 亚马逊 AS3 Api
 *
 */
public class AS3Util {
	
	private static CognitoCachingCredentialsProvider sCredProvider;

	public static CognitoCachingCredentialsProvider getCredProvider(Context context) {
		if (sCredProvider == null) {
			sCredProvider = new CognitoCachingCredentialsProvider(
					context,
					Constant.AWS_ACCOUNT_ID, 
					Constant.COGNITO_POOL_ID,
					Constant.COGNITO_ROLE_UNAUTH, 
					Constant.COGNITO_ROLE_AUTH,
					Regions.US_EAST_1
			);
		}
		return sCredProvider;
	}
	
	/**
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getMediaPath(String fileName){//mFile.getName()
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
		Date date = new Date();
		String ymr[] = format.format(date).split("-");
		String year = ymr[0];
		String month =  ymr[1];
		String day =  ymr[2];
		return "media" +Constant.P + year + Constant.P + month + Constant.P + day + Constant.P + fileName;
	}
	
	/**
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getHeaderPath(String fileName){//mFile.getName()
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
		Date date = new Date();
		String ymr[] = format.format(date).split("-");
		String year = ymr[0];
		String month =  ymr[1];
		String day =  ymr[2];
		return "profile" +Constant.P + year + Constant.P + month + Constant.P + day + Constant.P + fileName;
	}
	
	
}