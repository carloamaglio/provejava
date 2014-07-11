package gui.layouts;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;

public class SquareCellGridLayout implements LayoutManager, java.io.Serializable {
	private static final long serialVersionUID = -5384643874456398717L;
	int rows;
	int cols;

	public SquareCellGridLayout(int rows, int cols) {
		if ((rows == 0) && (cols == 0)) { throw new IllegalArgumentException("rows and cols cannot both be zero"); }
		this.rows = rows;
		this.cols = cols;
	}

	/**
	 * Gets the number of rows in this layout.
	 * 
	 * @return the number of rows in this layout
	 * @since JDK1.1
	 */
	public int getRows() {
		return rows;
	}

	/**
	 * Sets the number of rows in this layout to the specified value.
	 * 
	 * @param rows
	 *            the number of rows in this layout
	 * @exception IllegalArgumentException
	 *                if the value of both <code>rows</code> and <code>cols</code> is set to zero
	 * @since JDK1.1
	 */
	public void setRows(int rows) {
		if ((rows == 0) && (this.cols == 0)) { throw new IllegalArgumentException("rows and cols cannot both be zero"); }
		this.rows = rows;
	}

	/**
	 * Gets the number of columns in this layout.
	 * 
	 * @return the number of columns in this layout
	 * @since JDK1.1
	 */
	public int getColumns() {
		return cols;
	}

	/**
	 * Sets the number of columns in this layout to the specified value. Setting the number of columns has no affect on the layout if the number of rows specified by a constructor or by the <tt>setRows</tt> method is non-zero. In that case, the number of
	 * columns displayed in the layout is determined by the total number of components and the number of rows specified.
	 * 
	 * @param cols
	 *            the number of columns in this layout
	 * @exception IllegalArgumentException
	 *                if the value of both <code>rows</code> and <code>cols</code> is set to zero
	 * @since JDK1.1
	 */
	public void setColumns(int cols) {
		if ((cols == 0) && (this.rows == 0)) { throw new IllegalArgumentException("rows and cols cannot both be zero"); }
		this.cols = cols;
	}

	/**
	 * Adds the specified component with the specified name to the layout.
	 * 
	 * @param name
	 *            the name of the component
	 * @param comp
	 *            the component to be added
	 */
	public void addLayoutComponent(String name, Component comp) {}

	/**
	 * Removes the specified component from the layout.
	 * 
	 * @param comp
	 *            the component to be removed
	 */
	public void removeLayoutComponent(Component comp) {}

	public Dimension preferredLayoutSize(Container parent) {
		synchronized (parent.getTreeLock()) {
			Insets insets = parent.getInsets();
			int ncomponents = parent.getComponentCount();
			int nrows = rows;
			int ncols = cols;

			if (nrows > 0) {
				ncols = (ncomponents + nrows - 1) / nrows;
			} else {
				nrows = (ncomponents + ncols - 1) / ncols;
			}
			int w = 0;
			int h = 0;
			for (int i = 0; i < ncomponents; i++) {
				Component comp = parent.getComponent(i);
				Dimension d = comp.getPreferredSize();
				if (w < d.width) {
					w = d.width;
				}
				if (h < d.height) {
					h = d.height;
				}
			}
			if (w < h) w = h; else h = w;
			return new Dimension(
					insets.left + insets.right + ncols * w, 
					insets.top + insets.bottom + nrows * h
			);
		}
	}

	public Dimension minimumLayoutSize(Container parent) {
		synchronized (parent.getTreeLock()) {
			Insets insets = parent.getInsets();
			int ncomponents = parent.getComponentCount();
			int nrows = rows;
			int ncols = cols;

			if (nrows > 0) {
				ncols = (ncomponents + nrows - 1) / nrows;
			} else {
				nrows = (ncomponents + ncols - 1) / ncols;
			}
			int w = 0;
			int h = 0;
			for (int i = 0; i < ncomponents; i++) {
				Component comp = parent.getComponent(i);
				Dimension d = comp.getMinimumSize();
				if (w < d.width) {
					w = d.width;
				}
				if (h < d.height) {
					h = d.height;
				}
			}
			if (w < h) w = h; else h = w;
			return new Dimension(
					insets.left + insets.right + ncols * w, 
					insets.top + insets.bottom + nrows * h
			);
		}
	}

	public void layoutContainer(Container parent) {
		synchronized (parent.getTreeLock()) {
			Insets insets = parent.getInsets();
			int ncomponents = parent.getComponentCount();
			int nrows = rows;
			int ncols = cols;

			if (ncomponents == 0) { return; }
			if (nrows > 0) {
				ncols = (ncomponents + nrows - 1) / nrows;
			} else {
				nrows = (ncomponents + ncols - 1) / ncols;
			}
			int w = parent.getWidth() - (insets.left + insets.right);
			int h = parent.getHeight() - (insets.top + insets.bottom);
			w /= ncols;
			h /= nrows;
			if (w < h) h = w; else w = h;

			for (int c = 0, x = insets.left; c < ncols; c++, x += w) {
				for (int r = 0, y = insets.top; r < nrows; r++, y += h) {
					int i = r * ncols + c;
					if (i < ncomponents) {
						parent.getComponent(i).setBounds(x, y, w, h);
					}
				}
			}
		}
	}

	/**
	 * Returns the string representation of this grid layout's values.
	 * 
	 * @return a string representation of this grid layout
	 */
	public String toString() {
		return getClass().getName() + "rows=" + rows + ",cols=" + cols + "]";
	}
}
