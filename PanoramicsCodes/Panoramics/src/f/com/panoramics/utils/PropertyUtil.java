package f.com.panoramics.utils;

import java.io.IOException;
import java.util.Properties;

public class PropertyUtil {

	static Properties props = new Properties();

	static ClassLoader classLoader;

	static {
		try {
			classLoader = PropertyUtil.class.getClassLoader();
			props.load(classLoader.getResourceAsStream("config.properties"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private PropertyUtil() {

	};

	public static String getValue(String key) {
		return props.getProperty(key);
	}

	public static Class<?> getModuleValue(String key) {
		String className = props.getProperty(key);
		Class<?> targetClass = null;
		try {
			targetClass = classLoader.loadClass(className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return targetClass;
	}

	public static String getActivityName(String key) {
		String className = props.getProperty(key);
		return className;
	}
}
