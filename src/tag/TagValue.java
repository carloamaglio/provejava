package tag;

public class TagValue extends Number {
	private static final long serialVersionUID = 1L;

	String value;

	public TagValue(String value) {
		this.value = (value!=null ? value : "");
	}

	public TagValue() {
		this("");
	}

	public TagValue(TagValue value) {
		this.value = new String(value.value);
	}

	public String getValue() {
		return value;
	}

	@Override
	public double doubleValue() {
		return 0;
	}

	@Override
	public float floatValue() {
		return 0;
	}

	@Override
	public int intValue() {
		return 0;
	}

	@Override
	public long longValue() {
		return 0;
	}
}
