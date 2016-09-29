package com.flickrjandroidj.tasks;

import java.net.URL;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

import com.flickrjandroidj.FlickrHelper;
import com.googlecode.flickrjandroid.Flickr;
import com.googlecode.flickrjandroid.auth.Permission;
import com.googlecode.flickrjandroid.oauth.OAuthToken;

import f.com.panoramics.activity.ShareSuccessActivity;

/**
 * 
 * @author Fatty
 *
 */
public class SaveOAuthTask extends AsyncTask<Void, Integer, String> {

	public static final Uri OAUTH_CALLBACK_URI = Uri.parse(ShareSuccessActivity.CALLBACK_SCHEME + "://oauth");
	private final Context mContext;
	private ProgressDialog mProgressDialog;

	public SaveOAuthTask(Context context) {
		super();
		this.mContext = context;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mProgressDialog = ProgressDialog.show(mContext, "", "Generating the authorization request..."); //$NON-NLS-1$ //$NON-NLS-2$
		mProgressDialog.setCanceledOnTouchOutside(true);
		mProgressDialog.setCancelable(true);
		mProgressDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dlg) {
				SaveOAuthTask.this.cancel(true);
			}
		});
	}

	@Override
	protected String doInBackground(Void... params) {
		try {
			Flickr f = FlickrHelper.getInstance().getFlickr();
			OAuthToken oauthToken = f.getOAuthInterface().getRequestToken(OAUTH_CALLBACK_URI.toString());
			saveTokenSecrent(oauthToken.getOauthTokenSecret());
			URL oauthUrl = f.getOAuthInterface().buildAuthenticationUrl(Permission.WRITE, oauthToken);
			return oauthUrl.toString();
		} catch (Exception e) {
			return "error:" + e.getMessage();
		}
	}

	private void saveTokenSecrent(String tokenSecret) {
		ShareSuccessActivity act = (ShareSuccessActivity) mContext;
		act.saveOAuthToken(null, null, null, tokenSecret);
	}

	@Override
	protected void onPostExecute(String result) {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		}
		
		if (result != null && !result.startsWith("error") ) {
			mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(result)));
		} else {
			Toast.makeText(mContext, result, Toast.LENGTH_LONG).show();
		}
	}

}
