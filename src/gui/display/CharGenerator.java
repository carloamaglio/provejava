package gui.display;

public final class CharGenerator {
	final char c;
	final int width;
	final int height;
	final byte[][] bitmap;
	public CharGenerator(CharTable ct, char c, byte[][] bitmap) {
		this.c = c;
		this.width = bitmap[0].length;
		this.height = bitmap.length;
		this.bitmap = bitmap;
		ct.add(this);
	}
}
