package fatty.library.sqlite.core;

/**
 * Many to One
 * 
 * @author Fatty
 *
 */
public class TABLE_MTO extends TABLE_Property {

	private Class<?> manyClass;

	public Class<?> getManyClass() {
		return manyClass;
	}

	public void setManyClass(Class<?> manyClass) {
		this.manyClass = manyClass;
	}

}
