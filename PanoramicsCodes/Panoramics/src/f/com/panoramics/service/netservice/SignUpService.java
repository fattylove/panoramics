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
 * 注册
 */
public class SignUpService {

	public static final int REGISTER = 4003;
	public static final int REGISTER_SUCCESS201 = 201;
	public static final int REGISTER_SUCCESS206 = 206;
	public static final int ERROR_CODE400 = 400;
	public static final int ERROR_CODE409 = 409;
	public static final int ERROR_CODE500 = 500;

	private final Handler handler;

	/**
	 * 构造
	 * 
	 * @param handler
	 */
	public SignUpService(Handler handler) {
		this.handler = handler;
	}

	/**
	 * 注册
	 * 
	 * @param passport 注册邮箱
	 * @param password 注册密码
	 */
	public void register(String passport, String password) {
		final HttpService httpService = new HttpService();
		Parameters parameters = new Parameters();
		parameters.put("passport", passport);
		parameters.put("password", password);
		
		L.debug(Constant.NETWORK_SERVICE, ServerUrls.REGISTER + "?" + parameters.toString());
		
		httpService.post(ServerUrls.REGISTER, parameters, new CallBack<String>() {
			@Override
			public void onSuccess(String t, int code) {
				super.onSuccess(t, code);
				Message message = new Message();
				message.what = REGISTER;
				message.arg1 = code;
				handler.sendMessage(message);
			}

			@Override
			public void onFailure(Throwable t, int code, String strMsg) {
				super.onFailure(t, code, strMsg);
				Message message = new Message();
				message.what = REGISTER;
				message.arg1 = code;
				handler.sendMessage(message);
			}
		});
	}
}
