package parameters;

import java.util.EventObject;

public class OnValueChangeEvent extends EventObject {
	private static final long serialVersionUID = 1L;

	double oldValue;
	double newValue;

	public OnValueChangeEvent(Object source, double oldValue, double newValue) {
		super(source);
		this.oldValue = oldValue;
		this.newValue = newValue;
	}
}
