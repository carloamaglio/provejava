package gui.display;

import java.awt.Graphics2D;
import java.awt.Insets;

public class SevenDisplay extends Display {

	private static final long serialVersionUID = -7407422061398168770L;

	int segthick;	// spessore di un segmento
	int seglen;	// lunghezza di un segmento
	int hgap;
	int vgap;
	int hpitch;
	int vpitch;
	int rows;
	int cols;
	byte[][] bitmap;

	/**
	 * 
	 * @param width larghezza in pixel
	 * @param height altezza in pixel
	 * @param segsize lunghezza di un segmento
	 * @param gap distanza tra due cifre
	 */
	public SevenDisplay(int width, int height, int segsize, int gap) {
		super(width, height);
		this.segthick = segsize;
		this.seglen = 4*segsize;
		this.hgap = this.vgap = gap;
		this.hpitch = this.seglen + 2 * this.segthick + this.hgap;
		this.vpitch = this.seglen + 2 * this.segthick + this.vgap;
		this.rows = height / this.vpitch;
		this.cols = width / this.hpitch;
		bitmap = new byte[this.rows][this.cols];
	}

	public SevenDisplay(int width, int height, int segsize) {
		this(width, height, segsize, 1);
	}

	@Override
	public void clear() {
		for (int r=0; r<bitmap.length; r++) {
			for (int c=0; c<bitmap[r].length; c++) {
				bitmap[r][c] = 0;
			}
		}
		repaint();
	}

	@Override
	public void put(int x, int y, byte[][] sprite) {
		for (int r=0; r<sprite.length && r+y<height; r++) {
			for (int c=0; c<sprite[r].length && c+x<width; c++) {
				bitmap[r+y][c+x] = sprite[r][c];
			}
		}
		repaint();
	}

	@Override
    protected void paintComponent(Graphics2D g2d, Insets insets, int width, int height) {
		g2d.setColor(getForeground());
		for (int r=0, y=insets.top; r<bitmap.length; r++, y+=vpitch) {
			for (int c=0, x=insets.left; c<bitmap[r].length; c++, x+=hpitch) {
				if (bitmap[r][c]!=0) drawDigit(g2d, bitmap[r][c], x, y);
			}
		}
	}

	private void drawDigit(Graphics2D g, byte bitmap, int x, int y) {
		if ((bitmap & 0x80)!=0) g.fillRect(x+segthick,			y,						seglen,		segthick);		// A
		if ((bitmap & 0x40)!=0) g.fillRect(x+segthick+seglen,	y+segthick,				segthick,	seglen);		// B
		if ((bitmap & 0x20)!=0) g.fillRect(x+segthick+seglen,	y+2*segthick+seglen,	segthick,	seglen);		// C
		if ((bitmap & 0x10)!=0) g.fillRect(x+segthick,			y+2*segthick+2*seglen,	seglen,		segthick);		// D
		if ((bitmap & 0x08)!=0) g.fillRect(x,					y+2*segthick+seglen,	segthick,	seglen);		// E
		if ((bitmap & 0x04)!=0) g.fillRect(x,					y+segthick,				segthick,	seglen);		// F
		if ((bitmap & 0x02)!=0) g.fillRect(x+segthick,			y+segthick+seglen,		seglen,		segthick);		// G
	}
}
