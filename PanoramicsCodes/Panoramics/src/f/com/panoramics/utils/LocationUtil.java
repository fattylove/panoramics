package f.com.panoramics.utils;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;


/**
 * 
 * @author Fatty
 * 
 * location信息获取
 *
 */
public class LocationUtil {

	/**
	 * 获取当前地理位置信息
	 * 
	 * @param context
	 * 
	 * @return 返回纬度、经度数组
	 */
	public static double[] getCurrentLocation(Context context) {
		double[] results = new double[2];
		double latitude = 0.0;
		double longitude = 0.0;

		LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (location != null) {
				latitude = location.getLatitude();
				longitude = location.getLongitude();
			}
		} else {
			//Location Service 监听 Location
			LocationListener locationListener = new LocationListener() {
				@Override
				public void onStatusChanged(String provider, int status, Bundle extras) {
				}
				@Override
				public void onProviderEnabled(String provider) {
				}
				@Override
				public void onProviderDisabled(String provider) {
				}
				@Override
				public void onLocationChanged(Location location) {
				}
			};
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);//每秒监听一次
			Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			if (location != null) {
				latitude = location.getLatitude(); 
				longitude = location.getLongitude();
			}
		}
		results[0] = latitude;
		results[1] = longitude;
		return results;
	}
	
	/**
	 * 经纬度转换
	 * 
	 * @param j
	 * @param w
	 * @param d
	 * @return
	 */
	public static double jwd(double j, double w ,double d){
		double jjj = j / 1.00d;
		double www = w / 60.00d;
		double ddd = d / 3600.00d;
		return jjj+www+ddd;
	}
}
