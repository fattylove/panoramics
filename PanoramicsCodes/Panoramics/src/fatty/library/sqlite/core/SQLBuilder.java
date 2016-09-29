package fatty.library.sqlite.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.text.TextUtils;

/**
 * 
 * @author Fatty
 *
 */
@SuppressWarnings({ "rawtypes" })
public class SQLBuilder {

	/**
	 * 获取插入的sql语句 , 拼接insert语句
	 * 
	 * @return
	 */
	public static SQLStatement buildInsertSql(Object entity) {
		List<TABLE_KeyValue> keyValueList = getSaveKeyValueListByEntity(entity);

		StringBuffer strSQL = new StringBuffer();
		SQLStatement sqlInfo = null;
		if (keyValueList != null && keyValueList.size() > 0) {
			sqlInfo = new SQLStatement();
			strSQL.append("INSERT INTO ");
			strSQL.append(TABLE_TableEntity.get(entity.getClass()).getTableName());
			strSQL.append(" (");
			for (TABLE_KeyValue kv : keyValueList) {
				strSQL.append(kv.getKey()).append(",");
				sqlInfo.addValue(kv.getValue());
			}
			strSQL.deleteCharAt(strSQL.length() - 1);
			strSQL.append(") VALUES ( ");

			int length = keyValueList.size();
			for (int i = 0; i < length; i++) {
				strSQL.append("?,");
			}
			strSQL.deleteCharAt(strSQL.length() - 1);
			strSQL.append(")");
			sqlInfo.setSql(strSQL.toString());
		}
		return sqlInfo;
	}

	/**
	 * 
	 * @param entity
	 * @return
	 */
	public static List<TABLE_KeyValue> getSaveKeyValueListByEntity(Object entity) {
		List<TABLE_KeyValue> keyValueList = new ArrayList<TABLE_KeyValue>();
		TABLE_TableEntity table = TABLE_TableEntity.get(entity.getClass());
		Object idvalue = table.getId().getValue(entity);

		if (!(idvalue instanceof Integer)) { // 用了非自增长,添加id , 采用自增长就不需要添加id了
			if (idvalue instanceof String && idvalue != null) {
				TABLE_KeyValue kv = new TABLE_KeyValue(table.getId().getColumn(), idvalue);
				keyValueList.add(kv);
			}
		}

		// 添加属性
		Collection<TABLE_Property> propertys = table.propertyMap.values();
		for (TABLE_Property property : propertys) {
			TABLE_KeyValue kv = property2KeyValue(property, entity);
			if (kv != null)
				keyValueList.add(kv);
		}

		// 添加外键（多对一）
		Collection<TABLE_MTO> manyToOnes = table.manyToOneMap.values();
		for (TABLE_MTO many : manyToOnes) {
			TABLE_KeyValue kv = manyToOne2KeyValue(many, entity);
			if (kv != null)
				keyValueList.add(kv);
		}

		return keyValueList;
	}

	/**
	 * 拼接delete语句
	 * 
	 * @param tableName
	 * @return
	 */
	private static String getDeleteSqlBytableName(String tableName) {
		return "DELETE FROM " + tableName;
	}

	public static SQLStatement buildDeleteSql(Object entity) {
		TABLE_TableEntity table = TABLE_TableEntity.get(entity.getClass());

		TABLE_Id id = table.getId();
		Object idvalue = id.getValue(entity);

		if (idvalue == null) {
			throw new SQLException("getDeleteSQL:" + entity.getClass() + " id value is null");
		}
		StringBuffer strSQL = new StringBuffer(getDeleteSqlBytableName(table.getTableName()));
		strSQL.append(" WHERE ").append(id.getColumn()).append("=?");

		SQLStatement sqlInfo = new SQLStatement();
		sqlInfo.setSql(strSQL.toString());
		sqlInfo.addValue(idvalue);

		return sqlInfo;
	}

	/**
	 * 拼接delete语句
	 * 
	 * @param clazz
	 * @param idValue
	 * @return
	 */
	public static SQLStatement buildDeleteSql(Class<?> clazz, Object idValue) {
		TABLE_TableEntity table = TABLE_TableEntity.get(clazz);
		TABLE_Id id = table.getId();
		if (null == idValue) {
			throw new SQLException("getDeleteSQL:idValue is null");
		}
		StringBuffer strSQL = new StringBuffer(getDeleteSqlBytableName(table.getTableName()));
		strSQL.append(" WHERE ").append(id.getColumn()).append("=?");
		SQLStatement sqlInfo = new SQLStatement();
		sqlInfo.setSql(strSQL.toString());
		sqlInfo.addValue(idValue);
		return sqlInfo;
	}

	/**
	 * 根据条件删除数据 ，条件为空的时候将会删除所有的数据
	 * 
	 * @param clazz
	 * @param strWhere
	 * @return
	 */
	public static String buildDeleteSql(Class<?> clazz, String strWhere) {
		TABLE_TableEntity table = TABLE_TableEntity.get(clazz);
		StringBuffer strSQL = new StringBuffer(getDeleteSqlBytableName(table.getTableName()));
		if (!TextUtils.isEmpty(strWhere)) {
			strSQL.append(" WHERE ");
			strSQL.append(strWhere);
		}
		return strSQL.toString();
	}

