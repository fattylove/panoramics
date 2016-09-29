package f.com.panoramics.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.guxiu.panoramics.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import f.com.panoramics.activity.PersonalCenterActivity;
import f.com.panoramics.constant.Constant;
import f.com.panoramics.entity.FollowEntity;
import f.com.panoramics.service.netservice.FollowService;
import f.com.panoramics.utils.ImageConfig;
import f.com.panoramics.view.ActionSheetFollowDialog;
import f.com.panoramics.view.ActionSheetFollowDialog.OnActionSheetSelectedListener;
import f.com.panoramics.view.CircleImageView;
import f.com.panoramics.view.PanoToast;

/**
 * 
 * @author Fatty
 *
 * Following Followers 列表适配
 * 
 */
public class FollowAdapter extends BaseAdapter implements OnActionSheetSelectedListener{

	private Activity mContext;
	private ArrayList<FollowEntity> followEntities = new ArrayList<FollowEntity>();
	private SharedPreferences preferences;
	private ActionSheetFollowDialog actionSheet;

	private FollowEntity pickFollowEntity;
	private static View pickView;
	
	public FollowAdapter(Activity context) {
		this.mContext = context;
		preferences = mContext.getSharedPreferences(Constant.APP_NAME, Context.MODE_PRIVATE);
		
		actionSheet = new ActionSheetFollowDialog(mContext);
		actionSheet.setOnActionSheetSelectedListener(this);
	}

	@Override
	public int getCount() {
		return followEntities.size();
	}

	public void addFollow(ArrayList<FollowEntity> entities) {
		this.followEntities.addAll(entities);
		this.notifyDataSetChanged();
	}
	
	public void clear(){
		this.followEntities.clear();
		this.notifyDataSetChanged();
	}

	@Override
	public Object getItem(int position) {
		return followEntities.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.panoramics_follow_item_layout, null);
			viewHolder.headerImageView = (CircleImageView)convertView.findViewById(R.id.headerImageView);
			viewHolder.nicknameTextView = (TextView)convertView.findViewById(R.id.nicknameTextView);
			viewHolder.bioTextview = (TextView)convertView.findViewById(R.id.bioTextview);
			viewHolder.followerBtn = (Button)convertView.findViewById(R.id.followerBtn);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
			viewHolder.followerBtn.setBackgroundResource(R.drawable.p_follow_1);
		}
		
		final FollowEntity entity = (FollowEntity)getItem(position);
		
		//set nickname
		if(!TextUtils.isEmpty(entity.getNickname())){
			viewHolder.nicknameTextView.setText(entity.getNickname());
		}else{
			viewHolder.nicknameTextView.setText(entity.getUsername());
		}
		//set bio
		if(!TextUtils.isEmpty(entity.getBio())){
			viewHolder.bioTextview.setText(entity.getBio());
		}else{
			viewHolder.bioTextview.setVisibility(View.GONE);
		}
		
		//set header img
		ImageLoader.getInstance().displayImage(entity.getProfile_picture(), viewHolder.headerImageView, ImageConfig.getHeaderConfig());
		
		//set follow Btn
		if(TextUtils.equals(preferences.getString("uid", ""), entity.getAccountId())){
			viewHolder.followerBtn.setVisibility(View.INVISIBLE);
		}else{
			viewHolder.followerBtn.setVisibility(View.VISIBLE);
		}
		
		if(entity.getTag().equals(FollowService.STATE_ON)){
			viewHolder.followerBtn.setTag(FollowService.STATE_ON);
			viewHolder.followerBtn.setBackgroundResource(R.drawable.p_follow_3);
		}else{
			viewHolder.followerBtn.setTag(FollowService.STATE_OFF);
			viewHolder.followerBtn.setBackgroundResource(R.drawable.p_follow_1);
		}
		
		viewHolder.followerBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String tag = (String) viewHolder.followerBtn.getTag();
				if(tag.equals(FollowService.STATE_ON)){
					pickView = viewHolder.followerBtn;
					pickFollowEntity = entity;
					if(TextUtils.isEmpty(entity.getNickname())){
						actionSheet.setNickname(entity.getUsername());
					}else{
						actionSheet.setNickname(entity.getNickname());
					}
					actionSheet.setHeaderImage(entity.getProfile_picture());
					actionSheet.show();
				}else if(tag.equals(FollowService.STATE_OFF)){
					follow(entity, FollowService.follow);
					viewHolder.followerBtn.setTag(FollowService.STATE_ON);
					viewHolder.followerBtn.setBackgroundResource(R.drawable.p_follow_3);
				}
			}
		});
		
		viewHolder.headerImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(mContext, PersonalCenterActivity.class);
				intent.putExtra("accountId", entity.getAccountId());
				mContext.startActivity(intent);
			}
		});
		
		return convertView;
	}
	
	class ViewHolder{
		public CircleImageView headerImageView;
		public TextView nicknameTextView;
		public TextView bioTextview;
		public Button followerBtn;
	}
	
	public void follow(FollowEntity followEntity , String action){
		FollowService followService = new FollowService(handler);
		followService.relationship(followEntity.getAccountId(), preferences.getString("token", ""), action);
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
					break;
				case FollowService.ERROR_CODE404:
					break;
				case FollowService.ERROR_CODE503:
					break;
				default:
					PanoToast.showToast(mContext, "No Internet Connection.");
					break;
				}
			}
			break;
			}
		}
	};

	@Override
	public void onClick(int whichButton) {
		switch (whichButton) {
		case 1:
			follow(pickFollowEntity, FollowService.unfollow);
			pickView.setTag(FollowService.STATE_OFF);
			
			int index = followEntities.indexOf(pickFollowEntity);
			pickFollowEntity.setTag(FollowService.unfollow);
			
			followEntities.set(index, pickFollowEntity);
			FollowAdapter.this.notifyDataSetChanged();
			
			pickView = null;
			pickFollowEntity = null;
			break;
		}
	}

}
