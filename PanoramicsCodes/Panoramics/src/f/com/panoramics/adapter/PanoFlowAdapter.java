package f.com.panoramics.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guxiu.panoramics.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import f.com.panoramics.activity.ShowCommentsActivity;
import f.com.panoramics.constant.Constant;
import f.com.panoramics.entity.MediaEntity;
import f.com.panoramics.receiver.DataChangeReceiver;
import f.com.panoramics.receiver.RemoveMediaReceiver;
import f.com.panoramics.service.netservice.LikeService;
import f.com.panoramics.utils.ImageConfig;
import f.com.panoramics.utils.IntervalUtil;
import f.com.panoramics.view.FloatHeaderListView;

/**
 * 
 * @author Fatty
 *
 */
public class PanoFlowAdapter extends BaseExpandableListAdapter {

	private final LayoutInflater inflater;
	private final Activity mActivity;

	private ArrayList<MediaEntity> mediaEntities = new ArrayList<MediaEntity>();
	private SharedPreferences preferences;
	
	private DataChangeReceiver dataChangeReceiver ;
	private RemoveMediaReceiver removeMediaReceiver;
	private MediaEntity pickMediaEntity;

	public void destoryReceiver(){
		if(dataChangeReceiver!=null){
			mActivity.unregisterReceiver(dataChangeReceiver);
		}
		if(removeMediaReceiver!=null){
			mActivity.unregisterReceiver(removeMediaReceiver);
		}
	}
	
	/**
	 * 
	 * @param activity
	 */
	public PanoFlowAdapter(Activity activity) {
		inflater = LayoutInflater.from(activity);
		this.mActivity = activity;
		preferences = mActivity.getSharedPreferences(Constant.APP_NAME, Context.MODE_PRIVATE);

		dataChangeReceiver = new DataChangeReceiver(this);
		removeMediaReceiver = new RemoveMediaReceiver(this);
		activity.registerReceiver(dataChangeReceiver, new IntentFilter(DataChangeReceiver.ACTION));
		activity.registerReceiver(removeMediaReceiver, new IntentFilter(RemoveMediaReceiver.ACTION));
	}
	
	/**
	 * 
	 * @param mediaEntities
	 * @param floatHeaderListView
	 */
	public void setMediaEntities(ArrayList<MediaEntity> mediaEntities , FloatHeaderListView floatHeaderListView) {
		this.mediaEntities.addAll(mediaEntities);
		for (int i = 0 ; i < this.mediaEntities.size() ;  i++) {
			floatHeaderListView.expandGroup(i);
        }
		this.notifyDataSetChanged();
	}
	
	public void clear(){
		ImageLoader.getInstance().clearMemoryCache();
		this.mediaEntities.clear();
		this.notifyDataSetChanged();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		if(groupPosition == mediaEntities.size()){
			return 0;
		}
		return 1;
	}

