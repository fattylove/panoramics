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
 * 选择性别ActionSheet
 * 
 * @author Fatty
 *
 */
public class ActionSheetGenderDialog {

	private Dialog dlg;
	private Context mContext;
	private OnActionSheetGenderSelectedListener onActionSheetGenderSelectedListener;

	public OnActionSheetGenderSelectedListener getOnActionSheetGenderSelectedListener() {
		return onActionSheetGenderSelectedListener;
	}

	public void setOnActionSheetGenderSelectedListener(
			OnActionSheetGenderSelectedListener onActionSheetGenderSelectedListener) {
		this.onActionSheetGenderSelectedListener = onActionSheetGenderSelectedListener;
	}

	public interface OnActionSheetGenderSelectedListener {
		void onGenderClick(int whichButton);
	}

	public ActionSheetGenderDialog(Context context) {
		this.mContext = context;

		init();
	}

	public void init() {
		dlg = new Dialog(mContext, R.style.ActionSheet);
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.actionsheet_select_gender, null);
		layout.setMinimumWidth(mContext.getSharedPreferences(Constant.APP_NAME, Context.MODE_PRIVATE).getInt("screen_w", 10000));

		TextView T_1 = (TextView) layout.findViewById(R.id.T_1);
		TextView T_2 = (TextView) layout.findViewById(R.id.T_2);
		TextView T_3 = (TextView) layout.findViewById(R.id.T_3);
		TextView cancel = (TextView) layout.findViewById(R.id.cancel);

		T_1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onActionSheetGenderSelectedListener.onGenderClick(0);
				dlg.dismiss();
			}
		});

		T_2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onActionSheetGenderSelectedListener.onGenderClick(1);
				dlg.dismiss();
			}
		});
		
		T_3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onActionSheetGenderSelectedListener.onGenderClick(2);
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

	public void show() {
		dlg.show();
	}

}
