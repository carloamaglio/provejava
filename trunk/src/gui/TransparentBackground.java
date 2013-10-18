package gui;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;

public class TransparentBackground extends JComponent implements
		ComponentListener, WindowFocusListener, Runnable {

	private static final long serialVersionUID = 1L;

	// constants ---------------------------------------------------------------
	// instance ----------------------------------------------------------------
	private JFrame _frame;

	private BufferedImage _background;

	private long _lastUpdate = 0;

	private boolean _refreshRequested = true;

	private Robot _robot;

	private Rectangle _screenRect;

	private ConvolveOp _blurOp;

	// constructor -------------------------------------------------------------

	public TransparentBackground(JFrame frame) {
		_frame = frame;
		try {
			_robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
			return;
		}

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		_screenRect = new Rectangle(dim.width, dim.height);

		float[] my_kernel = { 0.10f, 0.10f, 0.10f, 0.10f, 0.20f, 0.10f, 0.10f,
				0.10f, 0.10f };
		_blurOp = new ConvolveOp(new Kernel(3, 3, my_kernel));

		updateBackground();
		_frame.addComponentListener(this);
		_frame.addWindowFocusListener(this);
		new Thread(this).start();
	}

	// protected ---------------------------------------------------------------

	protected void updateBackground() {
		_background = _robot.createScreenCapture(_screenRect);
	}

	protected void refresh() {
		if (_frame.isVisible() && this.isVisible()) {
			repaint();
			_refreshRequested = true;
			_lastUpdate = System.currentTimeMillis();
		}
	}

	// JComponent --------------------------------------------------------------

	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		Point pos = this.getLocationOnScreen();
		BufferedImage buf = new BufferedImage(getWidth(), getHeight(),
				BufferedImage.TYPE_INT_RGB);
		buf.getGraphics().drawImage(_background, -pos.x, -pos.y, null);

		Image img = _blurOp.filter(buf, null);
		g2.drawImage(img, 0, 0, null);
		g2.setColor(new Color(255, 255, 255, 192));
		g2.fillRect(0, 0, getWidth(), getHeight());
	}

	// ComponentListener -------------------------------------------------------
	public void componentHidden(ComponentEvent e) {
	}

	public void componentMoved(ComponentEvent e) {
		repaint();
	}

	public void componentResized(ComponentEvent e) {
		repaint();

	}

	public void componentShown(ComponentEvent e) {
		repaint();
	}

	// WindowFocusListener -----------------------------------------------------
	public void windowGainedFocus(WindowEvent e) {
		refresh();
	}

	public void windowLostFocus(WindowEvent e) {
		refresh();
	}

	// Runnable ----------------------------------------------------------------
	public void run() {
		try {
			while (true) {
				Thread.sleep(100);
				long now = System.currentTimeMillis();
				if (_refreshRequested && ((now - _lastUpdate) > 1000)) {
					if (_frame.isVisible()) {
						Point location = _frame.getLocation();
						_frame.setLocation(-_frame.getWidth(), -_frame
								.getHeight());
						updateBackground();
						_frame.setLocation(location);
						refresh();
					}
					_lastUpdate = now;
					_refreshRequested = false;
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("Transparent Window");
		TransparentBackground bg = new TransparentBackground(frame);

		bg.setLayout(new BorderLayout());
		JButton b = new JButton("CIAO");
		b.setBackground(Color.GREEN);
		b.setOpaque(false);
		bg.add(b);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(bg);
		frame.pack();
		frame.setSize(200, 200);
		frame.setLocation(500, 500);
		frame.setVisible(true);
	}
}
