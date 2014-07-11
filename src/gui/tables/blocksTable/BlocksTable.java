package gui.tables.blocksTable;

import java.awt.Dimension;

import javax.swing.JTable;

public class BlocksTable extends JTable {
	private static final long serialVersionUID = 7982436468205244384L;

	private int cellSize;

	public BlocksTable() {
		super(new BlocksTableModel());
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		for (int i=0; i<this.getColumnCount(); i++) {
			getColumnModel().getColumn(i).setResizable(false);
		}
		setCellSize(128);

		setDefaultRenderer(Object.class, new BlocksTableCellRenderer());

		setPreferredScrollableViewportSize(new Dimension(500, 350));
		setFillsViewportHeight(true);
	}

	public void setCellSize(int cellSize) {
		this.cellSize = cellSize;

		setRowHeight(cellSize);
		for (int i=0; i<this.getColumnCount(); i++) {
			getColumnModel().getColumn(i).setPreferredWidth(cellSize);
			getColumnModel().getColumn(i).setResizable(false);
		}
	}

//	private final static class ColumnModel extends DefaultTableColumnModel {
//		ColumnModel() {
//			
//		}
//	}
}
