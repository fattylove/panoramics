package f.com.panoramics.activity;

import java.io.File;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.guxiu.panoramics.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.polites.android.GestureImageView;

import f.com.panoramics.base.ActivityManager;
import f.com.panoramics.base.BaseFragmentActivity;
import f.com.panoramics.constant.Constant;
import f.com.panoramics.entity.MediaEntity;
import f.com.panoramics.receiver.DataChangeReceiver;
import f.com.panoramics.service.netservice.LikeService;
import f.com.panoramics.utils.ImageConfig;
import f.com.panoramics.utils.IntervalUtil;
import f.com.panoramics.utils.ShareUtil;

/**
 * 
 * @author Fatty
 * 
 * 全景查看图片界面
 *
 */
public class ImageLookActivity extends BaseFragmentActivity implements OnClickListener {

	private GestureImageView gestureImageView;
	private MediaEntity mediaEntity;

	private LinearLayout likeLayout;
	private ImageView lookXin;
	
	private ImageView editImageView;
	private ImageView zhuanImageView;
	
	private TextView usernameTextView;
	private TextView likeTextView;
	private TextView timeTextView;
	private TextView locationTextView;
	
	private ProgressBar progressBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.showAnimationIn();
		super.setScreenTag("ImageLookActivity");
		setContentView(R.layout.panoramics_image_look_layout);
		
		Intent intent = this.getIntent(); 
		mediaEntity = (MediaEntity)intent.getSerializableExtra("mediaEntity");
		
		gestureImageView = (GestureImageView) this.findViewById(R.id.gestureImageView);
		
		likeLayout = (LinearLayout) this.findViewById(R.id.likeLayout);
		lookXin = (ImageView)this.findViewById(R.id.lookXin);
		
		editImageView = (ImageView) this.findViewById(R.id.editImageView);
		zhuanImageView = (ImageView) this.findViewById(R.id.zhuanImageView);
		
		likeLayout.setOnClickListener(this);
		editImageView.setOnClickListener(this);
		zhuanImageView.setOnClickListener(this);
		
		usernameTextView = (TextView)this.findViewById(R.id.usernameTextView);
		likeTextView = (TextView)this.findViewById(R.id.likeTextView);
		timeTextView = (TextView)this.findViewById(R.id.timeTextView);
		locationTextView = (TextView)this.findViewById(R.id.locationTextView);
		usernameTextView.setShadowLayer(1, 1, 2, Color.parseColor("#000000"));
		likeTextView.setShadowLayer(    1, 1, 2, Color.parseColor("#000000"));
		timeTextView.setShadowLayer(    1, 1, 2, Color.parseColor("#000000"));
		locationTextView.setShadowLayer(1, 1, 2, Color.parseColor("#000000"));
		
		String nickname =  mediaEntity.getNickname();
		
		usernameTextView.setText(nickname);
		timeTextView.setText(IntervalUtil.getInterval(mediaEntity.getP_time()));
		locationTextView.setText(mediaEntity.getLocation());
		
		/*****************     处理Like    **********************/
		likeTextView.setText(mediaEntity.getLike()+"");
		if(mediaEntity.getLikeState() == Constant.LIKED){
			lookXin.setBackgroundResource(R.drawable.p_img_look_on);
			likeLayout.setTag(Constant.LIKED);
		}else if(mediaEntity.getLikeState() == Constant.UNLIKED){
			lookXin.setBackgroundResource(R.drawable.p_img_look_off);
			likeLayout.setTag(Constant.UNLIKED);
		}
		
