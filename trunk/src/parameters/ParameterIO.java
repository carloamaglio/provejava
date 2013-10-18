package parameters;

public interface ParameterIO {
	double getValue();
	double setValue(double value);
	void addOnValueChange(OnValueChangeListener listener);
	void removeOnValueChange(OnValueChangeListener listener);
}
