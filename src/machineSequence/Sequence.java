package machineSequence;

import machineSequence.block.Block;

public class Sequence {

	private String name;
	private Block start;

	public Sequence() {
		
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setStart(Block start) {
		this.start = start;
	}

	public static Sequence createRadialeCiclo01(int radiale) {
		Sequence rv = new Sequence();
		rv.setStart(
				Block.createRadialeMov1(radiale)
				.addNextBlock(
						Block.createChiusuraIniezioneApertura()
						.addNextBlock(
								Block.createRadialeMov2(radiale)
						)
				)
		);
		return rv;
	}

	public static void main() {
		Sequence s = Sequence.createRadialeCiclo01(1);
	}
}