		progressBar = (ProgressBar)this.findViewById(R.id.progress);
		Drawable draw= getResources().getDrawable(R.drawable.p_myprogressbar_style);
		progressBar.setProgressDrawable(draw);
		ImageLoader.getInstance().displayImage(mediaEntity.getStandard_resolution(), gestureImageView, ImageConfig.getLookPhotoConfig(), new SimpleImageLoadingListener() {
			public void onLoadingStarted(String imageUri, View view) {
				progressBar.setProgress(0);
				progressBar.setVisibility(View.VISIBLE);
			}
			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
				progressBar.setVisibility(View.GONE);
			}
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				progressBar.setVisibility(View.GONE);
			}
		}, new ImageLoadingProgressListener() {
			public void onProgressUpdate(String imageUri, View view, int current, int total) {
				progressBar.setProgress(Math.round(100.0f * current / total));
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.likeLayout:
			LikeService likeService = new LikeService(handler);
			Integer likeState  = (Integer) v.getTag();
			if(likeState == Constant.LIKED){
				
				int count = mediaEntity.getLike();
				v.setTag(Constant.UNLIKED);
				lookXin.setBackgroundResource(R.drawable.p_img_look_off);
				likeTextView.setText( (count-1) + "");
				
				mediaEntity.setLikeState(Constant.UNLIKED);
				mediaEntity.setLike(count-1);
				Intent intent = new Intent(DataChangeReceiver.ACTION);  
				Bundle bundle = new Bundle();
				bundle.putSerializable("mediaEntity", mediaEntity);
				intent.putExtras(bundle);
			    ImageLookActivity.this.sendBroadcast(intent);
			    
			    likeService.deleteLike(getPreString("token"), mediaEntity.getMediaId());
			}else if(likeState == Constant.UNLIKED){
				
				int count = mediaEntity.getLike();
				v.setTag(Constant.LIKED);
				lookXin.setBackgroundResource(R.drawable.p_img_look_on);
				likeTextView.setText((count+1) + "");
				
				mediaEntity.setLikeState(Constant.LIKED);
				mediaEntity.setLike(count+1);
				Intent intent = new Intent(DataChangeReceiver.ACTION);  
				Bundle bundle = new Bundle();
				bundle.putSerializable("mediaEntity", mediaEntity);
				intent.putExtras(bundle);
				ImageLookActivity.this.sendBroadcast(intent);
				
				likeService.postLike(getPreString("token"), mediaEntity.getMediaId());
			}
			break;
			
		case R.id.editImageView:
			Intent intent = new Intent();
			intent.putExtra("where", false);
			Bundle bundle = new Bundle();
			bundle.putSerializable("mediaEntity", mediaEntity);
			intent.putExtras(bundle);
			intent.setClass(ImageLookActivity.this, SendCommnetActivity.class);
			ImageLookActivity.this.startActivity(intent);
			break;
			
		case R.id.zhuanImageView:
			File file = ImageLoader.getInstance().getDiskCache().get(mediaEntity.getStandard_resolution());
			if(file!=null){
				String filePath = file.getAbsolutePath().toString();
				if(!TextUtils.isEmpty(filePath)){
					ShareUtil.share(ImageLookActivity.this , filePath);
				}
			}
			break;
		}
	}
	
	/**
	 * 
	 */
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			/// like post
			case LikeService.LIKE_POST:{
				switch (msg.arg1) {
				case LikeService.LIKE_POST_SUCCESS:
					break;
				case LikeService.ERROR_CODE400:
					break;
				case LikeService.ERROR_CODE401:
					break;
				case LikeService.ERROR_CODE409:
					break;
				default:
					break;
				}
			}
			break;
			
			
			////Like remove
			case LikeService.LIKE_REMOVE:{
				switch (msg.arg1) {
				case LikeService.LIKE_REMOVE_SUCCESS:
					String root = (String) msg.obj;
					System.err.println(root);
					break;
				case LikeService.ERROR_CODE400:
					break;
				case LikeService.ERROR_CODE401:
					break;
				case LikeService.ERROR_CODE409:
					break;
				default:
					break;
				}
			}
			break;
			
			}
		}
	};
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			
			Intent imgIntent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putSerializable("mediaEntity", mediaEntity);
			imgIntent.putExtras(bundle);
			imgIntent.setClass(ImageLookActivity.this, ShowCommentsActivity.class);
			ImageLookActivity.this.setResult(ShowCommentsActivity.REQUEST_CODE, imgIntent);
			
			ActivityManager.getAppManager().finishActivity(this);
			showAnimationOut();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}