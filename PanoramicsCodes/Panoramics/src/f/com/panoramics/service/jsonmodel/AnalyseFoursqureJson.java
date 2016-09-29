package f.com.panoramics.service.jsonmodel;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import f.com.panoramics.entity.LocationEntity;

/**
 * 
 * @author Fatty
 *
 */
public class AnalyseFoursqureJson {

	/**
	 * get all Foursqure data
	 * 
	 * @param content
	 * @return
	 */
	public static ArrayList<LocationEntity> get_all(String content){
		ArrayList<LocationEntity> locationEntities = new ArrayList<LocationEntity>();
		try {
			JSONObject root = new JSONObject(content);
			JSONObject response = root.getJSONObject("response");
			JSONArray venues = response.getJSONArray("venues");
			if(venues.length()>0){
				for(int i=0 ; i<venues.length() ; i ++){
					LocationEntity entity = new LocationEntity();
					JSONObject location = venues.getJSONObject(i);
					String name = location.getString("name");
					entity.setName(name);
					JSONArray categories = location.getJSONArray("categories");
					if(categories.length()>0){
						JSONObject iconContent = categories.getJSONObject(0);
						JSONObject icon = iconContent.getJSONObject("icon");
						String prefix = icon.getString("prefix");
						String suffix = icon.getString("suffix");
						String imgUrl = prefix+"32"+suffix;
						entity.setUrl(imgUrl);
					}
					JSONObject ll = location.getJSONObject("location"); 
					double ll_lat = ll.getDouble("lat");
					double ll_lng = ll.getDouble("lng");
					entity.setLat(ll_lat);
					entity.setLng(ll_lng);
					locationEntities.add(entity);
				}
			}
		} catch (Exception e) {
		}
		return locationEntities;
	}
	
	/**
	 * get one Foursqure data
	 * 
	 * @param content
	 * @return
	 */
	public static LocationEntity get_one(String content){
		LocationEntity locationEntity = new LocationEntity();
		try {
			JSONObject root = new JSONObject(content);
			JSONObject response = root.getJSONObject("response");
			JSONArray venues = response.getJSONArray("venues");
			JSONObject location = venues.getJSONObject(0);
			String name = location.getString("name");
			JSONArray categories = location.getJSONArray("categories");
			JSONObject iconContent = categories.getJSONObject(0);
			JSONObject icon = iconContent.getJSONObject("icon");
			String prefix = icon.getString("prefix");
			String suffix = icon.getString("suffix");
			String imgUrl = prefix+"32"+suffix;
			
			JSONObject ll = location.getJSONObject("location"); 
			double ll_lat = ll.getDouble("lat");
			double ll_lng = ll.getDouble("lng");
			
			locationEntity.setLat(ll_lat);
			locationEntity.setLng(ll_lng);
			locationEntity.setName(name);
			locationEntity.setUrl(imgUrl);
		} catch (Exception e) {
		}
		return locationEntity;
	}
	
	
}
