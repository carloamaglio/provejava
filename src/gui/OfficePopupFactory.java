package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolTip;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class OfficePopupFactory extends PopupFactory {

	private static final int MARGIN = 3;
	private static final Color START_COLOR = Color.WHITE;
	private static final Color END_COLOR = new Color(249, 249, 202);	//214, 217, 236);
	private static final Color BORDER_COLOR = new Color(180, 180, 57);	//Color.BLACK;

	@Override
	public Popup getPopup(Component owner, Component contents, int x, int y)
			throws IllegalArgumentException {
		if (contents instanceof JToolTip) {
			final JToolTip tooltip = (JToolTip) contents;
			JPanel panel = new JPanel(new BorderLayout(), true) {
				private static final long serialVersionUID = 1L;

				@Override
				protected void paintComponent(Graphics g) {
					Graphics2D g2d = (Graphics2D) g;
					g2d.setPaint(new GradientPaint(0, 0, START_COLOR, 0,
							tooltip.getPreferredSize().height + 2 * MARGIN,
							END_COLOR));
					g2d.fillRect(0, 0, tooltip.getPreferredSize().width + 2
							* MARGIN, tooltip.getPreferredSize().height + 2
							* MARGIN);
				}
			};
			Border border = new CompoundBorder(new LineBorder(BORDER_COLOR, 3), 
					new LineBorder(BORDER_COLOR, 3));
//					new EmptyBorder(MARGIN, MARGIN, MARGIN, MARGIN));
			panel.setBorder(border);
			JLabel label = new JLabel(tooltip.getTipText());
			label.setFont(tooltip.getFont());
			label.setOpaque(false);
			panel.add(label, BorderLayout.CENTER);
			panel.setPreferredSize(new Dimension(
					tooltip.getPreferredSize().width + 2 * MARGIN, tooltip
							.getPreferredSize().height
							+ 2 * MARGIN));
			return super.getPopup(owner, panel, x, y);
		}

		return super.getPopup(owner, contents, x, y);
	}

	public static JPanel buildPanel(final JLabel lbl) {
		JPanel panel = new JPanel(new BorderLayout(), true) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2d = (Graphics2D) g;
				g2d.setPaint(new GradientPaint(0, 0, START_COLOR, 0, lbl.getPreferredSize().height + 2 * MARGIN, END_COLOR));
				g2d.fillRect(0, 0, lbl.getPreferredSize().width + 2 * MARGIN, lbl.getPreferredSize().height + 2 * MARGIN);
			}
		};
		Border border = new CompoundBorder(new LineBorder(BORDER_COLOR, 2), new EmptyBorder(MARGIN, MARGIN, MARGIN, MARGIN));
		panel.setBorder(border);
		lbl.setOpaque(false);
		panel.add(lbl, BorderLayout.CENTER);
//		panel.setPreferredSize(new Dimension(
//				lbl.getPreferredSize().width + 2 * MARGIN, lbl.getPreferredSize().height + 2 * MARGIN));
		return panel;
	}

}
