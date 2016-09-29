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
public class HomeHotService {

	private Handler handler;
	public static final int HOT_SUCCESS = 200;
	public static final int ERROR_CODE400 = 400;
	public static final int ERROR_CODE401 = 401;
	
	/**
	 * 构造
	 * 
	 * @param handler
	 */
	public HomeHotService(Handler handler){
		this.handler = handler;
	}
	
	/**
	 * 获取Hot列表
	 * 
	 * @param token
	 * @param skip
	 * @param limit
	 */
	public void hot(String token  , int skip){
		Parameters parameters = new Parameters();
		parameters.put("access_token", token);
		parameters.put("skip", skip+"");
		parameters.put("limit", Constant.PAGE_COUNT+"");
		
		L.debug(Constant.NETWORK_SERVICE, ServerUrls.HOT + "?" + parameters.toString());
		
		new HttpService().get(ServerUrls.HOT, parameters, new CallBack<String>() {
			@Override
			public void onSuccess(String t, int code) {
				super.onSuccess(t, code);
				Message message = new Message();
				message.what = HOT_SUCCESS;
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
