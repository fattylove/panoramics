package f.com.panoramics.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.guxiu.panoramics.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import f.com.panoramics.adapter.LocationAdapter;
import f.com.panoramics.base.BaseFragmentActivity;
import f.com.panoramics.entity.LocationEntity;
import f.com.panoramics.service.jsonmodel.AnalyseFoursqureJson;
import f.com.panoramics.service.netservice.FoursquareService;
import f.com.panoramics.view.PanoToast;

/**
 * 
 * @author Fatty
 * 
 * 更换Location界面
 *
 */
public class ChangeLocation1Activity extends BaseFragmentActivity implements OnClickListener{

	private Button cancelBtn;
	private String photoLocation;
	
	private ImageView locationIconImageView;
	private TextView locationEditText;

	private ListView listView;
	private LocationAdapter adapter;
	private final ArrayList<LocationEntity> locationEntities = new ArrayList<LocationEntity>();
	
	private ProgressBar inverseProgressBar;
	private ProgressBar smallProgressBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.showAnimationIn();
		super.setScreenTag("ChangeLocation1Activity");
		setContentView(R.layout.panoramics_change_location_layout);
		photoLocation = getIntent().getStringExtra("photoLocation");

		initUI();
		
		if(!TextUtils.isEmpty(photoLocation)){
			inverseProgressBar.setVisibility(View.VISIBLE);
			smallProgressBar.setVisibility(View.VISIBLE);
			FoursquareService service = new FoursquareService(handler);
			service.foursquareAll(photoLocation);
		}
	}
	
	/**
	 * 初始化UI
	 */
	public void initUI(){
		inverseProgressBar = (ProgressBar)this.findViewById(R.id.inverseProgressBar);
		smallProgressBar = (ProgressBar)this.findViewById(R.id.smallProgressBar);
		
		locationIconImageView = (ImageView)this.findViewById(R.id.locationIconImageView);
		locationEditText = (TextView)this.findViewById(R.id.locationEditText);
		
		cancelBtn = (Button)this.findViewById(R.id.cancelBtn);
		cancelBtn.setOnClickListener(this);
		
		listView = (ListView)this.findViewById(R.id.listView);
		adapter = new LocationAdapter(this, true);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				LocationEntity entity = (LocationEntity)listView.getItemAtPosition(position);
				Intent intent = new Intent();
				intent.setClass(ChangeLocation1Activity.this, SharePhotoActivity.class);
				intent.putExtra("name", entity.getName());
				intent.putExtra("url", entity.getUrl());
				intent.putExtra("lat", entity.getLat()+"");
				intent.putExtra("lng", entity.getLng()+"");
				ChangeLocation1Activity.this.setResult(RESULT_OK, intent);
				finishCurrentActivityWithAmination();
			}
		});
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancelBtn:
			finishCurrentActivityWithAmination();
			break;
		}
	}
	
	/**
	 * Handler
	 */
	private final Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case FoursquareService.Foursquare_SUCCESS:
				inverseProgressBar.setVisibility(View.GONE);
				smallProgressBar.setVisibility(View.GONE);
				locationEntities.clear();
				String content = (String)msg.obj;
				if(TextUtils.isEmpty(content)){
					return;
				}
				
				/* location data  */
				locationEntities.addAll(AnalyseFoursqureJson.get_all(content));
				adapter.clear();
				adapter.setEntities(locationEntities);
				adapter.notifyDataSetChanged();
				if(!locationEntities.isEmpty()){
					locationEditText.setText(locationEntities.get(0).getName());
					ImageLoader.getInstance().displayImage(locationEntities.get(0).getUrl(), locationIconImageView);
				}
				break;
			case FoursquareService.Foursquare_FAILED:
				inverseProgressBar.setVisibility(View.GONE);
				smallProgressBar.setVisibility(View.GONE);
				PanoToast.showToast(ChangeLocation1Activity.this, null);
				break;
			}
		}
	};
}
