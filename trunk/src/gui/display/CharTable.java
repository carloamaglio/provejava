package gui.display;

import java.util.Hashtable;

public final class CharTable {
	final Hashtable<Character, CharGenerator> table;

	public CharTable() {
		table = new Hashtable<Character, CharGenerator>();
	}

	public CharTable add(CharGenerator c) {
		table.put(c.c, c);
		return this;
	}

	public CharGenerator get(char c) {
		CharGenerator rv = table.get(c);
		return rv;
	}

	public static final CharTable font7x5 = new CharTable();
	public static final CharTable font7seg = new CharTable();

	private static final CharGenerator zero = new CharGenerator(font7x5, '0', 
			new byte[][] {
				{ 0, 1, 1, 1, 0, 0 },
				{ 1, 0, 0, 0, 1, 0 },
				{ 1, 0, 0, 1, 1, 0 },
				{ 1, 0, 1, 0, 1, 0 },
				{ 1, 1, 0, 0, 1, 0 },
				{ 1, 0, 0, 0, 1, 0 },
				{ 0, 1, 1, 1, 0, 0 },
				{ 0, 0, 0, 0, 0, 0 },
			}
	);

	private static final CharGenerator uno = new CharGenerator(font7x5, '1', 
			new byte[][] {
				{ 0, 0, 1, 0, 0, 0 },
				{ 0, 1, 1, 0, 0, 0 },
				{ 0, 0, 1, 0, 0, 0 },
				{ 0, 0, 1, 0, 0, 0 },
				{ 0, 0, 1, 0, 0, 0 },
				{ 0, 0, 1, 0, 0, 0 },
				{ 0, 1, 1, 1, 0, 0 },
				{ 0, 0, 0, 0, 0, 0 },
			}
	);

	private static final CharGenerator due = new CharGenerator(font7x5, '2', 
			new byte[][] {
				{ 0, 1, 1, 1, 0, 0 }, 
				{ 1, 0, 0, 0, 1, 0 }, 
				{ 0, 0, 0, 0, 1, 0 }, 
				{ 0, 0, 0, 1, 0, 0 }, 
				{ 0, 1, 1, 0, 0, 0 },
				{ 1, 0, 0, 0, 0, 0 },
				{ 1, 1, 1, 1, 1, 0 },
				{ 0, 0, 0, 0, 0, 0 },
			}
	);

	private static final CharGenerator tre = new CharGenerator(font7x5, '3', 
			new byte[][] {
				{ 0, 1, 1, 1, 0, 0 },
				{ 1, 0, 0, 0, 1, 0 },
				{ 0, 0, 0, 0, 1, 0 },
				{ 0, 0, 1, 1, 0, 0 },
				{ 0, 0, 0, 0, 1, 0 },
				{ 1, 0, 0, 0, 1, 0 },
				{ 0, 1, 1, 1, 0, 0 },
				{ 0, 0, 0, 0, 0, 0 },
			}
	);

	private static final CharGenerator quattro = new CharGenerator(font7x5, '4', 
			new byte[][] {
				{ 0, 0, 0, 1, 0, 0 },
				{ 0, 0, 1, 1, 0, 0 },
				{ 0, 1, 0, 1, 0, 0 },
				{ 1, 0, 0, 1, 0, 0 },
				{ 1, 1, 1, 1, 1, 0 },
				{ 0, 0, 0, 1, 0, 0 },
				{ 0, 0, 0, 1, 0, 0 },
				{ 0, 0, 0, 0, 0, 0 },
			}
	);

	private static final CharGenerator cinque = new CharGenerator(font7x5, '5', 
			new byte[][] {
				{ 1, 1, 1, 1, 1, 0 },
				{ 1, 0, 0, 0, 0, 0 },
				{ 1, 0, 0, 0, 0, 0 },
				{ 1, 1, 1, 1, 0, 0 },
				{ 0, 0, 0, 0, 1, 0 },
				{ 1, 0, 0, 0, 1, 0 },
				{ 0, 1, 1, 1, 0, 0 },
				{ 0, 0, 0, 0, 0, 0 },
			}
	);

