package f.com.panoramics.activity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.guxiu.panoramics.R;

import f.com.panoramics.adapter.LocationAdapter;
import f.com.panoramics.base.BaseFragmentActivity;
import f.com.panoramics.entity.LocationEntity;
import f.com.panoramics.fragment.adapter.PCenterFragmentAdapter;
import f.com.panoramics.service.jsonmodel.AnalyseFoursqureJson;
import f.com.panoramics.service.netservice.FoursquareService;
import f.com.panoramics.utils.LocationUtil;

/**
 * 
 * @author Fatty
 * 
 * 更换Location界面2
 *
 */
public class ChangeLocation2Activity extends BaseFragmentActivity {
	
	private final static int LOAD_MAP_SUCCESS = 200001;
	private final static int LOAD_MAP_FAILED = 200002;
	
	private Button cancelBtn;
	//google map api
	private GoogleMap googleMap ; 
	private double lat ;
	private double lng ;
	private SupportMapFragment mapFragment;
	
	private ProgressBar progressBar;
	private ImageView ImageView;
	private ViewPager contentView;
	private ListView locationListView;
	private LocationAdapter adapter;
	private final ArrayList<LocationEntity> locationEntities = new ArrayList<LocationEntity>();
	
	private final ArrayList<Fragment> views = new ArrayList<Fragment>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.showAnimationIn();
		super.setScreenTag("ChangeLocation2Activity");
		
		setContentView(R.layout.panoramics_select_location_layout);
		initView();
	}
	
	/**
	 * 初始化UI
	 */
	public void initView(){
		
		progressBar = (ProgressBar)this.findViewById(R.id.progressBar);
		contentView = (ViewPager)this.findViewById(R.id.contentView);
		locationListView = (ListView)this.findViewById(R.id.locationListView);
		ImageView = (ImageView)this.findViewById(R.id.datouzhenImageView);
		cancelBtn = (Button)this.findViewById(R.id.cancelBtn);
		cancelBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finishCurrentActivityWithAmination();
			}
		});
		
		adapter = new LocationAdapter(this, false);
		locationListView.setAdapter(adapter);
		locationListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				LocationEntity entity = (LocationEntity)locationListView.getItemAtPosition(position);
				Intent intent = new Intent();
				intent.setClass(ChangeLocation2Activity.this, SharePhotoActivity.class);
				intent.putExtra("name", entity.getName());
				intent.putExtra("url", entity.getUrl());
				intent.putExtra("lat", entity.getLat()+"");
				intent.putExtra("lng", entity.getLng()+"");
				ChangeLocation2Activity.this.setResult(RESULT_OK, intent);
				finishCurrentActivityWithAmination();
			}
		});
		
		lat = LocationUtil.getCurrentLocation(this)[0];
		lng = LocationUtil.getCurrentLocation(this)[1];
		
		GoogleMapOptions options = new GoogleMapOptions();
		options.camera(new CameraPosition(new LatLng(lat, lng), 14.0f, 0.0f, 0.0f));
		options.mapType(GoogleMap.MAP_TYPE_NORMAL);
		options.compassEnabled(false);
		mapFragment = SupportMapFragment.newInstance(options);
		
		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if(mapFragment.getMap()!=null){
					googleMap = mapFragment.getMap();
					Message message = new Message();
					message.what = LOAD_MAP_SUCCESS;
					handler.sendMessage(message);
					timer.cancel();
				}
			}
		}, 500, 500);
		
		views.add(mapFragment);
		contentView.setAdapter(new PCenterFragmentAdapter(getSupportFragmentManager(), views)); 
		
		progressBar.setVisibility(View.VISIBLE);
		FoursquareService service = new FoursquareService(handler);
		service.foursquareAll(lat+","+lng);
	}

	
	/**
	 * user google map api
	 * 
	 * @param gMap
	 */
	public void useGoogleMap(final GoogleMap gMap){
		if(gMap!=null){
			lat = LocationUtil.getCurrentLocation(this)[0];
			lng = LocationUtil.getCurrentLocation(this)[1];
			
			gMap.setOnCameraChangeListener(new OnCameraChangeListener() {
				@Override
				public void onCameraChange(CameraPosition arg0) {
					showAnim();
					gMap.clear();
					adapter.clear();
					
					LatLng latLng = arg0.target;
					double latitude = latLng.latitude;
					double longitude = latLng.longitude;
					
					progressBar.setVisibility(View.VISIBLE);
					FoursquareService service = new FoursquareService(handler);
					service.foursquareAll(latitude+","+longitude);
				}
			});
		}
	}
	
	/**
	 * 
	 */
	private final Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case LOAD_MAP_SUCCESS:
				useGoogleMap(googleMap);
				break;
			case LOAD_MAP_FAILED:
				
				break;
			case FoursquareService.Foursquare_SUCCESS:
				String content = (String)msg.obj;
				locationEntities.clear();
				locationEntities.addAll(AnalyseFoursqureJson.get_all(content));
				adapter.clear();
				adapter.setEntities(locationEntities);
				adapter.notifyDataSetChanged();
				progressBar.setVisibility(View.GONE);
				break;
			case FoursquareService.Foursquare_FAILED:
				progressBar.setVisibility(View.GONE);
				break;
			}
		}
	};
	
	/**
	 *  显示图标动画
	 */
	public void showAnim(){
		Animation translateAnimation_on = new TranslateAnimation(0.0f,0.0f, 0.0f, -30.0f);
		translateAnimation_on.setDuration(150);
		ImageView.startAnimation(translateAnimation_on);
	}
	
}
