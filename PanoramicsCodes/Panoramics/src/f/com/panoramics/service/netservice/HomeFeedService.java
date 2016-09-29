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

public class HomeFeedService {

	
	public static final int FEED_SUCCESS  = 200;
	public static final int ERROR_CODE400 = 400;
	public static final int ERROR_CODE401 = 401;
	
	private Handler handler ;
	
	public HomeFeedService(Handler handler){
		this.handler = handler;
	}

	/**
	 * 
	 * 获取Feed列表
	 * 
	 * @param uid
	 * @param access_token
	 * @param count
	 * @param max_timestamp 最大时间戳
	 * @param min_timestamp 最小时间戳
	 * @param max_id  		最大ID
	 * @param min_id  		最小ID
	 */
	public void feed(String access_token ,String max_timestamp ,String min_timestamp ){
		
		Parameters parameters = new Parameters();
		parameters.put("access_token", access_token);
		parameters.put("count", Constant.PAGE_COUNT+"");
		
		if(!TextUtils.isEmpty(max_timestamp)){
			parameters.put("max_timestamp", max_timestamp);
		}
		if(!TextUtils.isEmpty(min_timestamp)){
			parameters.put("min_timestamp", min_timestamp);
		}
		
		L.debug(Constant.NETWORK_SERVICE, ServerUrls.FEED+"?"+parameters.toString());
		
		new HttpService().get(ServerUrls.FEED, parameters	, new CallBack<String>() {

			@Override
			public void onSuccess(String t, int code) {
				super.onSuccess(t, code);
				Message message = new Message();
				message.what = FEED_SUCCESS;
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
