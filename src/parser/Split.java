package parser;

import java.util.LinkedList;


public class Split {

	public static String decode(String value) {
		value = value.replaceAll("\\\\\\", "\\");
		value = value.replaceAll("\\\\s", ";");
		return value;
	}

	private static void test01() {
		String line = "filename=datalog.txt   period=10000 TRIEMPIMENTO OFSPRSPLC(6) ";
		String[] options = line.split("[ ]+");
		System.out.println("options:");
		for (String s : options) {
			System.out.println("'" + s + "'");
		}

		String vars = "  TRIEMPIMENTO,    OFSPRSPLC(6),CIAO   ,   BOH  ";
		vars = vars.trim();
		String[] variables = vars.split("[ ]*,[ ]*");
		System.out.println("variables:");
		for (String s : variables) {
			System.out.println("'" + s + "'");
		}

		line = "620#0#621##622#1#623#0#624##625##626#0#627##628#0#629#ALFAPLAST_CIA#630##631#10.02476923076923#632#PP(POLIPROPILENE)#633##634#0#635#0#636#64.37677103783611#637##638#0#639##";
		options = line.split("#");
		System.out.println("split(\"#\") di '" + line + "'");
		for (String s : options) {
			System.out.println("'" + s + "'");
		}
	}

	private static void test02() {
		String v = "ciao";
		System.out.println("'" + v + "' --> '" + decode(v) + "'");

		v = "ciao\\;10;ma";
		System.out.println("'" + v + "' --> '" + decode(v) + "'");

		v = "ciao\\sbo";
		System.out.println("'" + v + "' --> '" + decode(v) + "'");

		v = "ciao\\\\";
		System.out.println("'" + v + "' --> '" + decode(v) + "'");

		v = "ciao\\\\sbo";
		System.out.println("'" + v + "' --> '" + decode(v) + "'");
	}

	private static void test03() {
		String s = "660#19.736873999999997#661#6.0#662##663#0#664##665##666##667##668#130165#669#0#670#0#671##672##673#0#674#0#675#1#676##677#15.0#678#303.88131200000004#679##";
		s = "######";
		String[] t = s.split("#");
		System.out.println("len = " + t.length);
		for (String v : t) {
			System.out.println("'" + (v!=null ? v : "null") + "'");
		}
	}

	private static void test04() {
		String s = "01 a";
		String[] w = s.split(" ");
		if (w!=null) {
			if (w.length>0) {
				for (String t : w) {
					System.out.println("t='" + t + "'");
				}
			} else {
				System.out.println("w.length=0!");
			}
		} else {
			System.out.println("w=null!");
		}
	}

	private static void test05() {
		LinkedList<String> l = new LinkedList<String>();

		l.add("[*******************************************************************************");
		l.add("[ PLC Selca Macchina Elettrica");
		l.add("[");
		l.add("[ 25.08.2004 Prima versione senza simulazioni");
		l.add("[");
		l.add("[ AC 060218	TEMPOMANT(i) non piu' modificato da PLC e considerato come incrementale, quindi");
		l.add("[ 			TMP2P1(i) e' calcolato come somma dei TEMPOMANT(i) precedenti.");
		l.add("[ 			Aggiunta la variabile SOMMAI nella sezione RAM32");
		l.add("[ AC 060220	Portata a 8 la dimensione dei vettori relativi al profilo di dosatura");
		l.add("[ 			Corretto test per allarme tolleranza zone contenitore");
		l.add("[ AC 060223	Aggiunte variabili per raffreddamento stampo (RAFS*)");
		l.add("[");
		l.add("[ AMPLIAMENTO NUMERO MAX CARATTERI = 12");
		l.add("[*******************************************************************************");
		l.add("CONST");
		l.add("_MXCHR = 12");
		l.add("");
		l.add("[*******************************************************************************");
		l.add("[ 								INPUT");
		l.add("[*******************************************************************************");
		l.add("[ Input primo modulo");
		l.add("[ Inp: 	1  = dimensione variabile (bit)");
		l.add("[ 		17 = indirizzo \"master rio\" (sempre 17)");
		l.add("[ 		2  = indirizzo modulo impostabile con uno switch sulla scheda");
		l.add("		   		");
		l.add("");
		l.add("[ PULSANTIERA NUOVA RIO");
		l.add("INP,8,17,62");
		l.add("KEYIN1");
		l.add("[*******************************************************************************");
		l.add("[    	 						OUTPUT");
		l.add("[ OUTPUT MODULO PULSANTIERA");
		l.add("OUT,8,17,62");
		l.add("KEYO1");
		l.add("KEYO2");
		l.add("SRAM,32");
		l.add("EMCCPCM(8)");
		l.add("EMCCPCA(8)");
		l.add("APESCCPC(8)");
		l.add("AVFASESICST(8)									[ parametri di avviamento");
		l.add("NCAVSICST										[ numero cicli avviamento sicurezza stampo");
		l.add("APNCAVSICST										[ numero cicli appoggio avviamento sicurezza stampo");
		l.add("RAM,32");
		l.add("RAM32");
		l.add("CIAO										[ numero cicli avviamento sicurezza stampo");
		l.add("BDELAY");

		String regex = ".?RAM\\,.*|.?OUT\\,.*|BDELAY.*";
		for (String s : l) {
			System.out.println("" + s.matches(regex) + ": " + s);
		}
	}

	public static void main(String[] args) {
		test05();
//		test03();
//		test02();
//		test01();
	}

}
