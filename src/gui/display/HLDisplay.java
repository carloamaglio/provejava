package gui.display;

import java.awt.Component;

public class HLDisplay {

	final Display display;
	CharTable font;
	int cx, cy;		// posizione del cursore (in pixel)

	public HLDisplay(Display display, CharTable font) {
		this.display = display;
		this.font = font;
	}

	public HLDisplay(Display display) {
		this(display, CharTable.font7x5);
	}

	public Component getComponent() {
		return display;
	}

	/**
	 * Posiziona il 'cursore' alla posizione x,y (in pixel)
	 * @param x
	 * @param y
	 */
	public void gotoxy(int x, int y) {
		cx = x;
		cy = y;
	}

	public void putc(char c) {
		CharGenerator cg = font.get(c);
		if (cg!=null) {
			display.put(cx, cy, cg.bitmap);
			if ((cx+=cg.width) > display.width-cg.width) {
				cx = 0;
				if ((cy+=cg.height) > display.height-cg.height) {
					cy = 0;
				}
			}
		}
	}

	public void puts(String s) {
		for (int i=0; i<s.length(); i++) {
			char c = s.charAt(i);
			putc(c);
		}
	}

	public void puts(int x, int y, String s) {
		gotoxy(x, y);
		puts(s);
	}

	public void cls() {
		display.clear();
		gotoxy(0, 0);
	}
}
