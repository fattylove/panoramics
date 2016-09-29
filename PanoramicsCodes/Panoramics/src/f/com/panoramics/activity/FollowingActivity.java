package f.com.panoramics.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.guxiu.panoramics.R;

import f.com.panoramics.adapter.FollowAdapter;
import f.com.panoramics.base.BaseFragmentActivity;
import f.com.panoramics.entity.FollowEntity;
import f.com.panoramics.service.jsonmodel.AnalyseFollowJson;
import f.com.panoramics.service.netservice.FollowService;
import f.com.panoramics.utils.AndroidStateUtil;
import f.com.panoramics.view.LoadMoreListView;
import f.com.panoramics.view.LoadMoreListView.OnLoadMoreListener;
import f.com.panoramics.view.PanoToast;

/**
 * 
 * @author Fatty
 * 
 * Following me的列表
 *
 */
public class FollowingActivity extends BaseFragmentActivity 
implements OnLoadMoreListener,
OnRefreshListener{

	private Button cancelBtn;
	private ProgressBar progressBar;
	
	private LinearLayout noInternetLayout;
	private View noInternetView;
	
	private SwipeRefreshLayout swipeLayout;  
	private LoadMoreListView followListView;
	private FollowAdapter adapter;
	
	private String uid ;
	private FollowEntity lastFollowEntity;
	private boolean isLoading = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.showAnimationIn();
		super.setScreenTag("FollowingActivity");
		setContentView(R.layout.panoramics_following_layout);
		uid = getIntent().getStringExtra("uid");
		
		progressBar = (ProgressBar)this.findViewById(R.id.progressBar);
		cancelBtn = (Button)this.findViewById(R.id.cancelBtn);
		cancelBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finishCurrentActivityWithAmination();
			}
		});
		
		swipeLayout = (SwipeRefreshLayout)this.findViewById(R.id.swipe_container);
		swipeLayout.setOnRefreshListener(this);  
	    swipeLayout.setColorSchemeResources(
	    		android.R.color.white,  
	    		android.R.color.holo_green_light,  
	    		android.R.color.holo_orange_light, 
	    		android.R.color.holo_red_light);  
		followListView = (LoadMoreListView)this.findViewById(R.id.follow);
		followListView.setOnLoadMoreListener(this);
		adapter = new FollowAdapter(this);
		followListView.setAdapter(adapter);
		
		///
		noInternetLayout = (LinearLayout) this.findViewById(R.id.noInternetLayout);
		noInternetView = LayoutInflater.from(this).inflate(R.layout.panoramics_no_internet_connection_layout, null);
		noInternetView.findViewById(R.id.noInternetConnectionImageView).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (!TextUtils.isEmpty(uid)) {
					currentFollowings(uid);
				}
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		adapter.clear();
		
		if (!AndroidStateUtil.isNetworkAvailable(this)) {
			swipeLayout.setVisibility(View.GONE);
			noInternetLayout.setVisibility(View.VISIBLE);
			noInternetLayout.addView(noInternetView);
		}else{
			if(!TextUtils.isEmpty(uid)){
				currentFollowings(uid);
			}
		}
	}
	
	/** 
	 * 获取当前followings
	 * 
	 * @param uid
	 */
	public void currentFollowings(String uid ){
		progressBar.setVisibility(View.VISIBLE);
		isLoading = true;
		FollowService followService = new FollowService(handler);
		followService.follows(uid, getPreString("token"), null, null);
	}
	
	/**
	 * 
	 */
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case FollowService.RELATIONSHIP_FOLLOW:
				switch (msg.arg1) {
				case FollowService.FOLLOW_SUCCESS:
					noInternetLayout.setVisibility(View.GONE);
					swipeLayout.setVisibility(View.VISIBLE);
					String root = (String) msg.obj;
					if(TextUtils.isEmpty(root)){
						return;
					}
					ArrayList<FollowEntity> followEntities = AnalyseFollowJson.getAllFollows(root);
					if(!followEntities.isEmpty()){
						if(swipeLayout.isRefreshing()){
							adapter.clear();
							adapter.addFollow(followEntities);
							swipeLayout.setRefreshing(false);
						}else if(isLoading){
							adapter.addFollow(followEntities);
						}
						followListView.onLoadMoreComplete();
						lastFollowEntity = followEntities.get(followEntities.size() - 1);
						followEntities.clear();
					}else{
						followListView.setCanLoadMore(false);
					}
					isLoading  = false;
					progressBar.setVisibility(View.INVISIBLE);
					break;
				case FollowService.ERROR_CODE400:
					isLoading  = false;
					progressBar.setVisibility(View.INVISIBLE);
					break;
				default:
					isLoading  = false;
					progressBar.setVisibility(View.INVISIBLE);
					if(swipeLayout.isRefreshing()){
						swipeLayout.setRefreshing(false);
					}
					followListView.onLoadMoreComplete();
					PanoToast.showToast(FollowingActivity.this, "No Internet Connection");
					break;
				}
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void onLoadMore() {
		isLoading = true;
		FollowService followService = new FollowService(handler);
		followService.follows(uid, getPreString("token") ,null, lastFollowEntity.getCreateTime() + "");
	}

	@Override
	public void onRefresh() {
		swipeLayout.setRefreshing(true);
		followListView.setCanLoadMore(true);
		followListView.onLoadMoreComplete();
		
		FollowService followService = new FollowService(handler);
		followService.follows(uid, getPreString("token") ,null, null);
	};
}
