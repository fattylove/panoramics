package f.com.panoramics.activity;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.guxiu.panoramics.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import f.com.panoramics.base.BaseFragmentActivity;
import f.com.panoramics.entity.CommentEntity;
import f.com.panoramics.entity.MediaEntity;
import f.com.panoramics.service.netservice.CommentService;
import f.com.panoramics.utils.DialogUtil;
import f.com.panoramics.utils.ImageConfig;
import f.com.panoramics.view.EarthProgressDialog;

/**
 * 
 * @author Fatty
 * 
 * 发送评论Comment
 *
 */
public class SendCommnetActivity extends BaseFragmentActivity implements OnClickListener{

	private Button cancelBtn;
	private EditText contentTextView;
	private Button sendBtn;
	private ImageView topImageView;
	
	private MediaEntity mediaEntity;
	private EarthProgressDialog progressDialog;
	
	private boolean where = false ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.showAnimationIn();
		super.setScreenTag("SendCommnetActivity");
		setContentView(R.layout.panoramics_add_comment_layout);
		
		progressDialog = DialogUtil.createProgressDialog(this);
		
		cancelBtn = (Button)this.findViewById(R.id.cancelBtn);
		contentTextView = (EditText)this.findViewById(R.id.contentTextView);
		sendBtn = (Button)this.findViewById(R.id.sendBtn);
		topImageView = (ImageView)this.findViewById(R.id.topImageView);
		
		cancelBtn.setOnClickListener(this);
		sendBtn.setOnClickListener(this);
		
		Intent intent = this.getIntent(); 
		mediaEntity = (MediaEntity)intent.getSerializableExtra("mediaEntity");
		where= intent.getBooleanExtra("where", false);
		
		if(where){
			contentTextView.setHint("@"+mediaEntity.getNickname()+":");
		}
		
		String lowPhoto = mediaEntity.getLow_resolution();
		if(!TextUtils.isEmpty(lowPhoto)){
			ImageLoader.getInstance().displayImage(lowPhoto, topImageView, ImageConfig.getLookPhotoConfig());
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancelBtn:
			finishCurrentActivityWithAmination();
			break;
		case R.id.sendBtn:
			String content = contentTextView.getText().toString().trim();
			if(TextUtils.isEmpty(content)){
				toast("please comment it!");
				return;
			}
			progressDialog.show();
			CommentService commentService = new CommentService(handler);
			if(where){
				commentService.create(mediaEntity.getMediaId(), getPreString("token"), "@"+mediaEntity.getNickname()+": " +content);
			}else{
				commentService.create(mediaEntity.getMediaId(), getPreString("token"), content);
			}
			break;
		}
	}
	
	/**
	 * 
	 */
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case CommentService.CREATE_COMMENT:{
					switch (msg.arg1) {
					case CommentService.CREATE_COMMENT_SUCCESS:
						String root = (String) msg.obj;
						CommentEntity commentEntity = new CommentEntity();
						try {
							JSONObject rootJsonObject = new JSONObject(root);
							JSONObject data = rootJsonObject.getJSONObject("data");
							String id = data.getString("id");
							String text = data.getString("text");
							JSONObject from = data.getJSONObject("from");
							String accountId = from.getString("id");
							String nickname = from.getString("nickname");
							String username = from.getString("username");
							String profile_picture = from.getString("profile_picture");
							
							commentEntity.setCommentId(id);
							commentEntity.setAccountId(accountId);
							commentEntity.setNickname(nickname);
							commentEntity.setUsername(username);
							commentEntity.setAvatar(profile_picture);
							commentEntity.setText(text);
						}catch(Exception e){
						}
						dismissProgressDialog();
						
						Intent intent = new Intent(ShowCommentsActivity.COMENT_ACTION);  
						Bundle bundle = new Bundle();
						bundle.putSerializable("commentEntity", commentEntity);
						intent.putExtras(bundle);
					    SendCommnetActivity.this.sendBroadcast(intent);
						
						finishCurrentActivityWithAmination();
						break;
					case CommentService.ERROR_CODE400:
					
						break;
					case CommentService.ERROR_CODE401:
						tokenInvalid();
						break;
					case CommentService.ERROR_CODE404:
						
						break;
					case CommentService.ERROR_CODE409:
						
						break;
					default:
						
						break;
					}
				}
				break;
			default:
				break;
			}
		}
	};
	
	public void dismissProgressDialog(){
		if(progressDialog!=null && progressDialog.isShowing()){
			progressDialog.dismiss();
			progressDialog.cancel();
		}
	}
}
