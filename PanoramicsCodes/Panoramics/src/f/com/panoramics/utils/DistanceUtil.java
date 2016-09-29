package f.com.panoramics.utils;

import android.graphics.Point;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

public class DistanceUtil {
	
	/**
	 * 
	 * @param ll1
	 * @param ll2
	 * 
	 * @return 米
	 */
	public static double getDistance(LatLng ll1 ,LatLng ll2 ) {
		
		double lng_1 = ll1.longitude ;
		double lat_1 = ll1.latitude ;
		double lng_2 = ll2.longitude ; 
		double lat_2 = ll2.latitude ;
		
		double er = 6378137.0D; // 6378700.0f;
		
		double radlat_1 = Math.PI * lat_1 / 180.0D;
		double radlng_1 = Math.PI * lng_1 / 180.0D;
		
		double radlat_2 = Math.PI * lat_2 / 180.0D;
		double radlng_2 = Math.PI * lng_2 / 180.0D;
		
		if (radlat_1 < 0)
			radlat_1 = Math.PI / 2 + Math.abs(radlat_1);// south
		if (radlat_1 > 0)
			radlat_1 = Math.PI / 2 - Math.abs(radlat_1);// north
		if (radlng_1 < 0)
			radlng_1 = Math.PI * 2 - Math.abs(radlng_1);// west
		if (radlat_2 < 0)
			radlat_2 = Math.PI / 2 + Math.abs(radlat_2);// south
		if (radlat_2 > 0)
			radlat_2 = Math.PI / 2 - Math.abs(radlat_2);// north
		if (radlng_2 < 0)
			radlng_2 = Math.PI * 2 - Math.abs(radlng_2);// west
		
		double x1 = er * Math.cos(radlng_1) * Math.sin(radlat_1);
		double y1 = er * Math.sin(radlng_1) * Math.sin(radlat_1);
		double z1 = er * Math.cos(radlat_1);
		double x2 = er * Math.cos(radlng_2) * Math.sin(radlat_2);
		double y2 = er * Math.sin(radlng_2) * Math.sin(radlat_2);
		double z2 = er * Math.cos(radlat_2);
		
		double d = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2) + (z1 - z2) * (z1 - z2));

		double theta = Math.acos((er * er + er * er - d * d) / (2 * er * er));
		double distance = theta * er;
		
		return distance;
	}
	
	/**
	 * 像素点间距离
	 * 
	 * @param googleMap
	 * @return
	 */
	public static double getPxDistance(GoogleMap googleMap){
		Point p1 = new Point(0, 0);
		Point p2 = new Point(0, 1);
		LatLng ll1 = googleMap.getProjection().fromScreenLocation(p1);
		LatLng ll2 = googleMap.getProjection().fromScreenLocation(p2);
		return  getDistance(ll1,  ll2);
	}

}
