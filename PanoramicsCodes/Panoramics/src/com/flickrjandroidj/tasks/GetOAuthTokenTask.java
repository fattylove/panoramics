package com.flickrjandroidj.tasks;

import android.os.AsyncTask;

import com.flickrjandroidj.FlickrHelper;
import com.googlecode.flickrjandroid.Flickr;
import com.googlecode.flickrjandroid.oauth.OAuth;
import com.googlecode.flickrjandroid.oauth.OAuthInterface;

import f.com.panoramics.activity.ShareSuccessActivity;

/**
 * 
 * @author Fatty
 *
 */
public class GetOAuthTokenTask extends AsyncTask<String, Integer, OAuth> {
	private final ShareSuccessActivity activity;

	public GetOAuthTokenTask(ShareSuccessActivity context) {
		this.activity = context;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}
	
	@Override
	protected OAuth doInBackground(String... params) {
		String oauthToken = params[0];
		String oauthTokenSecret = params[1];
		String verifier = params[2];

		Flickr f = FlickrHelper.getInstance().getFlickr();
		OAuthInterface oauthApi = f.getOAuthInterface();
		try {
			return oauthApi.getAccessToken(oauthToken, oauthTokenSecret, verifier);
		} catch (Exception e) {
			System.err.println(e.toString());
			return null;
		}
	}

	@Override
	protected void onPostExecute(OAuth result) {
		if (activity != null) {
			activity.onOAuthDone(result);
		}
	}


}
