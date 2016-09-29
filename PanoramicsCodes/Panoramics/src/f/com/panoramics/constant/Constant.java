package f.com.panoramics.constant;

import java.io.File;

/**
 * 
 * @author Fatty
 *
 */
public class Constant {
	
	//Tags
	public static final String NETWORK_SERVICE="NETWORK_SERVICE";
	
	//分页count
	public static final int PAGE_COUNT = 20;
	
	//7种状态
	public static final int WORLD   = 1;
	public static final int HOT     = 2;
	public static final int NEWEST  = 3;
	public static final int FLOW    = 4;
	public static final int LIKE    = 5;
	public static final int MAPLIST = 6;
	public static final int FEED    = 7;
	
	//Like状态
	public static final int UNLIKED = 0;
	public static final int LIKED   = 1;
	
	//App packageName
	public static final String PACKAGE_NAME = "com.guxiu.panoramics";
	
	//App name
	public static final String APP_NAME = "PANORAMICS";

	//分割符
	public static final String P = File.separator;
	
	//文件缓存目录
	public static final String PANORAMICS = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + P + ".panoramics";
	public static final String CACHE = PANORAMICS + P + "Cache" + P;
	public static final String PIC   = PANORAMICS + P + "Pic"   + P;
	public static final String TEMP  = PANORAMICS + P + "Temp"  + P;
	public static final String MUSIC = PANORAMICS + P + "Music" + P;
	public static final String DATA  = PANORAMICS + P + "Data"  + P;
	public static final String SHARE = PANORAMICS + P + "Share" + P;
	
	//压缩图片尺寸
	public static final int PHOTO_WIDTH = 3000;
	public static final int PHOTO_HEIGHT = 1500;
	
	//Share的图片名字
	public static String SHAREIMAGE = PANORAMICS + "panoramics.jpg";
	
	// twitter keys
	public static final String twitter_consumer_key 		= "Cj5ANecoWZAiVX8qOP8H0VBxC";
	public static final String twitter_consumer_key_secret	= "kLhRDEJWdW0rhtGYZYknIoupSg6m5F28xUTgkprPO8k02izUJq";
	public static final String twitter_access_token 		= "1052093226-qmfcKsSNtJW0k1TvhPiH1gt3N3RZDmgr0V92soz";
	public static final String twitter_access_token_secret 	= "sjHDh6esv9jjGL0XQoQCXgRvXHnQuLTZ5IXsQqVd7KlUf";
	
	// facebook key
	public static final String FACEBOOK_APPLICATION_ID 		= "729273440490762";

	//foursquare keys
	public static final String foursquare_Client_id 	= "LD4VF1KSBLXFII4KRKZZ0NZWDYSO5MQEQTZAEZCJYS0W0SXX";
	public static final String foursquare_Client_secret = "XBLHFNQQOHBYGHR2AD1MYUWQQXKX4GIAPSRXTD0NYS5PLSE4";
	
	// GCM keys
	public static final String GCM_PROJECT_NUMBER = "998066304753";
	
	//AS3 keys
    public static final String AWS_ACCOUNT_ID = "281703765655";
    public static final String COGNITO_POOL_ID =  "us-east-1:134cbdd1-432c-45ee-bc31-b4da725f49c8";
    public static final String COGNITO_ROLE_UNAUTH =  "arn:aws:iam::281703765655:role/Cognito_PanoramicsUnauth_DefaultRole";
    public static final String COGNITO_ROLE_AUTH =  "arn:aws:iam::281703765655:role/Cognito_PanoramicsAuth_DefaultRole";
    public static final String BUCKET_NAME = "panoramixs";
}
