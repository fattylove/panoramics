package f.com.panoramics.activity;

import java.io.File;
import java.util.Arrays;

import org.json.JSONObject;

import twitter4j.TwitterException;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.facebook.Session;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.guxiu.panoramics.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.polites.android.GestureImageView;
import com.twitter.sdk.TwitterAndroid;
import com.twitter.sdk.TwitterAndroid.SessionState;
import com.twitter.sdk.TwitterAndroid.StatusCallback;

import f.com.panoramics.base.BaseFragmentActivity;
import f.com.panoramics.constant.Constant;
import f.com.panoramics.service.netservice.SignInService;
import f.com.panoramics.utils.DialogUtil;
import f.com.panoramics.view.EarthProgressDialog;

/**
 * 
 * @author Fatty
 * 
 * 选择登录方式界面
 *
 */
public class SelectSignInActivity extends BaseFragmentActivity implements OnClickListener {

	private Button twitterBtn;
	private LoginButton facebookAuthButton;
	private Button signUpBtn;
	private Button loginBtn;
	private GestureImageView loginBgImageView;
	private EarthProgressDialog progressDialog;
	
	private TwitterAndroid twitter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.showAnimationIn();
		super.setScreenTag("SelectSignInActivity");
		setContentView(R.layout.panoramics_login_layout);
		progressDialog = DialogUtil.createProgressDialog(this);
		
		twitterBtn = (Button) this.findViewById(R.id.twitterBtn);
		signUpBtn = (Button) this.findViewById(R.id.signUpBtn);
		loginBtn = (Button) this.findViewById(R.id.loginBtn);
		
		loginBgImageView = (GestureImageView)this.findViewById(R.id.loginBgImageView);
		ImageLoader.getInstance().displayImage("assets://panoramics_l.jpg", loginBgImageView);
		
		facebookAuthButton = (LoginButton) this.findViewById(R.id.facebookAuthButton);
		
		twitterBtn.setShadowLayer(1, 0, -2, Color.parseColor("#000000"));
		signUpBtn.setShadowLayer(1, 0, -2, Color.parseColor("#000000"));
		loginBtn.setShadowLayer(1, 0, -2, Color.parseColor("#000000"));

		twitterBtn.setOnClickListener(this);
		signUpBtn.setOnClickListener(this);
		loginBtn.setOnClickListener(this);

		initFacebookLogin(savedInstanceState);
		initTwitter();
		
