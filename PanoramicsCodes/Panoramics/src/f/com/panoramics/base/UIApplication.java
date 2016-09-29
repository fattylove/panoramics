package f.com.panoramics.base;

import java.util.HashMap;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.guxiu.panoramics.R;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

public class UIApplication extends Application {
	
	
	private static final String PROPERTY_ID = "UA-56379033-1";
	
	/**
	 * Enum used to identify the tracker that needs to be used for tracking.
	 * 
	 * A single tracker is usually enough for most purposes. In case you do need
	 * multiple trackers, storing them all in Application object helps ensure
	 * that they are created only once per application instance.
	 */
	public enum TrackerName {
		APP_TRACKER, 		// Tracker used only in this app.
		GLOBAL_TRACKER, 	// Tracker used by all the apps from a company. eg:  roll-up tracking.
		ECOMMERCE_TRACKER, 	// Tracker used by all ecommerce transactions from a company.
	}

	HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();
	
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressWarnings("unused")
	@Override
	public void onCreate() {
		if (false && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyDialog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyDeath().build());
		}

		super.onCreate();
		initImageLoader(getApplicationContext());
		
		//定义Tracker
        Tracker t = this.getTracker(TrackerName.APP_TRACKER);
        t.setScreenName("UIApplication");
        t.send(new HitBuilders.AppViewBuilder().build());
	}
	
	/**
	 * 初始化Tracker
	 * 
	 * @param trackerId
	 * @return
	 */
	public synchronized Tracker getTracker(TrackerName trackerId) {
		if (!mTrackers.containsKey(trackerId)) {
			GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
			Tracker t = (trackerId == TrackerName.APP_TRACKER) ? analytics.newTracker(PROPERTY_ID): (trackerId == TrackerName.GLOBAL_TRACKER) ? analytics
							.newTracker(R.xml.global_tracker) : analytics.newTracker(R.xml.global_tracker);
			mTrackers.put(trackerId, t);
		}
		return mTrackers.get(trackerId);
	}
	
	/**


File cacheDir = StorageUtils.getCacheDirectory(context);
ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
        .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
        .diskCacheExtraOptions(480, 800, null)
        .taskExecutor(...)
        .taskExecutorForCachedImages(...)
        .threadPoolSize(3) // default
        .threadPriority(Thread.NORM_PRIORITY - 2) // default
        .tasksProcessingOrder(QueueProcessingType.FIFO) // default
        .denyCacheImageMultipleSizesInMemory()
        .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
        .memoryCacheSize(2 * 1024 * 1024)
        .memoryCacheSizePercentage(13) // default
        .diskCache(new UnlimitedDiscCache(cacheDir)) // default
        .diskCacheSize(50 * 1024 * 1024)
        .diskCacheFileCount(100)
        .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
        .imageDownloader(new BaseImageDownloader(context)) // default
        .imageDecoder(new BaseImageDecoder()) // default
        .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
        .writeDebugLogs()
        .build();



// DON'T COPY THIS CODE TO YOUR PROJECT! This is just example of ALL options using.
// See the sample project how to use ImageLoader correctly.
DisplayImageOptions options = new DisplayImageOptions.Builder()
        .showImageOnLoading(R.drawable.ic_stub) // resource or drawable
        .showImageForEmptyUri(R.drawable.ic_empty) // resource or drawable
        .showImageOnFail(R.drawable.ic_error) // resource or drawable
        .resetViewBeforeLoading(false)  // default
        .delayBeforeLoading(1000)
        .cacheInMemory(false) // default
        .cacheOnDisk(false) // default
        .preProcessor(...)
        .postProcessor(...)
        .extraForDownloader(...)
        .considerExifParams(false) // default
        .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
        .bitmapConfig(Bitmap.Config.ARGB_8888) // default
        .decodingOptions(...)
        .displayer(new SimpleBitmapDisplayer()) // default
        .handler(new Handler()) // default
        .build();
	 */
	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you may tune some of them,
		// or you can create default configuration by
		//  ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
		 .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
	        .diskCacheExtraOptions(480, 800, null)
	        .threadPoolSize(3) // default
	        .threadPriority(Thread.NORM_PRIORITY - 1) // default
	        .tasksProcessingOrder(QueueProcessingType.FIFO) // default
	        .denyCacheImageMultipleSizesInMemory()
	        .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
	        .memoryCacheSize(2 * 1024 * 1024)
	        .memoryCacheSizePercentage(13) // default
	        .diskCacheSize(50 * 1024 * 1024)
	        .diskCacheFileCount(100)
	        .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
	        .imageDownloader(new BaseImageDownloader(context)) // default
	        .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
//	        .writeDebugLogs()
	        .build();
		
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}
	
	@Override
	public void onLowMemory() {
		super.onLowMemory();
		//清除Image内存中的缓存
		ImageLoader.getInstance().clearMemoryCache();
		
		//提醒系统GC
		System.gc();
	}
}