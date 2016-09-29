package fatty.library.sqlite.core;

/**
 * 
 * Many to One Lazy Loader
 * 
 * @param <O>
 *            宿主实体的class
 * @param <M>
 *            多放实体class
 */
public class MTOLazyLoader<M, O> {
	M manyEntity;
	Class<M> manyClazz;
	Class<O> oneClazz;
	SQLService db;
	private Object fieldValue;
	O oneEntity;
	boolean hasLoaded = false;

	public MTOLazyLoader(M manyEntity, Class<M> manyClazz, Class<O> oneClazz, SQLService db) {
		this.manyEntity = manyEntity;
		this.manyClazz = manyClazz;
		this.oneClazz = oneClazz;
		this.db = db;
	}
	
	public O get() {
		if (oneEntity == null && !hasLoaded) {
			this.db.loadManyToOne(null, this.manyEntity, this.manyClazz, this.oneClazz);
			hasLoaded = true;
		}
		return oneEntity;
	}

	public void set(O value) {
		oneEntity = value;
	}

	public Object getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(Object fieldValue) {
		this.fieldValue = fieldValue;
	}
}
