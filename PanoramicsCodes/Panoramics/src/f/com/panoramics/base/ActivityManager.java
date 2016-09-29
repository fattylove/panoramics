package f.com.panoramics.base;

import java.util.Stack;

import android.app.Activity;

/**
 * 
 * @author Fatty
 *
 */
public class ActivityManager {

	private static Stack<Activity> activityStack;
	private static ActivityManager instance;

	public static Stack<Activity> getActivityStack() {
		return activityStack;
	}

	private ActivityManager() {
	}

	public static ActivityManager getAppManager() {
		if (instance == null) {
			instance = new ActivityManager();
			if (activityStack == null) {
				activityStack = new Stack<Activity>();
			}
		}
		return instance;
	}

	public void addActivity(Activity activity) {
		activityStack.add(activity);
	}

	public Activity currentActivity() {
		Activity activity;
		try {
			activity = activityStack.lastElement();
		} catch (Exception e) {
			return null;
		}
		return activity;
	}
	
	/**
	 * 
	 * @param cla
	 * @return
	 */
	public Activity getActivity(Class<?> cla) {
		Activity activity;
		try {
			int index = activityStack.indexOf(cla);
			activity = activityStack.get(index);
		} catch (Exception e) {
			return null;
		}
		return activity;
	}

	public void finishActivity() {
		if (activityStack.size() > 0) {
			Activity activity = activityStack.lastElement();
			if (null != activity) {
				activity.finish();
				activity = null;
			}
		}
	}

	public void finishActivity(Activity activity) {
		if (activity != null) {
			activityStack.remove(activity);
			activity.finish();
			activity = null;
		}

	}

	public void finishActivity(Class<?> cls) {
		for (Activity activity : activityStack) {
			if (activity.getClass().equals(cls)) {
				finishActivity(activity);
			}
		}
	}

	public void finishAllActivity() {
		for (int i = 0, size = activityStack.size(); i < size; i++) {
			if (null != activityStack.get(i)) {
				activityStack.get(i).finish();
			}
		}
		activityStack.clear();
	}

	public boolean hasThisActivity(Class<?> cls) {
		if (activityStack.contains(cls)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isHasActivity(Activity activity) {
		boolean result = false;
		if (activityStack.size() > 0) {
			result = activityStack.contains(activity);
		} else {
			result = false;
		}
		return result;
	}
}