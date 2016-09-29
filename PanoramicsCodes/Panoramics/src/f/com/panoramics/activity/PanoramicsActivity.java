package f.com.panoramics.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.guxiu.panoramics.R;

import f.com.panoramics.adapter.LocalPhotoAdapter;
import f.com.panoramics.base.BaseFragmentActivity;
import f.com.panoramics.entity.PanoramicsEntity;
import f.com.panoramics.utils.ImageUtil;
import f.com.panoramics.view.PanoToast;


/**
 * 
 * @author Fatty
 * 
 * 选择全景图片界面
 *
 */
public class PanoramicsActivity extends BaseFragmentActivity implements OnClickListener{
	
	private Button cancelBtn;
	private ListView pListView;
	private LinearLayout noPanoLayout;
	
	private ArrayList<PanoramicsEntity> panoramicsEntities = new ArrayList<PanoramicsEntity>();
	private LocalPhotoAdapter adapter ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.showAnimationIn();
		super.setScreenTag("PanoramicsActivity");
		setContentView(R.layout.panoramics_pan_layout);
		
		noPanoLayout = (LinearLayout)this.findViewById(R.id.noPanoLayout);
		
		cancelBtn = (Button)this.findViewById(R.id.cancelBtn);
		cancelBtn.setOnClickListener(this);
		
		adapter = new LocalPhotoAdapter(this);
		pListView = (ListView)this.findViewById(R.id.pListView);
		pListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				PanoramicsEntity panoramicsEntity = (PanoramicsEntity) pListView.getItemAtPosition(position);
				Intent intent = new Intent();
				intent.putExtra("imgPath", panoramicsEntity.getImgPath());
				intent.setClass(PanoramicsActivity.this, SharePhotoActivity.class);
				PanoramicsActivity.this.startActivity(intent);
			}
		});
		
		panoramicsEntities = ImageUtil.SDCardImages.getAllMediaImages(PanoramicsActivity.this);
		
		View v = LayoutInflater.from(this).inflate(R.layout.panoramics_no_local_panoramics_layout, null);
		ImageView noLocalPanoImageView = (ImageView)v.findViewById(R.id.noLocalPanoImageView);
		if(panoramicsEntities.isEmpty()){
			noPanoLayout.setVisibility(View.VISIBLE);
			noPanoLayout.addView(v);
			noLocalPanoImageView.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					PanoToast.showToast(PanoramicsActivity.this, "Take a Pano Photo");
				}
			});
		}
		
		adapter.setPanoramicsEntities(panoramicsEntities);
		pListView.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancelBtn:
			finishCurrentActivityWithAmination();
			break;
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		panoramicsEntities.clear();
		adapter.clear();
	}
}
