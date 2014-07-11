package gui.layouts;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;

public class CentreLayout implements LayoutManager, java.io.Serializable {
	private static final long serialVersionUID = -7762514203528734751L;

	public void addLayoutComponent(String name, Component comp) {}

	public void removeLayoutComponent(Component comp) {}

	public Dimension preferredLayoutSize(Container target) {
		return target.getPreferredSize();
	}

	public Dimension minimumLayoutSize(Container target) {
		return target.getMinimumSize();
	}

	public void layoutContainer(Container target) {
		synchronized (target.getTreeLock()) {
			Insets insets = target.getInsets();
			Dimension size = target.getSize();
			int w = size.width - (insets.left + insets.right);
			int h = size.height - (insets.top + insets.bottom);
			int count = target.getComponentCount();

			for (int i = 0; i < count; i++) {
				Component m = target.getComponent(i);
				if (m.isVisible()) {
					Dimension d = m.getPreferredSize();
					m.setBounds((w - d.width) / 2, (h - d.height) / 2, d.width, d.height);
					break;
				}
			}
		}
	}

}
