package com.flickrjandroidj;

import javax.xml.parsers.ParserConfigurationException;

import com.googlecode.flickrjandroid.Flickr;
import com.googlecode.flickrjandroid.REST;
import com.googlecode.flickrjandroid.RequestContext;
import com.googlecode.flickrjandroid.oauth.OAuth;
import com.googlecode.flickrjandroid.oauth.OAuthToken;
import com.googlecode.flickrjandroid.uploader.Uploader;

/**
 * 
 * @author fatty
 *
 */
public final class FlickrHelper {

	private static FlickrHelper instance = null;
	public static final String API_KEY = "54ee9c73e9f8d16a883b0f3d260b9a0e";
	public static final String API_SEC = "c2db69a1df7a190c"; 

	private FlickrHelper() {
	}

	/**
	 * 
	 * @return
	 */
	public static FlickrHelper getInstance() {
		if (instance == null) {
			instance = new FlickrHelper();
		}
		return instance;
	}

	/**
	 * 
	 * @return
	 */
	public Flickr getFlickr() {
		try {
			Flickr f = new Flickr(API_KEY, API_SEC, new REST());
			return f;
		} catch (ParserConfigurationException e) {
			return null;
		}
	}
	
	public Flickr getFlickrAuthed(String token, String secret) {
		Flickr f = getFlickr();
		RequestContext requestContext = RequestContext.getRequestContext();
		OAuth auth = new OAuth();
		auth.setToken(new OAuthToken(token, secret));
		requestContext.setOAuth(auth);
		return f;
	}
	
	public Uploader getUploader(){
		Flickr f = getFlickr();
		if (f != null) {
			return f.getUploader();
		} else {
			return null;
		}
	}

}
