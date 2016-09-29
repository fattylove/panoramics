package f.com.panoramics.activity;

import java.io.File;
import java.util.List;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.flickrjandroidj.tasks.GetOAuthTokenTask;
import com.flickrjandroidj.tasks.SaveOAuthTask;
import com.flickrjandroidj.tasks.UploadPhotoTask;
import com.googlecode.flickrjandroid.oauth.OAuth;
import com.googlecode.flickrjandroid.oauth.OAuthToken;
import com.googlecode.flickrjandroid.people.User;
import com.guxiu.panoramics.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import f.com.panoramics.base.BaseFragmentActivity;
import f.com.panoramics.constant.Constant;
import f.com.panoramics.utils.ImageConfig;
import f.com.panoramics.utils.ImageUtil;

public class ShareSuccessActivity extends BaseFragmentActivity implements OnClickListener {
	
	
	//flickr  Preferences Key
	public static final String CALLBACK_SCHEME = "flickrj-android-sample-oauth"; //$NON-NLS-1$
	
	public static final String PREFS_NAME = "flickrj-android-sample-pref"; //$NON-NLS-1$
	public static final String KEY_OAUTH_TOKEN = "flickrj-android-oauthToken"; //$NON-NLS-1$
	public static final String KEY_TOKEN_SECRET = "flickrj-android-tokenSecret"; //$NON-NLS-1$
	public static final String KEY_USER_NAME = "flickrj-android-userName"; //$NON-NLS-1$
	public static final String KEY_USER_ID = "flickrj-android-userId"; //$NON-NLS-1$
	
	private String imgPath;
	private ImageView shareImageView;
	
	private Button exitBtn;
	private ImageView facebookBtn;
	private ImageView twitterBtn;
	private ImageView instagramBtn;
	private ImageView flikrBtn;
	private Button finishBtn;
	
	private UiLifecycleHelper uiHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.showAnimationIn();
		super.setScreenTag("ShareSuccessActivity");
		setContentView(R.layout.panoramics_share_photo_success_layout);
		
		shareImageView = (ImageView)this.findViewById(R.id.shareImageView);
		exitBtn = (Button)this.findViewById(R.id.exitBtn);
		facebookBtn = (ImageView)this.findViewById(R.id.facebookBtn);
		twitterBtn = (ImageView)this.findViewById(R.id.twitterBtn);
		instagramBtn = (ImageView)this.findViewById(R.id.instagramBtn);
		flikrBtn = (ImageView)this.findViewById(R.id.flikrBtn);
		finishBtn = (Button)this.findViewById(R.id.finishBtn);
		
		exitBtn.setOnClickListener(this);
		facebookBtn.setOnClickListener(this);
		twitterBtn.setOnClickListener(this);
		instagramBtn.setOnClickListener(this);
		flikrBtn.setOnClickListener(this);
		finishBtn.setOnClickListener(this);
		
		imgPath = getIntent().getStringExtra("imgPath");
		ImageLoader.getInstance().displayImage("file:///" +imgPath, shareImageView, ImageConfig.getPanoLocalPhotoConfig());
		
