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
 * 注销登录ActionSheet
 * 
 * @author Fatty
 *
 */
public class ActionSheetLogoutDialog {

	private Dialog dlg;
	private Context mContext;
	private OnActionSheetLogoutSelectedListener onActionSheetLogoutSelectedListener;

	public OnActionSheetLogoutSelectedListener getOnActionSheetLogoutSelectedListener() {
		return onActionSheetLogoutSelectedListener;
	}

	public void setOnActionSheetLogoutSelectedListener(
			OnActionSheetLogoutSelectedListener onActionSheetLogoutSelectedListener) {
		this.onActionSheetLogoutSelectedListener = onActionSheetLogoutSelectedListener;
	}

	public interface OnActionSheetLogoutSelectedListener {
		void onLogoutClick(int whichButton);
	}

	public ActionSheetLogoutDialog(Context context) {
		this.mContext = context;

		init();
	}

	public void init() {
		dlg = new Dialog(mContext, R.style.ActionSheet);
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.actionsheet_select_logout, null);
		layout.setMinimumWidth(mContext.getSharedPreferences(Constant.APP_NAME, Context.MODE_PRIVATE).getInt("screen_w", 10000));

		TextView T_1 = (TextView) layout.findViewById(R.id.T_1);
		TextView cancel = (TextView) layout.findViewById(R.id.cancel);

		T_1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onActionSheetLogoutSelectedListener.onLogoutClick(0);
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
