package f.com.panoramics.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.guxiu.panoramics.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import f.com.panoramics.activity.ShowCommentsActivity;
import f.com.panoramics.base.BaseFragment;
import f.com.panoramics.entity.MediaEntity;
import f.com.panoramics.utils.ImageConfig;
import f.com.panoramics.utils.ImageUtil;
import f.com.panoramics.utils.LocationUtil;
import f.com.panoramics.view.CircleImageView;


/**
 * 
 * @author Fatty
 *
 */
public class PCenterLocationFragment extends BaseFragment  {

	private MapView mMapView;
	private GoogleMap googleMap;
	private double latitude;
	private double longitude;
	private Activity activity;
	private ArrayList<MediaEntity> mediaEntities = new ArrayList<MediaEntity>();
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.panoramics_pcenter_bottom_location_layout, container, false);
		mMapView = (MapView) v.findViewById(R.id.mapView);
		mMapView.onCreate(savedInstanceState);
		mMapView.onResume();
		googleMap = mMapView.getMap();
		return v;
	}
	
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		try {
			MapsInitializer.initialize(activity.getApplicationContext());
		} catch (Exception e) {
			e.printStackTrace();
		}
		latitude = LocationUtil.getCurrentLocation(getActivity())[0];
		longitude = LocationUtil.getCurrentLocation(getActivity())[1];
		MarkerOptions marker = new MarkerOptions();
		marker.position(new LatLng(latitude, longitude));
		marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.p_mylocation_icon));
		googleMap.addMarker(marker);
		
		CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(latitude, longitude)).zoom(14).build();
		googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
	}
	
	/**
	 * 
	 * @param mediaEntities
	 */
	public void setData(ArrayList<MediaEntity> mediaEntities){
		if(mediaEntities.size() > 0){
			this.mediaEntities.addAll(mediaEntities);
			setGoogleMapData(this.mediaEntities);
		}
	}
	
	public void clear(){
		this.mediaEntities.clear();
		googleMap.clear();
	}
	
	/**
	 * user google map api
	 * 
	 * @param gMap
	 */
	public void setGoogleMapData(final ArrayList<MediaEntity> mediaEntities){
		if(googleMap!=null){
			latitude = LocationUtil.getCurrentLocation(activity)[0];
			longitude = LocationUtil.getCurrentLocation(activity)[1];
			googleMap.clear();
			MarkerOptions markerOptions = new MarkerOptions();
			markerOptions.position(new LatLng(latitude, longitude));
			markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.p_mylocation_icon));
			googleMap.addMarker(markerOptions);
			addIcons(mediaEntities);
			
			googleMap.setOnCameraChangeListener(new OnCameraChangeListener() {
				public void onCameraChange(CameraPosition arg0) {
					googleMap.clear();
					MarkerOptions markerOptions = new MarkerOptions();
					markerOptions.position(new LatLng(latitude ,longitude));
					markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.p_mylocation_icon));
					googleMap.addMarker(markerOptions);
					addIcons( mediaEntities);
				}
			});
			googleMap.setOnMyLocationChangeListener(new OnMyLocationChangeListener() {
				public void onMyLocationChange(Location arg0) {
					
				}
			});
			googleMap.setOnMarkerClickListener(new OnMarkerClickListener() {
				public boolean onMarkerClick(Marker arg0) {
					Intent imgIntent = new Intent();
					imgIntent.putExtra("mediaId", arg0.getSnippet());
					imgIntent.setClass(activity, ShowCommentsActivity.class);
					activity.startActivity(imgIntent);
					return false;
				}
			});
		}
	}
	
	/**
	 * 填充数据
	 * 
	 * @param gMap
	 */
	public void addIcons(ArrayList<MediaEntity> mediaEntities){
		for(final MediaEntity mediaEntity : mediaEntities){
			if(mediaEntity.getLat() == 0 && mediaEntity.getLng() == 0){
				
			}else{
				final MarkerOptions markerOptions = new MarkerOptions();
				final View iconView = LayoutInflater.from(activity).inflate(R.layout.panoramics_word_icon_layout, null);
				final RelativeLayout rootLayout = (RelativeLayout)iconView.findViewById(R.id.homeIconLayout);
				final CircleImageView iconImageView = (CircleImageView)iconView.findViewById(R.id.homeIcon);
				
				ImageLoader.getInstance().displayImage(mediaEntity.getThumbnail(), iconImageView, ImageConfig.getMapPhotoConfig(),new ImageLoadingListener() {
					public void onLoadingStarted(String imageUri, View view) {
					}
					public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
					}
					public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
						//MarkerOptions settings
						Bitmap icon = ImageUtil.BitmapUtil.convertViewToBitmap(rootLayout);
						markerOptions.position(new LatLng(mediaEntity.getLat(), mediaEntity.getLng()));
						markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
						markerOptions.snippet(mediaEntity.getMediaId());
						googleMap.addMarker(markerOptions);
					}
					public void onLoadingCancelled(String imageUri, View view) {
						
					}
				});
			}
		}
	}
	
}
