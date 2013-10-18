/*
 * Created on 16-set-2005
 *
 */
package _carlo_nuovi.archivio;

/**
 * @author Dott. Ing. Carlo Amaglio - BMB S.p.A.
 *
 */
public class TagChanged extends ValueChanged {
	public TagChanged(String varName, String oldValue, String newValue) {
		super(MachineEventType.TAGCHANGED, varName, oldValue, newValue);
	}
	public TagChanged(String varName, double oldValue, double newValue) {
		this(varName, Double.toString(oldValue), Double.toString(newValue));
	}
}
