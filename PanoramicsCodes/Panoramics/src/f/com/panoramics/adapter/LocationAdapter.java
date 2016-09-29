package f.com.panoramics.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guxiu.panoramics.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import f.com.panoramics.entity.LocationEntity;

/**
 * 
 * @author Fatty
 *
 */
public class LocationAdapter extends BaseAdapter {

	private final Context mContext;
	private final ArrayList<LocationEntity> locationEntities = new ArrayList<LocationEntity>();
	private boolean type;

	public LocationAdapter(Context context , boolean type) {
		this.mContext = context;
		this.type = type;
	}

	@Override
	public int getCount() {
		return locationEntities.size();
	}

	public void setEntities(ArrayList<LocationEntity> locationEntities) {
		this.locationEntities.addAll(locationEntities);
	}
	
	public void clear(){
		this.locationEntities.clear();
		this.notifyDataSetChanged();
	}

	@Override
	public Object getItem(int position) {
		return locationEntities.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewLocationHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewLocationHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.panoramics_change_location_item_layout, null);
			viewHolder.locationIconImageView = (ImageView)convertView.findViewById(R.id.locationIconImageView);
			viewHolder.locationEditText = (TextView)convertView.findViewById(R.id.locationEditText);
			viewHolder.titleLayout =(LinearLayout)convertView.findViewById(R.id.titleLayout);
			viewHolder.titleTextView =(TextView)convertView.findViewById(R.id.titleTextView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewLocationHolder) convertView.getTag();
			viewHolder.titleLayout.setVisibility(View.GONE);
		}
		LocationEntity entity = (LocationEntity) getItem(position);
		viewHolder.locationEditText.setText(entity.getName());
		if(!TextUtils.isEmpty(entity.getUrl())){
			ImageLoader.getInstance().displayImage(entity.getUrl(), viewHolder.locationIconImageView);
		}else{
			viewHolder.locationIconImageView.setImageResource(R.drawable.p_default_earth);
		}
		if(!type){
			switch (position) {
			case 0:
				viewHolder.titleLayout.setVisibility(View.VISIBLE);
				viewHolder.titleTextView.setText("GUESS YOUR IN:");
				break;
			case 1:
				viewHolder.titleLayout.setVisibility(View.VISIBLE);
				viewHolder.titleTextView.setText("OTHER PLACE:");
				break;
			}
		}
		
		return convertView;
	}
	
	static class ViewLocationHolder{
		public ImageView locationIconImageView;
		public TextView locationEditText;
		
		public LinearLayout titleLayout;
		public TextView titleTextView;
	}

}
