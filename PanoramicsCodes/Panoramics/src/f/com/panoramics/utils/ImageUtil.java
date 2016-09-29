package f.com.panoramics.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.MeasureSpec;
import f.com.panoramics.constant.Constant;
import f.com.panoramics.entity.PanoramicsEntity;

/**
 * 
 * @author Fatty
 * 
 *         image
 * 
 */
public class ImageUtil {

	/**
	 * 
	 * 图片大小处理
	 * 
	 * @author Fatty
	 * 
	 */
	public static class ImageSizeUtils {
		/**
		 * 
		 * dip转px
		 * 
		 * @param context
		 * @param dpValue
		 * @return
		 */
		public static int dip2px(Context context, float dpValue) {
			final float scale = context.getResources().getDisplayMetrics().density;
			return (int) (dpValue * scale + 0.5f);
		}

		/**
		 * 
		 * px转dip
		 * 
		 * @param context
		 * @param pxValue
		 * @return
		 */
		public static int px2dip(Context context, float pxValue) {
			final float scale = context.getResources().getDisplayMetrics().density;
			return (int) (pxValue / scale + 0.5f);
		}
	}

	/**
	 * 
	 * @author Fatty
	 * 
	 *         截图头像图片
	 * 
	 */
	public static class ImageFixUtils {

		public static final int PHOTORESOULT = 2;

		private static final String IMAGE_UNSPECIFIED = "image/*";

		public static void photoZoom(Context context, Uri uri, int outputX,
				int outputY) {
			Activity act = (Activity) context;
			Intent intent = new Intent("com.android.camera.action.CROP");
			intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
			intent.putExtra("crop", "true");
			intent.putExtra("aspectX", 200);
			intent.putExtra("aspectY", 200);
			intent.putExtra("outputX", outputX);
			intent.putExtra("outputY", outputY);
			intent.putExtra("scale", false);
			intent.putExtra("return-data", true);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
			intent.putExtra("noFaceDetection", true);
			act.startActivityForResult(intent, PHOTORESOULT);
		}
	}

