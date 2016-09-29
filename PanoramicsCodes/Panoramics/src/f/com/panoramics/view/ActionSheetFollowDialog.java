package f.com.panoramics.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guxiu.panoramics.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import f.com.panoramics.constant.Constant;
import f.com.panoramics.utils.ImageConfig;

/**
 * Unfollow ActionSheet
 * 
 * @author Fatty
 * 
 */
public class ActionSheetFollowDialog {

	/**
	 * 
	 * @author Fatty
	 * 
	 */
	public interface OnActionSheetSelectedListener {
		void onClick(int whichButton);
	}

	private Context mContext;
	private OnActionSheetSelectedListener onActionSheetSelectedListener;
	
	private Dialog dialog;
	
	private CircleImageView headerImageView;
	private TextView unfollowNicknameTextView;

	public ActionSheetFollowDialog(Context context) {
		this.mContext = context;
		
		 init();
	}

	public void init() {
		dialog = new Dialog(mContext, R.style.ActionSheet);
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = (LinearLayout) inflater.inflate( R.layout.actionsheet_select_follow, null);
		layout.setMinimumWidth(mContext.getSharedPreferences(Constant.APP_NAME, Context.MODE_PRIVATE).getInt("screen_w", 10000));
		
		headerImageView = (CircleImageView) layout.findViewById(R.id.headerImageView);
		unfollowNicknameTextView = (TextView) layout.findViewById(R.id.unfollowNicknameTextView);
		Button unfollowBtn = (Button) layout.findViewById(R.id.unfollowBtn);
		Button cancelBtn = (Button) layout.findViewById(R.id.cancelBtn);

		Window w = dialog.getWindow();
		WindowManager.LayoutParams lp = w.getAttributes();
		lp.x = 0;
		final int cMakeBottom = -1000;
		lp.y = cMakeBottom;
		lp.gravity = Gravity.BOTTOM;
		dialog.onWindowAttributesChanged(lp);
		dialog.setContentView(layout);
		
		dialog.setOnDismissListener(new OnDismissListener() {
			public void onDismiss(DialogInterface dialog) {
				onActionSheetSelectedListener.onClick(0);
			}
		});
		
		unfollowBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onActionSheetSelectedListener.onClick(1);
				dialog.dismiss();
			}
		});

		cancelBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onActionSheetSelectedListener.onClick(0);
				dialog.dismiss();
			}
		});
	}
	
	/**
	 * 
	 * @param headerBitmap
	 */
	public void setHeaderImage(String avater){
		ImageLoader.getInstance().displayImage(avater, headerImageView, ImageConfig.getHeaderConfig());
	}
	
	/**
	 * 
	 * @param nickname
	 */
	public void setNickname(String nickname){
		unfollowNicknameTextView.setText(nickname);
	}

	/**
	 * 
	 * @param onActionSheetSelectedListener
	 */
	public void setOnActionSheetSelectedListener( OnActionSheetSelectedListener onActionSheetSelectedListener) {
		this.onActionSheetSelectedListener = onActionSheetSelectedListener;
	}

	/**
	 * 
	 */
	public void show() {
		dialog.show();
	}

}
