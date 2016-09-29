package com.flickrjandroidj.tasks;

import java.io.File;
import java.io.FileInputStream;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.widget.Toast;

import com.flickrjandroidj.FlickrHelper;
import com.googlecode.flickrjandroid.Flickr;
import com.googlecode.flickrjandroid.oauth.OAuth;
import com.googlecode.flickrjandroid.oauth.OAuthToken;
import com.googlecode.flickrjandroid.uploader.UploadMetaData;

import f.com.panoramics.activity.ShareSuccessActivity;

/**
 * 
 * @author Fatty
 *
 */
public class UploadPhotoTask extends AsyncTask<OAuth, Void, String> {
	
	private final ShareSuccessActivity sActivity;
	private String file;

	public UploadPhotoTask(ShareSuccessActivity sActivity, String file) {
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
				UploadPhotoTask.this.cancel(true);
			}
		});
	}

	@Override
	protected String doInBackground(OAuth... params) {
		OAuth oauth = params[0];
		OAuthToken token = oauth.getToken();

		try {
			Flickr f = FlickrHelper.getInstance().getFlickrAuthed(token.getOauthToken(), token.getOauthTokenSecret());
			UploadMetaData uploadMetaData = new UploadMetaData();
			uploadMetaData.setTitle("Panoramics");
			uploadMetaData.setDescription("Panoramics");
			return f.getUploader().upload("Panoramics", new FileInputStream(new File(file)), uploadMetaData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
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