package parameters;


public abstract class Parameter implements ParameterIO {

	protected double value;

	public Parameter(double value) {
		this.value = value;
	}

	public double getValue() {
		return value;
	}

}