		uiHelper = new UiLifecycleHelper(this, null);
		uiHelper.onCreate(savedInstanceState);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.exitBtn:
			finishAllActivity();
			goActivity(HomeActivity.class);
			break;
		case R.id.facebookBtn:
			sharePhotoByFacebook(imgPath);
			break;
		case R.id.twitterBtn:
			shareToTwitter(imgPath);
			break;
		case R.id.instagramBtn:
			shareToInstagram(imgPath);
			break;
		case R.id.flikrBtn:
			final OAuth oAuth = getOAuthToken();
			if(oAuth == null){
				SaveOAuthTask task = new SaveOAuthTask(ShareSuccessActivity.this);
				task.execute();
			}else{
				sharePhotoByFlickr(imgPath  , oAuth);
			}
			break;
		case R.id.finishBtn:
			finishAllActivity();
			goActivity(HomeActivity.class);
			break;
		}
	}
	
	/**
	 * 
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    
	    ////    facebook 
	    uiHelper.onActivityResult(requestCode, resultCode, data, new FacebookDialog.Callback() {
	        @Override
	        public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
	            Log.e("Activity", String.format("Error: %s", error.toString()));
	        }

	        @Override
	        public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
	            Log.i("Activity", "Success!");
	        }
	    });
	}
	
	@Override
	protected void onResume() {
	    super.onResume();
	    
	    //facebook
	    uiHelper.onResume();
	    
	    //flickr
	    Intent intent = getIntent();
		String scheme = intent.getScheme();
		OAuth savedToken = getOAuthToken();
		if (CALLBACK_SCHEME.equals(scheme) && (savedToken == null || savedToken.getUser() == null)) {
			Uri uri = intent.getData();
			String query = uri.getQuery();
			String[] data = query.split("&"); 
			if (data != null && data.length == 2) {
				String oauthToken = data[0].substring(data[0].indexOf("=") + 1); 
				String oauthVerifier = data[1].substring(data[1].indexOf("=") + 1); 
				OAuth oauth = getOAuthToken();
				if (oauth != null && oauth.getToken() != null && oauth.getToken().getOauthTokenSecret() != null) {
					GetOAuthTokenTask task = new GetOAuthTokenTask(this);
					task.execute( oauthToken,  oauth.getToken().getOauthTokenSecret(),  oauthVerifier);
				}
			}
		}
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    uiHelper.onSaveInstanceState(outState);
	}

	@Override
	public void onPause() {
	    super.onPause();
	    uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
	    super.onDestroy();
	    uiHelper.onDestroy();
	}
	
	
	
	///Flickr api
	/**
	 * 
	 *******************************************************************************************************************************
	 * 获取 Flickr认证
	 * @return
	 */
    public OAuth getOAuthToken() {
       SharedPreferences settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
       String oauthTokenString = settings.getString(KEY_OAUTH_TOKEN, null);
       String tokenSecret = settings.getString(KEY_TOKEN_SECRET, null);
       if (oauthTokenString == null && tokenSecret == null) {
       		return null;
       }
       OAuth oauth = new OAuth();
       String userName = settings.getString(KEY_USER_NAME, null);
       String userId = settings.getString(KEY_USER_ID, null);
       if (userId != null) {
       		User user = new User();
       		user.setUsername(userName);
       		user.setId(userId);
       		oauth.setUser(user);
       }
       OAuthToken oauthToken = new OAuthToken();
       oauth.setToken(oauthToken);
       oauthToken.setOauthToken(oauthTokenString);
       oauthToken.setOauthTokenSecret(tokenSecret);
       return oauth;
    }
    
    /**
     * 存储Flikr信息
     * 
     * @param userName
     * @param userId
     * @param token
     * @param tokenSecret
     */
    public void saveOAuthToken(String userName, String userId, String token, String tokenSecret) {
    	SharedPreferences sp = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString(KEY_OAUTH_TOKEN, token);
		editor.putString(KEY_TOKEN_SECRET, tokenSecret);
		editor.putString(KEY_USER_NAME, userName);
		editor.putString(KEY_USER_ID, userId);
		editor.commit();
    }
   
    /**
     * 
     * @param OAuth
     */
    public void onOAuthDone(OAuth result) {
		if (result != null) {
			User user = result.getUser();
			OAuthToken token = result.getToken();
			if (user == null || user.getId() == null || token == null|| token.getOauthToken() == null || token.getOauthTokenSecret() == null) {
				Toast.makeText(this, "Authorization failed", Toast.LENGTH_LONG).show();
				return;
			}
			//String message = String.format(Locale.US, "Authorization Succeed: user=%s, userId=%s, oauthToken=%s, tokenSecret=%s", user.getUsername(), user.getId(), token.getOauthToken(), token.getOauthTokenSecret());
			saveOAuthToken(user.getUsername(), user.getId(), token.getOauthToken(), token.getOauthTokenSecret());
			sharePhotoByFlickr(imgPath  , result);
		}
	}
    
    /**
     ***************************************     四大分享          *********************************************
     */    
    /**
     * Facebook share
     */
    public void sharePhotoByFacebook(String imgPath ){
    	FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(this)
    			.setPicture("file:///"+imgPath)//TODO 链接...
    			.build();
		uiHelper.trackPendingDialogCall(shareDialog.present());
    }
    
    /**
     *  Flickr share
     */
    public void sharePhotoByFlickr(final String imgPath, final OAuth oauth){
    	if (oauth != null) {
			UploadPhotoTask taskUpload = new UploadPhotoTask(this, imgPath );
			taskUpload.setOnUploadDone(new UploadPhotoTask.OnUploadPhotoFinish() {
				public void onComplete() {
					Toast.makeText(ShareSuccessActivity.this, "share success!", Toast.LENGTH_SHORT).show();
				}
			});
			taskUpload.execute(oauth);
		}
    }
    
    /**
     * Instagram share
     */
    private void shareToInstagram(String imgPath){
    	File media = new File(imgPath);
    	
    	Bitmap background = ImageUtil.BitmapUtil.getBreviaryBitmapByFilepath(media , 1000, 500);
    	if(background == null){
    		return;
    	}
    	
    	Bitmap fixBitmap = ImageUtil.BitmapUtil.toConformBitmap(background);
    	if(fixBitmap ==null ){
    		return;
    	}
    	
    	String fixPhoto = ImageUtil.BitmapUtil.saveBitmapInSdcard("fixPhoto.jpg", Constant.CACHE, fixBitmap);
    	
	    Intent share = new Intent(Intent.ACTION_SEND);
	    share.setType( "image/*" );
	    Uri uri = Uri.fromFile(new File(fixPhoto));
	    share.putExtra(Intent.EXTRA_STREAM, uri);
	    share.putExtra(Intent.EXTRA_TEXT, "Panoramics");
	    startActivity(Intent.createChooser(share, "Share to"));
	}
    
    /**
     * Twitter share
     */   
    public void shareToTwitter(String imgPath){
    	try {
    		Intent tweetIntent = new Intent(Intent.ACTION_SEND);
            tweetIntent.putExtra(Intent.EXTRA_TEXT, "twitter share");
            tweetIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile( new File(imgPath)));
            tweetIntent.setType("image/*");
            PackageManager pm = this.getPackageManager();
            List<ResolveInfo> lract = pm.queryIntentActivities(tweetIntent, PackageManager.MATCH_DEFAULT_ONLY);
            boolean resolved = false;
            for (ResolveInfo ri : lract) {
                if (ri.activityInfo.name.contains("twitter")) {
                    tweetIntent.setClassName(ri.activityInfo.packageName, ri.activityInfo.name);
                    resolved = true;
                    break;
                }
            }
            startActivity(resolved ? tweetIntent : Intent.createChooser(tweetIntent, "Choose one"));
        } catch (final ActivityNotFoundException e) {
            Toast.makeText(this, "You don't seem to have twitter installed on this device", Toast.LENGTH_SHORT).show();
        }  
    }
    
}
