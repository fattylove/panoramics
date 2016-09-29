package f.com.panoramics.activity;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.guxiu.panoramics.R;

import f.com.panoramics.base.ActivityManager;
import f.com.panoramics.base.BaseFragmentActivity;
import f.com.panoramics.constant.Constant;
import f.com.panoramics.utils.ShareUtil;
import f.com.panoramics.view.ActionSheetLogoutDialog;
import f.com.panoramics.view.ActionSheetLogoutDialog.OnActionSheetLogoutSelectedListener;
import fatty.library.sqlite.core.SQLService;

/**
 * 
 * @author Fatty
 * 
 * 设置界面
 *
 */
public class SettingsActivity extends BaseFragmentActivity implements OnClickListener , OnActionSheetLogoutSelectedListener{
	
	private Button cancelBtn;
	private Button feedbackBtn;
	private Button privatePolicyBtn;
	private Button rateAroundBtn;
	private Button shareBtn;
	private Button logoutBtn;
	
	private ActionSheetLogoutDialog actionSheetLogoutDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.showAnimationIn();
		super.setScreenTag("SettingsActivity");
		
		setContentView(R.layout.panoramics_setting_layout);
		
		actionSheetLogoutDialog = new ActionSheetLogoutDialog(this);
		actionSheetLogoutDialog.setOnActionSheetLogoutSelectedListener(this);
	
		cancelBtn = (Button)this.findViewById(R.id.cancelBtn);
		feedbackBtn = (Button)this.findViewById(R.id.feedbackBtn);
		privatePolicyBtn = (Button)this.findViewById(R.id.privatePolicyBtn);
		rateAroundBtn = (Button)this.findViewById(R.id.rateAroundBtn);
		shareBtn = (Button)this.findViewById(R.id.shareBtn);
		logoutBtn = (Button)this.findViewById(R.id.logoutBtn);
		
		
		cancelBtn.setOnClickListener(this);
		feedbackBtn.setOnClickListener(this);
		privatePolicyBtn.setOnClickListener(this);
		rateAroundBtn.setOnClickListener(this);
		shareBtn.setOnClickListener(this);
		logoutBtn.setOnClickListener(this);
		
	}

	/**
	 * 
	 * @param context
	 */
	public void feedback(Context context){
		Intent email = new Intent(android.content.Intent.ACTION_SEND);
		email.setType("plain/text");
		String[] emailReciver = new String[] { "Feedback@guxiu.ca" };
		String[] emailCReciver = new String[] { "hi@guxiu.ca" };
		
		String emailSubject = "Panoramics Android Feedback.";
		email.putExtra(android.content.Intent.EXTRA_EMAIL,   emailReciver);
		email.putExtra(android.content.Intent.EXTRA_CC,      emailCReciver);
		email.putExtra(android.content.Intent.EXTRA_SUBJECT, emailSubject);
		startActivity(Intent.createChooser(email, "Choose Email Client"));
	}

	/**
	 * 
	 * @param content
	 */
	public void privatePolicy(Activity content){
		Intent profileIntent = new Intent();
		profileIntent.setClass(content,PrivateProlicyActivity.class);
		content.startActivity(profileIntent);
	}
	
	/**
	 * 
	 * @param content
	 */
	public void rate(Activity content){
		String str = "market://details?id=com.guxiu.panoramics";
		try {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse(str));
			content.startActivity(intent);
		} catch (Exception e) {
			Toast.makeText(content, "no find the market", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancelBtn:
			finishCurrentActivityWithAmination();
			break;
		case R.id.feedbackBtn:
			feedback(SettingsActivity.this);
			break;
		case R.id.privatePolicyBtn:
			privatePolicy(SettingsActivity.this);
			break;
		case R.id.rateAroundBtn:
			rate(SettingsActivity.this);
			break;
		case R.id.shareBtn:
			ShareUtil.share(SettingsActivity.this , "shareimnage.jpg");
			break;
		case R.id.logoutBtn:
			actionSheetLogoutDialog.show();
			break;
		}
	}

	@Override
	public void onLogoutClick(int whichButton) {
		switch (whichButton) {
		case 0:
			
			putPreBoolean("isLogin", false);
			
			SQLService service = SQLService.create(SettingsActivity.this , true);
			service.dropDb();
			
			File file = new File(Constant.PANORAMICS);
			if(file.isDirectory()){
				deleteFiles(file);
			}
			
			Intent intent = new Intent(); 
			intent.setClass(SettingsActivity.this, SelectSignInActivity.class);
			SettingsActivity.this.startActivity(intent);
			
			ActivityManager activityManager = ActivityManager.getAppManager();
			activityManager.finishAllActivity();
			break;
		}
	}
	
	/**
	 * delete files
	 * 
	 * @param dir
	 * @return
	 */
	private boolean deleteFiles(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteFiles(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
	}
}
