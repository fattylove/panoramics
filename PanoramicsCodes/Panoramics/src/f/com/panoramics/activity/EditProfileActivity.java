package f.com.panoramics.activity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amazonaws.event.ProgressEvent;
import com.amazonaws.event.ProgressListener;
import com.amazonaws.mobileconnectors.s3.transfermanager.TransferManager;
import com.amazonaws.mobileconnectors.s3.transfermanager.Upload;
import com.guxiu.panoramics.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import f.com.panoramics.base.BaseFragmentActivity;
import f.com.panoramics.constant.Constant;
import f.com.panoramics.entity.AccountEntity;
import f.com.panoramics.service.dbservice.AccountDBService;
import f.com.panoramics.service.netservice.AccountService;
import f.com.panoramics.utils.AS3Util;
import f.com.panoramics.utils.ImageConfig;
import f.com.panoramics.utils.ImageUtil;
import f.com.panoramics.utils.MD5Util;
import f.com.panoramics.view.ActionSheetGenderDialog;
import f.com.panoramics.view.ActionSheetGenderDialog.OnActionSheetGenderSelectedListener;
import f.com.panoramics.view.ActionSheetPhotoDialog;
import f.com.panoramics.view.ActionSheetPhotoDialog.OnActionSheetPhotoSelectedListener;
import f.com.panoramics.view.CircleImageView;
import f.com.panoramics.view.PanoToast;


/**
 * 
 * @author Fatty
 * 
 * 个人信息设置界面
 *
 */