	/**
	 * 
	 * 获取图片的地理位置信息
	 * 
	 * @author Fatty
	 * 
	 */
	public static class ImageInfoUtils {
		/**
		 * 经纬度
		 * 
		 * @param imgFilePath
		 * 
		 * @return 返回纬度、经度数组信息
		 * 
		 */
		public static String[] getImageInfo(String imgFilePath) {
			String[] results = new String[2];
			try {
				ExifInterface exif = new ExifInterface(imgFilePath);
				String lat = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE);// 纬度
				String lon = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);// 精度
				results[0] = lat;
				results[1] = lon;
			} catch (Exception e) {
				e.toString();
			}
			return results;
		}

		/**
		 * 经纬度
		 * 
		 * @param imgFilePath
		 * 
		 * @return 返回纬度、经度数组信息
		 * 
		 */
		public static double[] getImageDoubleInfo(String imgFilePath) {
			double[] results = new double[2];
			try {
				ExifInterface exif = new ExifInterface(imgFilePath);
				double lat = exif.getAttributeDouble(
						ExifInterface.TAG_GPS_LATITUDE, 0.0d);// 纬度
				double lon = exif.getAttributeDouble(
						ExifInterface.TAG_GPS_LONGITUDE, 0.0d);// 精度
				results[0] = lat;
				results[1] = lon;
			} catch (Exception e) {
				e.toString();
			}
			return results;
		}
	}

	/**
	 * 
	 * @author Fatty
	 * 
	 */
	public static class SDCardImages {

		/**
		 * 获取SDcard下panoramics-pic文件夹里面的所有photos
		 * 
		 * @param context
		 * @return
		 */
		public static ArrayList<String> getAllPanoramicsImages(Activity context) {
			ArrayList<String> imgs = new ArrayList<String>();
			File dir = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "panoramics-pic");
			for (String str : dir.list()) {
				imgs.add(dir.toString() + File.separator + str);
			}
			return imgs;
		}

		/**
		 * 获取系统Images所有图片
		 * 
		 * @param context
		 * @return
		 */
		public static ArrayList<PanoramicsEntity> getAllMediaImages(Activity context) {
			ArrayList<PanoramicsEntity> panoramicsEntities = new ArrayList<PanoramicsEntity>();
			Cursor cursor = context.managedQuery(
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
					null,
					null, 
					null, 
					null);
			cursor.moveToFirst();
			while (cursor.moveToNext()) {
				
				PanoramicsEntity entity = new PanoramicsEntity();
				
				String path = cursor.getString(cursor.getColumnIndexOrThrow(android.provider.MediaStore.Images.Media.DATA));
				double lat = cursor.getDouble(cursor.getColumnIndexOrThrow(android.provider.MediaStore.Images.Media.LATITUDE));
				double lng = cursor.getDouble(cursor.getColumnIndexOrThrow(android.provider.MediaStore.Images.Media.LONGITUDE));
				int img_w = cursor.getInt(cursor.getColumnIndexOrThrow(android.provider.MediaStore.Images.Thumbnails.WIDTH));
				int img_h = cursor.getInt(cursor.getColumnIndexOrThrow(android.provider.MediaStore.Images.Thumbnails.HEIGHT));
				
				entity.setImgPath(path);
				entity.setLat(lat);
				entity.setLng(lng);
				entity.setImg_h(img_h);
				entity.setImg_w(img_w);

				if(img_h > 0 && img_w > 0){
					if(img_w/img_h >= 2 || img_h/img_w >= 2){
						panoramicsEntities.add(entity);
					}
				}
			}
			
			return panoramicsEntities;
		}
	}

	/**
	 * 
	 * @author Fatty
	 * 
	 *         复制图片到Sdcard
	 */
	public static class CopyImages {
		private final Context mContext;
		private final String dirName = "panoramics-pic";

		public CopyImages(Context context) {
			mContext = context;
		}

		/**
		 * Copy assets/panoramics-pic目录下所有的图片到sdcard里面
		 * 
		 * @return
		 * @throws IOException
		 */
		public boolean copy() throws IOException {
			String filenames[] = mContext.getResources().getAssets()
					.list(dirName);
			for (String asset : filenames) {
				copyImage(asset);
			}
			return true;
		}

		/**
		 * copy 一张图片到指定目录
		 * 
		 * @param imageName
		 */
		private void copyImage(String imageName) {
			FileOutputStream fos = null;
			InputStream in = null;
			boolean sdCardExist = Environment.getExternalStorageState().equals(
					android.os.Environment.MEDIA_MOUNTED);
			if (!sdCardExist) {
				return;
			}
			File dir = new File(Environment.getExternalStorageDirectory()
					.getPath() + File.separator + dirName);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File imgFile = new File(dir, imageName);
			try {
				if (!imgFile.exists()) {
					imgFile.createNewFile();
				}
				fos = new FileOutputStream(imgFile);
				AssetManager assetManager = mContext.getAssets();
				in = assetManager.open(dirName + "/" + imageName);
				byte[] b = new byte[1024];
				while (in.read(b) != -1) {
					fos.write(b);
				}
				fos.flush();
			} catch (Exception e) {
			} finally {
				try {
					if (in != null) {
						in.close();
					}
					if (fos != null) {
						fos.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 
	 * @author Fatty
	 *
	 */
	public static class BitmapUtil {

		/**
		 * 两张bitmap覆盖
		 * 
		 * 
		 * @param background
		 * @param foreground
		 * @return
		 */
		public static Bitmap toConformBitmap(Bitmap background) {
			int bgWidth = background.getWidth();
			int bgHeight = background.getHeight();
			
			int tempH = bgHeight / 2;
			int tempNH = bgWidth / 2;
			
			Bitmap newbmp = Bitmap.createBitmap(bgWidth	, bgWidth, Config.ARGB_8888);
			Canvas cv = new Canvas(newbmp);
			cv.drawColor(Color.WHITE);
			
			cv.drawBitmap(background, 0, tempNH - tempH  , null);
			cv.save(Canvas.ALL_SAVE_FLAG);
			cv.restore();
			return newbmp;
		}

		/**
		 * Drawable对象转换成Bitmap对象
		 * 
		 * @param drawable
		 * @return
		 */
		public static Bitmap drawable2Bitmap(Drawable drawable) {
			Bitmap bitmap = Bitmap
					.createBitmap(
							drawable.getIntrinsicWidth(),
							drawable.getIntrinsicHeight(),
							drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
									: Bitmap.Config.RGB_565);
			Canvas canvas = new Canvas(bitmap);
			drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
			drawable.draw(canvas);
			return bitmap;
		}

		/**
		 * 获取View的bitmap对象
		 * 
		 * @param view
		 * @return
		 */
		public static String convertViewToBitmap(View view ,String photoName) {
			view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
			view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
			view.buildDrawingCache();
			Bitmap bitmap = view.getDrawingCache();
			return saveBitmapInSdcard(photoName , Constant.CACHE , bitmap);
		}
		
		/**
		 * 抽取图片  地图上的ICON
		 * 
		 * @param view
		 * @return
		 */
		public static Bitmap convertViewToBitmap(View view ) {
			view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
			view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
			view.buildDrawingCache();
			Bitmap bitmap = view.getDrawingCache();
			return bitmap;
		}
		
		/**
		 * 
		 */
		private static int width = 0;
		private static int height = 0;
		
		public static String saveBitmapInSdcard(String photoName, String sdcardPath , Bitmap saveBitmap ){
			FileOutputStream fos = null;
			File saveNewPhotoFile = new File(sdcardPath + Constant.P, photoName);
			if(saveNewPhotoFile.exists()){
				saveNewPhotoFile.delete();
			}
			try {
				saveNewPhotoFile.createNewFile();
				fos = new FileOutputStream(saveNewPhotoFile);
				saveBitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
				fos.flush();
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			} catch (OutOfMemoryError e1) {
				e1.printStackTrace();
				return null;
			}
			return sdcardPath + Constant.P + photoName;
		}
		
		/**
		 * 
		 * @param sdcardPath
		 * @param photoName
		 * @param savePhoto
		 * @return
		 */
		public static String compressBitmap(String sdcardPath , String photoName , File savePhoto){
			
			System.err.println("压缩前图片大小：" + (float)(savePhoto.length())/(float)(1024 * 1024));
			Bitmap bitmap = getBreviaryBitmapByFilepath(savePhoto, Constant.PHOTO_WIDTH, Constant.PHOTO_HEIGHT);//第一次压缩
			if(bitmap == null){
				 bitmap = getBreviaryBitmapByFilepath(savePhoto, 1500, 750);//如果第一次压缩失败，执行第二次压缩
				 if(bitmap ==null){
					 bitmap = getBreviaryBitmapByFilepath(savePhoto, 1000, 500);//如果第二次压缩失败，执行第三次压缩
					 if(bitmap ==null){
						 return null;//三次压缩都失败，直接返回Null ,提示用户图片压缩失败。
					 }
				 }
			}
			
			width = bitmap.getWidth();
			height = bitmap.getHeight();
			
			if(width < height){//如果图片width<height,旋转图片
				Matrix matrix = new Matrix();
				matrix.postRotate(90);
				try {
					bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
				} catch (OutOfMemoryError e) {
					System.err.println("OutOfMemoryError : " + e.toString());
					return null;
				}
				
				width = bitmap.getWidth();
				height = bitmap.getHeight();
			}
			
			FileOutputStream fos = null;
			File saveNewPhotoFile = new File(sdcardPath + Constant.P, photoName);
			if(saveNewPhotoFile.exists()){
				saveNewPhotoFile.delete();
			}
			try {
				saveNewPhotoFile.createNewFile();
				fos = new FileOutputStream(saveNewPhotoFile);
				try {
					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
				} catch (OutOfMemoryError e) {
					System.err.println("OutOfMemoryError : " + e.toString());
					return null;
				}
				
				if(bitmap!=null){//得到压缩图片后，recycle bitmap对象
					bitmap.recycle();
					bitmap = null;
					System.gc();//GC系统引用
				}
			} catch (Exception e) {
				System.err.println("File Exception : " + e.toString());
				return null;
			}finally{
				try {
					fos.flush();
					fos.close();
				} catch (IOException e) {
				}
			}
			return sdcardPath + Constant.P + photoName;
		}
		
		/**
		 * 
		 * @return
		 */
		public static int getWidth(){
			return width;
		}
		
		/**
		 * 
		 * @return
		 */
		public static int getHeight(){
			return height;
		}
		
		/**
		 * 
		 * 根据图片宽，高压缩图片
		 * 
		 * @param file
		 * @return
		 */
		public static Bitmap getBreviaryBitmapByFilepath(File file , int w , int h) {
			if (null != file && file.exists()) {
				if (w > 0 && h > 0) {
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inJustDecodeBounds = true;
					BitmapFactory.decodeFile(file.getPath(), options);
					final int minSideLength = Math.min(w, h);
					options.inSampleSize = computeSampleSize(options, minSideLength , w * h);
					options.inJustDecodeBounds = false;
					options.inInputShareable = true;
					try {
						return BitmapFactory.decodeFile(file.getPath(), options);
					} catch (OutOfMemoryError e) {
						return null;
					}
				}
			}
			return null;
		}
		
		/**
		 * 
		 * @param options
		 * @param minSideLength
		 * @param maxNumOfPixels
		 * @return
		 */
		private static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
			int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
			int roundedSize;
			if (initialSize <= 8) {
				roundedSize = 1;
				while (roundedSize < initialSize) {
					roundedSize <<= 1;
				}
			} else {
				roundedSize = (initialSize + 7) / 8 * 8;
			}
			return roundedSize;
		}
		
		/**
		 * 
		 * @param options
		 * @param minSideLength
		 * @param maxNumOfPixels
		 * @return
		 * 			sample Size
		 */
		private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
			double w = options.outWidth;
			double h = options.outHeight;

			int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
			int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));

			if (upperBound < lowerBound) {
				return lowerBound;
			}

			if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
				return 1;
			} else if (minSideLength == -1) {
				return lowerBound;
			} else {
				return upperBound;
			}
		}

	}
}
