package machineSequence.block;

import java.awt.Color;
import java.awt.Component;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;

import machineSequence.HasName;
import machineSequence.Sequence;

public class Block implements HasName {

	private String name;
	private Sequence sequence;
	private List<Block> nextBlocks;

	public Block() {
		nextBlocks = new LinkedList<Block>();
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	public void setSequence(Sequence sequence) {
		this.sequence = sequence;
		for (Block b : nextBlocks) {
			if (!sequence.equals(b.sequence)) b.setSequence(sequence);
		}
	}

	public Block addNextBlock(Block block) {
		nextBlocks.add(block);
		return this;
	}

	Component component;

	public Component getComponent() {
		return component;
	}

	public static Block createChiusuraIniezioneApertura() {
		Block rv = new Block();
		rv.setName("ChiusuraIniezioneApertura");
		BlockViewerChiusuraIniezioneApertura v = new BlockViewerChiusuraIniezioneApertura();
		new BlockControllerChiusuraIniezioneApertura(v);
		rv.component = v;
		return rv;
	}

	public static Block createRadialeMov1(int radiale) {
		Block rv = new Block();
		rv.setName("RadialeMov1." + radiale);
		rv.component = new BlockViewerRadialeMov1(radiale);
		return rv;
	}

	public static Block createRadialeMov2(int radiale) {
		Block rv = new Block();
		rv.setName("RadialeMov2." + radiale);
		rv.component = new BlockViewerRadialeMov2(radiale);
		return rv;
	}
}

abstract class BlockViewer extends JPanel {
	private static final long serialVersionUID = -7377710529910216821L;

	public enum State { Undefined, Waiting, Running, Finished }
	protected State state;

	BlockViewer() {
		super();
	}
	public void setGlobalState(State state) {
		if (state != this.state) {
			this.state = state;
			switch (state) {
				case Undefined:
					setBackground(Color.gray);
					break;
				case Waiting:
					setBackground(Color.red);
					break;
				case Running:
					setBackground(Color.yellow);
					break;
				case Finished:
					setBackground(Color.green);
					break;
			}
			repaint();
		}
	}
}

abstract class BlockController<V extends BlockViewer> {

	protected V viewer;
	BlockController(V viewer) {
		this.viewer = viewer;
	}
}


class BlockViewerChiusuraIniezioneApertura extends BlockViewer {
	private static final long serialVersionUID = -6232435639044203579L;

	BlockViewerChiusuraIniezioneApertura() {
		super();
	}

	public void setMouldMovement() {
		
	}
	public void setInjectionMovement() {
		
	}
}

class BlockControllerChiusuraIniezioneApertura extends BlockController<BlockViewerChiusuraIniezioneApertura> {

	BlockControllerChiusuraIniezioneApertura(BlockViewerChiusuraIniezioneApertura viewer) {
		super(viewer);
		viewer.setGlobalState(BlockViewer.State.Undefined);
	}

	void init() {
		
	}
}

class BlockViewerRadialeMov1 extends BlockViewer {
	private static final long serialVersionUID = -1184807853759611736L;

	BlockViewerRadialeMov1(int radiale) {
		super();
	}

	@Override
	public void setGlobalState(State state) {
		
	}
}

class BlockViewerRadialeMov2 extends BlockViewer {
	private static final long serialVersionUID = 4843730234114969402L;

	BlockViewerRadialeMov2(int radiale) {
		super();
	}

	@Override
	public void setGlobalState(State state) {
		
	}
}

