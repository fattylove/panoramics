package f.com.panoramics.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

import com.guxiu.panoramics.R;

/**
 * 
 * @author Fatty
 *
 */
public class UploadProgressDialog extends Dialog {
	
	private static UploadProgressDialog customProgressDialog = null;

	private UploadProgressDialog(Context context) {
		super(context);
	}

	private UploadProgressDialog(Context context, int theme) {
		super(context, theme);
	}

	/**
	 * 创建Earth对话框
	 * @param context
	 * @return
	 */
	public static UploadProgressDialog createDialog(Context context) {
		customProgressDialog = new UploadProgressDialog(context , R.style.customProgressDialog);
		customProgressDialog.setContentView(R.layout.progress_dialog);
		customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		return customProgressDialog;
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		if (customProgressDialog == null) {
			return;
		}
		ImageView imageView = (ImageView) customProgressDialog.findViewById(R.id.loadingUploadImageView);
		AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();
		animationDrawable.start();
	}
	
	/**
	 * 
	 * @param context
	 */
	public void setMessage(String context) {
		TextView  loadingText = (TextView) customProgressDialog.findViewById(R.id.loadingText);
		loadingText.setText(context);
	}
}