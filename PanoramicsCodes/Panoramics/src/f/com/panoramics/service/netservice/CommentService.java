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
public class CommentService {

	public static final int CREATE_COMMENT = 50001;
	public static final int LIST_COMMENT = 50002;
	public static final int DELETE_COMMENT = 50003;
	
	public static final int CREATE_COMMENT_SUCCESS = 201;
	public static final int LIST_COMMENT_SUCCESS = 200;
	public static final int DELETE_COMMENT_SUCCESS = 200;
	
	public static final  int ERROR_CODE400 = 400;
	public static final  int ERROR_CODE401 = 401;
	public static final  int ERROR_CODE404 = 404;
	public static final  int ERROR_CODE409 = 409;
	
	private final Handler handler;

	/**
	 * 构造
	 * 
	 * @param handler
	 */
	public CommentService(Handler handler){
		this.handler = handler;
	}
	
	/**
	 * 发送Comment
	 * 
	 * @param mediaId
	 * @param token
	 * @param comment
	 */
	public void create(String mediaId ,String token , String comment){
		Parameters parameters = new Parameters();
		parameters.put("access_token", token);
		parameters.put("text", comment);
		
		L.debug(Constant.NETWORK_SERVICE, ServerUrls.createComment(mediaId)+"?"+parameters.toString());
		
		new HttpService().post(ServerUrls.createComment(mediaId), parameters, new CallBack<String>() {
			@Override
			public void onSuccess(String t, int code) {
				super.onSuccess(t, code);
				Message message  = new Message();
				message.what = CREATE_COMMENT;
				message.arg1 = CREATE_COMMENT_SUCCESS;
				message.obj = t;
				handler.sendMessage(message);
			}
			
			@Override
			public void onFailure(Throwable t, int code, String strMsg) {
				super.onFailure(t, code, strMsg);
				Message message = new Message();
				message.what = CREATE_COMMENT;
				message.arg1 = code;
				handler.sendMessage(message);
			}
		});
	}
	
	/**
	 * 获取Comment列表
	 * 
	 * @param mediaId
	 * @param token
	 * @param skip
	 * @param limit
	 */
	public void list(String mediaId ,String token , String maxTime){
		Parameters parameters = new Parameters();
		parameters.put("access_token", token);
		if(!TextUtils.isEmpty(maxTime)){
			parameters.put("max_timestamp", maxTime+"");
		}
		parameters.put("count", Constant.PAGE_COUNT +"");
		
		L.debug(Constant.NETWORK_SERVICE, ServerUrls.commentList(mediaId)+"?"+parameters.toString());
		
		new HttpService().get(ServerUrls.commentList(mediaId), parameters, new CallBack<String>() {
			
			@Override
			public void onSuccess(String t, int code) {
				super.onSuccess(t, code);
				Message message  = new Message();
				message.what = LIST_COMMENT;
				message.arg1 = LIST_COMMENT_SUCCESS;
				message.obj = t;
				handler.sendMessage(message);
			}
			
			@Override
			public void onFailure(Throwable t, int code, String strMsg) {
				super.onFailure(t, code, strMsg);
				Message message = new Message();
				message.what = LIST_COMMENT;
				message.arg1 = code;
				handler.sendMessage(message);
			}
		});
	}
	
	/**
	 * 
	 * 删除Comment
	 * 
	 * @param mediaId
	 * @param commentID
	 * @param token
	 */
	public void delete(String mediaId ,String commentID ,String token){
		Parameters parameters = new Parameters();
		parameters.put("access_token", token);
		
		L.debug(Constant.NETWORK_SERVICE, ServerUrls.deleteComment(mediaId, commentID)+"?"+parameters.toString());
		
		new HttpService().delete(ServerUrls.deleteComment(mediaId, commentID)+"?"+parameters.toString() ,new CallBack<String>() {
			@Override
			public void onSuccess(String t, int code) {
				super.onSuccess(t, code);
				Message message  = new Message();
				message.what = DELETE_COMMENT;
				message.arg1 = DELETE_COMMENT_SUCCESS;
				message.obj = t;
				handler.sendMessage(message);
			}
			
			@Override
			public void onFailure(Throwable t, int code, String strMsg) {
				super.onFailure(t, code, strMsg);
				Message message = new Message();
				message.what = DELETE_COMMENT;
				message.arg1 = code;
				handler.sendMessage(message);
			}
		});
	}
}
