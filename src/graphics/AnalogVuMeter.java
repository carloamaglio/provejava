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

public class AnalogVuMeter extends JComponent {
	private static final long serialVersionUID = -8149490291911984607L;

	int cntr;

	public AnalogVuMeter() {
		cntr = 0;
	}

	@Override
    protected void paintComponent(Graphics g) {
		Insets insets = getInsets();
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.translate(insets.left, insets.top);
		int width = this.getWidth() - insets.left - insets.right;
		int height = this.getHeight() - insets.top - insets.bottom;

		double w = width / 2.0;
		double h = height / 3.0;
		double w2 = w * w;
		double h2 = h * h;
		double cosalfa = w/Math.sqrt(w2+h2);
		double alfa = Math.acos(cosalfa);
		double r = w / Math.sin(2.0*alfa);
		double a = 4 * alfa * 180.0 / Math.PI;
		g2d.fillOval((int)(w-r)-1, 0, (int)(2*r+0.5), (int)(2*r+0.5));
		g2d.setColor(Color.cyan);
		g2d.fillArc((int)(w-r)-1, 0, (int)(2*r+0.5), (int)(2*r+0.5), (int)(90-a/2.0+0.5), (int)(a+0.5));

		g2d.setColor(Color.green);
		g2d.drawLine(0, (int)(h+0.5), width, (int)(h+0.5));
		g2d.drawLine((int)(w+0.5), 0, (int)(w+0.5), height);

		g2d.dispose(); // clean up
	}


    static class Demo {
		JFrame frame;
		JPanel panel;
		AnalogVuMeter[] comp = new AnalogVuMeter[4];

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
			comp[i] = new AnalogVuMeter();
			comp[i].setBorder(BorderFactory.createLoweredBevelBorder());
			panel.add(comp[i++]);

			comp[i] = new AnalogVuMeter();
			panel.add(comp[i++]);

			comp[i] = new AnalogVuMeter();
			comp[i].setBackground(Color.red);
			comp[i].setForeground(Color.red);
			panel.add(comp[i++]);

			comp[i] = new AnalogVuMeter();
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
					for (int i = 0; i < demo.comp.length; i++) {
						//                        demo.comp[i].setValue((int)(Math.random() * 100));
					}
				}
			}
		}, 0, 1000);

	}
}
