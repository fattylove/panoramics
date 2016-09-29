package f.com.panoramics.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.guxiu.panoramics.R;

import f.com.panoramics.base.BaseFragmentActivity;
import f.com.panoramics.service.netservice.ChangePwdService;
import f.com.panoramics.utils.DialogUtil;
import f.com.panoramics.view.EarthProgressDialog;

/**
 * 
 * @author Fatty
 * 
 * 更换密码
 *
 */
public class ChangePwdActivity extends BaseFragmentActivity implements 	OnClickListener {

	private Button cancelBtn;
	private EditText currentPwdEditText;
	private EditText newPwdEditText;
	private EditText reNewPwdEditText;
	
	private Button rightBtn;
	
	private EarthProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.showAnimationIn();
		super.setScreenTag("ChangePwdActivity");
		
		setContentView(R.layout.panoramics_change_password_layout);
		initView();
	}

	/**
	 * 初始化UI
	 */
	public void initView() {
		progressDialog = DialogUtil.createProgressDialog(this);
		
		cancelBtn = (Button) this.findViewById(R.id.cancelBtn);
		cancelBtn.setOnClickListener(this);
		
		currentPwdEditText = (EditText)this.findViewById(R.id.currentPwdEditText);
		newPwdEditText = (EditText)this.findViewById(R.id.newPwdEditText);
		reNewPwdEditText = (EditText)this.findViewById(R.id.reNewPwdEditText);
		rightBtn = (Button)this.findViewById(R.id.rightBtn);
		
		rightBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancelBtn:
			finishCurrentActivityWithAmination();
			break;
		case R.id.rightBtn:
			progressDialog.show();
			String currentPwd = currentPwdEditText.getText().toString();
			String newPwd = newPwdEditText.getText().toString();
			String reNewPwd = reNewPwdEditText.getText().toString();
			String uid = getPreString("uid");
			String token = getPreString("token");
			
			if (TextUtils.isEmpty(currentPwd)) {
				return;
			}
			if (TextUtils.isEmpty(newPwd)) {
				return;
			}
			if (TextUtils.isEmpty(reNewPwd)) {
				return;
			}
			if (!TextUtils.equals(newPwd, reNewPwd)) {
				DialogUtil.showCheckDialog(ChangePwdActivity.this, "Password does not match.");
				return;
			}
			ChangePwdService changePwdService = new ChangePwdService(handler);
			changePwdService.change(uid, token, currentPwd, reNewPwd);
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
			case ChangePwdService.CHANGE_PASSWORD_SUCCESS:
				dismissDialog();
				finishCurrentActivityWithAmination();
				break;
			case ChangePwdService.ERROR_CODE400:
				toast("Bad Request, parameter errors.");
				dismissDialog();
				break;
			case ChangePwdService.ERROR_CODE401:
				toast("Unauthorized, oldpwd is invalid or invalid token.");
				dismissDialog();
				break;
			case ChangePwdService.ERROR_CODE404:
				toast("Not Found, the user is not found.");
				dismissDialog();
				break;
			case ChangePwdService.ERROR_CODE409:
				toast("Conflict, oldpwd has been modified.");
				dismissDialog();
				break;
			default:
				toast("Are you in DPRK?");
				dismissDialog();
				break;
			}
		}
	};
	
	/**
	 * 取消菊花
	 */
	public void dismissDialog(){
		if(progressDialog!=null && progressDialog.isShowing()){
			progressDialog.dismiss();
			progressDialog.cancel();
		}
	}
}
