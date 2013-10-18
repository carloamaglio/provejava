/*
 * Created on 5-set-2005
 *
 */
package _carlo;

/**
 * @author Dott. Ing. Carlo Amaglio - BMB S.p.A.
 *
 * History of changes
 */
public class ChangedValue extends MachineEventType {
	String oldValue;
	String newValue;
	
	ChangedValue(String desc) {
		super(desc);
	}
}
