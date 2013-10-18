package gui.display;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import layout.GraphPaperLayout;

public class TestLayout {

    static class Demo {
		JFrame frame;
		JPanel panel;

		public Demo() {
			//Create and set up the window.
			frame = new JFrame("Display test");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setSize(new Dimension(220, 400));
//			frame.setPreferredSize(new Dimension(220, 400));
			frame.setLocation(500, 100);

			//Create and set up the panel.
			GraphPaperLayout layout = new GraphPaperLayout(20, 2, 2, 1, false);
			layout.beSmart();
			panel = new JPanel(layout);

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
			JLabel l;
			JButton b;
			l = new JLabel("ciao");
			l.setBorder(BorderFactory.createLineBorder(Color.red));
			panel.add(l, new Rectangle(0,0,1,17));

			l = new JLabel("bello");
			l.setBorder(BorderFactory.createLineBorder(Color.green));
			panel.add(l, new Rectangle(1,0,1,17));

			b = new JButton("uffa");
			b.setBorder(BorderFactory.createLineBorder(Color.cyan));
			panel.add(b, new Rectangle(0,17,2,3));
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
