package tag;

public interface Taggable {
	public TagValue getValue();
	public void setValue(TagValue value);
	public TagValue getPLCValue();
	public void setPLCValue(TagValue value);
	public TagValue getUserValue();
	public void setUserValue(TagValue value);
}
