package f.com.panoramics.activity;

import java.io.File;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.guxiu.panoramics.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import f.com.panoramics.adapter.CommentAdapter;
import f.com.panoramics.base.ActivityManager;
import f.com.panoramics.base.BaseFragmentActivity;
import f.com.panoramics.constant.Constant;
import f.com.panoramics.entity.CommentEntity;
import f.com.panoramics.entity.MediaEntity;
import f.com.panoramics.receiver.DataChangeReceiver;
import f.com.panoramics.receiver.RemoveMediaReceiver;
import f.com.panoramics.service.jsonmodel.AnalyseCommentJson;
import f.com.panoramics.service.jsonmodel.AnalyseMediaJson;
import f.com.panoramics.service.netservice.CommentService;
import f.com.panoramics.service.netservice.MediaGetService;
import f.com.panoramics.service.netservice.LikeService;
import f.com.panoramics.service.netservice.MediaRemoveService;
import f.com.panoramics.utils.ImageConfig;
import f.com.panoramics.utils.IntervalUtil;
import f.com.panoramics.utils.ShareUtil;
import f.com.panoramics.view.ActionSheetRemoveOrReportCommentDialog;
import f.com.panoramics.view.ActionSheetRemoveOrReportCommentDialog.OnActionSheetRemoveCommentSelectedListener;
import f.com.panoramics.view.ActionSheetRemoveOrReportPhotoDialog;
import f.com.panoramics.view.ActionSheetRemoveOrReportPhotoDialog.OnActionSheetRemovePhotoSelectedListener;
import f.com.panoramics.view.CircleImageView;
import f.com.panoramics.view.LoadMoreListView;
import f.com.panoramics.view.LoadMoreListView.OnLoadMoreListener;
import f.com.panoramics.view.PanoToast;

/**
 * 评论界面
 * 
 * @author Fatty
 */
