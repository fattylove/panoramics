package f.com.panoramics.view;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.guxiu.panoramics.R;

import f.com.panoramics.utils.ImageUtil;

/**
 * 
 * @author Fatty
 *
 */
public class PanoToast {

	
	/**
	 * 
	 * @param activity
	 * @param msg
	 */
	public static void showToast(Activity activity , String msg) {
		LayoutInflater inflater = activity.getLayoutInflater();
		View layout = inflater.inflate(R.layout.panoramics_pano_toast_layout, null);
		TextView title = (TextView) layout.findViewById(R.id.toastTextView);
		title.setShadowLayer(1, 0, -2, Color.parseColor("#80000000"));
		if(!TextUtils.isEmpty(msg)){
			title.setText(msg);
		}
		Toast toast = new Toast(activity.getApplicationContext());
		toast.setGravity( Gravity.TOP, 0, ImageUtil.ImageSizeUtils.dip2px(activity, 50));
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(layout);
		toast.show();
	}
	
	/**
	 * 
	 * @param activity
	 * @param msg
	 */
	public static void showToast(Activity activity , int msgId) {
		LayoutInflater inflater = activity.getLayoutInflater();
		View layout = inflater.inflate(R.layout.panoramics_pano_toast_layout, null);
		TextView title = (TextView) layout.findViewById(R.id.toastTextView);
		title.setShadowLayer(1, 0, -2, Color.parseColor("#80000000"));
		title.setText(msgId);
		Toast toast = new Toast(activity.getApplicationContext());
		toast.setGravity( Gravity.TOP, 0, ImageUtil.ImageSizeUtils.dip2px(activity, 50));
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(layout);
		toast.show();
	}
	
	
}
