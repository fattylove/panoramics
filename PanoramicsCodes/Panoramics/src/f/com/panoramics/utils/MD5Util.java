package f.com.panoramics.utils;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * 
 * @author Fatty
 * 
 * MD5 加密
 *
 */
public class MD5Util {
	public static String makeMD5(String password) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			md.update(password.getBytes());
			String pwd = new BigInteger(1, md.digest()).toString(16);
			System.err.println(pwd);
			return pwd;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return password;
	}
}