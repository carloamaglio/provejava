package gui.display;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Test {

    static class Demo {
		JFrame frame;
		JPanel panel;
		Display[] comp = new Display[4];
		HLDisplay[] hl = new HLDisplay[4];

		public Demo() {
			//Create and set up the window.
			frame = new JFrame("Display test");
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
			hl[i] = new HLDisplay(comp[i] = new MatrixDisplay(64, 16, 1, 0));
			comp[i].setBorder(BorderFactory.createLoweredBevelBorder());
			comp[i].setBackground(Color.black);
			comp[i].setForeground(Color.green);
			panel.add(comp[i++]);

			hl[i] = new HLDisplay(comp[i] = new MatrixDisplay(64, 16, 2, 0));
			comp[i].setBackground(Color.black);
			comp[i].setForeground(Color.cyan);
			panel.add(comp[i++]);

			hl[i] = new HLDisplay(comp[i] = new SevenDisplay(64, 16, 4), CharTable.font7seg);
			comp[i].setBorder(BorderFactory.createLoweredBevelBorder());
			comp[i].setBackground(Color.red);
			comp[i].setForeground(Color.white);
			panel.add(comp[i++]);

			hl[i] = new HLDisplay(comp[i] = new MatrixDisplay(64, 16, 4));
			comp[i].setBorder(BorderFactory.createLoweredBevelBorder());
			comp[i].setBackground(new Color(64, 64, 64));
			comp[i].setForeground(Color.red);
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
			int cntr = 0;
			int inc = 1;

			public void run() {
				if (demo != null) {
					byte[][] sprite = new byte[16][64];
					for (int i = 0; i < demo.comp.length-1; i++) {
						if (true) {
							demo.hl[i].putc((char)((int)(Math.random()*10)+'0'));
						} else {
							for (int r=0; r<sprite.length; r++) {
								for (int c=0; c<sprite[r].length; c++) {
									sprite[r][c] = (byte)(Math.random()<0.01 ? 0 : 1);
								}
							}
							demo.comp[i].put(0, 0, sprite);
						}
					}
					demo.hl[3].cls();
					demo.hl[3].puts(cntr, 0, "030676028");
					if (cntr>=10) {
						inc = -1;
					} else if (cntr<=0) {
						inc = 1;
					}
					cntr += inc;
				}
			}
		}, 0, 100);

	}
}
