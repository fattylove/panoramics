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
public class LikeService {
	
	private Handler handler;
	
	public static final int LIKE_LIST = 90101;
	public static final int LIKE_POST = 90102;
	public static final int LIKE_REMOVE = 90103;
	public static final int LIKED_BY = 90104;
	
	public static final int LIKE_LIST_SUCCESS = 200;
	public static final int LIKE_POST_SUCCESS = 201;
	public static final int LIKE_REMOVE_SUCCESS = 200;
	public static final int LIKED_BY_SUCCESS = 200;
	
	public static final int ERROR_CODE400 = 400;
	public static final int ERROR_CODE401 = 401;
	public static final int ERROR_CODE404 = 404;
	public static final int ERROR_CODE409 = 409;
	
	
	/**
	 * 构造
	 * 
	 * @param handler
	 */
	public LikeService(Handler handler){
		this.handler = handler;
	}
	
	/**
	 * 
	 * 获取那些人Like了这个Media
	 * 
	 * @param mediaId
	 * @param token
	 * @param max_timestamp
	 * @param min_timestamp
	 */
	public void like_list(String mediaId ,String token ,String max_timestamp ,String min_timestamp ){
		Parameters parameters = new Parameters();
		parameters.put("access_token", token);
		parameters.put("count", Constant.PAGE_COUNT+"");
		
		if(!TextUtils.isEmpty(min_timestamp)){
			parameters.put("min_timestamp", min_timestamp);
		}
		
		if(!TextUtils.isEmpty(max_timestamp)){
			parameters.put("max_timestamp", max_timestamp);
		}
		
		L.debug(Constant.NETWORK_SERVICE, ServerUrls.mediaLike(mediaId) + "?" + parameters.toString());
		
		new HttpService().get(ServerUrls.mediaLike(mediaId), parameters, new CallBack<String>() {
			@Override
			public void onSuccess(String t, int code) {
				super.onSuccess(t, code);
				Message message = new Message();
				message.what = LIKE_LIST;
				message.arg1 = LIKE_LIST_SUCCESS;
				message.obj = t;
				handler.sendMessage(message);
			}

			@Override
			public void onFailure(Throwable t, int code, String strMsg) {
				super.onFailure(t, code, strMsg);
				Message message = new Message();
				message.what = LIKE_LIST;
				message.arg1 = code;
				handler.sendMessage(message);
			}
		});
	}
	
	
	/**
	 * Like Media
	 * 
	 * @param token
	 * @param mediaId
	 */
	public void postLike(String token , String mediaId){
		Parameters parameters = new Parameters();
		parameters.put("access_token", token);
		
		L.debug(Constant.NETWORK_SERVICE, ServerUrls.mediaLike(mediaId) + "?" + parameters.toString());
		
		new HttpService().post(ServerUrls.mediaLike(mediaId), parameters, new CallBack<String>() {
			@Override
			public void onSuccess(String t, int code) {
				super.onSuccess(t, code);
				Message message = new Message();
				message.what = LIKE_POST;
				message.arg1 = LIKE_POST_SUCCESS;
				message.obj = t;
				handler.sendMessage(message);
			}

			@Override
			public void onFailure(Throwable t, int code, String strMsg) {
				super.onFailure(t, code, strMsg);
				Message message = new Message();
				message.what = LIKE_POST;
				message.arg1 = code;
				handler.sendMessage(message);
			}
		});
	}
	
	/**
	 * 删除Like
	 * 
	 * @param token
	 * @param mediaId
	 */
	public void deleteLike(String token , String mediaId){
		
		L.debug(Constant.NETWORK_SERVICE, ServerUrls.mediaLike(mediaId)+"?access_token="+token );
		
		new HttpService().delete(ServerUrls.mediaLike(mediaId)+"?access_token="+token, new CallBack<String>() {
			@Override
			public void onSuccess(String t, int code) {
				super.onSuccess(t, code);
				Message message = new Message();
				message.what = LIKE_REMOVE;
				message.arg1 = LIKE_REMOVE_SUCCESS;
				message.obj = t;
				handler.sendMessage(message);
			}

			@Override
			public void onFailure(Throwable t, int code, String strMsg) {
				super.onFailure(t, code, strMsg);
				Message message = new Message();
				message.what = LIKE_REMOVE;
				message.arg1 = code;
				handler.sendMessage(message);
			}
		});
	}
	
	/**
	 *  获取Like我的人的列表
	 * 
	 * @param token
	 * @param uid
	 * @param max_timestamp
	 * @param min_timestamp
	 */
	public void likeByList(String token , String uid ,String max_timestamp,String min_timestamp){
		
		Parameters parameters = new Parameters();
		parameters.put("access_token", token);
		parameters.put("count", Constant.PAGE_COUNT+"");
		if(!TextUtils.isEmpty(max_timestamp)){
			parameters.put("max_timestamp", max_timestamp);
		}
		if(!TextUtils.isEmpty(min_timestamp)){
			parameters.put("min_timestamp", min_timestamp);
		}
		
		L.debug(Constant.NETWORK_SERVICE, ServerUrls.likeBy(uid)+ "?" + parameters.toString());
		
		new HttpService().get(ServerUrls.likeBy(uid), parameters, new CallBack<String>() {

			@Override
			public void onSuccess(String t, int code) {
				super.onSuccess(t, code);
				Message message = new Message();
				message.what = LIKED_BY;
				message.arg1 = LIKED_BY_SUCCESS;
				message.obj = t;
				handler.sendMessage(message);
			}

			@Override
			public void onFailure(Throwable t, int code, String strMsg) {
				super.onFailure(t, code, strMsg);
				handler.sendEmptyMessage(code);
				Message message = new Message();
				message.what = LIKED_BY;
				message.arg1 = code;
				handler.sendMessage(message);
			}
		});
	}


}
