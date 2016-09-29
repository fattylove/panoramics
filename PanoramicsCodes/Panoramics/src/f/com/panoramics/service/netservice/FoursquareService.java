package f.com.panoramics.service.netservice;

import android.os.Handler;
import android.os.Message;
import f.com.panoramics.constant.Constant;
import f.com.panoramics.constant.ServerUrls;
import f.com.panoramics.utils.L;
import fatty.library.http.core.CallBack;
import fatty.library.http.core.HttpService;
import fatty.library.http.core.Parameters;

public class FoursquareService {
	
	private final Handler handler;
	public static final int Foursquare_SUCCESS = 885010;
	public static final int Foursquare_FAILED = 885011;
	
	public FoursquareService(Handler handler){
		this.handler = handler;
	}
	
	/**
	 * 获取单个foursquare
	 * 
	 * @param ll
	 */
	public void foursquareOne(String ll){
		Parameters parameters = new Parameters();
		parameters.put("client_id", Constant.foursquare_Client_id);
		parameters.put("client_secret", Constant.foursquare_Client_secret);
		parameters.put("ll", ll);
		parameters.put("limit", 1+"");
		parameters.put("v", "20141127");
		
		L.debug(Constant.NETWORK_SERVICE, ServerUrls.Base_foursquare+"?"+parameters.toString());
		
		new HttpService().get(ServerUrls.Base_foursquare, parameters,new CallBack<String>() {
			@Override
			public void onSuccess(String t, int code) {
				super.onSuccess(t, code);
				Message message = new Message();
				message.obj = t;
				message.what = Foursquare_SUCCESS;
				handler.sendMessage(message);
			}
			
			@Override
			public void onFailure(Throwable t, int code, String strMsg) {
				super.onFailure(t, code, strMsg);
				handler.sendEmptyMessage(Foursquare_FAILED);
			}
		});
	}
	
	/**
	 * 获取foursquare列表
	 * 
	 * @param ll
	 */
	public void foursquareAll(String ll){
		Parameters parameters = new Parameters();
		parameters.put("client_id", Constant.foursquare_Client_id);
		parameters.put("client_secret", Constant.foursquare_Client_secret);
		parameters.put("ll", ll);
		parameters.put("v", "20141127");
		
		L.debug(Constant.NETWORK_SERVICE, ServerUrls.Base_foursquare+"?"+parameters.toString());
		
		new HttpService().get(ServerUrls.Base_foursquare, parameters,new CallBack<String>() {
			@Override
			public void onSuccess(String t, int code) {
				super.onSuccess(t, code);
				Message message = new Message();
				message.obj = t;
				message.what = Foursquare_SUCCESS;
				handler.sendMessage(message);
			}
			
			@Override
			public void onFailure(Throwable t, int code, String strMsg) {
				super.onFailure(t, code, strMsg);
				handler.sendEmptyMessage(Foursquare_FAILED);
			}
		});
	}

}
