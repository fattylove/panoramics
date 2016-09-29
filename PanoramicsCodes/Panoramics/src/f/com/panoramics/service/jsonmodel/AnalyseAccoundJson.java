package f.com.panoramics.service.jsonmodel;

import org.json.JSONObject;

import f.com.panoramics.entity.AccountEntity;

/**
 * 
 * @author Fatty
 *
 */
public class AnalyseAccoundJson {

	/**
	 * 解析Account
	 * 
	 * @param root
	 * @return
	 */
	public static AccountEntity getSingleAccount(String root , boolean isWho){
		AccountEntity accountEntity = new AccountEntity();
		try {
			JSONObject rootJsonObject = new JSONObject(root);
			JSONObject data = rootJsonObject.getJSONObject("data");

			JSONObject counts = data.getJSONObject("counts");
			int liked_by = counts.getInt("liked_by");
			int follows = counts.getInt("follows");
			int followed_by = counts.getInt("followed_by");

			String _id = data.getString("_id");
			String passport = data.getString("passport");
			String username = data.getString("username");
			String nickname = data.getString("nickname");
			String profile_picture = data.getString("profile_picture");
			String website = data.getString("website");
			String bio = data.getString("bio");
			String phone = data.getString("phone");
			String gender = data.getString("gender");
			
			if(!isWho){
				String relationship = data.getString("relationship");
				accountEntity.setFollowState(relationship);
			}

			//save account cache
			
			accountEntity.setAccountId(_id);
			accountEntity.setPassport(passport);
			accountEntity.setUsername(username);
			accountEntity.setNickname(nickname);
			accountEntity.setAvatar(profile_picture);
			accountEntity.setWebsite(website);
			accountEntity.setBio(bio);
			accountEntity.setPhone(phone);
			accountEntity.setGender(gender);
			
			accountEntity.setLiked_by(liked_by);
			accountEntity.setFollows(follows);
			accountEntity.setFollowed_by(followed_by);
			
			return accountEntity;
		} catch (Exception e) {
			System.err.println("ACCOUNT JSON ERROR:" + e.toString());
			return accountEntity;
		}
	}

}
