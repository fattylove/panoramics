package f.com.panoramics.gcm.service;

import android.os.Bundle;
import android.widget.TextView;

import com.guxiu.panoramics.R;

import f.com.panoramics.base.BaseFragmentActivity;

/**
 * 
 * @author Fatty
 * 
 * 接收消息的Activity
 *
 */
public class ShowMessageActivity extends BaseFragmentActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.panoramics_gcm_layout);
		String content = getIntent().getStringExtra("gcmContent");
		TextView textView = (TextView)this.findViewById(R.id.content);
		textView.setText(content);
	}
}
