package f.com.panoramics.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.guxiu.panoramics.R;

import f.com.panoramics.entity.FollowEntity;
import f.com.panoramics.view.CircleImageView;

/**
 * 
 * @author Fatty
 *
 * Liked by 列表适配
 * 
 */
public class OAdapter extends BaseAdapter{

	private Activity mContext;
	private ArrayList<FollowEntity> followEntities = new ArrayList<FollowEntity>();
	
	public OAdapter(Activity context) {
		this.mContext = context;
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.panoramics_likedby_item_layout, null);
			viewHolder.headerImageView = (CircleImageView)convertView.findViewById(R.id.headerImageView);
			viewHolder.likedNicknameTextView = (TextView)convertView.findViewById(R.id.likedNicknameTextView);
			viewHolder.likedTagTextView = (TextView)convertView.findViewById(R.id.likedTagTextView);
			viewHolder.likebyDateTextView = (TextView)convertView.findViewById(R.id.likebyDateTextView);
			viewHolder.likedImageView = (ImageView)convertView.findViewById(R.id.likedImageView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		return convertView;
	}
	
	class ViewHolder{
		public CircleImageView headerImageView;
		public TextView likedNicknameTextView;
		public TextView likedTagTextView;
		public TextView likebyDateTextView;
		public ImageView likedImageView;
	}
	

}
