package gui.display;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;



public abstract class Display extends SDLinearGradientPanel {

	final int width;
	final int height;

	CharTable font;
	int cx, cy;		// posizione del cursore (in caratteri)

	public Display(int width, int height, CharTable font) {
		super();
		this.width = width;
		this.height = height;
		this.font = font;
		setOpaque(true);
	}

	public abstract void clear();
	public abstract void put(int x, int y, byte[][] sprite);
    protected abstract void paintComponent(Graphics2D g, Insets insets, int width, int height);

	@Override
    protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Insets insets = getInsets();
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.translate(insets.left, insets.top);
		int width = this.getWidth() - insets.left - insets.right;
		int height = this.getHeight() - insets.top - insets.bottom;

	    paintComponent(g2d, insets, width, height);

		g2d.dispose(); // clean up
	}

	/**
	 * Posiziona il 'cursore' alla posizione x,y (in caratteri)
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
			put(cx, cy, cg.bitmap);
			if ((cx+=cg.width) > width-cg.width) {
				cx = 0;
				if ((cy+=cg.height) > height-cg.height) {
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
		clear();
		gotoxy(0, 0);
	}
}
