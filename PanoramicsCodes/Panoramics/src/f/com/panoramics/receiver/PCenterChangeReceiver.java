package f.com.panoramics.receiver;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import f.com.panoramics.adapter.PanoItemAdapter;
import f.com.panoramics.entity.MediaEntity;

/**
 * 
 * 通过个人中心，EditProfile修改个人信息的名字和头像，广播更新List列表
 * 
 * @author Fatty
 *
 */
public class PCenterChangeReceiver extends BroadcastReceiver {

	private PanoItemAdapter panoItemAdapter;
	private ArrayList<MediaEntity> mediaEntities;
	private MediaEntity changeMediaEntity ;
	
	
	public PCenterChangeReceiver(PanoItemAdapter panoItemAdapter){
		this.panoItemAdapter = panoItemAdapter;
	}
	
	public void setData(ArrayList<MediaEntity> mediaEntities){
		this.mediaEntities = mediaEntities;
	}
	
	public void setChangeMediaEntity(MediaEntity mediaEntity){
		this.changeMediaEntity = mediaEntity;
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {

		for(MediaEntity mediaEntity : this.mediaEntities){
			if(TextUtils.equals(mediaEntity.getAccountId(), changeMediaEntity.getAccountId())){
				mediaEntity.setAvatar(changeMediaEntity.getAvatar());
				mediaEntity.setNickname(changeMediaEntity.getNickname());
				panoItemAdapter.notifyDataSetChanged();
			}
		}
		panoItemAdapter.clear();
	}

}
