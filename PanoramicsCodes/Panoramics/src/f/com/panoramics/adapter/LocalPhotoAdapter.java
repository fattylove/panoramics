package f.com.panoramics.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.guxiu.panoramics.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import f.com.panoramics.entity.PanoramicsEntity;
import f.com.panoramics.utils.ImageConfig;

/**
 * 
 * 本地图片获取列表适配
 * 
 * @author Fatty
 *
 */
public class LocalPhotoAdapter extends BaseAdapter {

	private final Context mContext;
	private final ArrayList<PanoramicsEntity> panoramicsEntities = new ArrayList<PanoramicsEntity>();

	public LocalPhotoAdapter(Context context) {
		this.mContext = context;
	}

	@Override
	public int getCount() {
		return panoramicsEntities.size();
	}

	public void setPanoramicsEntities(ArrayList<PanoramicsEntity> panoramicsEntities) {
		this.panoramicsEntities.addAll(panoramicsEntities);
	}
	
	public void clear(){
		this.panoramicsEntities.clear();
		this.notifyDataSetChanged();
	}

	@Override
	public Object getItem(int position) {
		return panoramicsEntities.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder ;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.panoramics_pan_item_layout, null);
			viewHolder.panImageView = (ImageView)convertView.findViewById(R.id.panImageView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
			viewHolder.panImageView.setImageBitmap(null);
		}
		
		PanoramicsEntity panoramicsEntity = (PanoramicsEntity) getItem(position);
		ImageLoader.getInstance().displayImage("file:///" + panoramicsEntity.getImgPath(), viewHolder.panImageView, ImageConfig.getLocalPhotoConfig());
		
		return convertView;
	}
	
	static class ViewHolder{
		public ImageView panImageView;
	}
	
	
}
