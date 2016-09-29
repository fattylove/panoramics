package f.com.panoramics.activity;

import java.util.ArrayList;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.guxiu.panoramics.R;

import f.com.panoramics.base.BaseFragmentActivity;
import f.com.panoramics.fragment.FN_FollowingFragment;
import f.com.panoramics.fragment.FN_NewsFragment;
import f.com.panoramics.fragment.adapter.PCenterFragmentAdapter;


/**
 * 
 * @author Fatty
 * 
 * Following News
 *
 */
public class FNActivity extends BaseFragmentActivity implements OnClickListener{
	
	private Button cancelBtn;
	private Button followingBtn;
	private Button newsBtn;
	
	private ViewPager viewpager;
	private FN_FollowingFragment fn_FollowingFragment;
	private FN_NewsFragment fn_NewsFragment;
	private final ArrayList<Fragment> views = new ArrayList<Fragment>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.showAnimationIn();
		super.setScreenTag("FNActivity");
		
		setContentView(R.layout.panoramics_fn_layout);
		
		cancelBtn = (Button)this.findViewById(R.id.cancelBtn);
		followingBtn = (Button)this.findViewById(R.id.followingBtn);
		newsBtn = (Button)this.findViewById(R.id.newsBtn);
		cancelBtn.setOnClickListener(this);
		followingBtn.setOnClickListener(this);
		newsBtn.setOnClickListener(this);
		
		viewpager = (ViewPager)this.findViewById(R.id.viewpager);
		
		fn_FollowingFragment = new FN_FollowingFragment();
		fn_NewsFragment = new FN_NewsFragment();
		views.add(fn_FollowingFragment);
		views.add(fn_NewsFragment);
		
		viewpager.setAdapter(new PCenterFragmentAdapter(getSupportFragmentManager(), views)); 
		
		viewpager.setCurrentItem(0);
		setStyle(0);
		viewpager.setOnPageChangeListener(new OnPageChangeListener() {
			public void onPageSelected(int arg0) {
				setStyle(arg0);
			}
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}
	
	/**
	 * select style
	 * 
	 * @param state
	 */
	public void setStyle(int state){
		switch (state) {
		case 0:
			newsBtn.setBackgroundColor(Color.TRANSPARENT);
			followingBtn.setBackgroundResource(R.drawable.p_fn_title_select_btn_bg);
			followingBtn.setTextColor(Color.parseColor("#272c2e"));
			newsBtn.setTextColor(Color.parseColor("#ffffff"));
			break;
		case 1:
			followingBtn.setBackgroundColor(Color.TRANSPARENT);
			newsBtn.setBackgroundResource(R.drawable.p_fn_title_select_btn_bg);
			followingBtn.setTextColor(Color.parseColor("#ffffff"));
			newsBtn.setTextColor(Color.parseColor("#272c2e"));
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.followingBtn:
			setStyle(0);
			viewpager.setCurrentItem(0);
			break;
		case R.id.newsBtn:
			setStyle(1);
			viewpager.setCurrentItem(1);
			break;
		case R.id.cancelBtn:
			finishCurrentActivityWithAmination();
			break;
		}
	}
}
