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
 * 修改密码
 *
 */
public class ChangePwdService {
	
	private final Handler handler;
	
	public static final int CHANGE_PASSWORD_SUCCESS = 200;
	
	public static final int ERROR_CODE400 = 400;
	public static final int ERROR_CODE401 = 401;
	public static final int ERROR_CODE404 = 404;
	public static final int ERROR_CODE409 = 409;
	
	
	public ChangePwdService(Handler handler){
		this.handler = handler;
	}
	
	/**
	 * 修改密码
	 * 
	 * @param uid
	 * @param token
	 * @param oldPwd
	 * @param newPwd
	 */
	public void change(String uid, String token , String oldPwd , String newPwd){
		Parameters parameters = new Parameters();
		parameters.put("oldpwd", oldPwd);
		parameters.put("newpwd", newPwd);
		
		L.debug(Constant.NETWORK_SERVICE, ServerUrls.changePasword(uid) +"?access_token=" +token + "&" + parameters.toString());
		
		new HttpService().put(ServerUrls.changePasword(uid) +"?access_token=" +token, parameters, new CallBack<String>() {

			public void onSuccess(String t, int code) {
				super.onSuccess(t, code);
				Message message = new Message();
				message.what  = code;
				message.obj = t;
				handler.sendMessage(message);
			}

			public void onFailure(Throwable t, int code, String strMsg) {
				super.onFailure(t, code, strMsg);
				handler.sendEmptyMessage(code);
			}
		});
	}
}