	private static final CharGenerator sei = new CharGenerator(font7x5, '6', 
			new byte[][] {
				{ 0, 0, 1, 1, 0, 0 },
				{ 0, 1, 0, 0, 0, 0 },
				{ 1, 0, 0, 0, 0, 0 },
				{ 1, 1, 1, 1, 0, 0 },
				{ 1, 0, 0, 0, 1, 0 },
				{ 1, 0, 0, 0, 1, 0 },
				{ 0, 1, 1, 1, 0, 0 },
				{ 0, 0, 0, 0, 0, 0 },
			}
	);

	private static final CharGenerator sette = new CharGenerator(font7x5, '7', 
			new byte[][] {
				{ 1, 1, 1, 1, 1, 0 },
				{ 0, 0, 0, 0, 1, 0 },
				{ 0, 0, 0, 1, 0, 0 },
				{ 0, 0, 1, 0, 0, 0 },
				{ 0, 1, 0, 0, 0, 0 },
				{ 0, 1, 0, 0, 0, 0 },
				{ 0, 1, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0 },
			}
	);

	private static final CharGenerator otto = new CharGenerator(font7x5, '8', 
			new byte[][] {
				{ 0, 1, 1, 1, 0, 0 },
				{ 1, 0, 0, 0, 1, 0 },
				{ 1, 0, 0, 0, 1, 0 },
				{ 0, 1, 1, 1, 0, 0 },
				{ 1, 0, 0, 0, 1, 0 },
				{ 1, 0, 0, 0, 1, 0 },
				{ 0, 1, 1, 1, 0, 0 },
				{ 0, 0, 0, 0, 0, 0 },
			}
	);

	private static final CharGenerator nove = new CharGenerator(font7x5, '9', 
			new byte[][] {
				{ 0, 1, 1, 1, 0, 0 },
				{ 1, 0, 0, 0, 1, 0 },
				{ 1, 0, 0, 0, 1, 0 },
				{ 0, 1, 1, 1, 1, 0 },
				{ 0, 0, 0, 0, 1, 0 },
				{ 0, 0, 0, 1, 0, 0 },
				{ 0, 1, 1, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0 },
			}
	);


	private static final CharGenerator zero7s = new CharGenerator(font7seg, '0', 
			new byte[][] {
				{ (byte)0xFC },
			}
	);

	private static final CharGenerator uno7s = new CharGenerator(font7seg, '1', 
			new byte[][] {
				{ (byte)0x60 },
			}
	);

	private static final CharGenerator due7s = new CharGenerator(font7seg, '2', 
			new byte[][] {
				{ (byte)0xD8 },
			}
	);

	private static final CharGenerator tre7s = new CharGenerator(font7seg, '3', 
			new byte[][] {
				{ (byte)0xF2 },
			}
	);

	private static final CharGenerator quattro7s = new CharGenerator(font7seg, '4', 
			new byte[][] {
				{ (byte)0x66 },
			}
	);

	private static final CharGenerator cinque7s = new CharGenerator(font7seg, '5', 
			new byte[][] {
				{ (byte)0xB6 },
			}
	);

	private static final CharGenerator sei7s = new CharGenerator(font7seg, '6', 
			new byte[][] {
				{ (byte)0xCE },
			}
	);

	private static final CharGenerator sette7s = new CharGenerator(font7seg, '7', 
			new byte[][] {
				{ (byte)0xE0 },
			}
	);

	private static final CharGenerator otto7s = new CharGenerator(font7seg, '8', 
			new byte[][] {
				{ (byte)0xFE },
			}
	);

	private static final CharGenerator nove7s = new CharGenerator(font7seg, '9', 
			new byte[][] {
				{ (byte)0xF6 },
			}
	);
}
