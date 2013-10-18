package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class BGTest1 {
	public static void main(String[] args) {
		JFrame frame = new JFrame("Transparent Window");
		TransparentBackground bg = new TransparentBackground(frame);
		bg.setLayout(new BorderLayout());

		JButton button = new JButton("This is a button");
		bg.add("North", button);
		JLabel label = new JLabel("This is a label");
		bg.add("South", label);
		frame.getContentPane().add("Center", bg);
		frame.pack();
		frame.setSize(300, 300);
		frame.show();
	}

}


class TransparentBackground extends JComponent implements ComponentListener,
		WindowFocusListener, Runnable {
	private JFrame frame;

	protected Image background;

	private long lastupdate = 0;

	public boolean refreshRequested = true;

	public TransparentBackground(JFrame frame) {
		this.frame = frame;

		updateBackground();
		frame.addComponentListener(this);
		frame.addWindowFocusListener(this);
		new Thread(this).start();
	}

	public void updateBackground() {
		try {
			Robot rbt = new Robot();
			Toolkit tk = Toolkit.getDefaultToolkit();
			Dimension dim = tk.getScreenSize();
			background = rbt.createScreenCapture(new Rectangle(0, 0, (int) dim
					.getWidth(), (int) dim.getHeight()));
		} catch (Exception ex) {
			p(ex.toString());
			ex.printStackTrace();
		}
	}

	public void paintComponent(Graphics g) {
		Point pos = this.getLocationOnScreen();
		Point offset = new Point(-pos.x, -pos.y);
		g.drawImage(background, offset.x, offset.y, null);
	}

	public void componentShown(ComponentEvent evt) {
		repaint();
	}

	public void componentResized(ComponentEvent evt) {
		repaint();
	}

	public void componentMoved(ComponentEvent evt) {
		repaint();
	}

	public void componentHidden(ComponentEvent evt) {
	}

	public void windowGainedFocus(WindowEvent evt) {
		refresh();
	}

	public void windowLostFocus(WindowEvent evt) {
		refresh();
	}

	public void refresh() {
		if (this.isVisible() && frame.isVisible()) {
			repaint();
			refreshRequested = true;
			lastupdate = new Date().getTime();
		}
	}

	/*
	 * private boolean recurse = false; public void quickRefresh() { p("quick
	 * refresh"); long now = new Date().getTime(); if(recurse || ((now -
	 * lastupdate) < recurse =" true;" location =" frame.getLocation();" recurse ="
	 * false;" lastupdate =" now;" now =" new"> 1000)) { if(frame.isVisible()) {
	 * Point location = frame.getLocation(); frame.hide(); updateBackground();
	 * frame.show(); frame.setLocation(location); refresh(); } lastupdate = now;
	 * refreshRequested = false; } } } catch (Exception ex) { p(ex.toString());
	 * ex.printStackTrace(); } }
	 * 
	 * 
	 * public static void p(String str) { System.out.println(str); } }
	 */
	public static void p(String str) {
		System.out.println(str);
	}

	public void run() {
		// TODO Auto-generated method stub
		
	}
}
