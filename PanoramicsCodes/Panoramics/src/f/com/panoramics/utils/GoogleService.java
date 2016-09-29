package f.com.panoramics.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.net.Uri;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class GoogleService {
	
	public static  boolean checkGooglePlayServices(final Activity activity) {
		
		boolean isHave = false ;
		int result = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
		
		switch (result) {
		case ConnectionResult.SUCCESS:
			isHave = true;
			break;
		case ConnectionResult.SERVICE_INVALID:
			GooglePlayServicesUtil.getErrorDialog(ConnectionResult.SERVICE_INVALID, activity, 0, new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					Uri uri = Uri.parse("https://www.google.com");
					Intent it = new Intent(Intent.ACTION_VIEW,uri);
					activity.startActivity(it);
				}
			}).show();
			break;
		case ConnectionResult.SERVICE_MISSING:
			GooglePlayServicesUtil.getErrorDialog(ConnectionResult.SERVICE_MISSING, activity, 0, new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					Uri uri = Uri.parse("https://www.google.com");
					Intent it = new Intent(Intent.ACTION_VIEW,uri);
					activity.startActivity(it);
				}
			}).show();
			break;
		case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
			GooglePlayServicesUtil.getErrorDialog(ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED, activity, 0, new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					Uri uri = Uri.parse("https://www.google.com");
					Intent it = new Intent(Intent.ACTION_VIEW,uri);
					activity.startActivity(it);
				}
			}).show();
			break;
		case ConnectionResult.SERVICE_DISABLED:
			GooglePlayServicesUtil.getErrorDialog(ConnectionResult.SERVICE_DISABLED, activity, 0, new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					Uri uri = Uri.parse("https://www.google.com");
					Intent it = new Intent(Intent.ACTION_VIEW,uri);
					activity.startActivity(it);
				}
			}).show();
			break;
		}
		return isHave;
	}

}
