package fatty.library.http.core;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;

/**
 * 
 * @author Fatty
 *
 */
public class StringEntityHandler {

	/**
	 * 
	 * @param entity
	 * @param callback
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	public Object handleEntity(HttpEntity entity, EntityCallBack callback, String charset) throws IOException {
		if (entity == null)
			return null;

		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];

		long count = entity.getContentLength();
		long curCount = 0;
		int len = -1;
		InputStream is = entity.getContent();
		while ((len = is.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
			curCount += len;
			if (callback != null)
				callback.callBack(count, curCount, false);
		}
		if (callback != null)
			callback.callBack(count, curCount, true);
		byte[] data = outStream.toByteArray();
		outStream.close();
		is.close();
		return new String(data, charset);
	}

}
