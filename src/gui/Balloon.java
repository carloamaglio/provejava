package gui;

import java.awt.Component;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JWindow;
import javax.swing.Timer;

/**
 * display a component at a given position on the screen, wihtout having to put
 * it in a windows
 * 
 * @author Christophe Le Besnerais
 */
public class Balloon extends JWindow {
	private static final long serialVersionUID = 1L;

	private float alpha = 1.0f;
	private Timer timer;

	public Balloon() {
		super();
		this.init();
	}

	public Balloon(Frame owner) {
		super(owner);
		this.init();
	}

	public Balloon(GraphicsConfiguration gc) {
		super(gc);
		this.init();
	}

	public Balloon(Window owner, GraphicsConfiguration gc) {
		super(owner, gc);
		this.init();
	}

	public Balloon(Window owner) {
		super(owner);
		this.init();
	}

	private void init() {
		this.setAlwaysOnTop(true);
		System.setProperty("sun.java2d.noddraw", "true");
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void setVisible(boolean b) {
		super.setVisible(b);
		if (b) {
			//	   WindowUtils.setWindowTransparent(this, true);
		} else if (timer != null)
			timer.stop();
	}

	/**
	 * @inheritDoc
	 */
	@Override
	protected void addImpl(Component comp, Object constraints, int index) {
		super.addImpl(comp, constraints, index);
		if (comp instanceof JComponent) {
			JComponent jcomp = (JComponent) comp;
			jcomp.setOpaque(false);
		}
	}

	public void setAlpha(float alpha) {
		//	  WindowUtils.setWindowAlpha(this, alpha);
	}

	public float getAlpha() {
		return alpha;
	}

	/**
	 * Moves this component to a new location. The top-left corner of
	 * the new location is specified by point p. Point
	 * p is given in the parent's coordinate space.
	 * @param p the point defining the top-left corner
	 *          of the new location, given in the coordinate space of this
	 *          component's parent. If null, the cursor position will be use
	 * @see #getLocation
	 * @see #setBounds
	 */
	@Override
	public void setLocation(Point p) {
		if (p == null)
			p = getCursorPosition();

		super.setLocation(p);
	}

	public void followCursor() {
		timer = new Timer(10, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setLocation(null);
			}
		});
		timer.start();
	}

	private static Point getCursorPosition() {
		Point p = MouseInfo.getPointerInfo().getLocation();
		p.translate(15, 15); // will not be under the cursor

		return p;
	}

}
