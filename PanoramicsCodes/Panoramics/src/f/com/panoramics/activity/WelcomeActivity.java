package f.com.panoramics.activity;

import java.io.File;
import java.io.IOException;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;

import com.guxiu.panoramics.R;

import f.com.panoramics.base.BaseFragmentActivity;
import f.com.panoramics.constant.Constant;
import f.com.panoramics.gcm.service.MessageReceivingService;
import f.com.panoramics.utils.GoogleService;
import f.com.panoramics.utils.ImageUtil;
import f.com.panoramics.utils.ImageUtil.CopyImages;
import f.com.panoramics.utils.SDCardUtil;
import fatty.library.http.core.CallBack;
import fatty.library.http.core.HttpService;
import fatty.library.http.core.Parameters;

/**
 * 
 * @author Fatty
 * 
 * 欢迎界面
 *
 */
public class WelcomeActivity extends BaseFragmentActivity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setScreenTag("WelcomeActivity");
		
		View loadingPage = View.inflate(this, R.layout.panoramics_welcome_default_layout, null);
		setContentView(loadingPage);
		
		View contentView = View.inflate(this,R.layout.panoramics_welcome_layout, null);
		setScreenToSharedPreferences();
		
//		copyImagesToSdcard();
		
		openNotificationSerivce();
		
		if(	GoogleService.checkGooglePlayServices(this)){
			welcome(loadingPage, getPreBoolean("isLogin"));
		}
		
//		final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
//		final String tmDevice, tmSerial, androidId;
//		tmDevice = "" + tm.getDeviceId();
//		tmSerial = "" + tm.getSimSerialNumber();
//		androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
//		UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
//		String deviceId = deviceUuid.toString();
//		System.err.println("deviceId->"+tm.getDeviceId());
//		System.err.println("deviceId->" + deviceId);
		
		/**
		 * 
在AndroidManifest.xml文件中的Activity有个属性android:windowSoftInputMode，是用于控制软键盘的。
这个属性的设置将会影响两件事情:

1>     软键盘的状态——是否它是隐藏或显示——当活动(Activity)成为用户关注的焦点。

2>     活动的主窗口调整——是否减少活动主窗口大小以便腾出空间放软键盘或是否当活动窗口的部分被软键盘覆盖时它的内容的当前焦点是可见的。

"stateUnspecified" 软键盘的状态(是否它是隐藏或可见)没有被指定。系统将选择一个合适的状态或依赖于主题的设置。这个是为了软件盘行为默认的设置。 
"stateUnchanged" 软键盘被保持无论它上次是什么状态，是否可见或隐藏，当主窗口出现在前面时。 
"stateHidden" 当用户选择该Activity时，软键盘被隐藏——也就是，当用户确定导航到该Activity时，而不是返回到它由于离开另一个Activity。 
"stateAlwaysHidden" 软键盘总是被隐藏的，当该Activity主窗口获取焦点时。 
"stateVisible" 软键盘是可见的，当那个是正常合适的时(当用户导航到Activity主窗口时)。 
"stateAlwaysVisible" 当用户选择这个Activity时，软键盘是可见的——也就是，也就是，当用户确定导航到该Activity时，而不是返回到它由于离开另一个Activity。 
"adjustUnspecified" 它不被指定是否该Activity主窗口调整大小以便留出软键盘的空间，或是否窗口上的内容得到屏幕上当前的焦点是可见的。系统将自动选择这些模式中一种主要依赖于是否窗口的内容有任何布局视图能够滚动他们的内容。如果有这样的一个视图，这个窗口将调整大小，这样的假设可以使滚动窗口的内容在一个较小的区域中可见的。这个是主窗口默认的行为设置。 
"adjustResize" 该Activity主窗口总是被调整屏幕的大小以便留出软键盘的空间。 
"adjustPan" 该Activity主窗口并不调整屏幕的大小以便留出软键盘的空间。相反，当前窗口的内容将自动移动以便当前焦点从不被键盘覆盖和用户能总是看到输入内容的部分。这个通常是不期望比调整大小，因为用户可能关闭软键盘以便获得与被覆盖内容的交互操作。
		 */
