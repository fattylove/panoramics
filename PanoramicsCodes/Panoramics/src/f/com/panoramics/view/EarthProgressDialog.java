package f.com.panoramics.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.widget.ImageView;

import com.guxiu.panoramics.R;

/**
 * 
 * @author Fatty
 *
 */
public class EarthProgressDialog extends Dialog {
	
	private static EarthProgressDialog customProgressDialog = null;

	private EarthProgressDialog(Context context) {
		super(context);
	}

	private EarthProgressDialog(Context context, int theme) {
		super(context, theme);
	}

	/**
	 * 创建Earth对话框
	 * @param context
	 * @return
	 */
	public static EarthProgressDialog createDialog(Context context) {
		customProgressDialog = new EarthProgressDialog(context , R.style.customProgressDialog);
		customProgressDialog.setContentView(R.layout.progressdialog);
		customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		return customProgressDialog;
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		if (customProgressDialog == null) {
			return;
		}
		ImageView imageView = (ImageView) customProgressDialog.findViewById(R.id.loadingImageView);
		AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();
		animationDrawable.start();
	}
}