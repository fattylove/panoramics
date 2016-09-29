package fatty.library.sqlite.core;

/**
 * One to Many
 * 
 * @author Fatty
 *
 */
public class TABLE_OTM extends TABLE_Property {

	private Class<?> oneClass;

	public Class<?> getOneClass() {
		return oneClass;
	}

	public void setOneClass(Class<?> oneClass) {
		this.oneClass = oneClass;
	}
}
