package f.com.panoramics.adapter;

import java.util.LinkedList;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.guxiu.panoramics.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import f.com.panoramics.activity.PersonalCenterActivity;
import f.com.panoramics.activity.SendCommnetActivity;
import f.com.panoramics.activity.ShowCommentsActivity;
import f.com.panoramics.base.ActivityManager;
import f.com.panoramics.constant.Constant;
import f.com.panoramics.entity.CommentEntity;
import f.com.panoramics.entity.MediaEntity;
import f.com.panoramics.utils.ImageConfig;
import f.com.panoramics.view.CircleImageView;

/**
 * 
 * @author Fatty
 * 
 * 评论列表适配
 *
 */
public class CommentAdapter extends BaseAdapter{

	private final Context mContext;
	private final LinkedList<CommentEntity> commentEntities = new LinkedList<CommentEntity>();
	private MediaEntity mediaEntity ;
	private SharedPreferences preferences;
	
	/**
	 * 
	 * @param context
	 * @param mediaEntity
	 */
	public CommentAdapter(Context context , MediaEntity mediaEntity) {
		this.mContext = context;
		this.mediaEntity = mediaEntity;
		preferences = mContext.getSharedPreferences(Constant.APP_NAME, Context.MODE_PRIVATE);
	}

	@Override
	public int getCount() {
		return commentEntities.size();
	}

	public void addComments(LinkedList<CommentEntity> entities) {
		this.commentEntities.addAll(entities);
		this.notifyDataSetChanged();
	}
	
	public void removeComment(CommentEntity entity){
		this.commentEntities.remove(entity);
		this.notifyDataSetChanged();
	}
	
	public void addComment(CommentEntity mediaEntity){
		this.commentEntities.addFirst(mediaEntity);
		this.notifyDataSetChanged();
	}
	
	public void clear(){
		this.commentEntities.clear();
		this.notifyDataSetChanged();
	}

	@Override
	public Object getItem(int position) {
		return commentEntities.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.panoramics_comment_item_layout, null);
			viewHolder.headerImageView = (CircleImageView)convertView.findViewById(R.id.headerImageView);
			viewHolder.nameTextView = (TextView)convertView.findViewById(R.id.nameTextView);
			viewHolder.comTextView = (TextView)convertView.findViewById(R.id.comTextView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		final CommentEntity entity = (CommentEntity)getItem(position);
		ImageLoader.getInstance().displayImage(entity.getAvatar() ,viewHolder.headerImageView ,ImageConfig.getHeaderConfig());
		if(TextUtils.isEmpty(entity.getNickname())){
			viewHolder.nameTextView.setText(entity.getUsername());
		}else{
			viewHolder.nameTextView.setText(entity.getNickname());
		}
		
		viewHolder.comTextView.setText(entity.getText());
		viewHolder.headerImageView.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(mContext, PersonalCenterActivity.class);
				intent.putExtra("accountId", entity.getAccountId());
				mContext.startActivity(intent);
			}
		});
		
		viewHolder.nameTextView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				goSendComment(entity.getNickname() , mediaEntity);
			}
		});
		viewHolder.comTextView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				goSendComment(entity.getNickname(), mediaEntity);
			}
		});
		viewHolder.comTextView.setOnLongClickListener(new OnLongClickListener() {
			public boolean onLongClick(View v) {
				
				ShowCommentsActivity showCommentsActivity = (ShowCommentsActivity)ActivityManager.getAppManager().currentActivity();
				if(preferences.getString("uid", "").equals(mediaEntity.getAccountId())){ 
					if(preferences.getString("uid", "").equals(entity.getAccountId())){
						showCommentsActivity.initActionSheet(0 ,entity);
					}else{
						showCommentsActivity.initActionSheet(1 ,entity);
					}
				}else{
					if(preferences.getString("uid", "").equals(entity.getAccountId())){
						showCommentsActivity.initActionSheet(0 ,entity);
					}else{
						showCommentsActivity.initActionSheet(2 ,entity);
					}
				}
				return true;
			}
		});
		return convertView;
	}
	
	/**
	 * 
	 * 去评论
	 * 
	 * @param nickname
	 * @param mediaEntity
	 */
	public void goSendComment(String nickname , MediaEntity mediaEntity){
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putSerializable("mediaEntity", mediaEntity);
		intent.putExtras(bundle);
		intent.putExtra("where", true);
		intent.setClass(mContext, SendCommnetActivity.class);
		mContext.startActivity(intent);
	}
	
	/**
	 * 
	 * @author Fatty
	 *
	 */
	static class ViewHolder{
		public CircleImageView headerImageView;
		public TextView nameTextView;
		public TextView comTextView;
	}
}
