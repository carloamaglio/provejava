/*
 * Created on 16-set-2005
 *
 */
package _carlo_nuovi.archivio;

import java.util.GregorianCalendar;

import language.Translator;
import tools.Numbers;
import users.Users;

/**
 * @author Dott. Ing. Carlo Amaglio - BMB S.p.A.
 *
 * Evento memorizzato nello storico
 */
public abstract class MachineEvent {
	private final MachineEventType type;	// tipo di evento
	private final GregorianCalendar when;	// istante in cui si e' verificato l'evento
	private final String who;				// chi era l'utente nel momento in cui si e' verificato l'evento

	MachineEvent(MachineEventType type, GregorianCalendar when, String who) {
		this.type = type;
		this.when = when;
		this.who = who;
	}

	private static GregorianCalendar toCalendar(String d) {
		GregorianCalendar rv = new GregorianCalendar(Translator._locale);
		rv.setTimeInMillis(Numbers.parseLong(d));
		return rv;
	}


	public MachineEvent(String type, String when, String who) {
		this(MachineEventType.valueOf(type), toCalendar(when), who);
	}

	public MachineEvent(MachineEventType type) {
		this(type, new GregorianCalendar(Translator._locale), Users.getCurrentUserName());
	}

	public final MachineEventType type() {
		return type;
	}

	public final GregorianCalendar when() {
		return when;
	}

	public final String who() {
		return who;
	}

	public abstract String what();

	public abstract String oldValue(); 

	public abstract String newValue(); 
}
