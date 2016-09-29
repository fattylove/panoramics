package f.com.panoramics.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guxiu.panoramics.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import f.com.panoramics.base.BaseFragmentActivity;
import f.com.panoramics.entity.AccountEntity;
import f.com.panoramics.fragment.PCenterFlowFragment;
import f.com.panoramics.fragment.PCenterLikedFragment;
import f.com.panoramics.fragment.PCenterLocationFragment;
import f.com.panoramics.fragment.adapter.PCenterFragmentAdapter;
import f.com.panoramics.service.dbservice.AccountDBService;
import f.com.panoramics.service.jsonmodel.AnalyseAccoundJson;
import f.com.panoramics.service.netservice.AccountService;
import f.com.panoramics.service.netservice.FollowService;
import f.com.panoramics.utils.ImageConfig;
import f.com.panoramics.utils.ShareUtil;
import f.com.panoramics.view.ActionSheetFollowDialog;
import f.com.panoramics.view.ActionSheetFollowDialog.OnActionSheetSelectedListener;
import f.com.panoramics.view.CircleImageView;
import f.com.panoramics.view.FloatHeaderListView;
import f.com.panoramics.view.JJLinearLayout;
import f.com.panoramics.view.JJLinearLayout.OnGiveUpTouchEventListener;
import f.com.panoramics.view.NonScrollViewPager;
import f.com.panoramics.view.PanoToast;

/**
 * 
 * @author Fatty
 * 
 * 个人中心界面
 *
 */
