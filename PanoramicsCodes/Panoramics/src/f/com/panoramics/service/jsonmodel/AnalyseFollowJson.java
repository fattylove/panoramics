package f.com.panoramics.service.jsonmodel;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import f.com.panoramics.entity.FollowEntity;

/**
 * 
 * @author Fatty
 *
 */
public class AnalyseFollowJson {

	/**
	 * 解析FollowEntity列表
	 * 
	 * @param root
	 * @return
	 */
	public static ArrayList<FollowEntity> getAllFollows(String root){
		ArrayList<FollowEntity> followEntities = new ArrayList<FollowEntity>();
		try {
			JSONObject rootJsonObject = new JSONObject(root);
			JSONArray dataArr = rootJsonObject.getJSONArray("data");
			if(dataArr.length() > 0){
				for(int i = 0 ; i<dataArr.length() ; i++  ){
					FollowEntity followEntity = new FollowEntity();
					
					JSONObject data = dataArr.getJSONObject(i);
					String username = data.getString("username");
					String nickname = data.getString("nickname");
					String profile_picture = data.getString("profile_picture");
					String id = data.getString("id");
					String tag = data.getString("tag");
					String bio = data.getString("bio");
					long created_at = data.getLong("created_at");
					
					followEntity.setAccountId(id);
					followEntity.setUsername(username);
					followEntity.setNickname(nickname);
					followEntity.setProfile_picture(profile_picture);
					followEntity.setTag(tag);
					followEntity.setBio(bio);
					followEntity.setCreateTime(created_at);
					
					followEntities.add(followEntity);
				}
				return followEntities;
			}else{
				return followEntities;
			}
		} catch (Exception e) {
			System.err.println("FOLLOW JSON ERROR:"+e.toString());
			return followEntities;
		}
	}
}