	/**
	 * 拼接select语句
	 * 
	 * @param tableName
	 * @return
	 */
	private static String getSelectSqlByTableName(String tableName) {
		return new StringBuffer("SELECT * FROM ").append(tableName).toString();
	}

	/**
	 * 拼接select where语句
	 * 
	 * @param clazz
	 * @param idValue
	 * @return
	 */
	public static String getSelectSQL(Class<?> clazz, Object idValue) {
		TABLE_TableEntity table = TABLE_TableEntity.get(clazz);
		StringBuffer strSQL = new StringBuffer(getSelectSqlByTableName(table.getTableName()));
		strSQL.append(" WHERE ");
		strSQL.append(getPropertyStrSql(table.getId().getColumn(), idValue));
		return strSQL.toString();
	}

	/**
	 * 拼接select where语句
	 * 
	 * @param clazz
	 * @param idValue
	 * @return
	 */
	public static SQLStatement getSelectSqlAsSqlInfo(Class<?> clazz, Object idValue) {
		TABLE_TableEntity table = TABLE_TableEntity.get(clazz);
		StringBuffer strSQL = new StringBuffer(getSelectSqlByTableName(table.getTableName()));
		strSQL.append(" WHERE ").append(table.getId().getColumn()).append("=?");
		SQLStatement sqlInfo = new SQLStatement();
		sqlInfo.setSql(strSQL.toString());
		sqlInfo.addValue(idValue);
		return sqlInfo;
	}

	/**
	 * 拼接select语句
	 * 
	 * @param clazz
	 * @return
	 */
	public static String getSelectSQL(Class<?> clazz) {
		return getSelectSqlByTableName(TABLE_TableEntity.get(clazz).getTableName());
	}

	/**
	 * 拼接select语句
	 * 
	 * @param clazz
	 * @param strWhere
	 * @return
	 */
	public static String getSelectSQLByWhere(Class<?> clazz, String strWhere) {
		TABLE_TableEntity table = TABLE_TableEntity.get(clazz);
		StringBuffer strSQL = new StringBuffer(getSelectSqlByTableName(table.getTableName()));
		if (!TextUtils.isEmpty(strWhere)) {
			strSQL.append(" WHERE ").append(strWhere);
		}
		return strSQL.toString();
	}

	/**
	 * 拼接update语句
	 * 
	 * @param entity
	 * @return
	 */
	public static SQLStatement getUpdateSqlAsSqlInfo(Object entity) {
		TABLE_TableEntity table = TABLE_TableEntity.get(entity.getClass());
		Object idvalue = table.getId().getValue(entity);
		if (null == idvalue) {// 主键值不能为null，否则不能更新
			throw new SQLException("this entity[" + entity.getClass() + "]'s id value is null");
		}
		List<TABLE_KeyValue> keyValueList = new ArrayList<TABLE_KeyValue>();
		// 添加属性
		Collection<TABLE_Property> propertys = table.propertyMap.values();
		for (TABLE_Property property : propertys) {
			TABLE_KeyValue kv = property2KeyValue(property, entity);
			if (kv != null)
				keyValueList.add(kv);
		}

		// 添加外键（多对一）
		Collection<TABLE_MTO> manyToOnes = table.manyToOneMap.values();
		for (TABLE_MTO many : manyToOnes) {
			TABLE_KeyValue kv = manyToOne2KeyValue(many, entity);
			if (kv != null)
				keyValueList.add(kv);
		}

		if (keyValueList == null || keyValueList.size() == 0)
			return null;

		SQLStatement sqlInfo = new SQLStatement();
		StringBuffer strSQL = new StringBuffer("UPDATE ");
		strSQL.append(table.getTableName());
		strSQL.append(" SET ");
		for (TABLE_KeyValue kv : keyValueList) {
			strSQL.append(kv.getKey()).append("=?,");
			sqlInfo.addValue(kv.getValue());
		}
		strSQL.deleteCharAt(strSQL.length() - 1);
		strSQL.append(" WHERE ").append(table.getId().getColumn()).append("=?");
		sqlInfo.addValue(idvalue);
		sqlInfo.setSql(strSQL.toString());
		return sqlInfo;
	}

