package f.com.panoramics.service.dbservice;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import f.com.panoramics.entity.CommentEntity;
import fatty.library.sqlite.core.SQLService;


/**
 * 
 * @author Fatty
 *
 */
public class CommentDBService {

	private final SQLService service;

	public CommentDBService(Context context) {
		service = SQLService.create(context, true);
	}

	/**
	 * 存储Comment数据
	 * 
	 * @param mediaEntities
	 */
	public void saveCommentEntities(List<CommentEntity> commentEntities) {
		if(commentEntities.size()>0){
			for(CommentEntity commentEntity  : commentEntities){
				CommentEntity findCommentEntity = findCommentEntity(commentEntity.getCommentId());
				if(findCommentEntity!=null){
					if(!TextUtils.equals(commentEntity.getCommentId(), findCommentEntity.getCommentId())){
						save(commentEntity);
					}else{
						update(commentEntity);
					}
				}else{
					save(commentEntity);
				}
			}
		}
	}
	
	/**
	 * 保存commentEntity
	 * 
	 * @param commentEntity
	 */
	private void save(CommentEntity commentEntity) {
		service.save(commentEntity);
	}
	
	/**
	 * 更新commentEntity
	 * 
	 * @param commentEntity
	 */
	private void update(CommentEntity commentEntity) {
		service.update(commentEntity);
	}

	/**
	 * 查找commentEntity
	 * 
	 * @param commentId
	 * @return
	 */
	public CommentEntity findCommentEntity(String commentId){
		return service.findById(commentId, CommentEntity.class);
	}
	
	/**
	 * 删除commentEntity
	 * 
	 * @param commentEntity
	 */
	public void delete(CommentEntity commentEntity){
		service.delete(commentEntity);
	}
}
