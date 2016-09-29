package f.com.panoramics.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guxiu.panoramics.R;

import f.com.panoramics.constant.Constant;

/**
 * 
 * @author Fatty
 *
 */
public class ActionSheetRemoveOrReportPhotoDialog {

	private Dialog dlg;
	private Context mContext;
	private OnActionSheetRemovePhotoSelectedListener onActionSheetRemovePhotoSelectedListener;
	private TextView T_1;
	private TextView T_2;
	private int state ;
	
	public static final int REMOVE = 0;
	public static final int REPORT = 1;

	public OnActionSheetRemovePhotoSelectedListener getOnActionSheetRemovePhotoSelectedListener() {
		return onActionSheetRemovePhotoSelectedListener;
	}

	public void setOnActionSheetRemovePhotoSelectedListener(
			OnActionSheetRemovePhotoSelectedListener onActionSheetRemovePhotoSelectedListener) {
		this.onActionSheetRemovePhotoSelectedListener = onActionSheetRemovePhotoSelectedListener;
	}

	public interface OnActionSheetRemovePhotoSelectedListener {
		void onRemovePhotoClick(int whichButton);
	}

	public ActionSheetRemoveOrReportPhotoDialog(Context context , int state ) {
		this.mContext = context;
		this.state = state;
		init();
	}

	public void init() {
		dlg = new Dialog(mContext, R.style.ActionSheet);
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.actionsheet_select_remove_or_report_photo, null);
		layout.setMinimumWidth(mContext.getSharedPreferences(Constant.APP_NAME, Context.MODE_PRIVATE).getInt("screen_w", 10000));

		T_1 = (TextView)layout.findViewById(R.id.T_1);
		T_2 = (TextView) layout.findViewById(R.id.T_2);
		TextView cancel = (TextView) layout.findViewById(R.id.cancel);

		T_2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onActionSheetRemovePhotoSelectedListener.onRemovePhotoClick(state);
				dlg.dismiss();
			}
		});

		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dlg.dismiss();
			}
		});

		Window w = dlg.getWindow();
		WindowManager.LayoutParams lp = w.getAttributes();
		lp.x = 0;
		final int cMakeBottom = -1000;
		lp.y = cMakeBottom;
		lp.gravity = Gravity.BOTTOM;
		dlg.onWindowAttributesChanged(lp);
		dlg.setContentView(layout);
	}
	
	public void setTitle(String title){
		T_1.setText(title);
	}
	
	public void setContent(String content){
		T_2.setText(content);
	}

	public void show() {
		dlg.show();
	}

}
