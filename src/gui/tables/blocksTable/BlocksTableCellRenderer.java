package gui.tables.blocksTable;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class BlocksTableCellRenderer extends JPanel implements TableCellRenderer {
	private static final long serialVersionUID = 4987689414030865786L;

	public BlocksTableCellRenderer() {
		super();
//		setBorder(BorderFactory.createRaisedBevelBorder());
		setOpaque(true);
		setBackground(new Color(44, 64, 80));
//		setPreferredSize(new Dimension(48, 48));
//		setMinimumSize(new Dimension(48, 48));
	}

	private int rnd() {
		return (int)(Math.random()*255);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
//		setBackground(new Color(rnd(), rnd(), rnd()));
		return this;
	}
}
