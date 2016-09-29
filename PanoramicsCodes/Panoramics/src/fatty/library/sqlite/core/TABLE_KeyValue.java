package fatty.library.sqlite.core;

/**
 * 
 * @author Fatty
 *
 */
public class TABLE_KeyValue {
	
	private String key;
	private Object value;

	public TABLE_KeyValue(String key, Object value) {
		this.key = key;
		this.value = value;
	}

	public TABLE_KeyValue() {
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Object getValue() {
		if (value instanceof java.util.Date || value instanceof java.sql.Date) {
			return TOOL__Field.SDF.format(value);
		}
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

}
