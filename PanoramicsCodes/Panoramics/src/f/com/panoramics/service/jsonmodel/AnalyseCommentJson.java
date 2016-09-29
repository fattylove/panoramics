package f.com.panoramics.service.jsonmodel;

import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONObject;

import f.com.panoramics.entity.CommentEntity;

/**
 * 
 * @author Fatty
 *
 */
public class AnalyseCommentJson {

	/**
	 * 解析CommentEntity列表
	 * 
	 * @param root
	 * @return
	 */
	public static LinkedList<CommentEntity> getAllComments(String root){
		LinkedList<CommentEntity> commentEntities = new LinkedList<CommentEntity>();
		try {
			JSONObject rootJsonObject = new JSONObject(root);
			JSONArray data = rootJsonObject.getJSONArray("data");
			if(data.length() > 0){
				for(int j =0 ;j < data.length() ; j++){
					CommentEntity commentEntity = new CommentEntity();
					JSONObject comment = data.getJSONObject(j);
					
					JSONObject from = comment.getJSONObject("from");
					String accountId = from.getString("id");
					String nickname = from.getString("nickname");
					String username = from.getString("username");
					String profile_picture = from.getString("profile_picture");
					String text = comment.getString("text");
					String id = comment.getString("id");
					long createTime = comment.getLong("created_at");
					
					commentEntity.setCommentId(id);
					commentEntity.setAccountId(accountId);
					commentEntity.setNickname(nickname);
					commentEntity.setUsername(username);
					commentEntity.setAvatar(profile_picture);
					commentEntity.setText(text);
					commentEntity.setCreateTime(createTime);
					
					commentEntities.add(commentEntity);
				}
				return commentEntities;
			}else{
				return commentEntities;
			}
			
		} catch (Exception e) {
			System.err.println("COMMENT JSON ERROR:"+e.toString());
			return commentEntities;
		}
	}
}
