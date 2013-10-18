package graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class SmallTrend extends JComponent {
	private static final long serialVersionUID = -8149490291911984607L;

	static class FIFO {
		int[] values;
		int first;
		int n;

		FIFO(int size) {
			setSize(size);
		}

		void add(int value) {
			if (n<values.length) {
				values[n++] = value;
			} else {
				values[first] = value;
				if (++first==values.length) first=0;
			}
		}

		int get(int i) {
			if (i<n) {
				i += first;
				if (i<values.length) {
					return values[i];
				} else {
					return values[i-values.length];
				}
			} else {
				return 0;
			}
		}

		void setSize(int size) {
			values = new int[size];
			clear();
		}

		void clear() {
			first = n = 0;
		}

		int getMin() {
			if (n>0) {
				int rv = Integer.MAX_VALUE;
				for (int i=0; i<n; i++) {
					int v = get(i);
					if (v < rv) rv=v;
				}
				return rv;
			} else {
				return 0;
			}
		}

		int getMax() {
			if (n>0) {
				int rv = Integer.MIN_VALUE;
				for (int i=0; i<n; i++) {
					int v = get(i);
					if (v > rv) rv=v;
				}
				return rv;
			} else {
				return 100;
			}
		}
	}

	FIFO data;

	boolean autorange;
	int min;
	int max;

	public SmallTrend(int numOfSamples) {
		data = new FIFO(numOfSamples);
		setNumOfSamples(numOfSamples);
		clear();
		setAutorange();
		setOpaque(true);
		setBackground(new Color(0, 0, 50));
		setForeground(Color.cyan);
	}

	@Override
    protected void paintComponent(Graphics g) {
		Insets insets = getInsets();
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.translate(insets.left, insets.top);
		int width = this.getWidth() - insets.left - insets.right;
		int height = this.getHeight() - insets.top - insets.bottom;

		g2d.setBackground(getBackground());
		g2d.clearRect(0, 0, width, height);

		// griglia
		if (width>40 && height>40) {
		}

//		g2d.setColor(Color.blue);

		if (data.n>1) {
			int n1 = data.values.length-1;
			int lx=0, ly=0;
			for (int i=0; i<data.n; i++) {
				int x = (i * width) / n1;
				int y = ((data.get(i) - min) * height) / (max - min);

//				x = width - x;
				y = height - y;
				if (i>0) {
					g2d.drawLine(x, y, lx, ly);
//				} else {
//					g2d.drawRect(x, y, 1, 1);
				}
				lx = x;
				ly = y;
			}
		} else {
			g2d.drawString("No data", 10, height / 2);
		}

		g2d.dispose(); // clean up
	}

	public void clear() {
		data.clear();
	}

	public void addSample(int y) {
		data.add(y);
		if (autorange) {
			min = data.getMin();
			max = data.getMax();
		}
	}

	public void setRange(int min, int max) {
		autorange = false;
		this.min = min;
		this.max = max;
	}

	public void setAutorange() {
		autorange = true;
		min = data.getMin();
		max = data.getMax();
	}

	public void setNumOfSamples(int n) {
		data.setSize(n);
	}

	static class Demo {
		JFrame frame;
		JPanel panel;
		SmallTrend[] comp = new SmallTrend[4];

		public Demo() {
			//Create and set up the window.
			frame = new JFrame("Demo");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setSize(new Dimension(220, 400));
//			frame.setPreferredSize(new Dimension(220, 400));
			frame.setLocation(500, 100);

			//Create and set up the panel.
			panel = new JPanel(new GridLayout(3, 3));

			//Add the widgets.
			addWidgets();

			//Add the panel to the window.
			frame.getContentPane().add(panel, BorderLayout.CENTER);

			//Display the window.
			frame.pack();
			frame.setVisible(true);
			frame.setSize(new Dimension(220, 400));
		}

		/**
		 * Create and add the widgets.
		 */
		private void addWidgets() {
			int i = 0;
			comp[i] = new SmallTrend(60);
			comp[i].setRange(150, 400);
			comp[i].setBorder(BorderFactory.createLoweredBevelBorder());
			panel.add(comp[i++]);

			comp[i] = new SmallTrend(60);
			comp[i].setRange(150, 400);
			panel.add(comp[i++]);

			comp[i] = new SmallTrend(60);
			comp[i].setRange(150, 400);
			comp[i].setBackground(Color.DARK_GRAY);
			comp[i].setForeground(Color.red);
			panel.add(comp[i++]);

			comp[i] = new SmallTrend(60);
			comp[i].setRange(150, 400);
			panel.add(comp[i++]);
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

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			int cntr;

			public void run() {
				if (demo != null) {
					for (SmallTrend t : demo.comp) {
						for (int i=0; i<1; i++) {
							t.addSample(250 + (int)(Math.random() * 10));
						}
						t.repaint();
					}
				}
			}
		}, 0, 100);

	}
}
