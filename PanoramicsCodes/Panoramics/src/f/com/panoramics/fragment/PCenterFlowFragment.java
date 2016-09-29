package f.com.panoramics.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.guxiu.panoramics.R;

import f.com.panoramics.adapter.PanoFlowAdapter;
import f.com.panoramics.base.BaseFragment;
import f.com.panoramics.constant.Constant;
import f.com.panoramics.entity.MediaEntity;
import f.com.panoramics.service.dbservice.MediaDBService;
import f.com.panoramics.service.jsonmodel.AnalyseMediaJson;
import f.com.panoramics.service.netservice.PCenterFlowService;
import f.com.panoramics.utils.IntervalUtil;
import f.com.panoramics.view.FloatHeaderListView;
import f.com.panoramics.view.PanoToast;

/**
 * 
 * @author Fatty
 * 
 * Flow Fragment
 *
 */
public class PCenterFlowFragment extends BaseFragment implements 
FloatHeaderListView.OnHeaderUpdateListener,
FloatHeaderListView.OnLoadMoreListener{
	
	
	//UI
	private Activity activity ;
	private FloatHeaderListView pListView;
	private PanoFlowAdapter adapter;

	// DATA
	private MediaDBService mediaDBService ;
	private MediaEntity lastMediaEntity;
	private String accountId ;
	private boolean isWho = false;
	private PCenterLocationFragment locationFragment;
	
	/**
	 * 
	 * @param accountId
	 * @param isWho
	 */
	public PCenterFlowFragment(String accountId ,  PCenterLocationFragment locationFragment ,boolean isWho ){
		this.accountId = accountId;
		this.isWho = isWho;
		this.locationFragment = locationFragment;
	}
	
    @Override
    public void onAttach(Activity activity) {
    	super.onAttach(activity);
    	this.activity  = activity;
    	mediaDBService = new MediaDBService(activity);
    }
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.panoramics_pcenter_bottom_flow_layout, container, false);
		return view;
	}
	
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	    pListView = (FloatHeaderListView) view.findViewById(R.id.pcenter_FlowListView);
		adapter = new PanoFlowAdapter(activity);
		pListView.setAdapter(adapter);
		pListView.setOnLoadMoreListener(this);
		pListView.setOnHeaderUpdateListener(this);
		
		View headerView = activity.getLayoutInflater().inflate(R.layout.panoramics_pcenter_flow_group_item_layout, null);
	    headerView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	    pListView.setHeaderView(headerView);
		
		getRecent(accountId, null, null, null, null);
	}
	
	@Override
	public void setHeadViewContent(View headerView, int position) {
		if(position > -1){
			final MediaEntity entity = (MediaEntity) adapter.getGroup(position);
			TextView timeTextView = (TextView) headerView.findViewById(R.id.timeTextView);
			TextView locationTextView = (TextView) headerView.findViewById(R.id.locationTextView);
			TextView likeTextView = (TextView) headerView.findViewById(R.id.likeTextView);
			ImageView likeImageView = (ImageView)headerView.findViewById(R.id.likeImageView);
			
			locationTextView.setText(entity.getLocation());
			timeTextView.setText(IntervalUtil.getInterval(entity.getP_time()));
			likeTextView.setText(entity.getLike()+"");
			
			if(entity.getLikeState() == Constant.LIKED){
				likeImageView.setBackgroundResource(R.drawable.p_like_on);
			}else if(entity.getLikeState() == Constant.UNLIKED){
				likeImageView.setBackgroundResource(R.drawable.p_like_off);
			}
		}
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		locationFragment.clear();
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		
		adapter.destoryReceiver();
	}
	
	public void onLoadMore() {
		if(lastMediaEntity!=null){
			getRecent(accountId, lastMediaEntity.getCreateTime()+"", null, null, null);
		}else{
			pListView.setCanLoadMore(false);
		}
	}
	
	public int getAdapterCount(){
		return adapter.getGroupCount();
	}

	public void getRecent(String uid ,String max_timestamp ,String min_timestamp ,String max_id ,String min_id){
		PCenterFlowService service = new PCenterFlowService(handler);
		service.recent(uid, getPreString("token") ,max_timestamp , min_timestamp);
	}
	
	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			//Recent
			case PCenterFlowService.RECENT: {
				switch (msg.arg1) {
				case PCenterFlowService.RECENT_SUCCESS:
					String recentRoot = (String) msg.obj;
					if(TextUtils.isEmpty(recentRoot)){
						return;
					}
					//处理数据
					ArrayList<MediaEntity> mediaEntities = AnalyseMediaJson.getAllMedia(recentRoot , Constant.FLOW);
					if(!mediaEntities.isEmpty()){
						adapter.setMediaEntities(mediaEntities , pListView);
						locationFragment.setData(mediaEntities);
						//存入缓存
						if(isWho){
							mediaDBService.saveMediaEntities(mediaEntities);
						}
						lastMediaEntity = mediaEntities.get(mediaEntities.size()-1);
						
						pListView.onLoadMoreComplete();
					}else{
						pListView.setCanLoadMore(false);
					}
					mediaEntities.clear();
					break;
				case PCenterFlowService.ERROR_CODE400:
					toast("ERROR_CODE400");
					break;
				case PCenterFlowService.ERROR_CODE401:
					tokenInvalid();
					break;
				default :
					if(isWho){
						if(getAdapterCount()==0){
							ArrayList<MediaEntity> cacheMediaEntities = (ArrayList<MediaEntity>) mediaDBService.findMediaEntities(0L , Constant.FLOW);
							pListView.setOnHeaderUpdateListener(PCenterFlowFragment.this);
							adapter.setMediaEntities(cacheMediaEntities , pListView);
							
							locationFragment.setData(cacheMediaEntities);
						}	
					}
					pListView.onLoadMoreComplete();
					PanoToast.showToast(activity, "No Internet Connection");
					break;
				}
			}
			break;
			}
		}
	};
}
