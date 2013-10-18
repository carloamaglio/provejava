package gui.display;

import java.awt.Graphics2D;
import java.awt.Insets;

public class MatrixDisplay extends Display {

	private static final long serialVersionUID = 6372569473459461827L;

	int pixelwidth;
	int pixelheight;
	int hgap;
	int vgap;
	int hpitch;
	int vpitch;
	byte[][] bitmap;

	public MatrixDisplay(int width, int height, int pixelsize, int gap) {
		super(width, height);
		this.pixelwidth = this.pixelheight = pixelsize;
		this.hgap = this.vgap = gap;
		this.hpitch = this.pixelwidth + this.hgap;
		this.vpitch = this.pixelheight + this.vgap;
		bitmap = new byte[height][width];
	}

	public MatrixDisplay(int width, int height, int pixelsize) {
		this(width, height, pixelsize, 1);
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
				if (bitmap[r][c]!=0) g2d.fillRect(x, y, pixelwidth, pixelheight);
			}
		}
	}
}
