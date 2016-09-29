package f.com.panoramics.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.guxiu.panoramics.R;

import f.com.panoramics.base.ActivityManager;
import f.com.panoramics.base.BaseFragmentActivity;


/**
 * 
 * @author Fatty
 * 
 * Panoramics App隐私协议界面
 * 
 *
 */
public class PrivateProlicyActivity extends BaseFragmentActivity {

	String p = "http://guxiusupport.com/privacy.html";
	
	private Button cancelBtn;
	private WebView prWebView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.showAnimationBottomIn();
		super.setScreenTag("PrivateProlicyActivity");
		
		setContentView(R.layout.panoramics_private_prolicy_layout);

		cancelBtn = (Button)this.findViewById(R.id.cancelBtn);
		cancelBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finishCurrentActivity();
				showAnimationBottomOut();
			}
		});
		
		prWebView = (WebView) this.findViewById(R.id.webview);
		prWebView.loadUrl(p);
	}
	
	/**
	 * back
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			ActivityManager.getAppManager().finishActivity(this);
			showAnimationBottomOut();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	
}
