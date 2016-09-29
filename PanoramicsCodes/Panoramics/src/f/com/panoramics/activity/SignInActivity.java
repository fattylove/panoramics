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

import com.guxiu.panoramics.R;

import f.com.panoramics.base.BaseFragmentActivity;
import f.com.panoramics.service.netservice.SignInService;
import f.com.panoramics.utils.DialogUtil;
import f.com.panoramics.utils.EmailUtil;
import f.com.panoramics.view.EarthProgressDialog;
import f.com.panoramics.view.PanoToast;

/**
 * 
 * @author Fatty
 * 
 * 用户登录界面
 *
 */
public class SignInActivity extends BaseFragmentActivity implements OnClickListener {

	private Button cancelBtn;
	private Button rightBtn;

	private EditText emailEditText;
	private EditText passwordEditText;
	private Button signInBtn;
	private EarthProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.showAnimationIn();
		super.setScreenTag("SignInActivity");
		
		setContentView(R.layout.panoramics_sign_in_layout);
		progressDialog = DialogUtil.createProgressDialog(this);
		initUI();
	}

	public void initUI() {
		cancelBtn = (Button) this.findViewById(R.id.cancelBtn);
		rightBtn = (Button) this.findViewById(R.id.rightBtn);
		signInBtn = (Button) this.findViewById(R.id.signInBtn);
		
		emailEditText = (EditText) this.findViewById(R.id.emailEditText);
		passwordEditText = (EditText) this.findViewById(R.id.passwordEditText);
		
		signInBtn.setOnClickListener(this);
		cancelBtn.setOnClickListener(this);
		rightBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancelBtn:
			Intent selectIntent = new Intent();
			selectIntent.setClass(SignInActivity.this,SelectSignInActivity.class);
			SignInActivity.this.startActivity(selectIntent);
			finishCurrentActivityWithAmination();
			break;
		case R.id.rightBtn:
			Intent intent = new Intent();
			intent.setClass(SignInActivity.this, SignUpActivity.class);
			SignInActivity.this.startActivity(intent);
			finishCurrentActivityWithAmination();
			break;
		case R.id.signInBtn:
			String email = emailEditText.getText().toString().trim();
			String password = passwordEditText.getText().toString().trim();
			if (TextUtils.isEmpty(email)) {
				DialogUtil.showCheckDialog(SignInActivity.this, R.string.pano_sign_error_email_is_empty);
				return;
			}
			if (!EmailUtil.isEmail(email)) {
				DialogUtil.showCheckDialog(SignInActivity.this,R.string.pano_sign_error_please_enter_a_correct_email_address);
				return;
			}
			if (TextUtils.isEmpty(password)) {
				DialogUtil.showCheckDialog(SignInActivity.this, R.string.pano_sign_error_wrong_password);
				return;
			}
			SignInService loginService = new SignInService(handler);
			loginService.login(email, password);
			progressDialog.show();
			break;
		}
	}

	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
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
							homeIntent.setClass(SignInActivity.this, HomeActivity.class);
							SignInActivity.this.startActivity(homeIntent);
							finishCurrentActivityWithAmination();
						} catch (Exception e) {
						}
						dismissProgressDialog();
					}
					
					break;
				case SignInService.ERROR_CODE400:
					toast(R.string.pano_errorcode_400);
					dismissProgressDialog();
					break;
				case SignInService.ERROR_CODE401:
					toast(R.string.pano_errorcode_401);
					dismissProgressDialog();
					break;
				case SignInService.ERROR_CODE404:
					toast(R.string.pano_errorcode_404);
					dismissProgressDialog();
					break;
				case SignInService.ERROR_CODE409:
					toast(R.string.pano_errorcode_409);
					dismissProgressDialog();
					break;
				default :
					PanoToast.showToast(SignInActivity.this, null);
					dismissProgressDialog();
					break;
				}
				break;
			
			//others
			default :
				
				break;
			}
		
		}
	};
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent selectIntent = new Intent();
			selectIntent.setClass(SignInActivity.this,SelectSignInActivity.class);
			SignInActivity.this.startActivity(selectIntent);
			finishCurrentActivityWithAmination();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	public void dismissProgressDialog(){
		if(progressDialog!=null && progressDialog.isShowing()){
			progressDialog.cancel();
			progressDialog.dismiss();
		}
	}
}

