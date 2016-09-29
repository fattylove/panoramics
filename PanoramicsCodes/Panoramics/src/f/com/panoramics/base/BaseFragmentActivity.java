package f.com.panoramics.base;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.guxiu.panoramics.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import f.com.panoramics.activity.SelectSignInActivity;
import f.com.panoramics.base.UIApplication.TrackerName;
import f.com.panoramics.constant.Constant;

/**
 * 
 * @author Fatty
 *
 */
public abstract class BaseFragmentActivity extends FragmentActivity {

	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;
	private String screenTag ;
	private Tracker tracker ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.panoramics);
		ActivityManager.getAppManager().addActivity(this);
		preferences = this.getSharedPreferences(Constant.APP_NAME, Context.MODE_PRIVATE);
		editor = preferences.edit();
		tracker =((UIApplication) this.getApplication()).getTracker(TrackerName.APP_TRACKER);
	}

	public void finishCurrentActivity() {
		ActivityManager.getAppManager().finishActivity();
	}
	
	public void finishCurrentActivityWithAmination() {
		ActivityManager.getAppManager().finishActivity();
		showAnimationOut();
	}
	
	public void finishAllActivity(){
		ActivityManager.getAppManager().finishAllActivity();
	}

	/**
	 * 
	 * @param msg
	 */
	public void toast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 
	 * @param r
	 */
	public void toast(int r) {
		Toast.makeText(this, r, Toast.LENGTH_SHORT).show();
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

	/**
	 * 
	 * @param c
	 */
	public void goActivity(Class<?> c) {
		Intent intent = new Intent();
		intent.setClass(this, c);
		startActivity(intent);
	}

	/**
	 * back
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			ActivityManager.getAppManager().finishActivity(this);
			showAnimationOut();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	public String getScreenTag() {
		return screenTag;
	}

	public void setScreenTag(String screenTag) {
		this.screenTag = screenTag;
		tracker.setScreenName(this.screenTag);
	}

	@Override
	protected void onResume() {
		super.onResume();
		tracker.send(new HitBuilders.AppViewBuilder().set("Activity", "onResume()").build());
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityManager.getAppManager().finishActivity(this);
		tracker.send(new HitBuilders.AppViewBuilder().set("Activity", "onDestroy()").build());
		ImageLoader.getInstance().clearMemoryCache();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();

		// 清除Image内存中的缓存
		ImageLoader.getInstance().clearMemoryCache();

		// 提醒系统GC
		System.gc();
	}

	@Override
	protected void onPause() {
		super.onPause();
		tracker.send(new HitBuilders.AppViewBuilder().set("Activity", "onPause()").build());
	}
	
	/**
	 * in animation
	 */
	public void showAnimationOut() {
		overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
	}

	/**
	 * back Animation
	 */
	public void showAnimationIn() {
		overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
	}

	/**
	 * back Animation
	 */
	public void showAnimationBottomIn() {
		overridePendingTransition(R.anim.push_bottom_in_translate, R.anim.push_bottom_out_alpha);
	}

	/**
	 * in animation
	 */
	public void showAnimationBottomOut() {
		overridePendingTransition(R.anim.push_bottom_in_alpha, R.anim.push_bottom_out_translate);
	}
	
	public void tokenInvalid(){
		putPreBoolean("isLogin", false);
		Intent intent = new Intent();
		intent.setClass(this,SelectSignInActivity.class);
		this.startActivity(intent);

		ActivityManager manager = ActivityManager.getAppManager();
		manager.finishAllActivity();
	}
}
