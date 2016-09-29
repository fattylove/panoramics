package f.com.panoramics.service.netservice;

import android.os.Handler;
import android.os.Message;
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
public class MediaGetService {

	private Handler handler;
	public static final int GET_MEDIA_ONE = 98001;
	public static final int GET_MEDIA_ALL = 98001;
	
	
	public static final int GET_MEDIA_ALL_SUCCESS = 200;
	public static final int GET_MEDIA_ONE_SUCCESS = 201;
	public static final int ERROR_CODE400 = 400;
	public static final int ERROR_CODE401 = 401;
	
	
	/**
	 * 构造
	 * 
	 * @param handler
	 */
	public MediaGetService(Handler handler){
		this.handler = handler;
	}
	
	/**
	 * 获取单个MediaEntity
	 * 
	 * @param token
	 * @param mediaId
	 */
	public void get_one(String token ,String mediaId){
		Parameters parameters = new Parameters();
		parameters.put("access_token", token);
		
		L.debug(Constant.NETWORK_SERVICE, ServerUrls.get_media(mediaId) +"?" + parameters.toString());
		
		new HttpService().get(ServerUrls.get_media(mediaId), parameters, new CallBack<String>() {
			@Override
			public void onSuccess(String t, int code) {
				super.onSuccess(t, code);
				Message message = new Message();
				message.what  = GET_MEDIA_ONE;
				message.arg1 = GET_MEDIA_ONE_SUCCESS;
				message.obj = t;
				handler.sendMessage(message);
			}

			@Override
			public void onFailure(Throwable t, int code, String strMsg) {
				super.onFailure(t, code, strMsg);
				Message message = new Message();
				message.what = GET_MEDIA_ONE;
				message.arg1 = code;
				handler.sendMessage(message);
			}
		});
	}
	
	/**
	 * 获取MediaEntity列表
	 * 
	 * @param token
	 * @param ids
	 */
	public void get_all(String token ,String ids){
		Parameters parameters = new Parameters();
		parameters.put("access_token", token);
		parameters.put("ids", ids);
		
		L.debug(Constant.NETWORK_SERVICE, ServerUrls.MEDIA +"?" + parameters.toString());
		
		new HttpService().post(ServerUrls.MEDIA, parameters, new CallBack<String>() {
			@Override
			public void onSuccess(String t, int code) {
				super.onSuccess(t, code);
				Message message = new Message();
				message.what  = GET_MEDIA_ALL;
				message.arg1 = GET_MEDIA_ALL_SUCCESS;
				message.obj = t;
				handler.sendMessage(message);
			}

			@Override
			public void onFailure(Throwable t, int code, String strMsg) {
				super.onFailure(t, code, strMsg);
				Message message = new Message();
				message.what = GET_MEDIA_ALL;
				message.arg1 = code;
				handler.sendMessage(message);
			}
		});
	}
	
}
