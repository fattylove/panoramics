package f.com.panoramics.constant;

public class ServerUrls {

	//foursquare api
	public static final String Base_foursquare = "https://api.foursquare.com/v2/venues/search";
	
	//Base
	public static final String BASE = "http://ec2-54-85-45-250.compute-1.amazonaws.com/";
	
	// users
	public static final String USER =     BASE  + "users";
	
	//accounts
	public static final String REGISTER = BASE + "accounts";
	
	//accesstokens
	public static final String LOGIN =    BASE + "accesstokens";
	
	//media
	public static final String MEDIA =    BASE + "media";
	
	//popular
	public static final String HOT   =    MEDIA +"/popular";
	
	//search
	public static final String WORLD =    MEDIA +"/search";
	
	//recent
	public static final String NEWEST =   MEDIA +"/recent";
	
	//feed
	public static final String FEED =     USER+"/self/feed";
	
	/**
	 * 获取用户信息接口
	 * 
	 * @param uid
	 * @return
	 */
	public static String getAccountInfo(String uid ){
		return USER +"/" +uid;
	}
	
	/**
	 * 修改密码接口
	 * 
	 * @param uid
	 * @return
	 */
	public static String changePasword(String uid){
		return REGISTER +"/" +uid +"/pwd";
	}
	
	/**
	 * 获取个人中心Flow列表接口
	 * 
	 * @param uid
	 * @return
	 */
	public static String recentUrl(String uid){
		return BASE +"users" +"/" +uid +"/media/recent";
	}
	
	/**
	 * 分享图片接口
	 * 
	 * @param uid
	 * @return
	 */
	public static String sharePhoto(String uid){
		return BASE +"users" +"/" +uid +"/media";
	}
	
	/**
	 * 获取评论列表接口
	 * 
	 * @param mediaId
	 * @return
	 */
	public static String commentList(String mediaId){
		return MEDIA + "/"+ mediaId+"/comments" ;
	}
	
	/**
	 * 发送评论接口
	 * 
	 * @param mediaId
	 * @return
	 */
	public static String createComment(String mediaId){
		return MEDIA + "/"+ mediaId+"/comments" ;
	}
	
	/**
	 * 删除评论接口
	 * 
	 * @param mediaId
	 * @param commentID
	 * @return
	 */
	public static String deleteComment(String mediaId , String commentID){
		return MEDIA + "/"+ mediaId+"/comments/" +commentID;
	}
	
	/**
	 * 根据mediaId获取MediaEntity接口
	 * 
	 * @param mediaId
	 * @return
	 */
	public static String get_media(String mediaId ){
		return MEDIA +"/"+ mediaId;
	}
	
	/**
	 * Follow接口
	 * 
	 * @param yourUid
	 * @return
	 */
	public static String relationship(String yourUid){
		return USER +"/"+ yourUid +"/relationship";
	}
	
	/**
	 * like接口
	 * 
	 * @param uid
	 * @return
	 */
	public static String likeBy(String uid){
		return USER +"/"+ uid +"/liked-by";
	}
	
	/**
	 * 个人中心获取following接口
	 * 
	 * @param myUid
	 * @return
	 */
	public static String follows(String myUid){
		return USER +"/"+ myUid +"/follows";
	}
	
	/**
	 * 个人中心获取followers接口
	 * 
	 * @param myUid
	 * @return
	 */
	public static String followed_by(String myUid){
		return USER +"/"+ myUid +"/followed-by";
	}
	
	/**
	 * 个人中心获取likes列表
	 * 
	 * @param uid
	 * @return
	 */
	public static String liked(String uid){
		return USER +"/"+uid +"/media/likes";
	}
	
	/**
	 * like meida接口
	 * 
	 * @param mediaId
	 * @return
	 */
	public static String mediaLike(String mediaId){
		return MEDIA +"/"+mediaId +"/likes";
	}
	
	/**
	 * 删除Meida接口
	 * 
	 * @param mediaId
	 * @return
	 */
	public static String deleteMedia(String mediaId){
		return MEDIA +"/"+mediaId ;
	}

}
