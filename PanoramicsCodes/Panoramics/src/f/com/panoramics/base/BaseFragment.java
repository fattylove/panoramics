package f.com.panoramics.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import f.com.panoramics.activity.SelectSignInActivity;
import f.com.panoramics.constant.Constant;

/**
 * 
 * @author Fatty
 *
 */
public class BaseFragment extends Fragment {

	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;
	private Activity activity;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
		preferences = activity.getSharedPreferences(Constant.APP_NAME, Context.MODE_PRIVATE);
		editor = preferences.edit();
	}

	/**
	 * 
	 * @param msg
	 */
	public void toast(String msg) {
		Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 
	 * @param r
	 */
	public void toast(int r) {
		Toast.makeText(activity, r, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public boolean containsPre(String key) {
		return preferences.contains(key) ? true : false;
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void putPreBoolean(String key, boolean value) {
		editor.putBoolean(key, value);
		editor.commit();
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void putPreString(String key, String value) {
		editor.putString(key, value);
		editor.commit();
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void putPreInt(String key, int value) {
		editor.putInt(key, value);
		editor.commit();
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void putPreFloat(String key, float value) {
		editor.putFloat(key, value);
		editor.commit();
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void putPreLong(String key, long value) {
		editor.putLong(key, value);
		editor.commit();
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public int getPreInt(String key) {
		return preferences.getInt(key, 0);
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public String getPreString(String key) {
		return preferences.getString(key, "");
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public long getPreLong(String key) {
		return preferences.getLong(key, 0);
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public boolean getPreBoolean(String key) {
		return preferences.getBoolean(key, false);
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public float getPreFloat(String key) {
		return preferences.getFloat(key, 0.0f);
	}
	
	public void tokenInvalid(){
		putPreBoolean("isLogin", false);
		Intent intent = new Intent();
		intent.setClass(activity,SelectSignInActivity.class);
		this.startActivity(intent);

		ActivityManager manager = ActivityManager.getAppManager();
		manager.finishAllActivity();
	}

	
	@Override
	public void onDetach() {
		super.onDetach();
		// 清除Image内存中的缓存
		ImageLoader.getInstance().clearMemoryCache();

		// 提醒系统GC
		System.gc();
	}
	
	@Override
	public void onLowMemory() {
		super.onLowMemory();
		// 清除Image内存中的缓存
		ImageLoader.getInstance().clearMemoryCache();

		// 提醒系统GC
		System.gc();
	}

}
