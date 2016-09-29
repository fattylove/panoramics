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
public class ActionSheetRemoveOrReportCommentDialog {

	private Dialog dlg;
	private Context mContext;
	private OnActionSheetRemoveCommentSelectedListener onActionSheetRemoveCommentSelectedListener;
	private TextView T_1;
	private TextView T_2;
	private TextView T_3;
	private int state ;
	
	public static final int REMOVE = 0;
	public static final int REPORT = 1;

	public OnActionSheetRemoveCommentSelectedListener getOnActionSheetRemoveCommentSelectedListener() {
		return onActionSheetRemoveCommentSelectedListener;
	}

	public void setOnActionSheetRemoveCommentSelectedListener(
			OnActionSheetRemoveCommentSelectedListener onActionSheetRemoveCommentSelectedListener) {
		this.onActionSheetRemoveCommentSelectedListener = onActionSheetRemoveCommentSelectedListener;
	}

	public interface OnActionSheetRemoveCommentSelectedListener {
		void onRemoveCommentClick(int whichButton);
	}

	public ActionSheetRemoveOrReportCommentDialog(Context context , int state ) {
		this.mContext = context;
		this.state = state;
		init();
	}

	public void init() {
		dlg = new Dialog(mContext, R.style.ActionSheet);
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.actionsheet_select_remove_or_report_comment, null);
		layout.setMinimumWidth(mContext.getSharedPreferences(Constant.APP_NAME, Context.MODE_PRIVATE).getInt("screen_w", 10000));

		T_1 = (TextView)layout.findViewById(R.id.T_1);
		T_2 = (TextView) layout.findViewById(R.id.T_2);
		T_3 = (TextView) layout.findViewById(R.id.T_3);
		TextView cancel = (TextView) layout.findViewById(R.id.cancel);
		
		if(state == 0){
			T_2.setVisibility(View.GONE);
			T_1.setText(R.string.pano_actionsheet_remove_title);
			T_3.setText(R.string.pano_actionsheet_remove);
			
			T_3.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					onActionSheetRemoveCommentSelectedListener.onRemoveCommentClick(REMOVE);
					dlg.dismiss();
				}
			});
		}
		
		if(state == 1){
			T_2.setVisibility(View.VISIBLE);
			T_1.setText(R.string.pano_actionsheet_remove_report_title);
			T_2.setText(R.string.pano_actionsheet_remove);
			T_3.setText(R.string.pano_actionsheet_report);
			
			T_2.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					onActionSheetRemoveCommentSelectedListener.onRemoveCommentClick(REMOVE);
					dlg.dismiss();
				}
			});
			
			T_3.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					onActionSheetRemoveCommentSelectedListener.onRemoveCommentClick(REPORT);
					dlg.dismiss();
				}
			});
		}
		
		if(state == 2 ){
			T_2.setVisibility(View.GONE);
			T_1.setText(R.string.pano_actionsheet_report_title);
			T_3.setText(R.string.pano_actionsheet_report);
			
			T_3.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					onActionSheetRemoveCommentSelectedListener.onRemoveCommentClick(REPORT);
					dlg.dismiss();
				}
			});
		}
		
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
