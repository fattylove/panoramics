package f.com.panoramics.service.netservice;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.google.android.gms.maps.model.LatLng;

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
public class HomeWorldService {

	public final static int WORLD_SUCCESS = 200;
	public final static int ERROR_CODE400 = 400;
	public final static int ERROR_CODE401 = 401;
	
	private Handler handler;
		
	public HomeWorldService(Handler handler){
		this.handler = handler;
	}
	
	/**
	 * 获取world列表
	 * 
	 * @param token
	 * @param latLng
	 * @param min_timestamp
	 * @param max_timestamp
	 * @param radius
	 * @param resolution
	 */
	public void world(String token , LatLng latLng, String min_timestamp ,String max_timestamp ,int radius ,double resolution ){
		
		Parameters parameters = new Parameters();
		parameters.put("access_token", token);
		
//		BigDecimal latBigDecimal = new BigDecimal(latLng.latitude);
//	    double lat = latBigDecimal.setScale(9, BigDecimal.ROUND_HALF_UP).doubleValue();
//	    
//	    BigDecimal lngBigDecimal = new BigDecimal(latLng.longitude);
//	    double lng = lngBigDecimal.setScale(9, BigDecimal.ROUND_HALF_UP).doubleValue();
//	    
//	    BigDecimal resolutionBigDecimal = new BigDecimal(resolution);
//	    double r = lngBigDecimal.setScale(9, BigDecimal.ROUND_HALF_UP).doubleValue();
	    
		parameters.put("radius", radius+"");
		parameters.put("resolution", resolution+"");
		parameters.put("lat", latLng.latitude+"");
		parameters.put("lng", latLng.longitude+"");
		
		if(!TextUtils.isEmpty(min_timestamp)){
			parameters.put("min_timestamp", min_timestamp);
		}
		if(!TextUtils.isEmpty(max_timestamp)){
			parameters.put("max_timestamp", max_timestamp);
		}
		
		L.debug(Constant.NETWORK_SERVICE, ServerUrls.WORLD + "?" + parameters.toString());
		
		new HttpService().get(ServerUrls.WORLD, parameters, new CallBack<String>() {
			@Override
			public void onSuccess(String t, int code) {
				super.onSuccess(t, code);
				Message message= new Message();
				message.what = WORLD_SUCCESS;
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
