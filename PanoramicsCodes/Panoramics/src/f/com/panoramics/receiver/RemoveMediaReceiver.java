package f.com.panoramics.receiver;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.BaseExpandableListAdapter;
import f.com.panoramics.entity.MediaEntity;

public class RemoveMediaReceiver extends BroadcastReceiver {
	
	//Action
	public static final String ACTION = "com.guxiu.action.REMOVE_MEDIA";
	
	private MediaEntity pickMediaEntity;
	private ArrayList<MediaEntity> mediaEntities;
	private BaseExpandableListAdapter baseExpandableListAdapter;
	
	
	public RemoveMediaReceiver(  BaseExpandableListAdapter baseExpandableListAdapter ){
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
				mediaEntities.remove(pickMediaEntity);
				baseExpandableListAdapter.notifyDataSetChanged();
			}
		}
	}

}
