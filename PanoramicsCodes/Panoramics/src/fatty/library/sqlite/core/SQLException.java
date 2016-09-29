package fatty.library.sqlite.core;

/**
 * 
 * @author Fatty
 * 
 */
public class SQLException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public SQLException() {
	}

	public SQLException(String msg) {
		super(msg);
	}

	public SQLException(Throwable ex) {
		super(ex);
	}

	public SQLException(String msg, Throwable ex) {
		super(msg, ex);
	}
}
