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
 * sign in with your email
 *
 */
public class SignInService {
	
	public static final int LOGIN = 4001;
	
	public static final int LOGIN_SUCCESS = 201;
	
	public static final int ERROR_CODE400 = 400;
	public static final int ERROR_CODE401 = 401;
	public static final int ERROR_CODE404 = 404;
	public static final int ERROR_CODE409 = 409;
	
	public static final String NORMAL = "NORMAL";
	public static final String FACEBOOK = "FACEBOOK";
	public static final String TWITTER = "TWITTER";
	
	public static final int FT_LOGIN = 4002;
	
	public static final int FT_LOGIN_SUCCESS = 200;
	public static final int FT_ERROR_CODE400 = 400;
	public static final int FT_ERROR_CODE401 = 401;
	public static final int FT_ERROR_CODE404 = 404;
	public static final int FT_ERROR_CODE409 = 409;
	
	private final Handler handler ;
	
	/**
	 * 构造
	 * 
	 * @param handler
	 */
	public SignInService(Handler handler){
		this.handler = handler;
	}

	/**
	 * 普通用户登录
	 * 
	 * @param passport 邮箱
	 * @param password 密码
	 * @param nickname 用户名
	 * @param type      ‘NORMAL’ - optional ,‘FACEBOOK’, ‘TWITTER’
	 */
	public void login(String passport , String password){
		Parameters parameters = new Parameters();
		parameters.put("passport", passport);
		parameters.put("password", password);
		
		L.debug(Constant.NETWORK_SERVICE, ServerUrls.LOGIN+"?"+parameters.toString());
		
		new HttpService().post(ServerUrls.LOGIN, parameters, new CallBack<String>() {
			@Override
			public void onSuccess(String t ,int code) {
				super.onSuccess(t ,code);
				Message message = new Message();
				message.what = LOGIN;
				message.arg1 = code;
				message.obj = t;
				handler.sendMessage(message);
			}
			
			@Override
			public void onFailure(Throwable t, int code, String strMsg) {
				super.onFailure(t, code, strMsg);
				Message message = new Message();
				message.what = LOGIN;
				message.arg1 = code;
				handler.sendMessage(message);
			}
		});
	}
	
	
	/**
	 * Facebook或Twitter登录
	 * 
	 * @param passport 邮箱
	 * @param password 密码
	 * @param nickname 用户名
	 * @param type      ‘NORMAL’ - optional ,‘FACEBOOK’, ‘TWITTER’
	 */
	public void FT_login(String passport  ,String username, String usertype){
		Parameters parameters = new Parameters();
		parameters.put("passport", passport);
		parameters.put("username", username);
		parameters.put("usertype", usertype);
		
		L.debug(Constant.NETWORK_SERVICE, ServerUrls.LOGIN+"?"+parameters.toString());
		
		new HttpService().put(ServerUrls.LOGIN, parameters, new CallBack<String>() {
			
			@Override
			public void onSuccess(String t ,int code) {
				super.onSuccess(t ,code);
				Message message = new Message();
				message.what = FT_LOGIN;
				message.arg1 = code;
				message.obj = t;
				handler.sendMessage(message);
			}
			
			@Override
			public void onFailure(Throwable t, int code, String strMsg) {
				super.onFailure(t, code, strMsg);
				Message message = new Message();
				message.what = FT_LOGIN;
				message.arg1 = code;
				handler.sendMessage(message);
			}
		});
	}
}
