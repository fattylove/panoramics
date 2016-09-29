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
import com.nostra13.universalimageloader.core.ImageLoader;

import f.com.panoramics.adapter.PanoItemAdapter;
import f.com.panoramics.base.BaseFragment;
import f.com.panoramics.constant.Constant;
import f.com.panoramics.entity.MediaEntity;
import f.com.panoramics.service.dbservice.MediaDBService;
import f.com.panoramics.service.jsonmodel.AnalyseMediaJson;
import f.com.panoramics.service.netservice.PCenterLikedService;
import f.com.panoramics.utils.ImageConfig;
import f.com.panoramics.view.CircleImageView;
import f.com.panoramics.view.FloatHeaderListView;
import f.com.panoramics.view.PanoToast;

/**
 * 
 * @author Fatty
 * 
 * Flow Fragment
 *
 */
public class PCenterLikedFragment extends BaseFragment implements 
FloatHeaderListView.OnHeaderUpdateListener,
FloatHeaderListView.OnLoadMoreListener{
	
	//UI
	private Activity activity ;
	private FloatHeaderListView pListView;
	private PanoItemAdapter adapter;

	// DATA
	private MediaDBService mediaDBService ;
	private MediaEntity lastMediaEntity;
	private String accountId ;
	private boolean isWho = false;
	
	/**
	 * 
	 * @param accountId
	 * @param isWho
	 */
	public PCenterLikedFragment(String accountId , boolean isWho){
		this.accountId = accountId;
		this.isWho = isWho;
	}
	
    @Override
    public void onAttach(Activity activity) {
    	super.onAttach(activity);
    	this.activity  = activity;
    	mediaDBService = new MediaDBService(activity);
    }
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.panoramics_pcenter_bottom_likes_layout, container, false);
		return view;
	}
	
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	    pListView = (FloatHeaderListView) view.findViewById(R.id.pcenter_LikesListView);
		adapter = new PanoItemAdapter(activity);
		pListView.setAdapter(adapter);
		pListView.setOnLoadMoreListener(this);
		pListView.setOnHeaderUpdateListener(this);
		
		View headerView = activity.getLayoutInflater().inflate(R.layout.panoramics_item_group_layout, null);
	    headerView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	    pListView.setHeaderView(headerView);
		
		getLiked(accountId, null);
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
	
	public void onLoadMore() {
		if(lastMediaEntity!=null){
			getLiked(accountId, lastMediaEntity.getLikedTime()+"");
		}else{
			pListView.setCanLoadMore(false);
		}
	}
	
	public int getAdapterCount(){
		return adapter.getGroupCount();
	}

	public void getLiked(String uid ,String max_timestamp){
		PCenterLikedService service = new PCenterLikedService(handler);
		service.liked(uid, getPreString("token") ,max_timestamp , null);
	}
	
	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case PCenterLikedService.LIKED_SUCCESS:
					String root = (String) msg.obj;
					if(TextUtils.isEmpty(root)){
						return;
					}
					//处理数据
					ArrayList<MediaEntity> mediaEntities = AnalyseMediaJson.getAllMedia(root , Constant.LIKE);
					if(!mediaEntities.isEmpty()){
						adapter.setMediaEntities(mediaEntities , pListView);
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
				case PCenterLikedService.ERROR_CODE400:
					toast("ERROR_CODE400");
					break;
				case PCenterLikedService.ERROR_CODE401:
					tokenInvalid();
					break;
				default :
					if(isWho){
						if(getAdapterCount()==0){
							ArrayList<MediaEntity> cacheMediaEntities = (ArrayList<MediaEntity>) mediaDBService.findMediaEntities(0L , Constant.LIKE);
							pListView.setOnHeaderUpdateListener(PCenterLikedFragment.this);
							adapter.setMediaEntities(cacheMediaEntities , pListView);
						}	
					}
					pListView.onLoadMoreComplete();
					PanoToast.showToast(activity, "No Internet Connection.");
					break;
				}
			}
	};
}
