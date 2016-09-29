package f.com.panoramics.service.netservice;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import f.com.panoramics.constant.Constant;
import f.com.panoramics.constant.ServerUrls;
import f.com.panoramics.entity.AccountEntity;
import f.com.panoramics.utils.L;
import fatty.library.http.core.CallBack;
import fatty.library.http.core.HttpService;
import fatty.library.http.core.Parameters;


/**
 * 
 * @author Fatty
 * 
 * 设置与获取用户信息
 *
 */
public class AccountService {
	
	public static final int GET_ACCOUNT = 4004;
	public static final int PUT_ACCOUNT = 4005;
	public static final int UPLOAD_HEADER = 4006;

	public static final int ACCOUNT_SUCCESS = 200;

	public static final int ERROR_CODE400 = 400;
	public static final int ERROR_CODE401 = 401;
	public static final int ERROR_CODE404 = 404;
	
	private final Handler handler;
	
	/**
	 * 构造
	 * 
	 * @param handler
	 */
	public AccountService(Handler handler){
		this.handler  = handler;
	}
	
	/**
	 * 获取单用户信息
	 * 
	 * @author Fatty
	 * @param entity
	 * @exception 
	 * 
	 * 获取用户信息
	 */
	public void getAccountEntity(String uid ,String token){
		Parameters parameters = new Parameters();
		parameters.put("access_token", token);
		
		L.debug(Constant.NETWORK_SERVICE, ServerUrls.getAccountInfo(uid)+"?"+parameters.toString());
		
		new HttpService().get(ServerUrls.getAccountInfo(uid), parameters , new CallBack<String>() {

			@Override
			public void onSuccess(String t, int code) {
				super.onSuccess(t, code);
				Message message = new Message();
				message.what = GET_ACCOUNT;
				message.arg1 = code;
				message.obj = t;
				handler.sendMessage(message);
			}

			@Override
			public void onFailure(Throwable t, int code, String strMsg) {
				super.onFailure(t, code, strMsg);
				Message message = new Message();
				message.what = GET_ACCOUNT;
				message.arg1 = code;
				handler.sendMessage(message);
			}
		});
	}
	
	
	/**
	 * 修改个人信息
	 * 
	 * @author Fatty
	 * @param entity
	 * @exception
	 * 设置用户信息
	 */
	public void putAccountEntity(AccountEntity entity , String token){
		Parameters parameters = new Parameters();
		
		if(!TextUtils.isEmpty(entity.getWebsite())){
			parameters.put("website", entity.getWebsite());
		}else{
			parameters.put("website", "");
		}
		if(!TextUtils.isEmpty(entity.getBio())){
			parameters.put("bio", entity.getBio());
		}else{
			parameters.put("bio", "");
		}
		if(!TextUtils.isEmpty(entity.getPhone())){
			parameters.put("phone", entity.getPhone());
		}else{
			parameters.put("phone", "");
		}
		if(!TextUtils.isEmpty(entity.getGender())){
			parameters.put("gender", entity.getGender());
		}
		if(!TextUtils.isEmpty(entity.getUsername())){
			parameters.put("username", entity.getUsername());
		}
		if(!TextUtils.isEmpty(entity.getNickname())){
			parameters.put("nickname", entity.getNickname());
		}else{
			parameters.put("nickname", "");
		}
		if(!TextUtils.isEmpty(entity.getAvatar())){
			parameters.put("profile_picture", entity.getAvatar());
		}
		
		L.debug(Constant.NETWORK_SERVICE, ServerUrls.getAccountInfo(entity.getAccountId())+"?access_token="+ token +"&"+parameters.toString());
		
		new HttpService().put(ServerUrls.getAccountInfo(entity.getAccountId())+"?access_token="+ token , parameters , new CallBack<String>() {
			
			@Override
			public void onSuccess(String t, int code) {
				super.onSuccess(t, code);
				Message message = new Message();
				message.what = PUT_ACCOUNT;
				message.arg1 = code;
				message.obj = t;
				handler.sendMessage(message);
			}
			
			@Override
			public void onFailure(Throwable t, int code, String strMsg) {
				super.onFailure(t, code, strMsg);
				Message message = new Message();
				message.what = PUT_ACCOUNT;
				message.arg1 = code;
				handler.sendMessage(message);
			}
		});
	}
	

	/**
	 * 上传头像
	 * 
	 * @author Fatty
	 * @param entity
	 * @exception
	 * 设置用户信息
	 */
	public void uploadAccountEntity(String uid  , String token ,String profile_picture){
		Parameters parameters = new Parameters();
		parameters.put("profile_picture", profile_picture);
		
		L.debug(Constant.NETWORK_SERVICE, ServerUrls.getAccountInfo(uid)+"?access_token=" + token+"&" + parameters.toString());
		
		new HttpService().put(ServerUrls.getAccountInfo(uid)+"?access_token="+token, parameters , new CallBack<String>() {
			
			@Override
			public void onSuccess(String t, int code) {
				super.onSuccess(t, code);
				Message message = new Message();
				message.what = UPLOAD_HEADER;
				message.arg1 = code;
				message.obj = t;
				handler.sendMessage(message);
			}
			
			@Override
			public void onFailure(Throwable t, int code, String strMsg) {
				super.onFailure(t, code, strMsg);
				Message message = new Message();
				message.what = UPLOAD_HEADER;
				message.arg1 = code;
				handler.sendMessage(message);
			}
		});
	}
}
