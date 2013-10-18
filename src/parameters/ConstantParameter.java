package parameters;

public class ConstantParameter extends Parameter {

	public ConstantParameter(double value) {
		super(value);
	}

	public void addOnValueChange(OnValueChangeListener listener) {
	}

	public void removeOnValueChange(OnValueChangeListener listener) {
	}

	public double setValue(double value) {
		return 0;
	}

}
