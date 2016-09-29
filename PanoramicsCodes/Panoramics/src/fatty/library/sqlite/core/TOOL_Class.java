package fatty.library.sqlite.core;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Fatty
 *
 */
public class TOOL_Class {

	/**
	 * 根据实体类 获得 实体类对应的表名
	 * 
	 * @param entity
	 * @return
	 */
	public static String getTableName(Class<?> clazz) {
		ANNOTATION_TABLE table = clazz.getAnnotation(ANNOTATION_TABLE.class);
		if (table == null || table.name().trim().length() == 0) {
			// 当没有注解的时候默认用类的名称作为表名,并把点(.)替换为下划线(_)
			return clazz.getName().replace('.', '_');
		}
		return table.name();
	}

	public static Object getPrimaryKeyValue(Object entity) {
		return TOOL__Field.getFieldValue(entity,
				TOOL_Class.getPrimaryKeyField(entity.getClass()));
	}

	/**
	 * 根据实体类 获得 实体类对应的表名
	 * 
	 * @param entity
	 * @return
	 */
	public static String getPrimaryKeyColumn(Class<?> clazz) {
		String primaryKey = null;
		Field[] fields = clazz.getDeclaredFields();
		if (fields != null) {
			ANNOTATION_ID idAnnotation = null;
			Field idField = null;

			for (Field field : fields) { // 获取ID注解
				idAnnotation = field.getAnnotation(ANNOTATION_ID.class);
				if (idAnnotation != null) {
					idField = field;
					break;
				}
			}

			if (idAnnotation != null) { // 有ID注解
				primaryKey = idAnnotation.column();
				if (primaryKey == null || primaryKey.trim().length() == 0)
					primaryKey = idField.getName();
			} else { // 没有ID注解,默认去找 _id 和 id 为主键，优先寻找 _id
				for (Field field : fields) {
					if ("_id".equals(field.getName()))
						return "_id";
				}

				for (Field field : fields) {
					if ("id".equals(field.getName()))
						return "id";
				}
			}
		} else {
			throw new RuntimeException("this model[" + clazz + "] has no field");
		}
		return primaryKey;
	}

	/**
	 * 根据实体类 获得 实体类对应的表名
	 * 
	 * @param entity
	 * @return
	 */
	public static Field getPrimaryKeyField(Class<?> clazz) {
		Field primaryKeyField = null;
		Field[] fields = clazz.getDeclaredFields();
		if (fields != null) {

			for (Field field : fields) { // 获取ID注解
				if (field.getAnnotation(ANNOTATION_ID.class) != null) {
					primaryKeyField = field;
					break;
				}
			}

			if (primaryKeyField == null) { // 没有ID注解
				for (Field field : fields) {
					if ("_id".equals(field.getName())) {
						primaryKeyField = field;
						break;
					}
				}
			}

			if (primaryKeyField == null) { // 如果没有_id的字段
				for (Field field : fields) {
					if ("id".equals(field.getName())) {
						primaryKeyField = field;
						break;
					}
				}
			}

		} else {
			throw new RuntimeException("this model[" + clazz + "] has no field");
		}
		return primaryKeyField;
	}

	/**
	 * 根据实体类 获得 实体类对应的表名
	 * 
	 * @param entity
	 * @return
	 */
	public static String getPrimaryKeyFieldName(Class<?> clazz) {
		Field f = getPrimaryKeyField(clazz);
		return f == null ? null : f.getName();
	}

