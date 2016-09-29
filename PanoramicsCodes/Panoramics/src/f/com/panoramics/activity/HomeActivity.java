package f.com.panoramics.activity;


import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guxiu.panoramics.R;

import f.com.panoramics.base.ActivityManager;
import f.com.panoramics.base.BaseFragmentActivity;
import f.com.panoramics.fragment.HomeFeedFragment;
import f.com.panoramics.fragment.HomeHotFragment;
import f.com.panoramics.fragment.HomeNewestFragment;
import f.com.panoramics.fragment.HomeWorldFragment;

/**
 * 
 * @author Fatty
 * 
 * Panoramics 主界面
 *
 */
public class HomeActivity extends BaseFragmentActivity implements
OnClickListener {

	private PopupWindow mPopupWindow;
	//popo window
//	private static final String[] POPS =new String[]{"WORLD" ,"HOT" ,"NEWEST" ,"FEED"};
	private static final int WORLD  = 0;
	private static final int HOT    = 1;
	private static final int NEWEST = 2;
	private static final int FEED   = 3;
	
	private int currentPosition = 0;
	
	private final int[] groupNames = new int[] { R.string.pano_home_world, R.string.pano_home_hot, R.string.pano_home_newest ,R.string.pano_home_feed};
	private final int[] defaults = new int[] {R.drawable.p_popo_world,R.drawable.p_popo_hot,R.drawable.p_popo_newest,R.drawable.p_popo_feed}; 
	private final int[] selects = new int[]  {R.drawable.p_popo_world,R.drawable.p_popo_hot,R.drawable.p_popo_newest,R.drawable.p_popo_feed};
	private final int[] selecteds = new int[]{R.drawable.p_popo_world,R.drawable.p_popo_hot,R.drawable.p_popo_newest,R.drawable.p_popo_feed};

	//Layout
	private ImageView camBtn;
	private ImageView selfBtn;
	private ImageView titleImageView;
	private ImageView titleArrImageView;
	private RelativeLayout titleLayout;
	
	private TextView titleTextView;
	private ImageView topSelectImageView;
	private LinearLayout titleContentLayout;

	//Fragment
	private FragmentManager fragmentManager;
	private FragmentTransaction fragmentTransaction;
	
	private HomeHotFragment hotFragment;
	private HomeFeedFragment feedFragment;
	private HomeWorldFragment worldFragment;
	private HomeNewestFragment newestFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.showAnimationIn();
		super.setScreenTag("HomeActivity");
		
		setContentView(R.layout.panoramics_home_layout);
		
		selfBtn = (ImageView) this.findViewById(R.id.selfBtn);
		titleImageView = (ImageView) this.findViewById(R.id.titleImageView);
		camBtn = (ImageView) this.findViewById(R.id.camBtn);
		titleLayout = (RelativeLayout)this.findViewById(R.id.titleLayout);
		titleArrImageView = (ImageView)this.findViewById(R.id.titleArrImageView);

		titleContentLayout = (LinearLayout)this.findViewById(R.id.titleContentLayout);
		titleTextView= (TextView)this.findViewById(R.id.titleTextView);
		topSelectImageView = (ImageView)this.findViewById(R.id.topSelectImageView);
		
		titleLayout.setOnClickListener(this);
		titleImageView.setOnClickListener(this);
		selfBtn.setOnClickListener(this);
		camBtn.setOnClickListener(this);
		
		showContent(WORLD);
	}

	/**
	 * 切换当前内容 
	 * 
	 * @param position
	 */
	public void showContent(int position) {
		
		hotFragment = new HomeHotFragment();
		feedFragment = new HomeFeedFragment();
		worldFragment = new HomeWorldFragment();
		newestFragment = new HomeNewestFragment();
		
		fragmentManager = getSupportFragmentManager();
		fragmentTransaction = fragmentManager.beginTransaction();
		
		switch (position) {
		case WORLD:
			fragmentTransaction.replace(R.id.contentLayout, worldFragment);
			break;
		case HOT:
			fragmentTransaction.replace(R.id.contentLayout, hotFragment);
			break;
		case NEWEST:
			fragmentTransaction.replace(R.id.contentLayout, newestFragment);
			break;
		case FEED:
			fragmentTransaction.replace(R.id.contentLayout, feedFragment);
		}
		fragmentTransaction.commit();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.selfBtn:
			
			//
			Intent intent = new Intent();
			intent.putExtra("accountId", getPreString("uid"));
			intent.setClass(HomeActivity.this, PersonalCenterActivity.class);
			HomeActivity.this.startActivity(intent);
			
			break;
		case R.id.camBtn:
			
			//
			Intent cIntent = new Intent();
			cIntent.setClass(HomeActivity.this, PanoramicsActivity.class);
			HomeActivity.this.startActivity(cIntent);
			
			break;
		case R.id.titleLayout:
			
			//
			titleArrImageView.setBackgroundResource(R.drawable.p_home_title_bottom_arr_click);
			showPopwindow();
			
			break;
		}
	}
	
	/**
	 * show world ,hot , newest
	 */
	private void showPopwindow() {
		LayoutInflater mLayoutInflater = LayoutInflater.from(this);
		View contentView = mLayoutInflater.inflate(R.layout.panoramics_home_pop_group_list, null);
		final ListView mPopListView = (ListView) contentView.findViewById(R.id.lv_group);
		final TextView  popoTopView= (TextView)contentView.findViewById(R.id.popoTopView);
		final TextView popoBottomView = (TextView)contentView.findViewById(R.id.popoBottomView);
		mPopListView.setAdapter(new BaseAdapter() {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				LayoutInflater itemLayoutInfater = LayoutInflater.from(HomeActivity.this);
				convertView = itemLayoutInfater.inflate(R.layout.panoramics_home_pop_group_item, null);
				TextView groupItemTextView = (TextView) convertView.findViewById(R.id.tv_group_item);
				ImageView tv_group_img = (ImageView) convertView.findViewById(R.id.tv_group_img);
				groupItemTextView.setText(groupNames[position]);
				if(currentPosition == position){
					tv_group_img.setBackgroundResource(selects[position]);
				}else{
					tv_group_img.setBackgroundResource(defaults[position]);
				}

				//ListView item Animation.
				Animation animation = (Animation) AnimationUtils.loadAnimation(HomeActivity.this, R.anim.listview_item_in);
				LayoutAnimationController lac = new LayoutAnimationController(animation);
				lac.setDelay(0.3f); 
				lac.setOrder(LayoutAnimationController.ORDER_REVERSE); 
				mPopListView.setLayoutAnimation(lac);
				
				return convertView;
			}
			@Override
			public long getItemId(int position) {
				return position;
			}
			@Override
			public Object getItem(int position) {
				return groupNames[position];
			}
			@Override
			public int getCount() {
				return groupNames.length;
			}
		});

		mPopupWindow = new PopupWindow(contentView, getPreInt("screen_w"), getPreInt("screen_h"));
		mPopupWindow.setAnimationStyle(R.style.popwindow_show_categary_list);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		mPopupWindow.update();
		mPopupWindow.setFocusable(true);
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.showAsDropDown(findViewById(R.id.popoLine), 0, 0);
		mPopListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1 , int position, long arg3) {
				titleImageView.setVisibility(View.GONE);
				titleContentLayout.setVisibility(View.VISIBLE);
				titleTextView.setText(groupNames[position]);
				topSelectImageView.setBackgroundResource(selecteds[position]);
				showContent(position);
				currentPosition = position;
				if (mPopupWindow.isShowing()) {
					mPopupWindow.dismiss();
				}
			}
		});
		
		popoTopView.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (mPopupWindow.isShowing()) {
					mPopupWindow.dismiss();
				}
			}
		});
		
		popoBottomView.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (mPopupWindow.isShowing()) {
					mPopupWindow.dismiss();
				}
			}
		});
		
		mPopupWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				titleArrImageView.setBackgroundResource(R.drawable.p_home_title_bottom_arr);				
			}
		});
	}
	
	/**
	 * back
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(mPopupWindow!=null && mPopupWindow.isShowing()){
				mPopupWindow.dismiss();
				mPopupWindow = null;
			}else{
				ActivityManager.getAppManager().finishActivity(this);
				showAnimationOut();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
