package fatty.library.sqlite.core;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;


/**
 * Table
 * 
 * @author Fatty
 *
 */
@SuppressWarnings("unused")
public class TABLE_TableEntity {

	private String className;
	private String tableName;

	private TABLE_Id id;

	public final HashMap<String, TABLE_Property> propertyMap = new HashMap<String, TABLE_Property>();
	public final HashMap<String, TABLE_OTM> oneToManyMap = new HashMap<String, TABLE_OTM>();
	public final HashMap<String, TABLE_MTO> manyToOneMap = new HashMap<String, TABLE_MTO>();

	private boolean checkDatabese;// 在对实体进行数据库操作的时候查询是否已经有表了，只需查询一遍，用此标示

	private static final HashMap<String, TABLE_TableEntity> tableInfoMap = new HashMap<String, TABLE_TableEntity>();

	private TABLE_TableEntity() {
	}

	public static TABLE_TableEntity get(Class<?> clazz) {
		if (clazz == null)
			throw new SQLException("table info get error,because the clazz is null");

		TABLE_TableEntity tableInfo = tableInfoMap.get(clazz.getName());
		if (tableInfo == null) {
			tableInfo = new TABLE_TableEntity();
			tableInfo.setTableName(TOOL_Class.getTableName(clazz));
			tableInfo.setClassName(clazz.getName());
			Field idField = TOOL_Class.getPrimaryKeyField(clazz);
			if (idField != null) {
				TABLE_Id id = new TABLE_Id();
				id.setColumn(TOOL__Field.getColumnByField(idField));
				id.setFieldName(idField.getName());
				id.setSet(TOOL__Field.getFieldSetMethod(clazz, idField));
				id.setGet(TOOL__Field.getFieldGetMethod(clazz, idField));
				id.setDataType(idField.getType());
				tableInfo.setId(id);
			} else {
				throw new SQLException("the class["+ clazz + "]'s idField is null , \n you can define _id,id property or use annotation @id to solution this exception");
			}
			List<TABLE_Property> pList = TOOL_Class.getPropertyList(clazz);
			if (pList != null) {
				for (TABLE_Property p : pList) {
					if (p != null)
						tableInfo.propertyMap.put(p.getColumn(), p);
				}
			}
			List<TABLE_MTO> mList = TOOL_Class.getManyToOneList(clazz);
			if (mList != null) {
				for (TABLE_MTO m : mList) {
					if (m != null)
						tableInfo.manyToOneMap.put(m.getColumn(), m);
				}
			}
			List<TABLE_OTM> oList = TOOL_Class.getOneToManyList(clazz);
			if (oList != null) {
				for (TABLE_OTM o : oList) {
					if (o != null)
						tableInfo.oneToManyMap.put(o.getColumn(), o);
				}
			}
			tableInfoMap.put(clazz.getName(), tableInfo);
		}

		if (tableInfo == null)
			throw new SQLException("the class[" + clazz + "]'s table is null");
		return tableInfo;
	}

	public static TABLE_TableEntity get(String className) {
		try {
			return get(Class.forName(className));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public TABLE_Id getId() {
		return id;
	}

	public void setId(TABLE_Id id) {
		this.id = id;
	}

	public boolean isCheckDatabese() {
		return checkDatabese;
	}

	public void setCheckDatabese(boolean checkDatabese) {
		this.checkDatabese = checkDatabese;
	}

}
