package f.com.panoramics.gcm.service;

import java.io.IOException;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build.VERSION;
import android.os.IBinder;
import android.preference.PreferenceManager;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import f.com.panoramics.constant.Constant;

/**
 * 
 * @author Fatty
 * 
 * 推送服务
 *
 */
public class MessageReceivingService extends Service{
	
    private GoogleCloudMessaging gcm;
    public static SharedPreferences savedValues;

    @Override
	public void onCreate(){
        super.onCreate();
        
        savedValues = getSharedPreferences("GCM_PushSerivce", Context.MODE_PRIVATE);
        if(VERSION.SDK_INT >  9){
            savedValues = getSharedPreferences("GCM_PushSerivce", Context.MODE_MULTI_PROCESS);
        }
        
        gcm = GoogleCloudMessaging.getInstance(getBaseContext());
        SharedPreferences savedValues = PreferenceManager.getDefaultSharedPreferences(this);
        if(savedValues.getBoolean("GCM_PushSerivce_first_launch", true)){
            register();
            SharedPreferences.Editor editor = savedValues.edit();
            editor.putBoolean("GCM_PushSerivce_first_launch", false);
            editor.commit();
        }
    }
 
    /**
     *  GCM注册 token
     *  
     *   并获取Token
     */
	private void register() {
        new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String token = gcm.register(Constant.GCM_PROJECT_NUMBER);
					savedValues.edit().putString("GCM_Token", token).commit();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
    }

    @Override
	public IBinder onBind(Intent arg0) {
        return null;
    }

}