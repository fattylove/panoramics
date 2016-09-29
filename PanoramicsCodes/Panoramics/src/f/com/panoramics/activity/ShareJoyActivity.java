package f.com.panoramics.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.guxiu.panoramics.R;

import f.com.panoramics.base.ActivityManager;

/**
 * 
 * @author Fatty
 * 
 * Share the Joy
 *
 */
public class ShareJoyActivity extends FragmentActivity implements OnClickListener{

	private LayoutInflater layoutInflater;
	private LinearLayout rootLayout;
	private ImageView shareFacebook;
	private ImageView shareTwitter;
	private ImageView shareFlikr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		layoutInflater = LayoutInflater.from(this);
		rootLayout = (LinearLayout) layoutInflater.inflate( R.layout.panoramics_share_joy_layout, null);
		shareFacebook = (ImageView) rootLayout.findViewById(R.id.shareFacebook);
		shareTwitter = (ImageView) rootLayout.findViewById(R.id.shareTwitter);
		shareFlikr = (ImageView) rootLayout.findViewById(R.id.shareFlikr);

		rootLayout.setOnClickListener(this);
		shareFacebook.setOnClickListener(this);
		shareTwitter.setOnClickListener(this);
		shareFlikr.setOnClickListener(this);

		setContentView(rootLayout);

		showViews();
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.shareFacebook:
			Toast.makeText(ShareJoyActivity.this, "shareFacebook", Toast.LENGTH_SHORT).show();
			break;
		case R.id.shareTwitter:
			Toast.makeText(ShareJoyActivity.this, "shareTwitter", Toast.LENGTH_SHORT).show();
			break;
		case R.id.shareFlikr:
			Toast.makeText(ShareJoyActivity.this, "shareFlikr", Toast.LENGTH_SHORT).show();
			break;
		case R.id.rootLayout:
			break;
		}
	}
	
	
	/**
	 * back
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			dismissViews();
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					ActivityManager.getAppManager().finishActivity(ShareJoyActivity.this);
				}
			}, 500);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * show Views
	 */
	public void showViews() {
		rootLayout.setBackgroundColor(Color.parseColor("#b0000000"));
		Animation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
		alphaAnimation.setDuration(500);
		rootLayout.startAnimation(alphaAnimation);

		Animation translateAnimation_facebook = new TranslateAnimation(0.0f,0.0f, 1200.0f, 0.0f);
		translateAnimation_facebook.setDuration(500);
		shareFacebook.startAnimation(translateAnimation_facebook);

		Animation translateAnimation_twitter = new TranslateAnimation(0.0f,0.0f, 1000.0f, 0.0f);
		translateAnimation_twitter.setDuration(400);
		shareTwitter.startAnimation(translateAnimation_twitter);

		Animation translateAnimation_flikr = new TranslateAnimation(0.0f, 0.0f, 900.0f, 0.0f);
		translateAnimation_flikr.setDuration(300);
		shareFlikr.startAnimation(translateAnimation_flikr);
	}

	/**
	 * dismiss Animation
	 */
	public void dismissViews() {

		Animation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
		alphaAnimation.setDuration(500);
		rootLayout.startAnimation(alphaAnimation);
		alphaAnimation.setAnimationListener(new AnimationListener() {
			public void onAnimationStart(Animation animation) {
			}
			public void onAnimationRepeat(Animation animation) {
			}
			public void onAnimationEnd(Animation animation) {
				rootLayout.setBackgroundColor(Color.parseColor("#00000000"));
			}
		});

		Animation translateAnimation_facebook = new TranslateAnimation(0.0f, 0.0f, 0.0f, -800.0f);
		translateAnimation_facebook.setDuration(300);
		shareFacebook.startAnimation(translateAnimation_facebook);
		translateAnimation_facebook.setAnimationListener(new AnimationListener() {
					public void onAnimationStart(Animation animation) {
					}

					public void onAnimationRepeat(Animation animation) {
					}

					public void onAnimationEnd(Animation animation) {
						shareFacebook.setVisibility(View.GONE);
					}
				});

		Animation translateAnimation_twitter = new TranslateAnimation(0.0f,	0.0f, 0.0f, -1000.0f);
		translateAnimation_twitter.setDuration(400);
		shareTwitter.startAnimation(translateAnimation_twitter);
		translateAnimation_twitter
				.setAnimationListener(new AnimationListener() {
					public void onAnimationStart(Animation animation) {
					}
					public void onAnimationRepeat(Animation animation) {
					}
					public void onAnimationEnd(Animation animation) {
						shareTwitter.setVisibility(View.GONE);
					}
				});

		Animation translateAnimation_flikr = new TranslateAnimation(0.0f, 0.0f, 0.0f, -1200.0f);
		translateAnimation_flikr.setDuration(500);
		shareFlikr.startAnimation(translateAnimation_flikr);
		translateAnimation_flikr.setAnimationListener(new AnimationListener() {
			public void onAnimationStart(Animation animation) {
			}
			public void onAnimationRepeat(Animation animation) {
			}
			public void onAnimationEnd(Animation animation) {
				shareFlikr.setVisibility(View.GONE);
			}
		});
	}
}
