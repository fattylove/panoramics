package f.com.panoramics.service.netservice;

import android.os.Handler;
import android.os.Message;
import f.com.panoramics.constant.Constant;
import f.com.panoramics.constant.ServerUrls;
import f.com.panoramics.utils.L;
import fatty.library.http.core.CallBack;
import fatty.library.http.core.HttpService;

/**
 * 
 * @author Fatty
 *
 */
public class MediaRemoveService {

	private Handler handler;
	
	public static final int REMOVE_MEDIA = 76501;
	
	public static final int DELETE_SUCCESS = 200;
	public static final int ERROR_CODE400 = 400;
	public static final int ERROR_CODE401 = 401;
	public static final int ERROR_CODE404 = 404;
	public static final int ERROR_CODE409 = 409;
	public static final int ERROR_CODE503 = 503;
	
	public MediaRemoveService(Handler handler){
		this.handler = handler;
	}
	
	public void remove(String token , String mediaId){
		
		L.debug(Constant.NETWORK_SERVICE, ServerUrls.deleteMedia(mediaId) + "?" + token);
		
		new HttpService().delete(ServerUrls.deleteMedia(mediaId) + "?access_token=" + token, new CallBack<String>(){
			@Override
			public void onSuccess(String t, int code) {
				super.onSuccess(t, code);
				System.err.println(t);
				Message message = new Message();
				message.what = REMOVE_MEDIA;
				message.arg1 = DELETE_SUCCESS;
				message.obj = t;
				handler.sendMessage(message);
			}

			@Override
			public void onFailure(Throwable t, int code, String strMsg) {
				super.onFailure(t, code, strMsg);
				Message message = new Message();
				message.what =REMOVE_MEDIA;
				message.arg1 = code;
				handler.sendMessage(message);
			}
		});
	}

}
