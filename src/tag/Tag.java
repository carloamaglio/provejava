package tag;

public class Tag implements Taggable {
	TagConverter plcConverter;
	TagConverter userConverter;

	TagValue value;

	public Tag() {
		plcConverter = userConverter = identita;
	}

	@Override
	public TagValue getValue() {
		return value;
	}

	@Override
	public void setValue(TagValue value) {
		this.value = value;
	}

	@Override
	public TagValue getPLCValue() {
		return plcConverter.f(value);
	}

	@Override
	public void setPLCValue(TagValue value) {
		this.value = plcConverter.inv(value);
	}

	@Override
	public TagValue getUserValue() {
		return userConverter.f(value);
	}

	@Override
	public void setUserValue(TagValue value) {
		this.value = userConverter.inv(value);
	}

	static final class Identita implements TagConverter, Cloneable {
		@Override
		public TagValue f(TagValue value) {
			return new TagValue(value);
		}

		@Override
		public TagValue inv(TagValue value) {
			return new TagValue(value);
		}
	}
	static final TagConverter identita = new Identita();
}
