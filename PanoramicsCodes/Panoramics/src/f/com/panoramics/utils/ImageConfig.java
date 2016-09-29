package f.com.panoramics.utils;

import android.graphics.Bitmap;

import com.guxiu.panoramics.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class ImageConfig {

	/**
	 * 
	 * @return
	 */
	public static DisplayImageOptions getHeaderConfig() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.p_default_header_img)
				.showImageOnFail(R.drawable.p_default_header_img)
				.showImageForEmptyUri(R.drawable.p_default_header_img)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		return options;
	}
	
	public static DisplayImageOptions getPCenterHeaderConfig() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
//				.showImageOnLoading(R.drawable.p_default_header_img)
				.showImageOnFail(R.drawable.p_default_header_img)
				.showImageForEmptyUri(R.drawable.p_default_header_img)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		return options;
	}

	/**
	 * 
	 * list item 
	 * 
	 * @return
	 */
	public static DisplayImageOptions getListPhotoConfig() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.build();
		return options;
	}
	
	/**
	 * 
	 * list item 
	 * 
	 * @return
	 */
	public static DisplayImageOptions getLocalPhotoConfig() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.build();
		return options;
	}
	
	/**
	 * 
	 * @return
	 */
	public static DisplayImageOptions getPanoLocalPhotoConfig() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.build();
		return options;
	}

	/**
	 * 
	 * @return
	 */
	public static DisplayImageOptions getLookPhotoConfig() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.considerExifParams(true)
				.cacheOnDisk(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.build();
		return options;
	}

	/**
	 * 
	 * @return
	 */
	public static DisplayImageOptions getMapPhotoConfig() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.p_default_map_img)
				.showImageForEmptyUri(R.drawable.p_default_map_img)
				.showImageOnFail(R.drawable.p_default_map_img)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		return options;
	}

}
