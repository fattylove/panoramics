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
public class PCenterLikedService {

	private Handler handler;
	public static final int LIKED_SUCCESS = 200;
	public static final int ERROR_CODE400 = 400;
	public static final int ERROR_CODE401 = 401;
	
	/**
	 * 构造
	 * 
	 * @param handler
	 */
	public PCenterLikedService(Handler handler){
		this.handler = handler;
	}
	
	/**
	 * 获取Liked列表
	 * 
	 * @param uid
	 * @param token
	 * @param max_timestamp
	 * @param min_timestamp
	 */
	public void liked(String uid ,String token ,String max_timestamp ,String min_timestamp ){
		Parameters parameters = new Parameters();
		parameters.put("access_token", token);
		parameters.put("count", Constant.PAGE_COUNT+"");
		
		if(!TextUtils.isEmpty(min_timestamp)){
			parameters.put("min_timestamp", min_timestamp);
		}
		
		if(!TextUtils.isEmpty(max_timestamp)){
			parameters.put("max_timestamp", max_timestamp);
		}
		
		L.debug(Constant.NETWORK_SERVICE, ServerUrls.liked(uid) + "?" + parameters.toString());
		
		new HttpService().get(ServerUrls.liked(uid), parameters, new CallBack<String>() {
			@Override
			public void onSuccess(String t, int code) {
				super.onSuccess(t, code);
				Message message = new Message();
				message.what = LIKED_SUCCESS;
				message.obj = t;
				handler.sendMessage(message);
			}

			@Override
			public void onFailure(Throwable t, int code, String strMsg) {
				super.onFailure(t, code, strMsg);
				handler.sendEmptyMessage(code);
			}
		});
	}

}
