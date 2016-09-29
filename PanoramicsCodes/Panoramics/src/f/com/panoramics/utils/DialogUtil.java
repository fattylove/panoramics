package f.com.panoramics.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import f.com.panoramics.base.ActivityManager;
import f.com.panoramics.view.EarthProgressDialog;
import f.com.panoramics.view.UploadProgressDialog;


/**
 * 
 * @author Fatty
 * 
 * Dialog
 *
 */
public class DialogUtil {
	
	/**
	 * 
	 * @param context
	 * @param r
	 */
	public static void showCheckDialog(final Activity context, int r) {
		if(ActivityManager.isHasActivity(context)){
			AlertDialog.Builder builder = new Builder(context);
			builder.setMessage(context.getString(r));
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			builder.create().show();
		}
	}
	
	/**
	 * 
	 * @param context
	 * @param msg
	 */
	public static void showCheckDialog(final Activity context, String msg) {
		if(ActivityManager.isHasActivity(context)){
			AlertDialog.Builder builder = new Builder(context);
			builder.setMessage(msg);
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			builder.create().show();
		}
	}
	
	/**
	 * 
	 * @param activity
	 * @return
	 */
	public static EarthProgressDialog createProgressDialog(Activity activity){
		return EarthProgressDialog.createDialog(activity);
	}
	
	public static UploadProgressDialog createUploadProgressDialog(Activity activity){
		return UploadProgressDialog.createDialog(activity);
	}
}
