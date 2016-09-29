package f.com.panoramics.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.guxiu.panoramics.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import f.com.panoramics.adapter.PanoItemAdapter;
import f.com.panoramics.base.BaseFragment;
import f.com.panoramics.constant.Constant;
import f.com.panoramics.entity.MediaEntity;
import f.com.panoramics.service.jsonmodel.AnalyseMediaJson;
import f.com.panoramics.service.netservice.HomeHotService;
import f.com.panoramics.service.netservice.HomeNewestService;
import f.com.panoramics.utils.DialogUtil;
import f.com.panoramics.utils.ImageConfig;
import f.com.panoramics.view.CircleImageView;
import f.com.panoramics.view.EarthProgressDialog;
import f.com.panoramics.view.FloatHeaderListView;
import f.com.panoramics.view.PanoToast;


/**
 * 
 * @author Fatty
 * 
 * Hot Fragment
 *
 */
public class HomeHotFragment extends BaseFragment implements 
FloatHeaderListView.OnHeaderUpdateListener,
OnRefreshListener,
FloatHeaderListView.OnLoadMoreListener
{
	
	private Activity activity;
	
	private FloatHeaderListView pListView;
	private PanoItemAdapter adapter;
	private SwipeRefreshLayout swipeLayout;  
	private EarthProgressDialog progressDialog;
	
	private boolean isLoading = false ;
	
    @Override
    public void onAttach(Activity activity) {
    	super.onAttach(activity);
    	this.activity = activity;
    	progressDialog = DialogUtil.createProgressDialog(activity);
    }
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view  = inflater.inflate(R.layout.panoramics_fragment_home_hot_layout, container, false);
		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		swipeLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_container);
		swipeLayout.setOnRefreshListener(this);  
	    swipeLayout.setColorSchemeResources(
	    		android.R.color.white,  
	    		android.R.color.holo_green_light,  
	    		android.R.color.holo_orange_light, 
	    		android.R.color.holo_red_light);  
		
	    pListView = (FloatHeaderListView) view.findViewById(R.id.hot_pListView);
		adapter = new PanoItemAdapter(activity);
		pListView.setAdapter(adapter);
		pListView.setOnLoadMoreListener(this);
		pListView.setOnHeaderUpdateListener(this);
		
		View headerView = activity.getLayoutInflater().inflate(R.layout.panoramics_item_group_layout, null);
	    headerView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	    pListView.setHeaderView(headerView);
	    
	    currentLoad();
	}
	
	@Override
	public void setHeadViewContent(View headerView, int position) {
		if(position > -1){
			final MediaEntity entity = (MediaEntity) adapter.getGroup(position);
			CircleImageView headerImageView = (CircleImageView) headerView.findViewById(R.id.headerImageView);
			TextView perNameTextView = (TextView) headerView.findViewById(R.id.perNameTextView);
			TextView locationTextView = (TextView) headerView.findViewById(R.id.locationTextView);
			TextView likeTextView = (TextView) headerView.findViewById(R.id.likeTextView);
			ImageView likeImageView = (ImageView)headerView.findViewById(R.id.likeImageView);
			
			ImageLoader.getInstance().displayImage(entity.getAvatar(), headerImageView ,ImageConfig.getHeaderConfig());
			locationTextView.setText(entity.getLocation());
			perNameTextView.setText(entity.getNickname());
			likeTextView.setText(entity.getLike()+"");
			if(entity.getLikeState() == Constant.LIKED){
				likeImageView.setBackgroundResource(R.drawable.p_like_on);
			}else if(entity.getLikeState() == Constant.UNLIKED){
				likeImageView.setBackgroundResource(R.drawable.p_like_off);
			}
		}
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		adapter.destoryReceiver();
	}
	
	public void currentLoad(){
		progressDialog.show();
		isLoading = true;
		HomeHotService hotService = new HomeHotService(handler);
		hotService.hot(getPreString("token"), 0);
	}
	
	public void onRefresh() {
		swipeLayout.setRefreshing(true);
		pListView.setCanLoadMore(true);
		pListView.onLoadMoreComplete();
		HomeHotService hotService = new HomeHotService(handler);
		hotService.hot(getPreString("token"), 0);
	}

	public void onLoadMore() {
		isLoading = true;
		HomeHotService hotService = new HomeHotService(handler);
		hotService.hot(getPreString("token"), adapter.getGroupCount());
	}
	
	public void dismissDialog(){
		if(progressDialog !=null && progressDialog.isShowing()){
			progressDialog.dismiss();
			progressDialog.cancel();
		}
	}
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case HomeNewestService.NEWEST_SUCCESS:
				String root = (String) msg.obj;
				if(TextUtils.isEmpty(root)){
					return;
				}
				//处理数据
				ArrayList<MediaEntity> mediaEntities = AnalyseMediaJson.getAllMedia(root , Constant.HOT);
				if(!mediaEntities.isEmpty()){
					if(swipeLayout.isRefreshing()){//判断是否正在刷新
						adapter.clear();
						adapter.setMediaEntities(mediaEntities, pListView);
						swipeLayout.setRefreshing(false);
					}else if(isLoading){
						adapter.setMediaEntities(mediaEntities, pListView);
					}
					pListView.onLoadMoreComplete();
				}else{
					pListView.setCanLoadMore(false);
				}
				mediaEntities.clear();
				isLoading = false;
				dismissDialog();
				break;
			case HomeNewestService.ERROR_CODE400:
				isLoading = false;
				dismissDialog();
				break;
			case HomeNewestService.ERROR_CODE401:
				isLoading = false;
				tokenInvalid();
				dismissDialog();
				break;
			default:
				isLoading = false;
				if(swipeLayout.isRefreshing()){
					swipeLayout.setRefreshing(false);
				}
				pListView.onLoadMoreComplete();
				PanoToast.showToast(activity, "No Internet Connection");
				dismissDialog();
				break;
			}
		}
	};
}
