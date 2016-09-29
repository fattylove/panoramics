package fatty.library.sqlite.core;

import java.util.HashMap;
import java.util.Map.Entry;

import android.database.Cursor;

/**
 * 
 * @author Fatty
 *
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class TOOL_Cursor {
	
	public static <T> T getEntity(Cursor cursor, Class<T> clazz, SQLService db) {
		try {
			if (cursor != null) {
				TABLE_TableEntity table = TABLE_TableEntity.get(clazz);
				int columnCount = cursor.getColumnCount();
				if (columnCount > 0) {
					T entity = (T) clazz.newInstance();
					for (int i = 0; i < columnCount; i++) {
						String column = cursor.getColumnName(i);
						TABLE_Property property = table.propertyMap.get(column);
						if (property != null) {
							property.setValue(entity, cursor.getString(i));
						} else {
							if (table.getId().getColumn().equals(column)) {
								table.getId().setValue(entity,cursor.getString(i));
							}
						}

					}
					/**
					 * 处理OneToMany的lazyLoad形式
					 */
					for (TABLE_OTM oneToManyProp : table.oneToManyMap.values()) {
						if (oneToManyProp.getDataType() == OTMLazyLoader.class) {
							OTMLazyLoader oneToManyLazyLoader = new OTMLazyLoader(entity, clazz, oneToManyProp.getOneClass(), db);
							oneToManyProp.setValue(entity, oneToManyLazyLoader);
						}
					}
					/**
					 * 处理ManyToOne的lazyLoad形式
					 */
					for (TABLE_MTO manyToOneProp : table.manyToOneMap.values()) {
						if (manyToOneProp.getDataType() == MTOLazyLoader.class) {
							MTOLazyLoader manyToOneLazyLoader = new MTOLazyLoader(entity, clazz, manyToOneProp.getManyClass(), db);
							manyToOneLazyLoader.setFieldValue(cursor.getInt(cursor.getColumnIndex(manyToOneProp.getColumn())));
							manyToOneProp.setValue(entity, manyToOneLazyLoader);
						}
					}
					return entity;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static SQLColumn getDbModel(Cursor cursor) {
		if (cursor != null && cursor.getColumnCount() > 0) {
			SQLColumn model = new SQLColumn();
			int columnCount = cursor.getColumnCount();
			for (int i = 0; i < columnCount; i++) {
				model.set(cursor.getColumnName(i), cursor.getString(i));
			}
			return model;
		}
		return null;
	}

	public static <T> T dbModel2Entity(SQLColumn dbModel, Class<?> clazz) {
		if (dbModel != null) {
			HashMap<String, Object> dataMap = dbModel.getDataMap();
			try {
				T entity = (T) clazz.newInstance();
				for (Entry<String, Object> entry : dataMap.entrySet()) {
					String column = entry.getKey();
					TABLE_TableEntity table = TABLE_TableEntity.get(clazz);
					TABLE_Property property = table.propertyMap.get(column);
					if (property != null) {
						property.setValue(entity,entry.getValue() == null ? null : entry.getValue().toString());
					} else {
						if (table.getId().getColumn().equals(column)) {
							table.getId().setValue(	entity,entry.getValue() == null ? null : entry.getValue().toString());
						}
					}
				}
				return entity;
			} catch (Exception e) {
				System.err.println(e.toString());
			}
		}
		return null;
	}

}
