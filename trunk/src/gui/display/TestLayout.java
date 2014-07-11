package gui.display;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import layout.GraphPaperLayout;

public class TestLayout {

	static class Demo {
		JFrame frame;
		JPanel panel;

		public Demo() {
			// Create and set up the window.
			frame = new JFrame("Display test");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setSize(new Dimension(220, 400));
//			frame.setPreferredSize(new Dimension(220, 400));
			frame.setLocation(500, 100);

			// Create and set up the panel.
			GraphPaperLayout layout = new GraphPaperLayout(20, 2, 2, 1, false);
			layout.beSmart();
			panel = new JPanel(layout);

			// Add the widgets.
			addWidgets();

			// Add the panel to the window.
			frame.getContentPane().add(panel, BorderLayout.CENTER);

			// Display the window.
			frame.pack();
			frame.setVisible(true);
			frame.setSize(new Dimension(220, 400));
		}

		SeqContainer sc;

		/**
		 * Create and add the widgets.
		 */
		private void addWidgets() {
			JLabel l;
			JButton b;

			{
				panel.add(
						new JScrollPane(sc = new SeqContainer(new SV())), 
						new Rectangle(0, 0, 1, 17));
			}

			{
				JPanel p = new JPanel(new GridLayout(2, 3));
				p.setBackground(Color.cyan);
				Dimension d = new Dimension(100, 100);
				p.setPreferredSize(d);
				p.setMinimumSize(d);
				p.setMaximumSize(d);
				p.setSize(d);
				p.setLocation(20, 20);

				l = new JLabel("bello");
				l.setBorder(BorderFactory.createLineBorder(Color.green));
				p.add(l);
				l = new JLabel("ciao");
				l.setBorder(BorderFactory.createLineBorder(Color.yellow));
				p.add(l);

				JPanel p1 = new JPanel(null);
				p1.add(p);
				panel.add(p1, new Rectangle(1, 0, 1, 17));
			}

			b = new JButton("uffa");
			b.setBorder(BorderFactory.createLineBorder(Color.cyan));
			b.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					sc.setSequenceViewer(new SV());
				}
			});
			panel.add(b, new Rectangle(0, 17, 2, 3));
		}
	}

	/*
	 * Sequence Container
	 */
	static class SeqContainer extends JPanel {
		private static final long serialVersionUID = -6802206970796780982L;

		SeqContainer() {
			super(new GridBagLayout());
			setBackground(Color.DARK_GRAY);
		}

		SeqContainer(SV sv) {
			this();
			setSequenceViewer(sv);
		}

		public void setSequenceViewer(SV sv) {
			removeAll();
			add(sv, new GridBagConstraints());
			repaint();
			revalidate();
//			validate();
//			doLayout();
//			invalidate();
		}
	}

	/*
	 * Sequence Viewer
	 */
	static class SV extends JPanel {
		private static final long serialVersionUID = -6301371130369134059L;
		int rows, cols;
		SV(int rows, int cols) {
			super();
			setLayout(new GridLayout(this.rows = rows, this.cols = cols));
//			setBackground(Color.blue);
//			setBorder(BorderFactory.createLineBorder(Color.magenta));
			setOpaque(false);
			for (int r=0; r<rows; r++) {
				for (int c=0; c<cols; c++) {
					add(new BV());
				}
			}
		}

		SV() {
			this((int)(Math.random()*10+1), (int)(Math.random()*10+1));
		}

		@Override
		public Dimension getPreferredSize() {
//			return super.getPreferredSize();
			Dimension d;
			if (getComponentCount()>0) {
				d = getComponent(0).getPreferredSize();
			} else {
				d = new Dimension(48, 48);
			}
			return new Dimension(cols*d.width, rows*d.height);
		}

	}

	/*
	 * Block Viewer
	 */
	static class BV extends JPanel {
		private static final long serialVersionUID = 5201163769538013723L;
		private Dimension preferredDimension;
		private static final int UNIT = 16;
		private static final int INSIDE = UNIT*4;
		private static final int BLOCK = INSIDE + 2*UNIT;
		BV() {
			super();
			FlowLayout l = (FlowLayout)this.getLayout();
			l.setAlignment(FlowLayout.CENTER);
			l.setHgap(UNIT);
			l.setVgap(UNIT);
//			setBackground(Color.blue);
			setBorder(BorderFactory.createLineBorder(Color.red));
			setOpaque(false);
			preferredDimension = new Dimension(BLOCK, BLOCK);
//			p.setBackground(Color.darkGray);
//			Dimension d = new Dimension(48, 48);
//			setPreferredSize(d);
//			p.setMinimumSize(d);
//			p.setMaximumSize(d);
//			p.setSize(d);
//			p.add(l);
			addMouseListener(new MouseAdapter() {
				@Override
			    public void mouseClicked(MouseEvent e) {
					click();
				}
			});
			click();
			JLabel b = new JLabel("5");
			b.setHorizontalAlignment(SwingConstants.CENTER);
//			b.setHorizontalTextPosition(SwingConstants.CENTER);
			b.setPreferredSize(new Dimension(INSIDE, INSIDE));
			b.setBackground(Color.green);
			b.setOpaque(true);
			add(b);
		}

		private void click() {
			setBackground(new Color((int)(Math.random()*255), (int)(Math.random()*255), (int)(Math.random()*255)));
		}

		@Override
		public Dimension getPreferredSize() {
//			return super.getPreferredSize();
			return preferredDimension;
		}

	}

	private static Demo createAndShowGUI() {
		JFrame.setDefaultLookAndFeelDecorated(true);
		Demo demo = new Demo();
		return demo;
	}

	static Demo demo = null;

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				demo = createAndShowGUI();
			}
		});
	}
}
