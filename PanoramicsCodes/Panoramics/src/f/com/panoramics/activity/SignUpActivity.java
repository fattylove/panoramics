package f.com.panoramics.activity;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.guxiu.panoramics.R;

import f.com.panoramics.base.BaseFragmentActivity;
import f.com.panoramics.service.netservice.SignInService;
import f.com.panoramics.service.netservice.SignUpService;
import f.com.panoramics.utils.DialogUtil;
import f.com.panoramics.utils.EmailUtil;
import f.com.panoramics.view.EarthProgressDialog;
import f.com.panoramics.view.PanoToast;

/**
 * 
 * @author Fatty
 * 
 * 用户注册界面
 *
 */
public class SignUpActivity extends BaseFragmentActivity implements OnClickListener {

	private Button cancelBtn;
	private Button signUpBtn;
	private Button rightBtn;
	private TextView profileBtn;

	private EditText emailEditText;
	private EditText passwordEditText;
	private EditText repasswordEditText;
	
	private SignUpService service;
	private String email ;
	private String password;
	private String repassword ;

	private EarthProgressDialog progressDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.showAnimationIn();
		super.setScreenTag("SignUpActivity");
		
		setContentView(R.layout.panoramics_sign_up_layout);

		progressDialog = DialogUtil.createProgressDialog(this);
		
		initUI();
	}

	/**
	 * 初始化控件
	 */
	public void initUI() {
		cancelBtn = (Button) this.findViewById(R.id.cancelBtn);
		signUpBtn = (Button) this.findViewById(R.id.signUpBtn);
		profileBtn = (TextView) this.findViewById(R.id.profileBtn);
		rightBtn = (Button)this.findViewById(R.id.rightBtn);

		emailEditText = (EditText) this.findViewById(R.id.emailEditText);
		passwordEditText = (EditText) this.findViewById(R.id.passwordEditText);
		repasswordEditText = (EditText) this.findViewById(R.id.repasswordEditText);

		cancelBtn.setOnClickListener(this);
		signUpBtn.setOnClickListener(this);
		profileBtn.setOnClickListener(this);
		rightBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancelBtn:
			Intent selectIntent = new Intent();
			selectIntent.setClass(SignUpActivity.this, SelectSignInActivity.class);
			SignUpActivity.this.startActivity(selectIntent);
			finishCurrentActivityWithAmination();
			break;
		case R.id.signUpBtn:
			email = emailEditText.getText().toString().trim();
			password = passwordEditText.getText().toString().trim();
			repassword = repasswordEditText.getText().toString().trim();
			if (TextUtils.isEmpty(email)) {
				DialogUtil.showCheckDialog(SignUpActivity.this, R.string.pano_sign_error_email_is_empty);
				return;
			}
			if (!EmailUtil.isEmail(email)) {
				DialogUtil.showCheckDialog(SignUpActivity.this, R.string.pano_sign_error_please_enter_a_correct_email_address);
				return;
			}
			if (TextUtils.isEmpty(password)) {
				DialogUtil.showCheckDialog(SignUpActivity.this, R.string.pano_sign_error_you_need_a_safety_password);
				return;
			}
			if (TextUtils.isEmpty(repassword)) {
				DialogUtil.showCheckDialog(SignUpActivity.this, R.string.pano_sign_error_password_does_not_match);
				return;
			}
			if (!TextUtils.equals(password, repassword)) {
				DialogUtil.showCheckDialog(SignUpActivity.this, R.string.pano_sign_error_password_does_not_match);
				return;
			}
			service = new SignUpService(handler);
			service.register(email, password);
			progressDialog.show();
			break;
		case R.id.profileBtn:
			Intent profileIntent = new Intent();
			profileIntent.setClass(SignUpActivity.this, PrivateProlicyActivity.class);
			SignUpActivity.this.startActivity(profileIntent);
			break;
		case R.id.rightBtn:
			Intent intent = new Intent();
			intent.setClass(SignUpActivity.this, SignInActivity.class);
			SignUpActivity.this.startActivity(intent);
			finishCurrentActivityWithAmination();
			break;
		}
	}

	/**
	 * 
	 */
	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			
			// Sign Up
			case SignUpService.REGISTER:
				switch (msg.arg1) {
				case SignUpService.REGISTER_SUCCESS201:
					putPreBoolean("isLogin", true);
					putPreString("passport", email);
					putPreString("password", password);
					//Sign In
					SignInService loginService = new SignInService(handler);
					loginService.login(email, password);
					break;
				case SignUpService.REGISTER_SUCCESS206:
					toast("Success to sign up, but fail to signin because token generation is failed.");
					dismissProgressDialog();
					break;
				case SignUpService.ERROR_CODE400:
					toast("Bad Request, Missing arguments or unsupported formats.");
					dismissProgressDialog();
					break;
				case SignUpService.ERROR_CODE409:
					toast("Conflict, The account has been existent.");
					dismissProgressDialog();
					break;
				case SignUpService.ERROR_CODE500:
					toast("Internal Server Error.");
					dismissProgressDialog();
					break;
				default:
					PanoToast.showToast(SignUpActivity.this, null);
					dismissProgressDialog();
					break;
				}
				break;
				
			// Sign In
			case SignInService.LOGIN:
				switch (msg.arg1) {
				case SignInService.LOGIN_SUCCESS:
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
							
							Intent homeIntent = new Intent();
							homeIntent.setClass(SignUpActivity.this, HomeActivity.class);
							SignUpActivity.this.startActivity(homeIntent);
							finishCurrentActivityWithAmination();
						} catch (Exception e) {
						}
					}
					dismissProgressDialog();
					break;
				case SignInService.ERROR_CODE400:
					toast("Some errors related with parameters occured.");
					dismissProgressDialog();
					break;
				case SignInService.ERROR_CODE401:
					toast("The password is mismatched.");
					dismissProgressDialog();
					break;
				case SignInService.ERROR_CODE404:
					toast("Can’t find the user.");
					dismissProgressDialog();
					break;
				case SignInService.ERROR_CODE409:
					toast("Conflict to create an access token.");
					dismissProgressDialog();
					break;
				default :
					PanoToast.showToast(SignUpActivity.this, null);
					dismissProgressDialog();
					break;
				}
				break;
				
			//Others
			default :
				break;
			}
		}
	};
	
	public void dismissProgressDialog(){
		if(progressDialog != null && progressDialog.isShowing()){
			progressDialog.cancel();
			progressDialog.dismiss();
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent selectIntent = new Intent();
			selectIntent.setClass(SignUpActivity.this, SelectSignInActivity.class);
			SignUpActivity.this.startActivity(selectIntent);
			finishCurrentActivityWithAmination();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