public class EditProfileActivity extends BaseFragmentActivity implements 
OnClickListener,
OnActionSheetGenderSelectedListener,
OnActionSheetPhotoSelectedListener{
	
	private Button cancelBtn;
	private Button doneBtn;
	private ProgressBar progressBar;
	
	private CircleImageView headerImageView;
	
	private LinearLayout changePasswordLayout;
	private LinearLayout genderLayout;
	private EditText uNameEditText;
	private EditText uNicknameEditText;
	private EditText websiteEditText;
	private EditText bioEditText;
	private EditText mailEditText;
	private EditText phoneEditText;
	private TextView genderTextView; 
	
	public static final int PHOTO_CAMERA = 0; // 表示图片采集
	public static final int PHOTO_GALLERY = 1; // 表示图片选取
	
	private String uName;
	private String uNickname;
	private String website;
	private String bio;
	private String mail;
	private String phone;
	private String gender;
	
	//ActionSheet
	private ActionSheetGenderDialog genderActionSheet ;
	private ActionSheetPhotoDialog photoSelectSheet;
	private static final String MALE = "M";
	private static final String FEMALE = "F";
	private static final String OTHERS = "MF";
	private final String[] genders = new String[]{"Male","Female","Others"};
	
	/**
	 * Upload Photo ,use AS3
	 */
	private TransferManager transferManager;
	private Upload mUpload;
	private static final int COMPLETED_EVENT_CODE = 8001;
	private static final int FAILED_EVENT_CODE = 8002;
	
	private String fileName ;
	private String uploadPhotoUrl ="http://"+Constant.BUCKET_NAME+".s3.amazonaws.com/";
	
	private AccountDBService accountDBService;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.showAnimationIn();
		super.setScreenTag("EditProfileActivity");
		setContentView(R.layout.panoramics_profile_layout);
		accountDBService = new AccountDBService(this);
		
		//title views
		progressBar = (ProgressBar)this.findViewById(R.id.progressBar);
		cancelBtn = (Button)this.findViewById(R.id.cancelBtn);
		doneBtn = (Button)this.findViewById(R.id.doneBtn);
		cancelBtn.setOnClickListener(this);
		doneBtn.setOnClickListener(this);
		
		//Header ImageView
		headerImageView  = (CircleImageView)this.findViewById(R.id.headerImageView);
		headerImageView.setOnClickListener(this);
		
		//Layout
		changePasswordLayout = (LinearLayout)this.findViewById(R.id.changePasswordLayout);
		genderLayout = (LinearLayout)this.findViewById(R.id.genderLayout);
		changePasswordLayout.setOnClickListener(this);
		genderLayout.setOnClickListener(this);
		
		//EditText
		uNameEditText = (EditText)this.findViewById(R.id.uNameEditText);
		uNicknameEditText = (EditText)this.findViewById(R.id.uNicknameEditText);
		websiteEditText = (EditText)this.findViewById(R.id.websiteEditText);
		bioEditText = (EditText)this.findViewById(R.id.bioEditText);
		mailEditText = (EditText)this.findViewById(R.id.mailEditText);
		phoneEditText = (EditText)this.findViewById(R.id.phoneEditText);
		genderTextView = (TextView)this.findViewById(R.id.sexEditText);
		
		//Sheet
		genderActionSheet = new ActionSheetGenderDialog(this);
		photoSelectSheet = new ActionSheetPhotoDialog(this);
		genderActionSheet.setOnActionSheetGenderSelectedListener(this);
		photoSelectSheet.setOnActionSheetPhotoSelectedListener(this);
		
		//defaultData
		if(!TextUtils.isEmpty(getIntent().getStringExtra("uid"))){
			AccountEntity accountEntity = accountDBService.findByAccountId(getIntent().getStringExtra("uid"));
			if(accountEntity!=null){
				ImageLoader.getInstance().displayImage(accountEntity.getAvatar() ,headerImageView,ImageConfig.getHeaderConfig());
				uNameEditText.setText(accountEntity.getUsername());
				uNicknameEditText.setText(accountEntity.getNickname());
				mailEditText.setText(accountEntity.getPassport());
				websiteEditText.setText(accountEntity.getWebsite());
				bioEditText.setText(accountEntity.getBio());
				phoneEditText.setText(accountEntity.getPhone());
				if(!TextUtils.isEmpty(accountEntity.getGender())){
					if(accountEntity.getGender().equals(MALE)){
						genderTextView.setText(genders[0]);
					}else if(accountEntity.getGender().equals(FEMALE)){
						genderTextView.setText(genders[1]);
					}else if(accountEntity.getGender().equals(OTHERS)){
						genderTextView.setText(genders[2]);
					}
				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.headerImageView:
			photoSelectSheet.show();
			break;
		case R.id.changePasswordLayout:
			Intent goChangePaswordIntent = new Intent();
			goChangePaswordIntent.setClass(EditProfileActivity.this, ChangePwdActivity.class);
			EditProfileActivity.this.startActivity(goChangePaswordIntent);
			break;
		case R.id.cancelBtn:
			finishCurrentActivityWithAmination();
			break;
		case R.id.doneBtn:
			uName = uNameEditText.getText().toString().trim();
			uNickname = uNicknameEditText.getText().toString().trim();
			website = websiteEditText.getText().toString().trim();
			bio = bioEditText.getText().toString().trim();
			mail = mailEditText.getText().toString().trim();
			phone = phoneEditText.getText().toString().trim();
			gender = genderTextView.getText().toString().trim();
			
			AccountEntity entity = new AccountEntity();
			entity.setAccountId(getPreString("uid"));
			entity.setNickname(uNickname);
			entity.setUsername(uName);
			entity.setWebsite(website);
			entity.setBio(bio);
			entity.setPassport(mail);
			entity.setPhone(phone);
			
			if(gender.equals(genders[0])){
				entity.setGender(MALE);
			}else if(gender.equals(genders[1])){
				entity.setGender(FEMALE);
			}else if(gender.equals(genders[2])){
				entity.setGender(OTHERS);
			}
			
			doneBtn.setVisibility(View.GONE);
			progressBar.setVisibility(View.VISIBLE);
			AccountService infoService = new AccountService(handler);
			infoService.putAccountEntity(entity , getPreString("token"));
			break;
		case R.id.genderLayout:
			genderActionSheet.show();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case PHOTO_CAMERA:
			File picture = new File(Constant.PIC, "header.jpg");
			if(!picture.exists()){
				return;
			}
			try {
				photoTools(Uri.fromFile(picture));
			} catch (Exception e1) {
				return;
			}
			break;
		case PHOTO_GALLERY:
			if (data == null)  return;
			else{
				if(data.getData()!=null){
					try {
						photoTools(data.getData());
					} catch (Exception e) {
					}
				}else return;
			}
			break;
		case ImageUtil.ImageFixUtils.PHOTORESOULT:
			if(data!=null){
				Bitmap headerBitmap = data.getParcelableExtra("data");
				if(headerBitmap!=null){
					try{
						File checkFile = new File(Constant.CACHE);
						if(!checkFile.exists()){
							checkFile.mkdirs();
						}
			            File cutPhotoFile = new File(Constant.CACHE , MD5Util.makeMD5(System.currentTimeMillis()+"")+".jpg");  
			            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(cutPhotoFile));  
			            headerBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);  
			            bos.flush();  
			            bos.close();
			            if(cutPhotoFile.exists()){
			            	progressBar.setVisibility(View.VISIBLE);
			            	doneBtn.setVisibility(View.INVISIBLE);
				        	sharePhoto(cutPhotoFile);
				        }
			        } catch(Exception e){
			           return;
			        }
				}
			}else{
				return;
			}
			break;
		}
	}
	
	/**
	 * Fix Photo
	 * 
	 * @param imageUri
	 * @throws Exception
	 */
	public void photoTools(final Uri imageUri) throws Exception{
		if (imageUri != null) {
			ImageUtil.ImageFixUtils.photoZoom(this, imageUri, 300, 300);
		} 
	}
	
	/**
	 * Handler
	 */
	private final Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			
			
			
			//PUT_ACCOUNT
			case AccountService.PUT_ACCOUNT:
				switch (msg.arg1) {
				case AccountService.ACCOUNT_SUCCESS:
					String root = (String) msg.obj;
					if(TextUtils.isEmpty(root)){
						return;
					}
					doneBtn.setVisibility(View.VISIBLE);
					progressBar.setVisibility(View.GONE);
					finishCurrentActivityWithAmination();
					break;
				case AccountService.ERROR_CODE400:
					doneBtn.setVisibility(View.VISIBLE);
					progressBar.setVisibility(View.GONE);
					toast("Bad Request");
					break;
				case AccountService.ERROR_CODE401:
					doneBtn.setVisibility(View.VISIBLE);
					progressBar.setVisibility(View.GONE);
					tokenInvalid();
					break;
				case AccountService.ERROR_CODE404:
					doneBtn.setVisibility(View.VISIBLE);
					progressBar.setVisibility(View.GONE);
					toast("the account is inexistent");
					break;
				default:
					doneBtn.setVisibility(View.VISIBLE);
					progressBar.setVisibility(View.GONE);
					PanoToast.showToast(EditProfileActivity.this, null);
					break;
				}
				break;
				
				
				
			//uploadHeader Success
			case AccountService.UPLOAD_HEADER:
				switch (msg.arg1) {
				case AccountService.ACCOUNT_SUCCESS:
					progressBar.setVisibility(View.GONE);
					doneBtn.setVisibility(View.VISIBLE);
					
					String uploadResultRoot = (String) msg.obj;
					if(TextUtils.isEmpty(uploadResultRoot)){
						return;
					}
					try {
						JSONObject root = new JSONObject(uploadResultRoot);
						JSONObject data = root.getJSONObject("data");
						String avatar = data.getString("profile_picture");
						ImageLoader.getInstance().displayImage(avatar ,headerImageView ,ImageConfig.getHeaderConfig());
					} catch (JSONException e) {
						e.printStackTrace();
					}
					break;
				case AccountService.ERROR_CODE400:
					progressBar.setVisibility(View.GONE);
					doneBtn.setVisibility(View.VISIBLE);
					break;
				case AccountService.ERROR_CODE401:
					progressBar.setVisibility(View.GONE);
					doneBtn.setVisibility(View.VISIBLE);
					tokenInvalid();
					break;
				case AccountService.ERROR_CODE404:
					progressBar.setVisibility(View.GONE);
					doneBtn.setVisibility(View.VISIBLE);
					break;
				default:
					progressBar.setVisibility(View.GONE);
					doneBtn.setVisibility(View.VISIBLE);
					PanoToast.showToast(EditProfileActivity.this, null);
					break;
				}
				break;
				
				
				
			case COMPLETED_EVENT_CODE:		
				String uploadHeaderUrl =(String)msg.obj;
				ImageLoader.getInstance().displayImage(uploadHeaderUrl ,headerImageView ,ImageConfig.getHeaderConfig());
				AccountService accountService = new AccountService(handler);
				accountService.uploadAccountEntity(getPreString("uid"), getPreString("token"), uploadHeaderUrl);
				break;		
				
				
				
			case FAILED_EVENT_CODE:
				PanoToast.showToast(EditProfileActivity.this, null);
				break;
	
				
			//others
			default :
				break;
			}
		}
	};

	/***********************************    AS3 Share avatar  ***********************************/
	/**
	 * 
	 * @param mFile
	 */
	public void sharePhoto(final File mFile){
		transferManager = new TransferManager(AS3Util.getCredProvider(this));
		new Thread(new Runnable() {
			@Override
			public void run() {
				mUpload = transferManager.upload(Constant.BUCKET_NAME.toLowerCase(Locale.US), AS3Util.getHeaderPath(mFile.getName()),  mFile);
				mUpload.addProgressListener(new upLoadProgressListener());
				fileName = AS3Util.getHeaderPath(mFile.getName());
				uploadPhotoUrl = uploadPhotoUrl + fileName;
			}
		}).start();
	}
	
	/**
	 * 
	 * @author Fatty
	 *     
	 *      upLoadProgressListener
	 */ 
	public class upLoadProgressListener implements ProgressListener {
		@Override
		public void progressChanged(ProgressEvent event) {
			if (event.getEventCode() == ProgressEvent.COMPLETED_EVENT_CODE) {
				Message message = new Message();
				message.obj = uploadPhotoUrl;
				message.what = COMPLETED_EVENT_CODE;
				handler.sendMessage(message);
			}else if(event.getEventCode() == ProgressEvent.FAILED_EVENT_CODE){
				Message message = new Message();
				message.what = FAILED_EVENT_CODE;
				handler.sendMessage(message);
			}
		}
	}
	
	
	/****************************      ActionSheet    ********************************/
	@Override
	public void onPhotoClick(int whichButton) {
		switch (whichButton) {

		case 0:
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					Intent cIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					File file = new File(Constant.PIC);
					if (!file.exists()) {
						file.mkdirs();
					}
					cIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(file, "header.jpg")));
					startActivityForResult(cIntent, PHOTO_CAMERA);
				}
			}, 200);
			break;
			
		case 1:
			Intent picture = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(picture, PHOTO_GALLERY);
			break;

		default:
			break;
		}
	}

	@Override
	public void onGenderClick(int whichButton) {
		switch (whichButton) {
		case 0:
			genderTextView.setText(genders[0]);
			break;
		case 1:
			genderTextView.setText(genders[1]);
			break;
		case 2:
			genderTextView.setText(genders[2]);
			break;
		}
	}
}
