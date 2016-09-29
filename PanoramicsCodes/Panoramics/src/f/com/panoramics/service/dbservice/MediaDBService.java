package f.com.panoramics.service.dbservice;

import java.util.ArrayList;

import android.content.Context;
import f.com.panoramics.constant.Constant;
import f.com.panoramics.entity.MediaEntity;
import fatty.library.sqlite.core.SQLService;

/**
 * 
 * @author Fatty
 *
 */
public class MediaDBService {
	private final SQLService service;

	public MediaDBService(Context context) {
		service = SQLService.create(context, true);
	}
	
	/**
	 * 存储Media数据
	 * 
	 * @param mediaEntities
	 */
	public void saveMediaEntities(ArrayList<MediaEntity> mediaEntities) {
		if(mediaEntities.size()>0){
			for(MediaEntity mediaEntity  : mediaEntities){
				MediaEntity findMediaEntity = findMeidaEntity(mediaEntity);
				if(findMediaEntity!=null){
					update(mediaEntity);
				}else{
					save(mediaEntity);
				}
			}
		}
	}
	
	/**
	 * save mediaEntity
	 * 
	 * @param mediaEntity
	 */
	private void save(MediaEntity mediaEntity) {
		service.save(mediaEntity);
	}
	
	/**
	 * update meidaEntity
	 * 
	 * @param mediaEntity
	 */
	private void update(MediaEntity mediaEntity) {
		service.update(mediaEntity);
	}

	/**
	 * find mediaEntity
	 * 
	 * @param mediaEntity
	 * @return
	 */
	public MediaEntity findMeidaEntity(MediaEntity mediaEntity){
		String sql = "select * from panoramics_media_table where createTime=" + mediaEntity.getCreateTime() + " and stateTag=" + mediaEntity.getStateTag() + " and mediaId='" + mediaEntity.getMediaId()+"'";
		ArrayList<MediaEntity> mediaEntities  =  (ArrayList<MediaEntity>) service.findAllBySql(MediaEntity.class, sql);
		return mediaEntities.size() <= 0 ? null : mediaEntities.get(0);
	}
	
	/**
	 * find mediaEntities
	 * 
	 * @param createTime
	 * @param stateTag
	 * @return
	 */
	public ArrayList<MediaEntity> findMediaEntities(long createTime , int stateTag ){
		String sql = "select * from panoramics_media_table where createTime>"+createTime+ " and stateTag=" + stateTag + " order by createTime desc limit "+Constant.PAGE_COUNT;
		return (ArrayList<MediaEntity>) service.findAllBySql(MediaEntity.class, sql);
	}
	
	/**
	 * 
	 * @param createTime
	 * @param stateTag
	 * @return
	 */
	public ArrayList<MediaEntity> findMediaEntities(long createTime , int stateTag , String accountId){
		String sql = "select * from panoramics_media_table where createTime>"+createTime+ " and stateTag=" + stateTag + " and accountId="+accountId+" order by createTime desc limit "+Constant.PAGE_COUNT;
		return (ArrayList<MediaEntity>) service.findAllBySql(MediaEntity.class, sql);
	}
	
	/**
	 * delete mediaEntity by stateTag
	 * 
	 * @param stateTag
	 */
	public void deleteMediaEntity(int stateTag){
		service.deleteByWhere(MediaEntity.class, "stateTag=" + stateTag);
	}

}
