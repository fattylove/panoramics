package f.com.panoramics.service.jsonmodel;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.text.TextUtils;
import f.com.panoramics.constant.Constant;
import f.com.panoramics.entity.MediaEntity;

/**
 * 
 * @author Fatty
 *
 */
public class AnalyseMediaJson {

	/**
	 * 获取所有MediaEntity
	 * 
	 * @param root
	 * @param state
	 * @return
	 */
	public static ArrayList<MediaEntity> getAllMedia(String root , int state) {
		ArrayList<MediaEntity> mediaEntities = new ArrayList<MediaEntity>();
		try {
			JSONObject rootObject = new JSONObject(root);
			JSONArray dataArray = rootObject.getJSONArray("data");
			if (dataArray.length() > 0) {
				for (int i = 0; i < dataArray.length(); i++) {
					MediaEntity media = new MediaEntity();
					JSONObject arr = dataArray.getJSONObject(i);

					String media_id = arr.getString("_id");
					String type = arr.getString("type");
					String location_name = arr.getString("location_name");
					long createTimestamp = arr.getLong("created_at");

					JSONObject user = arr.getJSONObject("user");
					String uid = user.getString("id");
					String username = user.getString("username");
					String nickname = user.getString("nickname");
					String profile_picture = user.getString("profile_picture");

					JSONArray tagsArr = arr.getJSONArray("tags");

					JSONObject imagesObj = arr.getJSONObject("images");
					// low
					JSONObject low_resolution = imagesObj.getJSONObject("low_resolution");
					String low_url = low_resolution.getString("url");
					// standard
					JSONObject standard_resolution = imagesObj.getJSONObject("standard_resolution");
					String standard_url = standard_resolution.getString("url");
					// thumbnail
					JSONObject thumbnail = imagesObj.getJSONObject("thumbnail");
					String thumbnail_url = thumbnail.getString("url");

					JSONObject locationObject = arr.getJSONObject("location");
					JSONArray locationArr = locationObject.getJSONArray("coordinates");
					double lng = locationArr.getDouble(0);
					double lat = locationArr.getDouble(1);

					int liked_by_user = arr.getInt("liked_by_user");
					int likes_count = arr.getInt("likes_count");
					
					if(state == Constant.LIKE){
						long liked_at = arr.getLong("liked_at");
						media.setLikedTime(liked_at);
					}

					media.setLike(likes_count);
					media.setLikeState(liked_by_user);
					media.setAccountId(uid);
					media.setMediaId(media_id);
					media.setType(type);
					media.setLocation(location_name);
					media.setTags(tagsArr.toString());
					media.setLow_resolution(low_url);
					media.setStandard_resolution(standard_url);
					media.setThumbnail(thumbnail_url);
					media.setAvatar(profile_picture);
					if (!TextUtils.isEmpty(nickname)) {
						media.setNickname(nickname);
					} else {
						media.setNickname(username);
					}
					media.setP_time(createTimestamp);
					media.setCreateTime(createTimestamp);
					media.setLat(lat);
					media.setLng(lng);
					media.setStateTag(state);
					mediaEntities.add(media);
				}
				return mediaEntities;
			}else{
				return mediaEntities;
			}
		} catch (Exception e) {
			System.err.println("MEDIA JSON ERROR:" + e.toString());
			return mediaEntities;
		}
	}
	
	
	/**
	 * 获取单个MediaEntity
	 * 
	 * @param root
	 * @return
	 */
	public static MediaEntity getSingleMedia(String root ) {
		MediaEntity media = new MediaEntity();
		try {
			JSONObject rootObject = new JSONObject(root);
			JSONObject mediaObject = rootObject.getJSONObject("data");
			String media_id = mediaObject.getString("_id");
			String type = mediaObject.getString("type");
			String location_name = mediaObject.getString("location_name");
			long createTimestamp = mediaObject.getLong("created_at");
			
			//User
			JSONObject user = mediaObject.getJSONObject("user");
			String uid = user.getString("id");
			String username = user.getString("username");
			String nickname = user.getString("nickname");
			String profile_picture= user.getString("profile_picture");
			
			JSONArray tagsArr = mediaObject.getJSONArray("tags");
			
			JSONObject imagesObj = mediaObject.getJSONObject("images");
			//low
			JSONObject low_resolution = imagesObj.getJSONObject("low_resolution");
			String low_url = low_resolution.getString("url");
			//standard
			JSONObject standard_resolution = imagesObj.getJSONObject("standard_resolution");
			String standard_url = standard_resolution.getString("url");
			//thumbnail
			JSONObject thumbnail = imagesObj.getJSONObject("thumbnail");
			String thumbnail_url = thumbnail.getString("url");
			
			JSONObject locationObject = mediaObject.getJSONObject("location");
			JSONArray locationArr = locationObject.getJSONArray("coordinates");
			double lng = locationArr.getDouble(0);
			double lat = locationArr.getDouble(1);
			
//			int comments_count = mediaObject.getInt("comments_count");
			int liked_by_user = mediaObject.getInt("liked_by_user");
			int likes_count = mediaObject.getInt("likes_count");

			media.setLike(likes_count);
			media.setLikeState(liked_by_user);
			
			media.setAccountId(uid);
			media.setMediaId(media_id);
			media.setType(type);
			media.setLocation(location_name);
			media.setTags(tagsArr.toString());
			media.setLow_resolution(low_url);
			media.setStandard_resolution(standard_url);
			media.setThumbnail(thumbnail_url);
			media.setAvatar(profile_picture);
			if(!TextUtils.isEmpty(nickname)){
				media.setNickname(nickname);
			}else{
				media.setNickname(username);
			}
			media.setP_time(createTimestamp);
			media.setCreateTime(createTimestamp);
			media.setLat(lat);
			media.setLng(lng);
		} catch (Exception e) {
			System.err.println("MEDIA JSON ERROR:" + e.toString());
			return media;
		}
		return media;
	}
}
