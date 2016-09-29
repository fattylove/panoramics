package f.com.panoramics.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;

import com.guxiu.panoramics.R;

import f.com.panoramics.constant.Constant;

/**
 * 
 * @author Fatty
 *
 */
public class ShareUtil {

	/**
	 *  系统分享
	 * 
	 * @param activity
	 * @param subject
	 * @param content
	 */
	public static void share(Activity activity , String subject , String content){
		File checkDir = new  File(Constant.SHARE);
		if(!checkDir.exists()){
			checkDir.mkdirs();
		}
		String shareImageName = Constant.SHARE + Constant.P +System.currentTimeMillis() +".jpg";
		View view = activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap bitmap = view.getDrawingCache();
		
		try {
			if(SDCardUtil.existSDCard()){
				FileOutputStream fout = new FileOutputStream(shareImageName);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fout);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		File imageFile = new File(shareImageName);
		if (imageFile.exists()&& imageFile.isFile()) {
			shareIntent.setType("image/*");
			Uri u = Uri.fromFile(imageFile);
			shareIntent.putExtra(Intent.EXTRA_STREAM, u);
		} else {
			shareIntent.setType("text/plain"); // 纯文本
		}
		shareIntent.putExtra(Intent.EXTRA_SUBJECT, activity.getString(R.string.app_name)+" ,"+subject);
		shareIntent.putExtra(Intent.EXTRA_TEXT,    activity.getString(R.string.app_name)+" ,"+content);
		shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		activity.startActivity(Intent.createChooser(shareIntent, "Share Panoramics"));
	}
	
	/**
	 *  系统分享
	 * 
	 * @param content
	 */
	public static void share(Activity content , String photoName){
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		File imageFile = new File(photoName);
		if (null != imageFile && imageFile.exists() && imageFile.isFile()) {
			shareIntent.setType("image/*");
			Uri u = Uri.fromFile(imageFile);
			shareIntent.putExtra(Intent.EXTRA_STREAM, u);
		} else {
			shareIntent.setType("text/plain"); 
		}
		shareIntent.putExtra(Intent.EXTRA_SUBJECT,  "Panoramics http://bit.ly/1kDHyce");
		shareIntent.putExtra(Intent.EXTRA_TEXT,     "Panoramics http://bit.ly/1kDHyce");
		shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		content.startActivity(Intent.createChooser(shareIntent, "Share Panoramics"));
	}
}
