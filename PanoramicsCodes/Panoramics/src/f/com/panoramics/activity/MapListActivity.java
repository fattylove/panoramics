package f.com.panoramics.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.guxiu.panoramics.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import f.com.panoramics.adapter.PanoItemAdapter;
import f.com.panoramics.base.ActivityManager;
import f.com.panoramics.base.BaseFragmentActivity;
import f.com.panoramics.constant.Constant;
import f.com.panoramics.entity.MediaEntity;
import f.com.panoramics.service.jsonmodel.AnalyseMediaJson;
import f.com.panoramics.service.netservice.MediaGetService;
import f.com.panoramics.utils.ImageConfig;
import f.com.panoramics.view.CircleImageView;
import f.com.panoramics.view.FloatHeaderListView;
import f.com.panoramics.view.PanoToast;

public class MapListActivity extends BaseFragmentActivity  implements 
FloatHeaderListView.OnHeaderUpdateListener {
	
	//
	private FloatHeaderListView pListView;
	private PanoItemAdapter adapter;
	private TextView title;
	
	//ids 
	private String ids ; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		showAnimationBottomIn();
		this.setContentView(R.layout.panoramics_maplist_layout);
		ids = getIntent().getStringExtra("ids");
		
	    pListView = (FloatHeaderListView)this.findViewById(R.id.group_listview);
	    title = (TextView)this.findViewById(R.id.title);
		
		adapter = new PanoItemAdapter(this);
		pListView.setAdapter(adapter);
		pListView.setOnHeaderUpdateListener(this);
		
		View headerView = this.getLayoutInflater().inflate(R.layout.panoramics_item_group_layout, null);
	    headerView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	    pListView.setHeaderView(headerView);
	    
	    if(!TextUtils.isEmpty(ids)){
	    	MediaGetService mediaService = new MediaGetService(handler);
			mediaService.get_all(getPreString("token"), ids);
	    }
	    
	}

	/**
	 * 给HeadView填充数据 
	 */
	@Override
	public void setHeadViewContent(View headerView, int position) {
		if(position > -1){
			final MediaEntity entity = (MediaEntity) adapter.getGroup(position);
			CircleImageView headerImageView = (CircleImageView) headerView.findViewById(R.id.headerImageView);
			TextView perNameTextView = (TextView) headerView.findViewById(R.id.perNameTextView);
			TextView locationTextView = (TextView) headerView.findViewById(R.id.locationTextView);
			TextView likeTextView = (TextView) headerView.findViewById(R.id.likeTextView);
			
			ImageLoader.getInstance().displayImage(entity.getAvatar(), headerImageView ,ImageConfig.getHeaderConfig());
			locationTextView.setText(entity.getLocation());
			perNameTextView.setText(entity.getNickname());
			likeTextView.setText(entity.getLike()+"");
		}
	}
	
	/**
	 * 
	 */
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MediaGetService.GET_MEDIA_ALL:{
				switch (msg.arg1) {
				case MediaGetService.GET_MEDIA_ALL_SUCCESS:
					String root = (String) msg.obj;
					if(TextUtils.isEmpty(root)){
						return;
					}
					System.err.println(root);
					ArrayList<MediaEntity> mediaEntities = AnalyseMediaJson.getAllMedia(root, Constant.MAPLIST);
					if(!mediaEntities.isEmpty()){
						adapter.clear();
						adapter.setMediaEntities(mediaEntities, pListView);
						title.setText(mediaEntities.get(0).getLocation());
						pListView.onLoadMoreComplete();
						mediaEntities.clear();
					}
					break;
				case MediaGetService.ERROR_CODE400:
					
					break;
				case MediaGetService.ERROR_CODE401:
					tokenInvalid();
					break;
				default:
					PanoToast.showToast(MapListActivity.this, "No Internet Connection.");
					break;
				}
			}
			break;
			}
		}
	};
	
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

	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		adapter.destoryReceiver();
	}
	
}
