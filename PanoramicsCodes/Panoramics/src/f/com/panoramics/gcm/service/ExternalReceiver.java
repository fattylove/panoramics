package f.com.panoramics.gcm.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.guxiu.panoramics.R;

import f.com.panoramics.constant.Constant;

/**
 * 
 * GCM Receiver
 * 
 * @author Fatty
 * 
 * Follows
 * 
 * Likes
 * 
 * Comment
 * 
 * others
 *
 */
public class ExternalReceiver extends BroadcastReceiver {

    @Override
	public void onReceive(Context context, Intent intent) {
        if(intent!=null){
        	String message = "";
            Bundle extras = intent.getExtras();
            for(String key : extras.keySet()){
                if(key.equals("message")){
                	message = extras.getString(key);
                }
            }
            if(!TextUtils.isEmpty(message)){
            	 postNotification(context, message , 0);
            }
        }
    }
    
    /**
     * 收到通知，显示在Notification上
     * 
     * @param context
     * @param message 消息内容
     * @param type 消息类型
     */
    @SuppressWarnings("deprecation")
	protected static void postNotification(Context context , String message , int type){
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intentAction = new Intent();
        intentAction.setComponent(new ComponentName(Constant.PACKAGE_NAME, "f.com.panoramics.gcm.service.ShowMessageActivity"));
        intentAction.putExtra("gcmContent", message);
        
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intentAction, Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL);
        Notification notification = new NotificationCompat.Builder(context).setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setWhen(System.currentTimeMillis())
		        .setTicker(message)   //notification bar text first arrives...
		        .setDefaults(Notification.DEFAULT_SOUND)
		        .setAutoCancel(true)
                .getNotification();
        mNotificationManager.notify(0, notification);
    }

}

