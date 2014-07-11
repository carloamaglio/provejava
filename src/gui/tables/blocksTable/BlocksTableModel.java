package gui.tables.blocksTable;

import javax.swing.table.DefaultTableModel;

public class BlocksTableModel extends DefaultTableModel {
	private static final long serialVersionUID = 9036603653630652760L;

	public BlocksTableModel() {
		super(2, 6);
	}

	public Object getValueAt(int row, int column) {
		return super.getValueAt(row, column);
	}

	@Override
	public void setValueAt(Object aValue, int row, int column) {
		super.setValueAt(aValue, row, column);
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return super.isCellEditable(row, column);
	}
}