	/**
	 * update where 语句
	 * 
	 * @param entity
	 * @param strWhere
	 * @return
	 */
	public static SQLStatement getUpdateSqlAsSqlInfo(Object entity, String strWhere) {
		TABLE_TableEntity table = TABLE_TableEntity.get(entity.getClass());
		List<TABLE_KeyValue> keyValueList = new ArrayList<TABLE_KeyValue>();
		// 添加属性
		Collection<TABLE_Property> propertys = table.propertyMap.values();
		for (TABLE_Property property : propertys) {
			TABLE_KeyValue kv = property2KeyValue(property, entity);
			if (kv != null)
				keyValueList.add(kv);
		}
		// 添加外键（多对一）
		Collection<TABLE_MTO> manyToOnes = table.manyToOneMap.values();
		for (TABLE_MTO many : manyToOnes) {
			TABLE_KeyValue kv = manyToOne2KeyValue(many, entity);
			if (kv != null)
				keyValueList.add(kv);
		}
		if (keyValueList == null || keyValueList.size() == 0) {
			throw new SQLException("this entity[" + entity.getClass() + "] has no property");
		}
		SQLStatement sqlInfo = new SQLStatement();
		StringBuffer strSQL = new StringBuffer("UPDATE ");
		strSQL.append(table.getTableName());
		strSQL.append(" SET ");
		for (TABLE_KeyValue kv : keyValueList) {
			strSQL.append(kv.getKey()).append("=?,");
			sqlInfo.addValue(kv.getValue());
		}
		strSQL.deleteCharAt(strSQL.length() - 1);
		if (!TextUtils.isEmpty(strWhere)) {
			strSQL.append(" WHERE ").append(strWhere);
		}
		sqlInfo.setSql(strSQL.toString());
		return sqlInfo;
	}

	/**
	 * create table语句
	 * 
	 * @param clazz
	 * @return
	 */
	public static String getCreatTableSQL(Class<?> clazz) {
		TABLE_TableEntity table = TABLE_TableEntity.get(clazz);
		TABLE_Id id = table.getId();
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("CREATE TABLE IF NOT EXISTS ");
		strSQL.append(table.getTableName());
		strSQL.append(" ( ");
		Class<?> primaryClazz = id.getDataType();
		if (primaryClazz == int.class || primaryClazz == Integer.class || primaryClazz == long.class || primaryClazz == Long.class) {
			strSQL.append(id.getColumn()).append(" INTEGER PRIMARY KEY AUTOINCREMENT,");
		} else {
			strSQL.append(id.getColumn()).append(" TEXT PRIMARY KEY,");
		}
		Collection<TABLE_Property> propertys = table.propertyMap.values();
		for (TABLE_Property property : propertys) {
			strSQL.append(property.getColumn());
			Class<?> dataType = property.getDataType();
			if (dataType == int.class || dataType == Integer.class|| dataType == long.class || dataType == Long.class) {
				strSQL.append(" INTEGER");
			} else if (dataType == float.class || dataType == Float.class|| dataType == double.class || dataType == Double.class) {
				strSQL.append(" REAL");
			} else if (dataType == boolean.class || dataType == Boolean.class) {
				strSQL.append(" NUMERIC");
			}
			strSQL.append(",");
		}

		Collection<TABLE_MTO> manyToOnes = table.manyToOneMap.values();
		for (TABLE_MTO manyToOne : manyToOnes) {
			strSQL.append(manyToOne.getColumn()).append(" INTEGER").append(",");
		}
		strSQL.deleteCharAt(strSQL.length() - 1);
		strSQL.append(" )");
		return strSQL.toString();
	}

	/**
	 * getPropertyStrSql
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	private static String getPropertyStrSql(String key, Object value) {
		StringBuffer sbSQL = new StringBuffer(key).append("=");
		if (value instanceof String || value instanceof java.util.Date || value instanceof java.sql.Date) {
			sbSQL.append("'").append(value).append("'");
		} else {
			sbSQL.append(value);
		}
		return sbSQL.toString();
	}

	/**
	 * property2KeyValue
	 * 
	 * @param property
	 * @param entity
	 * @return
	 */
	private static TABLE_KeyValue property2KeyValue(TABLE_Property property, Object entity) {
		TABLE_KeyValue kv = null;
		String pcolumn = property.getColumn();
		Object value = property.getValue(entity);
		if (value != null) {
			kv = new TABLE_KeyValue(pcolumn, value);
		} else {
			if (property.getDefaultValue() != null && property.getDefaultValue().trim().length() != 0)
				kv = new TABLE_KeyValue(pcolumn, property.getDefaultValue());
		}
		return kv;
	}

	/**
	 * manyToOne2KeyValue
	 * 
	 * @param many
	 * @param entity
	 * @return
	 */
	private static TABLE_KeyValue manyToOne2KeyValue(TABLE_MTO many, Object entity) {
		TABLE_KeyValue kv = null;
		String manycolumn = many.getColumn();
		Object manyobject = many.getValue(entity);
		if (manyobject != null) {
			Object manyvalue;
			if (manyobject.getClass() == MTOLazyLoader.class) {
				manyvalue = TABLE_TableEntity.get(many.getManyClass()).getId().getValue(((MTOLazyLoader) manyobject).get());
			} else {
				manyvalue = TABLE_TableEntity.get(manyobject.getClass()).getId().getValue(manyobject);
			}
			if (manycolumn != null && manyvalue != null) {
				kv = new TABLE_KeyValue(manycolumn, manyvalue);
			}
		}

		return kv;
	}
}