		initCacheFolder(Constant.PANORAMICS);
		initCacheFolder(Constant.CACHE);
		initCacheFolder(Constant.PIC);
		initCacheFolder(Constant.TEMP);
		initCacheFolder(Constant.MUSIC);
		initCacheFolder(Constant.DATA);
		initCacheFolder(Constant.SHARE);
		
	}
	
	public void initCacheFolder(String dir){
		File file = new File(dir);
		if(!file.exists()){
			file.mkdirs();
		}
	}
	
	/**
	 * init  Twitter
	 */
	public void initTwitter(){
		ConfigurationBuilder builder = new ConfigurationBuilder();
		builder.setOAuthConsumerKey(Constant.twitter_consumer_key);
		builder.setOAuthConsumerSecret(Constant.twitter_access_token_secret );
		builder.setOAuthAccessTokenSecret( Constant.twitter_access_token_secret );
		
		Configuration configuration = builder.build();
		twitter = new TwitterAndroid( this, statusCallback, configuration);
		twitter.logout();
	}
	
	private final StatusCallback statusCallback = new StatusCallback() {
		@Override
		public void call(SessionState newState) {
			TwitterException exception = newState.getException();
			int state = newState.getState();
			if (null != exception) {
				return;
			}
			switch (state) {
			case SessionState.LOGIN_FAILED:
				break;
			case SessionState.CONNECTED:
				String fullname = newState.getActiveSession().getScreenName();
				progressDialog.show();
				if(!TextUtils.isEmpty(fullname)){
					SignInService loginService = new SignInService(handler);
					loginService.FT_login(fullname+"@guxiu.ca", fullname , SignInService.TWITTER);
				}
				break;
			case SessionState.DISCONNECTED:
				break;
			case SessionState.CONNECTING:
				break;
			}
		}
	};
	
	/********  facebook  *********/
	private UiLifecycleHelper uiHelper;
	
	public void initFacebookLogin(Bundle savedInstanceState){
        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);
		facebookAuthButton.setReadPermissions(Arrays.asList("email", "user_likes", "user_status"));
		facebookAuthButton.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
            @Override
            public void onUserInfoFetched(GraphUser user) {
            	if(user!=null){
            		try {
            			progressDialog.show();
            			JSONObject root = new JSONObject(user.getInnerJSONObject().toString());
            			String email = root.getString("email");
            			String first_name = root.getString("first_name");
            			String last_name = root.getString("last_name");
            			if(!TextUtils.isEmpty(email)){
            				SignInService loginService = new SignInService(handler);
            				loginService.FT_login(email, first_name , SignInService.FACEBOOK);
            			}else{
            				SignInService loginService = new SignInService(handler);
            				loginService.FT_login((last_name.toString()+first_name.toString()) + "@guxiu.ca", first_name , SignInService.FACEBOOK);
            			}
					} catch (Exception e) {
					}
            	}
            }
        });
		
		Session session = Session.getActiveSession();
		if(session!=null){
			if(session.isOpened()){
				session.close();
			}
		}
	}
	
	private com.facebook.Session.StatusCallback callback = new com.facebook.Session.StatusCallback() {
		@Override
		public void call(com.facebook.Session session, com.facebook.SessionState state, Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};

	private void onSessionStateChange(com.facebook.Session session, com.facebook.SessionState state, Exception exception) {
		if (state.isOpened()) {
			System.err.println("Facebook Logged in ....");
		} else if (state.isClosed()) {
			System.err.println("Facebook Logged out...");
		}
	}
	
/**
 * ************************************************
 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.twitterBtn:
			twitter.login(SelectSignInActivity.this);
			break;
		case R.id.signUpBtn:
			Intent signUpIntent = new Intent();
			signUpIntent.setClass(SelectSignInActivity.this, SignUpActivity.class);
			SelectSignInActivity.this.startActivity(signUpIntent);
			finishCurrentActivityWithAmination();
			break;
		case R.id.loginBtn:
			Intent intent = new Intent();
			intent.setClass(SelectSignInActivity.this, SignInActivity.class);
			SelectSignInActivity.this.startActivity(intent);
			finishCurrentActivityWithAmination();
			break;
		}
	}
	
	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case SignInService.FT_LOGIN:
				switch (msg.arg1) {
				case SignInService.FT_LOGIN_SUCCESS:
					putPreBoolean("isLogin", true);
					String jsonContent = (String) msg.obj;
					if(!TextUtils.isEmpty(jsonContent)){
						try {
							JSONObject object = new JSONObject(jsonContent);
							JSONObject data  = object.getJSONObject("data");
							String uid = data.getString("uid");
							String token = data.getString("token");
							String username = data.getString("username");
							String nickname = data.getString("nickname");
							
							putPreString("uid", uid);
							putPreString("token", token);
							putPreString("dbName", "panoramics_.db");
							putPreString("nickname",nickname);
							putPreString("username",username);
							
							dismissProgressDialog();
							
							Intent homeIntent = new Intent();
							homeIntent.setClass(SelectSignInActivity.this, HomeActivity.class);
							SelectSignInActivity.this.startActivity(homeIntent);
							finishCurrentActivityWithAmination();
						} catch (Exception e) {
						}
					}
					
					break;
				case SignInService.FT_ERROR_CODE400:
					toast("Some errors related with parameters occured.");
					dismissProgressDialog();
					break;
				case SignInService.FT_ERROR_CODE401:
					toast("The password is mismatched.");
					dismissProgressDialog();
					break;
				case SignInService.FT_ERROR_CODE404:
					toast("Can’t find the user.");
					dismissProgressDialog();
					break;
				case SignInService.FT_ERROR_CODE409:
					toast("Conflict to create an access token.");
					dismissProgressDialog();
					break;
				default :
					toast("Are you in DPRK?");
					dismissProgressDialog();
					break;
				}
				break;
			}
		}
	};
	
	@Override
	protected void onResume() {
		super.onResume();
		com.facebook.Session session = com.facebook.Session.getActiveSession();
		if (session != null && (session.isOpened() || session.isClosed())) {
			onSessionStateChange(session, session.getState(), null);
		}
		uiHelper.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
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
	
	@Override  
    public void onSaveInstanceState(Bundle outState) {  
        super.onSaveInstanceState(outState);  
        uiHelper.onSaveInstanceState(outState);  
    }  
	
	public void dismissProgressDialog(){
		if(progressDialog!=null && progressDialog.isShowing()){
			progressDialog.dismiss();
			progressDialog.cancel();
		}
	}
}
