package fatty.library.http.core;

/**
 * 
 * @author Fatty
 * 
 */
public abstract class CallBack<T> {

	private boolean progress = true;
	private int rate = 1000 * 1;

	public boolean isProgress() {
		return progress;
	}

	public int getRate() {
		return rate;
	}
	
	/**
	 * 进度
	 * 
	 * @param progress
	 * @param rate
	 * @return
	 */
	public CallBack<T> progress(boolean progress, int rate) {
		this.progress = progress;
		this.rate = rate;
		return this;
	}

	/**
	 * 开始执行
	 */
	public void onStart() {
	};

	/**
	 * 加载中...
	 * 
	 * @param count
	 * @param current
	 */
	public void onLoading(long count, long current) {
	};

	/**
	 * 加载成功
	 * 
	 * @param t
	 * @param code
	 */
	public void onSuccess(T t, int code) {
	};

	/**
	 * 加载失败
	 * 
	 * @param t
	 * @param code
	 * @param strMsg
	 */
	public void onFailure(Throwable t, int code, String strMsg) {
	};
}