	@Override
	public int getGroupCount() {
		return mediaEntities.size();
	}
	
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return mediaEntities.get(groupPosition);
	}

	@Override
	public Object getGroup(int groupPosition) {
		return mediaEntities.get(groupPosition);
	}
	
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return groupPosition;
	}
	
	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}
	
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		final GroupHolder groupHolder ;
		if (convertView == null) {
			groupHolder = new GroupHolder();
			convertView = inflater.inflate(R.layout.panoramics_pcenter_flow_group_item_layout, parent , false);
			groupHolder.timeTextView = (TextView) convertView.findViewById(R.id.timeTextView);
			groupHolder.locationTextView = (TextView) convertView.findViewById(R.id.locationTextView);
			groupHolder.likeTextView = (TextView) convertView.findViewById(R.id.likeTextView);
			groupHolder.likeImageView = (ImageView)convertView.findViewById(R.id.likeImageView);
			groupHolder.itemLayout = (RelativeLayout)convertView.findViewById(R.id.itemLayout);
			
			groupHolder.likeLayout = (LinearLayout)convertView.findViewById(R.id.likeLayout);
			convertView.setTag(groupHolder);
		} else {
			groupHolder = (GroupHolder) convertView.getTag();
		}

		final MediaEntity entity = (MediaEntity)  getGroup(groupPosition);
		groupHolder.locationTextView.setText(entity.getLocation());
		groupHolder.likeTextView.setText(entity.getLike()+"");
		groupHolder.timeTextView.setText(IntervalUtil.getInterval(entity.getP_time()));
		
		groupHolder.itemLayout.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
			}
		});
		
		final int state = entity.getLikeState();
		if(state == Constant.UNLIKED){
			groupHolder.likeImageView.setBackgroundResource(R.drawable.p_like_off);
			groupHolder.likeImageView.setTag(Constant.UNLIKED);
		}else if(state == Constant.LIKED){
			groupHolder.likeImageView.setBackgroundResource(R.drawable.p_like_on);
			groupHolder.likeImageView.setTag(Constant.LIKED);
		}
		groupHolder.likeImageView.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Integer likeState = (Integer) v.getTag();
				LikeService likeService = new LikeService(handler);
				if(likeState == Constant.UNLIKED){
					int count = entity.getLike();
					//post
					v.setTag(Constant.LIKED);
					v.setBackgroundResource(R.drawable.p_like_on);
					likeService.postLike(preferences.getString("token", ""), entity.getMediaId());
					
					groupHolder.likeTextView.setText((count++) + "");
					
					entity.setLike(count++);
					entity.setLikeState(Constant.LIKED);
					int index = mediaEntities.indexOf(entity);
					mediaEntities.set(index, entity);
					PanoFlowAdapter.this.notifyDataSetChanged();
					
				}else if(likeState == Constant.LIKED){
					int count = entity.getLike();
					//remove
					v.setTag(Constant.UNLIKED);
					v.setBackgroundResource(R.drawable.p_like_off);
					likeService.deleteLike(preferences.getString("token", ""), entity.getMediaId());
					
					groupHolder.likeTextView.setText((count--)+"");
					
					entity.setLike(count--);
					entity.setLikeState(Constant.UNLIKED);
					int index = mediaEntities.indexOf(entity);
					mediaEntities.set(index, entity);
					PanoFlowAdapter.this.notifyDataSetChanged();
				}
			}
		});
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		final ChildHolder holder ;
		View view = convertView;
		if (view == null) {
			view = inflater.inflate(R.layout.panoramics_pcenter_flow_child_item_layout, parent, false);
			holder = new ChildHolder();
			assert view != null;
			holder.item_img = (ImageView) view.findViewById(R.id.item_img);
			holder.progressBar = (ProgressBar)view.findViewById(R.id.progress);
			Drawable draw=mActivity.getResources().getDrawable(R.drawable.p_myprogressbar_style);
			holder.progressBar.setProgressDrawable(draw);
			view.setTag(holder);
		} else {
			holder = (ChildHolder) view.getTag();
			holder.progressBar.setVisibility(View.GONE);
			holder.item_img.setImageBitmap(null);
		}
		
		final MediaEntity mediaEntity = (MediaEntity) getChild(groupPosition, childPosition);
		
		ImageLoader.getInstance().displayImage(mediaEntity.getLow_resolution(), holder.item_img, ImageConfig.getListPhotoConfig(), new SimpleImageLoadingListener() {
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				holder.progressBar.setProgress(0);
				holder.progressBar.setVisibility(View.VISIBLE);
			}
			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
				holder.progressBar.setVisibility(View.GONE);
			}
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				holder.progressBar.setVisibility(View.GONE);
			}
		}, new ImageLoadingProgressListener() {
			@Override
			public void onProgressUpdate(String imageUri, View view, int current, int total) {
				holder.progressBar.setProgress(Math.round(100.0f * current / total));
			}
		});
		
		holder.item_img.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				pickMediaEntity = mediaEntity;
				dataChangeReceiver.setData(pickMediaEntity, mediaEntities);
				removeMediaReceiver.setData(pickMediaEntity, mediaEntities);
				
				Intent imgIntent = new Intent();
				imgIntent.putExtra("mediaId", mediaEntity.getMediaId());
				imgIntent.setClass(mActivity, ShowCommentsActivity.class);
				mActivity.startActivity(imgIntent);
			}
		});
		return view;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	
	/**********************/
	static class GroupHolder {
		public TextView locationTextView;
		public TextView timeTextView;
		public TextView likeTextView;
		
		public ImageView likeImageView;
		
		public RelativeLayout itemLayout;
		public LinearLayout likeLayout;
	}
	
	static class ChildHolder {
		ImageView item_img;
		ProgressBar progressBar;
	}
	
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case LikeService.LIKE_POST:{
				switch (msg.arg1) {
				case LikeService.LIKE_POST_SUCCESS:
					break;
				case LikeService.ERROR_CODE400:
					break;
				case LikeService.ERROR_CODE401:
					break;
				case LikeService.ERROR_CODE409:
					break;
				default:
					break;
				}
			}
			break;
			case LikeService.LIKE_REMOVE:{
				switch (msg.arg1) {
				case LikeService.LIKE_REMOVE_SUCCESS:
					break;
				case LikeService.ERROR_CODE400:
					break;
				case LikeService.ERROR_CODE401:
					break;
				case LikeService.ERROR_CODE409:
					break;
				default:
					break;
				}
			}
			break;
			}
		}
	};
}