	/**
	 * 将对象转换为ContentValues
	 * 
	 * @param entity
	 * @param selective
	 *            是否忽略 值为null的字段
	 * @return
	 */
	public static List<TABLE_Property> getPropertyList(Class<?> clazz) {

		List<TABLE_Property> plist = new ArrayList<TABLE_Property>();
		try {
			Field[] fs = clazz.getDeclaredFields();
			String primaryKeyFieldName = getPrimaryKeyFieldName(clazz);
			for (Field f : fs) {
				// 必须是基本数据类型和没有标瞬时态的字段
				if (!TOOL__Field.isTransient(f)) {
					if (TOOL__Field.isBaseDateType(f)) {
						if (f.getName().equals(primaryKeyFieldName)) // 过滤主键
							continue;
						TABLE_Property property = new TABLE_Property();
						property.setColumn(TOOL__Field.getColumnByField(f));
						property.setFieldName(f.getName());
						property.setDataType(f.getType());
						property.setDefaultValue(TOOL__Field.getPropertyDefaultValue(f));
						property.setSet(TOOL__Field.getFieldSetMethod(clazz, f));
						property.setGet(TOOL__Field.getFieldGetMethod(clazz, f));
						property.setField(f);
						plist.add(property);
					}
				}
			}
			return plist;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 将对象转换为ContentValues
	 * 
	 * @param entity
	 * @param selective
	 *            是否忽略 值为null的字段
	 * @return
	 */
	public static List<TABLE_MTO> getManyToOneList(Class<?> clazz) {

		List<TABLE_MTO> mList = new ArrayList<TABLE_MTO>();
		try {
			Field[] fs = clazz.getDeclaredFields();
			for (Field f : fs) {
				if (!TOOL__Field.isTransient(f) && TOOL__Field.isManyToOne(f)) {

					TABLE_MTO mto = new TABLE_MTO();
					// 如果类型为ManyToOneLazyLoader则取第二个参数作为manyClass（一方实体）
					// 2013-7-26
					if (f.getType() == MTOLazyLoader.class) {
						Class<?> pClazz = (Class<?>) ((ParameterizedType) f.getGenericType()).getActualTypeArguments()[1];
						if (pClazz != null)
							mto.setManyClass(pClazz);
					} else {
						mto.setManyClass(f.getType());
					}
					mto.setColumn(TOOL__Field.getColumnByField(f));
					mto.setFieldName(f.getName());
					mto.setDataType(f.getType());
					mto.setSet(TOOL__Field.getFieldSetMethod(clazz, f));
					mto.setGet(TOOL__Field.getFieldGetMethod(clazz, f));

					mList.add(mto);
				}
			}
			return mList;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 将对象转换为ContentValues
	 * 
	 * @param entity
	 * @param selective
	 *            是否忽略 值为null的字段
	 * @return
	 */
	public static List<TABLE_OTM> getOneToManyList(Class<?> clazz) {

		List<TABLE_OTM> oList = new ArrayList<TABLE_OTM>();
		try {
			Field[] fs = clazz.getDeclaredFields();
			for (Field f : fs) {
				if (!TOOL__Field.isTransient(f) && TOOL__Field.isOneToMany(f)) {
					TABLE_OTM otm = new TABLE_OTM();
					otm.setColumn(TOOL__Field.getColumnByField(f));
					otm.setFieldName(f.getName());
					Type type = f.getGenericType();
					if (type instanceof ParameterizedType) {
						ParameterizedType pType = (ParameterizedType) f.getGenericType();
						if (pType.getActualTypeArguments().length == 1) {
							Class<?> pClazz = (Class<?>) pType.getActualTypeArguments()[0];
							if (pClazz != null)
								otm.setOneClass(pClazz);
						} else {
							Class<?> pClazz = (Class<?>) pType.getActualTypeArguments()[1];
							if (pClazz != null)
								otm.setOneClass(pClazz);
						}
					} else {
						throw new Exception("getOneToManyList Exception:" + f.getName() + "'s type is null");
					}
					/* 修正类型赋值错误的bug，f.getClass返回的是Filed */
					otm.setDataType(f.getType());
					otm.setSet(TOOL__Field.getFieldSetMethod(clazz, f));
					otm.setGet(TOOL__Field.getFieldGetMethod(clazz, f));
					oList.add(otm);
				}
			}
			return oList;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

}
