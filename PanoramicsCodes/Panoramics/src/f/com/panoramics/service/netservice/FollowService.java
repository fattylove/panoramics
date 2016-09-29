package f.com.panoramics.service.netservice;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import f.com.panoramics.constant.Constant;
import f.com.panoramics.constant.ServerUrls;
import f.com.panoramics.utils.L;
import fatty.library.http.core.CallBack;
import fatty.library.http.core.HttpService;
import fatty.library.http.core.Parameters;

/**
 * 
 * @author Fatty
 *
 */
public class FollowService {


	public static final String STATE_ON = "on";
	public static final String STATE_OFF = "off";
	
	public static final String follow                = "follow";
	public static final String unfollow              = "unfollow";
	
	public static final  int RELATIONSHIP            = 78001;
	public static final int RELATIONSHIP_FOLLOW      = 78002;
	public static final int RELATIONSHIP_FOLLOWED_BY = 78003;
	
	public static final int FOLLOW_SUCCESS = 200;
	
	public static final int RELATIONSHIP_SUCCESS = 201;
	public static final int ERROR_CODE400 = 400;
	public static final int ERROR_CODE404= 404;
	public static final int ERROR_CODE503 = 503;
	
	private Handler handler ;
	
	/**
	 * 构造
	 * 
	 * @param handler
	 */
	public FollowService (Handler handler){
		this.handler = handler;
	}
	
	/**
	 * 去关注
	 * 
	 * @param yourUid
	 * @param token
	 * @param action
	 */
	public void relationship(String yourUid , String token , String action){
		Parameters parameters = new Parameters();
		parameters.put("action", action);
		parameters.put("access_token", token);
		
		L.debug(Constant.NETWORK_SERVICE, ServerUrls.relationship(yourUid) + "?" + parameters.toString());
		
		new HttpService().post(ServerUrls.relationship(yourUid), parameters, new CallBack<String>() {

			@Override
			public void onSuccess(String t, int code) {
				super.onSuccess(t, code);
				Message message = new Message();
				message.what = RELATIONSHIP;
				message.arg1 = RELATIONSHIP_SUCCESS;
				message.obj = t;
				handler.sendMessage(message);
			}

			@Override
			public void onFailure(Throwable t, int code, String strMsg) {
				super.onFailure(t, code, strMsg);
				Message message = new Message();
				message.what = RELATIONSHIP;
				message.arg1 = code;
				handler.sendMessage(message);
			}
		});
	}
	
	
	/**
	 * 
	 * 我Follow的人
	 * 
	 * @param yourUid
	 * @param token
	 * @param action
	 */
	public void follows(String myUid , String token, String min_timestamp ,String max_timestamp ){
		Parameters parameters = new Parameters();
		parameters.put("access_token", token);
		parameters.put("count", Constant.PAGE_COUNT+"");
		if(!TextUtils.isEmpty(min_timestamp)){
			parameters.put("min_timestamp", min_timestamp);
		}
		if(!TextUtils.isEmpty(max_timestamp)){
			parameters.put("max_timestamp", max_timestamp);
		}

		L.debug(Constant.NETWORK_SERVICE, ServerUrls.follows(myUid) + "?" + parameters.toString());
		
		new HttpService().get(ServerUrls.follows(myUid), parameters, new CallBack<String>() {

			@Override
			public void onSuccess(String t, int code) {
				super.onSuccess(t, code);
				Message message = new Message();
				message.what = RELATIONSHIP_FOLLOW;
				message.arg1 = FOLLOW_SUCCESS;
				message.obj = t;
				handler.sendMessage(message);
			}

			@Override
			public void onFailure(Throwable t, int code, String strMsg) {
				super.onFailure(t, code, strMsg);
				Message message = new Message();
				message.what = RELATIONSHIP_FOLLOW;
				message.arg1 = code;
				handler.sendMessage(message);
			}
		});
	}
	
	/**
	 * 
	 * Follow过我的人
	 * 
	 * @param yourUid
	 * @param token
	 * @param action
	 */
	public void followed_by(String myUid , String token , String min_timestamp ,String max_timestamp ){
		Parameters parameters = new Parameters();
		parameters.put("access_token", token);
		parameters.put("count", Constant.PAGE_COUNT+"");
		if(!TextUtils.isEmpty(min_timestamp)){
			parameters.put("min_timestamp", min_timestamp);
		}
		if(!TextUtils.isEmpty(max_timestamp)){
			parameters.put("max_timestamp", max_timestamp);
		}
		
		L.debug(Constant.NETWORK_SERVICE, ServerUrls.followed_by(myUid) + "?" + parameters.toString());
		
		new HttpService().get(ServerUrls.followed_by(myUid), parameters, new CallBack<String>() {

			@Override
			public void onSuccess(String t, int code) {
				super.onSuccess(t, code);
				Message message = new Message();
				message.what = RELATIONSHIP_FOLLOWED_BY;
				message.arg1 = FOLLOW_SUCCESS;
				message.obj = t;
				handler.sendMessage(message);
			}

			@Override
			public void onFailure(Throwable t, int code, String strMsg) {
				super.onFailure(t, code, strMsg);
				Message message = new Message();
				message.what = RELATIONSHIP_FOLLOWED_BY;
				message.arg1 = code;
				handler.sendMessage(message);
			}
		});
	}
	
}
