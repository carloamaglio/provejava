package parameters;

import javax.swing.event.EventListenerList;

public class VariableParameter extends Parameter {

	public VariableParameter(double value) {
		super(value);
	}

	public VariableParameter() {
		this(0);
	}

	public double setValue(double value) {
		double rv = this.value;
		if (this.value != value) {
			this.value = value;
			fireValueChangeEvent(rv);
		}
		return rv;
	}

	protected EventListenerList listenerList = new EventListenerList();

	public void addOnValueChange(OnValueChangeListener listener) {
		listenerList.add(OnValueChangeListener.class, listener);
	}

	public void removeOnValueChange(OnValueChangeListener listener) {
		listenerList.remove(OnValueChangeListener.class, listener);
	}

	void fireValueChangeEvent(double oldValue) {
		Object[] listeners = listenerList.getListenerList();
		if (listeners.length > 0) {
			OnValueChangeEvent evt = new OnValueChangeEvent(this, oldValue, value);
			for (int i = 0; i < listeners.length; i += 2) {
				if (listeners[i] == OnValueChangeListener.class) {
					((OnValueChangeListener) listeners[i + 1]).valueChanged(evt);
				}
			}
		}
	}

}
