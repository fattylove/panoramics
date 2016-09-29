package f.com.panoramics.amazon.upload;

import java.io.File;
import java.util.Locale;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.s3.transfermanager.TransferManager;
import com.amazonaws.mobileconnectors.s3.transfermanager.TransferProgress;
import com.amazonaws.mobileconnectors.s3.transfermanager.Upload;

import f.com.panoramics.activity.SharePhotoActivity;
import f.com.panoramics.constant.Constant;
import f.com.panoramics.utils.AS3Util;

/**
 * 
 * @author Fatty
 *
 */
public class AS3UploadTask extends AsyncTask<String, Void, String> {
	
	private final SharePhotoActivity sActivity;
	private String file;


	public AS3UploadTask(SharePhotoActivity sActivity, String file) {
		this.sActivity = sActivity;
		this.file = file;
	}

	private ProgressDialog mProgressDialog;

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mProgressDialog = ProgressDialog.show(sActivity,"", "Uploading..."); 
		mProgressDialog.setCanceledOnTouchOutside(true);
		mProgressDialog.setCancelable(true);
		mProgressDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dlg) {
				AS3UploadTask.this.cancel(true);
			}
		});
	}

	@Override
	protected String doInBackground(String... params) {
		TransferManager transferManager = new TransferManager(AS3Util.getCredProvider(sActivity));
		Upload mUpload = transferManager.upload(Constant.BUCKET_NAME.toLowerCase(Locale.US), AS3Util.getMediaPath(new File(file).getName()), new File(file));
		TransferProgress progress = mUpload.getProgress();
		return progress.getTotalBytesToTransfer() +"";
	}

	@Override
	protected void onPostExecute(String response) {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		}

		if (monUploadDone != null) {
			monUploadDone.onComplete();
		}
		Toast.makeText(sActivity.getApplicationContext(), response, Toast.LENGTH_SHORT).show();
	}

	OnUploadPhotoFinish monUploadDone;

	public void setOnUploadDone(OnUploadPhotoFinish monUploadDone) {
		this.monUploadDone = monUploadDone;
	}

	public interface OnUploadPhotoFinish {
		void onComplete();
	}

}