public class PersonalCenterActivity extends BaseFragmentActivity implements
OnClickListener,
OnGiveUpTouchEventListener,
OnActionSheetSelectedListener
{
	private static final int FLOW  = 0;
	private static final int LOCATION = 1;
	private static final int LIKE = 2;

	private JJLinearLayout stickyLayout;
	private NonScrollViewPager viewpager;
	
	private ProgressBar progressBar;
	private Button cancelBtn;
	private Button settingBtn;
	private TextView nameTitle;
	
	private LinearLayout headerViewLayout;
	private LinearLayout centerViewLayout;
	private LayoutInflater inflater;
	
	private CircleImageView headerImageView;
	private ImageView seeImageView;
	
	// number
	private TextView followerTextView;
	private TextView likeTextView;
	private TextView followingTextView;
	
	private TextView editButton;
	private LinearLayout followersLayout;
	private LinearLayout likesHLayout;
	private LinearLayout followingLayout;
	
	private RelativeLayout fowLayout;
	private RelativeLayout locationLayout;
	private RelativeLayout likesLayout;
	
	private ImageView fowImageViewPoint;
	private ImageView locationImageViewPoint;
	private ImageView likeImageViewPoint;
	
	private ImageView fowImageView;
	private ImageView locationImageView;
	private ImageView likeImageView;
	
	private TextView fowTextView;
	private TextView locationTextView;
	private TextView likeHereTextView;
	
	private final ArrayList<Fragment> views = new ArrayList<Fragment>();
	private PCenterLocationFragment locationFragment;
	private PCenterFlowFragment flowFragment;
	private PCenterLikedFragment likesFragment;
	
	private String accountId  ;
	private boolean isWho = true;
	private AccountDBService accountDBService ;
	
	private ActionSheetFollowDialog actionSheet;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.showAnimationIn();
		super.setScreenTag("PersonalCenterActivity");
		setContentView(R.layout.panoramics_pcenter_layout);
		inflater = LayoutInflater.from(PersonalCenterActivity.this);

		accountDBService = new AccountDBService(this);
		
		accountId = getIntent().getStringExtra("accountId");
		isWho = TextUtils.equals(accountId, getPreString("uid")) ? true : false;
		
		initUI();
		headerView();
		centerView();
		bottomView();
		
		actionSheet = new ActionSheetFollowDialog(PersonalCenterActivity.this);
		actionSheet.setOnActionSheetSelectedListener(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		getAccount(accountId);
	}
	
	/**
	 * 初始化基础View
	 */
	public void initUI(){
		stickyLayout = (JJLinearLayout)this.findViewById(R.id.sticky_layout);
		stickyLayout.setOnGiveUpTouchEventListener(this);
		
		headerViewLayout = (LinearLayout)this.findViewById(R.id.headerViewLayout);
		centerViewLayout = (LinearLayout)this.findViewById(R.id.centerViewLayout);

		progressBar = (ProgressBar)this.findViewById(R.id.progressBar);
		cancelBtn = (Button)this.findViewById(R.id.cancelBtn);
		settingBtn = (Button)this.findViewById(R.id.settingBtn);
		if(isWho){
			settingBtn.setBackgroundResource(R.drawable.p_top_setting);
		}else{
			settingBtn.setBackgroundResource(R.drawable.p_pcenter_title_share);
		}
		nameTitle = (TextView)this.findViewById(R.id.nameTitle);
		
		cancelBtn.setOnClickListener(this);
		settingBtn.setOnClickListener(this);
	}
	
	/**
	 * 初始化HeaderView
	 */
	public void headerView(){
		View headerView = inflater.inflate(R.layout.panoramics_pcenter_header_layout, null);
		headerImageView = (CircleImageView)headerView.findViewById(R.id.headerImageView);
		seeImageView = (ImageView)headerView.findViewById(R.id.seeImageView);
		followerTextView = (TextView)headerView.findViewById(R.id.followerTextView);
		likeTextView = (TextView)headerView.findViewById(R.id.likeTextView);
		followingTextView = (TextView)headerView.findViewById(R.id.followingTextView);
		editButton = (Button)headerView.findViewById(R.id.editButton);
		
		if(isWho){
			seeImageView.setImageResource(R.drawable.p_pcenter_message_black);
		}else{
			seeImageView.setVisibility(View.INVISIBLE);
		}
		
		//TODO Notification Message Like and Follow
		seeImageView.setVisibility(View.INVISIBLE);
		
		likesHLayout = (LinearLayout)headerView.findViewById(R.id.likesHLayout);
		followersLayout = (LinearLayout)headerView.findViewById(R.id.followersLayout);
		followingLayout = (LinearLayout)headerView.findViewById(R.id.followingLayout);
		
		followersLayout.setOnClickListener(this);
		likesHLayout.setOnClickListener(this);
		followingLayout.setOnClickListener(this);
		
		headerImageView.setOnClickListener(this);
		seeImageView.setOnClickListener(this);
		editButton.setOnClickListener(this);
		
		headerViewLayout.addView(headerView);
	}
	
	/**
	 * 设置TopView
	 * 
	 * @param accountEntity
	 */
	public void setHeaderViewData(AccountEntity accountEntity){
		if(accountEntity != null){
			//判断个人编辑、Follow ， UnFollow
			editButton.setClickable(true);
			if(isWho){
				editButton.setText(R.string.pano_pcenter_edit_your_profile);
			}else{
				String followState = accountEntity.getFollowState();
				editButton.setTag(followState);
				if(TextUtils.equals(followState, FollowService.follow)){
					editButton.setText(R.string.pano_pcenter_unfollow);
					editButton.setTextColor(Color.WHITE);
					editButton.setBackgroundResource(R.drawable.p_pcenter_unfollow);
				}else{
					editButton.setBackgroundResource(R.drawable.p_pcenter_btn_bg);
					editButton.setText(R.string.pano_pcenter_follow);
					editButton.setTextColor(Color.parseColor("#6e7476"));
				}
			}
			
			if(TextUtils.isEmpty(accountEntity.getNickname())){
				nameTitle.setText(accountEntity.getUsername());
				actionSheet.setNickname(accountEntity.getUsername());
			}else{
				nameTitle.setText(accountEntity.getNickname());
				actionSheet.setNickname(accountEntity.getNickname());
			}
			
			followerTextView.setText("" + accountEntity.getFollowed_by());
			likeTextView.setText("" + accountEntity.getLiked_by());
			followingTextView.setText("" + accountEntity.getFollows());
			actionSheet.setHeaderImage(accountEntity.getAvatar());
			ImageLoader.getInstance().displayImage(accountEntity.getAvatar(), headerImageView, ImageConfig.getPCenterHeaderConfig());
		}
	}
	
	/**
	 * 初始化CenterView
	 */
	public void centerView(){
		View centerView = inflater.inflate(R.layout.panoramics_pcenter_center_funcation_layout, null);
		fowLayout = (RelativeLayout)centerView.findViewById(R.id.fowLayout);
		locationLayout = (RelativeLayout)centerView.findViewById(R.id.locationLayout);
		likesLayout = (RelativeLayout)centerView.findViewById(R.id.likesLayout);
		
		fowLayout.setOnClickListener(this);
		locationLayout.setOnClickListener(this);
		likesLayout.setOnClickListener(this);
		
		fowImageViewPoint = (ImageView)centerView.findViewById(R.id.fowImageViewPoint);
		locationImageViewPoint = (ImageView)centerView.findViewById(R.id.locationImageViewPoint);
		likeImageViewPoint = (ImageView)centerView.findViewById(R.id.likeImageViewPoint);
		
		fowImageView = (ImageView)centerView.findViewById(R.id.fowImageView);
		locationImageView = (ImageView)centerView.findViewById(R.id.locationImageView);
		likeImageView = (ImageView)centerView.findViewById(R.id.likeImageView);
		
		fowTextView = (TextView)centerView.findViewById(R.id.fowTextView);
		locationTextView = (TextView)centerView.findViewById(R.id.locationTextView);
		likeHereTextView = (TextView)centerView.findViewById(R.id.likeHereTextView);
		
		centerViewLayout.addView(centerView);
	}
	
	/**
	 * 初始化BottomView
	 */
	public void bottomView(){
		viewpager = (NonScrollViewPager)this.findViewById(R.id.viewpager);
		
		locationFragment = new PCenterLocationFragment();
		flowFragment = new PCenterFlowFragment(accountId , locationFragment ,isWho);
		likesFragment = new PCenterLikedFragment(accountId , isWho);
		
		views.add(flowFragment);
		views.add(locationFragment);
		views.add(likesFragment);
		
		viewpager.setAdapter(new PCenterFragmentAdapter(getSupportFragmentManager(), views)); 
		viewpager.setCurrentItem(FLOW);
		selectStyle(FLOW);
        viewpager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				selectStyle(arg0);
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.followersLayout:
			Intent followerIntent = new Intent();
			followerIntent.putExtra("uid", accountId);
			followerIntent.setClass(PersonalCenterActivity.this, FollowerActivity.class);
			PersonalCenterActivity.this.startActivity(followerIntent);
			break;
		case R.id.likesHLayout:
			break;
		case R.id.followingLayout:
			Intent followingIntent = new Intent();
			followingIntent.putExtra("uid", accountId);
			followingIntent.setClass(PersonalCenterActivity.this, FollowingActivity.class);
			PersonalCenterActivity.this.startActivity(followingIntent);
			break;
		case R.id.fowLayout:
			viewpager.setCurrentItem(FLOW);
			selectStyle(FLOW);
			break;
		case R.id.locationLayout:
			viewpager.setCurrentItem(LOCATION);
			selectStyle(LOCATION);
			break;
		case R.id.likesLayout:
			viewpager.setCurrentItem(LIKE);
			selectStyle(LIKE);
			break;
		case R.id.headerImageView:
			//TODO 头像放大
			break;
		case R.id.seeImageView:
			Intent seeIntent = new Intent();
			seeIntent.setClass(PersonalCenterActivity.this, FNActivity.class);
			PersonalCenterActivity.this.startActivity(seeIntent);
			break;
		case R.id.editButton:
			
			//判断个人编辑、Follow ， UnFollow
			if(isWho){
				Intent goProfileIntent = new Intent();
				goProfileIntent.putExtra("uid", accountId);
				goProfileIntent.setClass(PersonalCenterActivity.this, EditProfileActivity.class);
				PersonalCenterActivity.this.startActivity(goProfileIntent);
			}else{
				String followState = (String) v.getTag();
				if(TextUtils.equals(followState, FollowService.follow)){
					actionSheet.show();
					
				}else{
					((Button)v).setTag(FollowService.follow);
					((Button)v).setText(R.string.pano_pcenter_unfollow);
					((Button)v).setTextColor(Color.WHITE);
					((Button)v).setBackgroundResource(R.drawable.p_pcenter_unfollow);
					FollowService relationShipService = new FollowService(handler);
					relationShipService.relationship(accountId, getPreString("token"), FollowService.follow);
				}
			}
			break;
		case R.id.cancelBtn:
			finishCurrentActivityWithAmination();
			break;
		case R.id.settingBtn:
			if(isWho){
				Intent intent = new Intent();
				intent.setClass(PersonalCenterActivity.this, SettingsActivity.class);
				PersonalCenterActivity.this.startActivity(intent);
			}else{
				ShareUtil.share(PersonalCenterActivity.this, "Share Panoramics", "Share his or her profile!");
			}
			break;
		}
	}
	
	/**
	 * 切换CenterView的样式
	 * 
	 * @param style
	 */
	public void selectStyle(int style){
		switch (style) {
		case FLOW:
			fowImageViewPoint.setVisibility(View.VISIBLE);
			locationImageViewPoint.setVisibility(View.INVISIBLE);
			likeImageViewPoint.setVisibility(View.INVISIBLE);
			
			fowTextView.setTextColor(Color.parseColor("#3f484b"));
			locationTextView.setTextColor(Color.parseColor("#6e7476"));
			likeHereTextView.setTextColor(Color.parseColor("#6e7476"));
			
			fowImageView.setBackgroundResource(R.drawable.p_pcenter_flow_default);
			locationImageView.setBackgroundResource(R.drawable.p_pcenter_location_select);
			likeImageView.setBackgroundResource(R.drawable.p_pcenter_like_default);
			break;
		case LOCATION:
			fowImageViewPoint.setVisibility(View.INVISIBLE);
			locationImageViewPoint.setVisibility(View.VISIBLE);
			likeImageViewPoint.setVisibility(View.INVISIBLE);
			
			fowTextView.setTextColor(Color.parseColor("#6e7476"));
			locationTextView.setTextColor(Color.parseColor("#3f484b"));
			likeHereTextView.setTextColor(Color.parseColor("#6e7476"));
			
			fowImageView.setBackgroundResource(R.drawable.p_pcenter_flow_select);
			locationImageView.setBackgroundResource(R.drawable.p_pcenter_location_default);
			likeImageView.setBackgroundResource(R.drawable.p_pcenter_like_default);
			break;
		case LIKE:
			fowImageViewPoint.setVisibility(View.INVISIBLE);
			locationImageViewPoint.setVisibility(View.INVISIBLE);
			likeImageViewPoint.setVisibility(View.VISIBLE);
			
			fowTextView.setTextColor(Color.parseColor("#6e7476"));
			locationTextView.setTextColor(Color.parseColor("#6e7476"));
			likeHereTextView.setTextColor(Color.parseColor("#3f484b"));
			
			fowImageView.setBackgroundResource(R.drawable.p_pcenter_flow_select);
			locationImageView.setBackgroundResource(R.drawable.p_pcenter_location_select);
			likeImageView.setBackgroundResource(R.drawable.p_pcenter_like_selected);
			break;
		}
	}

	/**
	 * 
	 */
	@Override
	public boolean giveUpTouchEvent(MotionEvent event) {
		if(viewpager.getCurrentItem() == FLOW){
			if(flowFragment.getView()!=null){
				FloatHeaderListView plisview = (FloatHeaderListView)flowFragment.getView().findViewById(R.id.pcenter_FlowListView);
				if (plisview.getFirstVisiblePosition() == 0) {
		            View view = plisview.getChildAt(0);
		            if (view != null && view.getTop() >= 0) {
		                return true;
		            }
		        }
			}
		}
		if(viewpager.getCurrentItem() == LIKE){
			if(likesFragment.getView()!=null){
				FloatHeaderListView plisview = (FloatHeaderListView)likesFragment.getView().findViewById(R.id.pcenter_LikesListView);
				if (plisview.getFirstVisiblePosition() == 0) {
		            View view = plisview.getChildAt(0);
		            if (view != null && view.getTop() >= 0) {
		                return true;
		            }
		        }
			}
		}
		return false;
	}
	
	/**
	 * 获取用户信息
	 */
	public void getAccount(String uid){
		editButton.setClickable(false);
		editButton.setText("loading...");
		
		progressBar.setVisibility(View.VISIBLE);
		settingBtn.setVisibility(View.GONE);
		AccountService accountService = new AccountService(handler);
		accountService.getAccountEntity(uid, getPreString("token"));
	}

	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			
			
			
			//RELATIONSHIP
			case FollowService.RELATIONSHIP:{
				switch (msg.arg1) {
				case FollowService.RELATIONSHIP_SUCCESS:
					break;
				case FollowService.ERROR_CODE400:
					toast("Can’t follow yourself, parameters error.");
					break;
				case FollowService.ERROR_CODE404:
					toast("Can’t find current or targeted users.");
					break;
				case FollowService.ERROR_CODE503:
					toast("Fail to add follow relationship.");
					break;
				default:
					PanoToast.showToast(PersonalCenterActivity.this, R.string.pano_errorcode_network_error);
					break;
				}
			}
			break;
			
			
			
			// GET_ACCOUNT
			case AccountService.GET_ACCOUNT: {
				int state = msg.arg1;
				switch (state) {
				case AccountService.ACCOUNT_SUCCESS:
					String root = (String) msg.obj;
					if (TextUtils.isEmpty(root)) {
						return;
					}
					AccountEntity accountEntity = AnalyseAccoundJson.getSingleAccount(root , isWho);
					if(accountEntity!=null){
						//存入缓存
						if(isWho){
							accountDBService.saveAccountEntity(accountEntity);
						}
						setHeaderViewData(accountEntity);
					}
					progressBar.setVisibility(View.GONE);
					settingBtn.setVisibility(View.VISIBLE);
					break;
				case AccountService.ERROR_CODE400:
					progressBar.setVisibility(View.GONE);
					settingBtn.setVisibility(View.VISIBLE);
					toast("Bad Request.");
					break;
				case AccountService.ERROR_CODE401:
					tokenInvalid();
					break;
				case AccountService.ERROR_CODE404:
					progressBar.setVisibility(View.GONE);
					settingBtn.setVisibility(View.VISIBLE);
					toast("the account is inexistent");
					break;
				default:
					progressBar.setVisibility(View.GONE);
					settingBtn.setVisibility(View.VISIBLE);
					//读取缓存
					if(isWho){
						AccountEntity cacheAccountEntity = accountDBService.findByAccountId(accountId);
						setHeaderViewData(cacheAccountEntity);
					}
					PanoToast.showToast(PersonalCenterActivity.this, R.string.pano_errorcode_network_error);
					break;
				}
			}
			break;
			
			
			default:
				
				break;
			}
		}
	};

	@Override
	public void onClick(int whichButton) {
		switch (whichButton) {
		case 1:
			editButton.setTag(FollowService.unfollow);
			editButton.setBackgroundResource(R.drawable.p_pcenter_btn_bg);
			editButton.setTextColor(Color.parseColor("#6e7476"));
			editButton.setText(R.string.pano_pcenter_follow);
			
			FollowService relationShipService = new FollowService(handler);
			relationShipService.relationship(accountId, getPreString("token"), FollowService.unfollow);
			break;
		}
	}
}
