package f.com.panoramics.activity;

import java.io.File;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amazonaws.event.ProgressEvent;
import com.amazonaws.event.ProgressListener;
import com.amazonaws.mobileconnectors.s3.transfermanager.TransferManager;
import com.amazonaws.mobileconnectors.s3.transfermanager.TransferProgress;
import com.amazonaws.mobileconnectors.s3.transfermanager.Upload;
import com.guxiu.panoramics.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import f.com.panoramics.base.BaseFragmentActivity;
import f.com.panoramics.constant.Constant;
import f.com.panoramics.entity.LocationEntity;
import f.com.panoramics.service.jsonmodel.AnalyseFoursqureJson;
import f.com.panoramics.service.netservice.FoursquareService;
import f.com.panoramics.service.netservice.ShareService;
import f.com.panoramics.utils.AS3Util;
import f.com.panoramics.utils.DialogUtil;
import f.com.panoramics.utils.ImageConfig;
import f.com.panoramics.utils.ImageUtil;
import f.com.panoramics.utils.LocationUtil;
import f.com.panoramics.utils.MD5Util;
import f.com.panoramics.view.UploadProgressDialog;

/**
 * 
 * @author Fatty
 *
 *	分享全景图片界面
 *
 */
public class SharePhotoActivity extends BaseFragmentActivity implements OnClickListener {

	private boolean isHave = false;
	
	public static final int SELECT_CODE = 1;
	private String imgPath;

	private Button cancelBtn;
	private ImageView topImageView;
	private EditText tagEditText;
	private ImageView locationIconImageView;
	private TextView locationEditText;
	private Button changeBtn;
	private Button shareBtn;

	private String photoLocation ; 
	private ProgressBar smallProgressBar;
	
	/*********************    Upload Photo ,use AS3  | Share photo  *****************/
	private String locationName ;
	private String tags ; 
	private double lat;
	private double lng;
	private int w;
	private int h;
	
	private String fileName ;
	private String httpPhotoUrl ;
	
	private UploadProgressDialog uploadProgressDialog;

	private static final int COMPLETED_EVENT_CODE = 50001;
	private static final int FAILED_EVENT_CODE = 50002;
	private static final int PROGRESS_CODE = 50003;
	
	private boolean isClick = false;
	private static final int COMPLETED_OK = 50004;
	
	private TransferManager transferManager;
	private Upload mUpload;
	private TransferProgress progress ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.showAnimationIn();
		super.setScreenTag("SharePhotoActivity");
		setContentView(R.layout.panoramics_share_photo_layout);
		
		imgPath = getIntent().getStringExtra("imgPath");
		
		smallProgressBar = (ProgressBar)this.findViewById(R.id.smallProgressBar);
		cancelBtn = (Button) this.findViewById(R.id.cancelBtn);
		topImageView = (ImageView) this.findViewById(R.id.topImageView);
		tagEditText = (EditText) this.findViewById(R.id.tagEditText);
		locationIconImageView = (ImageView) this.findViewById(R.id.locationIconImageView);
		locationEditText = (TextView) this.findViewById(R.id.locationEditText);
		changeBtn = (Button) this.findViewById(R.id.changeBtn);
		shareBtn = (Button) this.findViewById(R.id.shareBtn);
		
		cancelBtn.setOnClickListener(this);
		changeBtn.setOnClickListener(this);
		shareBtn.setOnClickListener(this);
		
		ImageLoader.getInstance().displayImage("file:///" +imgPath, topImageView, ImageConfig.getPanoLocalPhotoConfig());
		String[] sts = ImageUtil.ImageInfoUtils.getImageInfo(imgPath);
		
		if(!TextUtils.isEmpty(sts[0]) && !TextUtils.isEmpty(sts[1])){
			isHave = true;
			String lats[] = sts[0].split(",");
			String lngs[] = sts[1].split(",");
			
			//lat
			double lat_jj = (Double.valueOf(lats[0].split("/")[0])) / (Double.valueOf(lats[0].split("/")[1]));
			double lat_ww = (Double.valueOf(lats[1].split("/")[0])) / (Double.valueOf(lats[1].split("/")[1]));
			double lat_dd = (Double.valueOf(lats[2].split("/")[0])) / (Double.valueOf(lats[2].split("/")[1]));
			//lng
			double lng_jj =  (Double.valueOf(lngs[0].split("/")[0])) / (Double.valueOf(lngs[0].split("/")[1]));
			double lng_ww =  (Double.valueOf(lngs[1].split("/")[0])) / (Double.valueOf(lngs[1].split("/")[1]));
			double lng_dd =  (Double.valueOf(lngs[2].split("/")[0])) / (Double.valueOf(lngs[2].split("/")[1]));
			
			double lat = LocationUtil.jwd(lat_jj, lat_ww, lat_dd);
			double lng = LocationUtil.jwd(lng_jj, lng_ww, lng_dd);

			smallProgressBar.setVisibility(View.VISIBLE);
			photoLocation = lat+","+lng;
			FoursquareService service = new FoursquareService(handler);
			service.foursquareOne(photoLocation);
			
		}else{
			isHave = false;
			locationEditText.setText(R.string.pano_share_unknow);
			photoLocation = "";
			locationIconImageView.setImageResource(R.drawable.p_default_earth);
		}
		
