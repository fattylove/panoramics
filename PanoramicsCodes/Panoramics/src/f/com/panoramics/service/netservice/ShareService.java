package f.com.panoramics.service.netservice;

import android.os.Handler;
import android.os.Message;
import f.com.panoramics.constant.Constant;
import f.com.panoramics.constant.ServerUrls;
import f.com.panoramics.utils.L;
import fatty.library.http.core.CallBack;
import fatty.library.http.core.HttpService;
import fatty.library.http.core.Parameters;

public class ShareService {

	private Handler handler;
	public static final int SHARE_SUCCESS = 201;
	public static final int ERROR_CODE404 = 404;
	public static final int ERROR_CODE409 = 409;
	
	/**
	 * 
	 * @param handler
	 */
	public ShareService(Handler handler){
		this.handler = handler;
	}
	
	/**
	 * 分享
	 * 
	 * @param uid
	 * @param tags
	 * @param photo_uri
	 * @param location_name
	 * @param lat
	 * @param lng
	 * @param width
	 * @param height
	 * @param token
	 */
	public void share(String uid, String tags, String photo_uri, String location_name , double lat ,double lng  , int width , int height ,String token){
		Parameters parameters = new Parameters();
		parameters.put("access_token", token);
		parameters.put("tags",         tags + "");
		parameters.put("photo_uri",    photo_uri + "");
		parameters.put("location_name",location_name + "");
		parameters.put("lng",          lng + "");
		parameters.put("lat",          lat + "");
		parameters.put("width",        width + "");
		parameters.put("height",       height + "");
		
		L.debug(Constant.NETWORK_SERVICE, ServerUrls.sharePhoto(uid)+"?"+parameters.toString());
		
		new HttpService().post(ServerUrls.sharePhoto(uid), parameters, new CallBack<String>() {

			public void onSuccess(String t, int code) {
				super.onSuccess(t, code);
				Message message = new Message();
				message.obj = t;
				message.what = SHARE_SUCCESS;
				handler.sendMessage(message);
			}

			public void onFailure(Throwable t, int code, String strMsg) {
				super.onFailure(t, code, strMsg);
				handler.sendEmptyMessage(code);
			}
		});
	}
}
