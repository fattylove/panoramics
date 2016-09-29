package fatty.library.sqlite.core;

import java.util.ArrayList;
import java.util.List;

/**
 * One to Many Lazy Loader
 * 
 * @param <O>
 *            宿主实体的class
 * @param <M>
 *            多放实体class
 */
public class OTMLazyLoader<O, M> {
	O ownerEntity;
	Class<O> ownerClazz;
	Class<M> listItemClazz;
	List<M> entities;
	SQLService db;

	public OTMLazyLoader(O ownerEntity, Class<O> ownerClazz, Class<M> listItemclazz, SQLService db) {
		this.ownerEntity = ownerEntity;
		this.ownerClazz = ownerClazz;
		this.listItemClazz = listItemclazz;
		this.db = db;
	}

	public List<M> getList() {
		if (entities == null) {
			this.db.loadOneToMany((O) this.ownerEntity, this.ownerClazz, this.listItemClazz);
		}
		if (entities == null) {
			entities = new ArrayList<M>();
		}
		return entities;
	}

	public void setList(List<M> value) {
		entities = value;
	}

}