		// Upload...
		uploadProgressDialog = DialogUtil.createUploadProgressDialog(this);
		uploadProgressDialog.setCanceledOnTouchOutside(false);
		new Handler().postDelayed(new Runnable() {
			public void run() {
				uploadAmazon(imgPath);
			}
		}, 1000);
		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancelBtn:
			finishCurrentActivityWithAmination();
			break;
		case R.id.changeBtn:
			Intent locationIntent = new Intent();
			if(isHave){
				locationIntent.setClass(SharePhotoActivity.this, ChangeLocation1Activity.class);
				locationIntent.putExtra("photoLocation", photoLocation);
				SharePhotoActivity.this.startActivityForResult(locationIntent,SELECT_CODE);
			}else{
				locationIntent.setClass(SharePhotoActivity.this, ChangeLocation2Activity.class);
				SharePhotoActivity.this.startActivityForResult(locationIntent, SELECT_CODE);
			}
			break;
			
		// share Photos to our server
		case R.id.shareBtn:
			isClick = true;
			uploadProgressDialog.show();
			
			String tags = tagEditText.getText().toString().trim();
			if(!TextUtils.isEmpty(tags)){
				this.tags = tags;
			}else{
				this.tags = "";
			}
			break;
		}
	}
	
	/**
	 * onActivityResult
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case SELECT_CODE:
			if(data!=null){
				String name = data.getStringExtra("name");
				String url = data.getStringExtra("url");
				double ll_lat = Double.valueOf(data.getStringExtra("lat"));
				double ll_lng = Double.valueOf(data.getStringExtra("lng"));
				photoLocation = ll_lat+","+ll_lng;
				SharePhotoActivity.this.lat =ll_lat;
				SharePhotoActivity.this.lng = ll_lng;
				SharePhotoActivity.this.locationName = name;
				if(!TextUtils.isEmpty(photoLocation)){
					isHave = true;
				}
				locationEditText.setText(name);
				ImageLoader.getInstance().displayImage(url, locationIconImageView);
			}
			break;
		}
	}
	
	Timer timer;
	/**
	 * 
	 * 上传到亚马逊
	 * 
	 * @param imagePath
	 */
	public void uploadAmazon(String imagePath){
		
		timer= new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				Message message = new Message();
				message.what = COMPLETED_OK;
				message.obj = isClick;
				handler.sendMessage(message);
			}
		}, 0, 1000);
		
		File file = new File(imagePath);
		File sdcardPath = new File(Constant.CACHE);
		if(!sdcardPath.exists()){
			sdcardPath.mkdirs();
		}
		
		final String photoPath = ImageUtil.BitmapUtil.compressBitmap(
				sdcardPath.getPath().toString(),
				MD5Util.makeMD5(System.currentTimeMillis()+"")+".jpg", 
				file);
		
		if(!TextUtils.isEmpty(photoPath)){
			this.w =ImageUtil.BitmapUtil.getWidth();
			this.h =ImageUtil.BitmapUtil.getHeight();
			
			if(!TextUtils.isEmpty(photoPath)){
				float photoSize = (float)(new File(photoPath).length())/(float)(1024 * 1024);
				System.err.println("压缩后的大小->" + photoSize);
				
				transferManager = new TransferManager(AS3Util.getCredProvider(this));
				new Thread(new Runnable() {
					public void run() {
						try {
							mUpload = transferManager.upload(
									Constant.BUCKET_NAME.toLowerCase(Locale.US), 
									AS3Util.getMediaPath(new File(photoPath).getName()),  
									new File(photoPath)
							);
							mUpload.addProgressListener(new upLoadProgressListener());
							progress = mUpload.getProgress();
						} catch (RuntimeException e) {
							handler.sendEmptyMessage(FAILED_EVENT_CODE);
						}
						fileName = AS3Util.getMediaPath(new File(photoPath).getName());
						httpPhotoUrl = fileName;
					}
				} ,"Upload").start();
			}
		}else{
			dismissUploadProgressDialog();
			new Handler().postDelayed(new Runnable() {
				public void run() {
					SharePhotoActivity.this.finishAllActivity();
					SharePhotoActivity.this.goActivity(HomeActivity.class);
				}
			}, 200);
		}
	}
	
	/**
	 * 
	 * @author Fatty
	 *     
	 *      upLoadProgressListener
	 */ 
	public class upLoadProgressListener implements ProgressListener {
		public void progressChanged(ProgressEvent event) {
			if (event.getEventCode() == ProgressEvent.COMPLETED_EVENT_CODE) {
				Message message = new Message();
				message.what = COMPLETED_EVENT_CODE;
				message.obj = httpPhotoUrl;
				handler.sendMessage(message);
			}else if(event.getEventCode() == ProgressEvent.FAILED_EVENT_CODE){
				handler.sendEmptyMessage(FAILED_EVENT_CODE);
			}else {
				Message message = new Message();
				message.what = PROGRESS_CODE;
				message.obj = progress.getPercentTransferred();
				handler.sendMessage(message);
			}
		}
	}
	
	/**
	 * Share Photos 
	 */
	private final Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			
			//Completed ok
			case COMPLETED_OK:
				boolean b = (Boolean) msg.obj;
				if(mUpload!=null){
					if(b && mUpload.isDone()){
						isClick = false;
						ShareService shareService = new ShareService(handler);
						String locationName = "Unkown";
						if(!TextUtils.isEmpty(SharePhotoActivity.this.locationName)){
							locationName = SharePhotoActivity.this.locationName;
						}
						shareService.share(getPreString("uid"), SharePhotoActivity.this.tags, httpPhotoUrl, 
								locationName, 
								SharePhotoActivity.this.lat, 
								SharePhotoActivity.this.lng, 
								SharePhotoActivity.this.w, 
								SharePhotoActivity.this.h,
								getPreString("token"));
						if(timer!=null){
							timer.cancel();
						}
					}else{
						System.err.println("upload...");
					}
				}
				break;
			
			//Foursquare
			case FoursquareService.Foursquare_SUCCESS:
				smallProgressBar.setVisibility(View.GONE);
				String content = (String)msg.obj;
				if(TextUtils.isEmpty(content)){
					return;
				}
				
				LocationEntity locationEntity = AnalyseFoursqureJson.get_one(content);
				if(locationEntity != null){
					SharePhotoActivity.this.lat =locationEntity.getLat();
					SharePhotoActivity.this.lng = locationEntity.getLng();
					SharePhotoActivity.this.locationName = locationEntity.getName();
					
					ImageLoader.getInstance().displayImage(locationEntity.getUrl(), locationIconImageView);
					locationEditText.setText(locationEntity.getName());
				}
				break;
			case FoursquareService.Foursquare_FAILED:
				smallProgressBar.setVisibility(View.GONE);
				locationEditText.setText("Unknow");
				break;
				
			//sharePhoto
			case COMPLETED_EVENT_CODE:
				String url = (String) msg.obj;
				httpPhotoUrl = url;

				break;
			case FAILED_EVENT_CODE:
				dismissUploadProgressDialog();
				
				break;
			case PROGRESS_CODE:
				double progress = (Double) msg.obj;
				uploadProgressDialog.setMessage((int)progress + "%");
				System.err.println("progress:" + (int)progress + "%");
				break;
				
			//share our service
			case ShareService.SHARE_SUCCESS:
				dismissUploadProgressDialog();

				String root =(String) msg.obj;
				if(TextUtils.isEmpty(root)){
					return;
				}
				Intent shareSuccessIntent = new Intent();
				shareSuccessIntent.putExtra("imgPath", imgPath);
				shareSuccessIntent.setClass(SharePhotoActivity.this, ShareSuccessActivity.class);
				SharePhotoActivity.this.startActivity(shareSuccessIntent);
				break;
			case ShareService.ERROR_CODE404:
				toast("share photo ERROR_CODE404");
				dismissUploadProgressDialog();
				break;
				
			case ShareService.ERROR_CODE409:
				toast("share photo ERROR_CODE409");
				dismissUploadProgressDialog();
				break;
			}
		}
	};
	
	public void dismissUploadProgressDialog(){
		if(uploadProgressDialog!=null && uploadProgressDialog.isShowing()){
			uploadProgressDialog.dismiss();
			uploadProgressDialog.cancel();
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ImageLoader.getInstance().clearMemoryCache();
		
		if(timer!=null){
			timer.cancel();
		}
		
		new Thread(new Runnable() {
			public void run() {
				if(transferManager!=null){
					transferManager.shutdownNow();
				}
			}
		}).start();
	}
}