public class ShowCommentsActivity extends BaseFragmentActivity implements 
OnLoadMoreListener, 
OnActionSheetRemovePhotoSelectedListener,
OnActionSheetRemoveCommentSelectedListener
{

	public static final int REQUEST_CODE = 7;
	
	private Button exitBtn;
	private LinearLayout tagLayout;
	
	private ImageView imgImageView;
	private ProgressBar progress;
	private CircleImageView headerImageView;
	private ImageView shareImageView;
	private ImageView likeImageView;
	
	private TextView nameTextView;
	private TextView myTimeTextView;
	private TextView myLocationTextView;
	private TextView likeNumTextView;
	
	private ActionSheetRemoveOrReportPhotoDialog actionSheetRemovePhotoDialog;
	private ActionSheetRemoveOrReportCommentDialog actionSheetRemoveOrReportCommentDialog;
	/////
	private Button addCommentBtn;
	private LoadMoreListView commonListView;
	private CommentAdapter adapter;
	
	/////
	private MediaEntity loadMediaEntity;
	private String mediaId;
	
	private CommentEntity lastCommentEntity;
	private CommentEntity removeOrReportCommentEntity ;
	
	
	public static final String COMENT_ACTION = "com.guxiu.panoramcis.COMMENT";
	private CommentChangedReceiver commentChangedReceiver;
	public class CommentChangedReceiver extends BroadcastReceiver{
		public void onReceive(Context context, Intent intent) {
			CommentEntity commentEntity = (CommentEntity) intent.getSerializableExtra("commentEntity");
			adapter.addComment(commentEntity);
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.showAnimationBottomIn();
		super.setScreenTag("ShowCommentsActivity");
		setContentView(R.layout.panoramics_comment_layout);
		
		commentChangedReceiver = new CommentChangedReceiver();
		this.registerReceiver(commentChangedReceiver, new IntentFilter(COMENT_ACTION));
		
		Intent intent = this.getIntent(); 
		mediaId = intent.getStringExtra("mediaId");
		
		MediaGetService getMediaService = new MediaGetService(handler);
		getMediaService.get_one(getPreString("token"), mediaId);
	}
	
	/**
	 * 
	 * @param mediaEntity
	 */
	public void init(final MediaEntity mediaEntity){
		if(TextUtils.equals(mediaEntity.getAccountId(), getPreString("uid"))){
			actionSheetRemovePhotoDialog = new ActionSheetRemoveOrReportPhotoDialog(this, 0);
			actionSheetRemovePhotoDialog.setTitle("Remove it?");
			actionSheetRemovePhotoDialog.setContent("Remove");
		}else{
			actionSheetRemovePhotoDialog = new ActionSheetRemoveOrReportPhotoDialog(this, 1);
			actionSheetRemovePhotoDialog.setTitle("Reprot it?");
			actionSheetRemovePhotoDialog.setContent("Reprot");
		}
		actionSheetRemovePhotoDialog.setOnActionSheetRemovePhotoSelectedListener(this);
		
		commonListView = (LoadMoreListView) this.findViewById(R.id.commonListView);
		commonListView.setOnLoadMoreListener(this);
		
		commonListView.addHeaderView(initTopView(mediaEntity));
		adapter = new CommentAdapter(this , mediaEntity);
		commonListView.setAdapter(adapter);
		
		addCommentBtn = (Button)this.findViewById(R.id.addCommentBtn);
		addCommentBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra("where", false);
				Bundle bundle = new Bundle();
				bundle.putSerializable("mediaEntity", mediaEntity);
				intent.putExtras(bundle);
				intent.setClass(ShowCommentsActivity.this, SendCommnetActivity.class);
				ShowCommentsActivity.this.startActivity(intent);
			}
		});
		
		loadComments(null);
	}
	
	/**
	 * 初始化topView
	 * 
	 * @return
	 */
	public View initTopView(final MediaEntity mediaEntity ) {
		View view = LayoutInflater.from(this).inflate(R.layout.panoramics_comment_topview_layout, null);
		
		exitBtn = (Button)view.findViewById(R.id.exitBtn);
		imgImageView = (ImageView)view.findViewById(R.id.imgImageView);
		progress = (ProgressBar)view.findViewById(R.id.progress);
		Drawable draw = this.getResources().getDrawable(R.drawable.p_myprogressbar_style);
		progress.setProgressDrawable(draw);
		
		headerImageView = (CircleImageView)view.findViewById(R.id.headerImageView);
		tagLayout = (LinearLayout)view.findViewById(R.id.tagLayout);
		
		shareImageView = (ImageView)view.findViewById(R.id.shareImageView);
		likeImageView = (ImageView)view.findViewById(R.id.likeImageView);
		
		nameTextView = (TextView)view.findViewById(R.id.nameTextView);
		myTimeTextView = (TextView)view.findViewById(R.id.myTimeTextView);
		myLocationTextView = (TextView)view.findViewById(R.id.myLocationTextView);
		likeNumTextView = (TextView)view.findViewById(R.id.likeNumTextView);
		
		final String lowPhoto = mediaEntity.getLow_resolution();
		final String avatar = mediaEntity.getAvatar();
		final String nickname = mediaEntity.getNickname();
		final long publishTime = mediaEntity.getP_time();
		final String location = mediaEntity.getLocation();
		final int likes = mediaEntity.getLike();
		final String tags = mediaEntity.getTags();
		
		ImageLoader.getInstance().displayImage(lowPhoto, imgImageView , ImageConfig.getLookPhotoConfig(), new SimpleImageLoadingListener() {
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				progress.setProgress(0);
				progress.setVisibility(View.VISIBLE);
			}

			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
				progress.setVisibility(View.GONE);
			}

			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				progress.setVisibility(View.GONE);
			}
		}, new ImageLoadingProgressListener() {
			@Override
			public void onProgressUpdate(String imageUri, View view, int current, int total) {
				progress.setProgress(Math.round(100.0f * current / total));
			}
		});
		
		ImageLoader.getInstance().displayImage(avatar, headerImageView , ImageConfig.getHeaderConfig());
		
		shareImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				File file = ImageLoader.getInstance().getDiskCache().get(lowPhoto);
				ShareUtil.share(ShowCommentsActivity.this ,file.getAbsolutePath().toString());
			}
		});
		
		exitBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finishCurrentActivity();
				showAnimationBottomOut();
			}
		});
		
		imgImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent imgIntent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putSerializable("mediaEntity", mediaEntity);
				imgIntent.putExtras(bundle);
				imgIntent.setClass(ShowCommentsActivity.this, ImageLookActivity.class);
				ShowCommentsActivity.this.startActivityForResult(imgIntent, REQUEST_CODE);
			}
		});
		
		imgImageView.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				actionSheetRemovePhotoDialog.show();
				return true;
			}
		});
		
		try {
			JSONArray arr = new JSONArray(tags);
			if(arr.length() > 0){
				for(int i=0 ; i< arr.length() ; i++){
					View tagView = LayoutInflater.from(ShowCommentsActivity.this).inflate(R.layout.panoramics_tag_layout, null);
					TextView tagTextView = (TextView)tagView.findViewById(R.id.myTagsTextView);
					tagTextView.setText(arr.getString(i));
					tagLayout.addView(tagView);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		nameTextView.setText(nickname);
		myTimeTextView.setText(IntervalUtil.getInterval(publishTime));
		myLocationTextView.setText(location);
		
		/*****************     处理Like  **********************/
		likeNumTextView.setText(likes+"");
		if(mediaEntity.getLikeState() == Constant.LIKED){
			likeImageView.setBackgroundResource(R.drawable.p_like_on);
			likeImageView.setTag(Constant.LIKED);
		}else if(mediaEntity.getLikeState() == Constant.UNLIKED){
			likeImageView.setBackgroundResource(R.drawable.p_like_off);
			likeImageView.setTag(Constant.UNLIKED);
		}
		
		likeImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LikeService likeService = new LikeService(handler);
				Integer likeState  = (Integer) v.getTag();
				if(likeState == Constant.LIKED){
					
					int count = mediaEntity.getLike();
					v.setTag(Constant.UNLIKED);
					v.setBackgroundResource(R.drawable.p_like_off);
					likeNumTextView.setText( (count-1) + "");
					
					mediaEntity.setLikeState(Constant.UNLIKED);
					mediaEntity.setLike(count-1);
					Intent intent = new Intent(DataChangeReceiver.ACTION);  
					Bundle bundle = new Bundle();
					bundle.putSerializable("mediaEntity", mediaEntity);
					intent.putExtras(bundle);
				    ShowCommentsActivity.this.sendBroadcast(intent);
				    
				    likeService.deleteLike(getPreString("token"), mediaEntity.getMediaId());
				}else if(likeState == Constant.UNLIKED){
					
					int count = mediaEntity.getLike();
					v.setTag(Constant.LIKED);
					v.setBackgroundResource(R.drawable.p_like_on);
					likeNumTextView.setText((count+1) + "");
					
					mediaEntity.setLikeState(Constant.LIKED);
					mediaEntity.setLike(count+1);
					Intent intent = new Intent(DataChangeReceiver.ACTION);  
					Bundle bundle = new Bundle();
					bundle.putSerializable("mediaEntity", mediaEntity);
					intent.putExtras(bundle);
					ShowCommentsActivity.this.sendBroadcast(intent);
					
					likeService.postLike(getPreString("token"), mediaEntity.getMediaId());
				}
			}
		});
		return view;
	}
	
	/**
	 * 加载Comment
	 * 
	 * @param minTime
	 * @param maxTime
	 */
	public void loadComments(String maxTime ){
		CommentService commentService = new CommentService(handler);
		commentService.list(mediaId, getPreString("token"), maxTime);
	}

	/**
	 * 加载更多
	 */
	public void onLoadMore() {
		loadComments(lastCommentEntity.getCreateTime()+"");
	}
	
	
	@Override
	public void onRemovePhotoClick(int whichButton) {
		switch (whichButton) {
		case 0:
			MediaRemoveService removeMediaService = new MediaRemoveService(handler);
			removeMediaService.remove(getPreString("token"), mediaId);
			break;
		case 1:
			feedback(ShowCommentsActivity.this);
			break;
		default:
			break;
		}
	}
	
	/*************************  Remove and Report Comment Action Sheet ***********************************/
	public void initActionSheet(int state , CommentEntity commentEntity){
		removeOrReportCommentEntity = commentEntity;
		actionSheetRemoveOrReportCommentDialog = new ActionSheetRemoveOrReportCommentDialog(this, state);
		actionSheetRemoveOrReportCommentDialog.setOnActionSheetRemoveCommentSelectedListener(this);
		actionSheetRemoveOrReportCommentDialog.show();
	}

	@Override
	public void onRemoveCommentClick(int whichButton) {
		switch (whichButton) {
		case ActionSheetRemoveOrReportCommentDialog.REMOVE:
			adapter.removeComment(removeOrReportCommentEntity);
			adapter.notifyDataSetChanged();
			CommentService commentService = new CommentService(handler);
			commentService.delete(mediaId, removeOrReportCommentEntity.getCommentId(), getPreString("token"));
			break;
		case ActionSheetRemoveOrReportCommentDialog.REPORT:
			feedback(ShowCommentsActivity.this);
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(commentChangedReceiver!=null){
			this.unregisterReceiver(commentChangedReceiver);
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			ActivityManager.getAppManager().finishActivity(this);
			showAnimationBottomOut();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	/**********************************  Feed back  *********************************/
	public void feedback(Context context){
		Intent email = new Intent(android.content.Intent.ACTION_SEND);
		email.setType("plain/text");
		String[] emailReciver = new String[] { "Feedback@guxiu.ca" };
		String[] emailCReciver = new String[] { "hi@guxiu.ca" };
		
		String emailSubject = "Report it? please write your text.";
		email.putExtra(android.content.Intent.EXTRA_EMAIL,   emailReciver);
		email.putExtra(android.content.Intent.EXTRA_CC,      emailCReciver);
		email.putExtra(android.content.Intent.EXTRA_SUBJECT, emailSubject);
		startActivity(Intent.createChooser(email, "Choose Email Client"));
	}
	
	/************************************************************************/
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			
			
			//  remove media
			case MediaRemoveService.REMOVE_MEDIA:
				switch (msg.arg1) {
				case MediaRemoveService.DELETE_SUCCESS:
					
					if(loadMediaEntity!=null){
						// 清除缓存
//						MediaDBService mediaDBService = new MediaDBService(ShowCommentsActivity.this);
//						mediaDBService.deleteMedia(loadMediaEntity , stateTag);
						
						finishCurrentActivity();
						showAnimationBottomOut();
						
						Intent intent = new Intent(RemoveMediaReceiver.ACTION);  
						Bundle bundle = new Bundle();
						bundle.putSerializable("mediaEntity", loadMediaEntity);
						intent.putExtras(bundle);
						ShowCommentsActivity.this.sendBroadcast(intent);
					}
					
					break;
				case MediaRemoveService.ERROR_CODE400:
					break;
				case MediaRemoveService.ERROR_CODE401:
					tokenInvalid();
					break;
				case MediaRemoveService.ERROR_CODE404:
					toast("Can’t find media.");
					break;
				case MediaRemoveService.ERROR_CODE409:
					toast("Cancel the operation at error occuring.");
					break;
				case MediaRemoveService.ERROR_CODE503:
					toast("Fail to restore media.");
					break;
				default:
					PanoToast.showToast(ShowCommentsActivity.this, null);
					break;
				}
				break;
			
				
			/// like post
			case LikeService.LIKE_POST:{
				switch (msg.arg1) {
				case LikeService.LIKE_POST_SUCCESS:
					break;
				case LikeService.ERROR_CODE400:
					break;
				case LikeService.ERROR_CODE401:
					break;
				case LikeService.ERROR_CODE409:
					break;
				default:
					break;
				}
			}
			break;
			
			
			////Like remove
			case LikeService.LIKE_REMOVE:{
				switch (msg.arg1) {
				case LikeService.LIKE_REMOVE_SUCCESS:
					String root = (String) msg.obj;
					System.err.println(root);
					break;
				case LikeService.ERROR_CODE400:
					break;
				case LikeService.ERROR_CODE401:
					break;
				case LikeService.ERROR_CODE409:
					break;
				default:
					break;
				}
			}
			break;
			
			///get media one
			case MediaGetService.GET_MEDIA_ONE:{
				switch (msg.arg1) {
				
				case MediaGetService.GET_MEDIA_ONE_SUCCESS:
					String mediaRoot = (String) msg.obj;
					if(TextUtils.isEmpty(mediaRoot)){
						return;
					}
					System.err.println(mediaRoot);
					MediaEntity media = AnalyseMediaJson.getSingleMedia(mediaRoot);
					init(media);
					loadMediaEntity = media;
					break;
					
				case MediaGetService.ERROR_CODE400:
					
					break;
				case MediaGetService.ERROR_CODE401:
					tokenInvalid();
					break;
				default:
					PanoToast.showToast(ShowCommentsActivity.this, "No Internet Connection.");
					break;
				}
			}
			break;
			
			//Comment list
			case CommentService.LIST_COMMENT:{
					switch (msg.arg1) {
					case CommentService.LIST_COMMENT_SUCCESS:
						String root = (String) msg.obj;
						if(TextUtils.isEmpty(root)){
							return ;
						}
						
						LinkedList<CommentEntity> commentEntities = AnalyseCommentJson.getAllComments(root);
						
						if(!commentEntities.isEmpty()){
							lastCommentEntity = commentEntities.getLast();
							adapter.addComments(commentEntities);
							commonListView.onLoadMoreComplete();
						}else{
							commonListView.setCanLoadMore(false);
						}
						
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
				
			//delete comment
			case CommentService.DELETE_COMMENT:{
				switch (msg.arg1) {
					case CommentService.DELETE_COMMENT_SUCCESS:
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

	@Override
	protected void onActivityResult(int requestCode, int arg1, Intent data) {
		super.onActivityResult(requestCode, arg1, data);
		switch (requestCode) {
		case REQUEST_CODE:
			if(data!=null){
				MediaEntity mediaEntity = (MediaEntity)data.getSerializableExtra("mediaEntity");
				likeNumTextView.setText(mediaEntity.getLike()+"");
				if(mediaEntity.getLikeState() == Constant.LIKED){
					likeImageView.setBackgroundResource(R.drawable.p_like_on);
					likeImageView.setTag(Constant.LIKED);
				}else if(mediaEntity.getLikeState() == Constant.UNLIKED){
					likeImageView.setBackgroundResource(R.drawable.p_like_off);
					likeImageView.setTag(Constant.UNLIKED);
				}
			}
			break;

		default:
			break;
		}
	}
	
	
}
