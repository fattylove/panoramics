package f.com.panoramics.receiver;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.BaseExpandableListAdapter;
import f.com.panoramics.entity.MediaEntity;

/**
 * 
 * @author Fatty
 *
 */
public class DataChangeReceiver extends BroadcastReceiver {

	//Action
	public static final String ACTION = "com.guxiu.action.RECEIVER";
	
	private MediaEntity pickMediaEntity;
	private ArrayList<MediaEntity> mediaEntities;
	private BaseExpandableListAdapter baseExpandableListAdapter;
	
	/**
	 * 
	 * @param baseExpandableListAdapter
	 */
	public DataChangeReceiver(BaseExpandableListAdapter baseExpandableListAdapter){
		this.baseExpandableListAdapter = baseExpandableListAdapter;
	}
	
	/**
	 * 
	 * @param pickMediaEntity
	 * @param mediaEntities
	 */
	public void setData(MediaEntity pickMediaEntity , ArrayList<MediaEntity> mediaEntities){
		this.pickMediaEntity = pickMediaEntity;
		this.mediaEntities = mediaEntities;
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		MediaEntity mediaEntity = (MediaEntity)intent.getSerializableExtra("mediaEntity");
		if(pickMediaEntity!=null){
			if(mediaEntities.contains(pickMediaEntity)){
				int index = mediaEntities.indexOf(pickMediaEntity);
				mediaEntities.set(index, mediaEntity);
				baseExpandableListAdapter.notifyDataSetChanged();
			}
		}
	}
	
	

}
