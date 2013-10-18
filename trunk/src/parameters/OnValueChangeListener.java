package parameters;

import java.util.EventListener;

public interface OnValueChangeListener extends EventListener {
	void valueChanged(OnValueChangeEvent evt);
}