//		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); 
		
		/**
		 * Android L
		 * 
		 *  1、更换Android L的 style
		 *  
		 *  2、使用V7 support  CardView RectyledView
		 *  
		 *  3、设计材料化
		 *  
		 *  4、ActionBar
		 *  
		 *  5、Menu
		 *  
		 *  6、Animation
		 *  
		 *  
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 */
		
		/*
		 * EditText 的字数限制
		 */
		
		
		
		
		
		
		
		
		
		
		
		
/*		
		
		
		
		ActivityManager actvityManager = (ActivityManager) this.getSystemService( ACTIVITY_SERVICE );
		ActivityManager.MemoryInfo mInfo = new ActivityManager.MemoryInfo ();
		actvityManager.getMemoryInfo( mInfo );
		List<RunningAppProcessInfo> runningServiceInfos = actvityManager.getRunningAppProcesses();
		
		WifiManager manager = (WifiManager)this.getSystemService(Context.WIFI_SERVICE);
		TelephonyManager telephonyManager =(TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
		DownloadManager downloadManager = (DownloadManager)this.getSystemService(Context.DOWNLOAD_SERVICE);
		
		Log.i( "TAG", " minfo.availMem " + mInfo.availMem );
		Log.i( "TAG", " minfo.lowMemory " + mInfo.lowMemory );
		Log.i( "TAG", " minfo.threshold " + mInfo.threshold );
		
		
*/	

		Parameters parameters = new Parameters();
		parameters.put("uid", "1");
		parameters.put("nickname", "nickname");
		parameters.put("username", "username");
		parameters.put("password", "password");
		HttpService service = new HttpService();
		service.get("", parameters, new CallBack<String>() {
			@Override
			public void onSuccess(String t, int code) {
				super.onSuccess(t, code);
			}

			@Override
			public void onFailure(Throwable t, int code, String strMsg) {
				super.onFailure(t, code, strMsg);
			}
		});
		
	}
	
	/**
	 * 复制assets图片到sdcard
	 */
	public void copyImagesToSdcard(){
		boolean hasSdcard = SDCardUtil.existSDCard();
		if(hasSdcard){
			File file = new File(Environment.getExternalStorageDirectory().getPath() + Constant.P + "panoramics-pic");
			if(!file.exists()){
				new Thread(new Runnable() {
					@Override
					public void run() {
						ImageUtil.CopyImages copyImages = new CopyImages(WelcomeActivity.this);
						try {
							copyImages.copy();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}).start();
			}
		}
	}
	
	/**
	 * 开启推送服务
	 */
	public void openNotificationSerivce(){
		startService(new Intent(this, MessageReceivingService.class));
	}

	/**
	 * 存储屏幕分辨率
	 */
	public void setScreenToSharedPreferences() {
		DisplayMetrics displayMetrices = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrices);
		putPreInt("screen_w", displayMetrices.widthPixels);
		putPreInt("screen_h", displayMetrices.heightPixels);
	}

	/**
	 * 主页跳转
	 * 
	 * @param contentView
	 * @param isLogin
	 */
	private void welcome(View contentView, final boolean isLogin) {
		Animation animation = AnimationUtils.loadAnimation(this, R.anim.alpha);
		contentView.startAnimation(animation);
		animation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation arg0) {
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				if (isLogin) {
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							Intent intent = new Intent();
							intent.setClass(WelcomeActivity.this, HomeActivity.class);
							WelcomeActivity.this.startActivity(intent);
							finishCurrentActivityWithAmination();
						}
					}, 500);
				} else {
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							Intent intent = new Intent();
							intent.setClass(WelcomeActivity.this, SelectSignInActivity.class);
							WelcomeActivity.this.startActivity(intent);
							finishCurrentActivityWithAmination();
						}
					}, 500);
				}
			}
		});
	}
	
	
}