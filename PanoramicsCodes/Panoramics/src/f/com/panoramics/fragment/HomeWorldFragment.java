package f.com.panoramics.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.CancelableCallback;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.guxiu.panoramics.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import f.com.panoramics.activity.MapListActivity;
import f.com.panoramics.activity.ShowCommentsActivity;
import f.com.panoramics.base.BaseFragment;
import f.com.panoramics.constant.Constant;
import f.com.panoramics.entity.MediaEntity;
import f.com.panoramics.service.netservice.HomeWorldService;
import f.com.panoramics.utils.DistanceUtil;
import f.com.panoramics.utils.ImageConfig;
import f.com.panoramics.utils.ImageUtil;
import f.com.panoramics.utils.LocationUtil;
import f.com.panoramics.view.CircleImageView;
import f.com.panoramics.view.PanoToast;

/**
 * 
 * @author Fatty
 * 
 */
public class HomeWorldFragment extends BaseFragment {

	private MapView mMapView; //google mapView
	private GoogleMap googleMap;//google map
	
	private double latitude;
	private double longitude;
	
	private Activity activity;
	
	private int defaultZoom = 14; //默认的zoomLevel
	private double screen_h ; //屏幕高
	
	private boolean isRequest = false; //当前是否正在请求
	
//	private MediaDBService mediaDBService ;
	private HashMap<Marker, String> markers = new HashMap<Marker, String>();
	
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
//		mediaDBService = new MediaDBService(activity);
		screen_h = (double)getPreInt("screen_h");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.panoramics_fragment_home_world_layout, container,false);
		mMapView = (MapView) v.findViewById(R.id.mapView);
		mMapView.onCreate(savedInstanceState);
		mMapView.onResume();
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
		googleMap = mMapView.getMap();
		latitude = LocationUtil.getCurrentLocation(getActivity())[0];
		longitude = LocationUtil.getCurrentLocation(getActivity())[1];
		
		googleMap.setOnCameraChangeListener(new OnCameraChangeListener() {
			public void onCameraChange(CameraPosition cameraPosition) {
				double distance =  DistanceUtil.getPxDistance(googleMap) * ( screen_h / 2) ;
				if(!isRequest){
					loadData(cameraPosition.target , (int) distance , DistanceUtil.getPxDistance(googleMap));
				}
			}
		});

		googleMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			public boolean onMarkerClick(Marker arg0) {
				if(markers.containsKey(arg0)){
					String ids = markers.get(arg0);
					try {
						JSONArray arr = new JSONArray(ids);
						Intent intent = new Intent();
						if(arr.length() ==1){
							String mediaId = (String) arr.get(0);
							intent.putExtra("mediaId", mediaId);
							intent.setClass(activity, ShowCommentsActivity.class);
						}else{
							intent.putExtra("ids", ids);
							intent.setClass(activity, MapListActivity.class);
						}
						activity.startActivity(intent);
					} catch (Exception e) {
					}
				}
				return false;
			}
		});
		
		CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(latitude, longitude)).zoom(defaultZoom).build();
		googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 100, new CancelableCallback() {
			public void onFinish() {
				double distance =  DistanceUtil.getPxDistance(googleMap) * ( screen_h / 2) ;
				loadData(new LatLng(latitude, longitude) , (int) distance , DistanceUtil.getPxDistance(googleMap));
			}
			
			public void onCancel() {
			}
		});
	}

	/**
	 * 加载数据
	 * 
	 * @param latLng
	 * @param distance
	 * @param resolution
	 */
	public void loadData(LatLng latLng , int distance , double resolution){
		isRequest = true;
		googleMap.clear();
		markers.clear();
		HomeWorldService worldService = new HomeWorldService(handler);
		worldService.world(getPreString("token"), latLng, null, null, distance , resolution);
	}

	/**
	 * user google map aMath.PI
	 * 
	 * @param gMap
	 */
	public void setGoogleMapData(final ArrayList<MediaEntity> mediaEntities) {
		
		MarkerOptions marker = new MarkerOptions();
		marker.position(new LatLng(latitude, longitude));
		marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.p_mylocation_icon));
		googleMap.addMarker(marker);
		
		if (mediaEntities.size() > 0) {
			if (googleMap != null) {
				for (final MediaEntity mediaEntity : mediaEntities) {
					final MarkerOptions mOptions = new MarkerOptions();
					final View iconView = LayoutInflater.from(activity).inflate(R.layout.panoramics_word_icon_layout, null);
					final RelativeLayout rootLayout = (RelativeLayout) iconView.findViewById(R.id.homeIconLayout);
					final CircleImageView iconImageView = (CircleImageView) iconView.findViewById(R.id.homeIcon);
					final TextView numTextView = (TextView)iconView.findViewById(R.id.groupNumTextView);
					
					if(mediaEntity.getGroupNum() != 1){
						numTextView.setVisibility(View.VISIBLE);
						numTextView.setText(mediaEntity.getGroupNum() + "");
					}else{
						numTextView.setVisibility(View.GONE);
					}
					
					mOptions.position(new LatLng(mediaEntity.getLat(), mediaEntity.getLng()));
					ImageLoader.getInstance().displayImage(mediaEntity.getThumbnail(), iconImageView, ImageConfig.getMapPhotoConfig() );
					Bitmap icon = ImageUtil.BitmapUtil.convertViewToBitmap(rootLayout);
					mOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
					Marker m = googleMap.addMarker(mOptions);
					markers.put(m, mediaEntity.getIds());
				}
			}
		}
	}

	/**
	 * 
	 */
	private Handler handler = new Handler(){

		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			switch (msg.what) {
			case HomeWorldService.WORLD_SUCCESS:
				String root = (String) msg.obj;
				if(TextUtils.isEmpty(root)){
					return;
				}
				try {
					ArrayList<MediaEntity> mediaEntities = new ArrayList<MediaEntity>();
					JSONObject rootObject = new JSONObject(root);
					JSONArray dataArr = rootObject.getJSONArray("data");
					if(dataArr.length() > 0){
						for(int i=0; i<dataArr.length() ; i++){
							MediaEntity mediaEntity = new MediaEntity();
							JSONObject mediaObject = dataArr.getJSONObject(i);
							double lat = mediaObject.getDouble("lat");
							double lng = mediaObject.getDouble("lng");
							int num = mediaObject.getInt("n");
							JSONArray ids = mediaObject.getJSONArray("ids");
							JSONObject images = mediaObject.getJSONObject("images");
							JSONObject thumbnail= images.getJSONObject("thumbnail");
							String url = thumbnail.getString("url");
							
							mediaEntity.setIds(ids.toString());
							mediaEntity.setLat(lat);
							mediaEntity.setLng(lng);
							mediaEntity.setGroupNum(num);
							mediaEntity.setThumbnail(url);
							mediaEntity.setStateTag(Constant.WORLD);
							mediaEntities.add(mediaEntity);
						}
					}
//					mediaDBService.saveMediaEntities(mediaEntities);
					setGoogleMapData(mediaEntities);
				} catch (Exception e) {
				
				}
				isRequest = false;
				break;
			case HomeWorldService.ERROR_CODE400:
				toast("ERROR_CODE400");
				break;
			case HomeWorldService.ERROR_CODE401:
				tokenInvalid();
				break;
			default:
				PanoToast.showToast(activity, "No Internet Connection");
				isRequest = false;
//				ArrayList<MediaEntity> mediaEntities = mediaDBService.findMediaEntities(0L, Constant.WORLD);
//				setGoogleMapData(mediaEntities);
				break;
			}
		}
	};
}
