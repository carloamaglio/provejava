package tastierino;

import java.awt.AWTException;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Robot;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JWindow;

public class NumKeyb extends JWindow {
	private static final long serialVersionUID = -2628002886717994863L;

	private static NumKeyb keyb;

	JButton tab;
	JButton punto;
	JButton segno;
	JButton left;
	JButton right;
	JButton back;
	JButton zero;
	JButton uno;
	JButton due;
	JButton tre;
	JButton quattro;
	JButton cinque;
	JButton sei;
	JButton sette;
	JButton otto;
	JButton nove;
	JButton enter;

	public static NumKeyb get() {
		return keyb;
	}

	public static void create(Frame owner) {
		if (keyb==null) {
			keyb = new NumKeyb(owner);
		} else {
			System.out.println("");
			System.exit(-1);
		}
	}

	private NumKeyb(Frame owner) {
		super(owner);
		setBounds(100, 100, 300, 200);
//		setVisible(true);
//		setPreferredSize(new Dimension(300, 300));
//		setLocation(100, 100);

		/** Creazione Robot */
		Robot rob = null;
		try {
			rob = new Robot();
		} catch (AWTException ex) {
			ex.printStackTrace();
		}

		Component keyboard = createKeyboard(rob);

		JPanel panel = new JPanel(new FlowLayout());

		panel.add(keyboard);

		add(panel);

		new DragMe(this);
	}

    public void setVisible(Component c) {
		if (!isVisible()) setLocation(c.getLocationOnScreen().x, c.getLocationOnScreen().y+c.getHeight());
		setVisible(true);
    }

	public class DragMe {
		private Window c;
		private int X=0;
		private int Y=0;

		public DragMe(Window c) {
			this.c = c;
			c.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					System.exit(0);	//An Exit Listener
				}
			});

			c.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					X = e.getX();
					Y = e.getY();
//					System.out.println("The (X,Y) coordinate of window is (" + X + "," + Y + ")");
				}
			});			

			c.addMouseMotionListener(new MouseMotionAdapter() {
				@Override
				public void mouseDragged(MouseEvent e){
					setLocation(getLocation().x+(e.getX()-X),getLocation().y+(e.getY()-Y));
				}
			});
		}

		public void setLocation(int x, int y) {
			c.setLocation(x, y);
//			Window w = c.getOwner();
//		   	if (w!=null) {
//		   		w.setLocation(x, y-25);
//		   	}
	    }
	}

	private Component createKeyboard(Robot rob) {
		JPanel panel = new JPanel(new GridLayout(4, 4, 2, 2));

		tab = mkButton(KeyEvent.VK_TAB, "TAB", rob);
		punto = mkButton(KeyEvent.VK_PERIOD, ".", rob);
		segno = mkButton(KeyEvent.VK_CLEAR, "C", rob);
		left = mkButton(KeyEvent.VK_LEFT, "<", rob);
		right = mkButton(KeyEvent.VK_RIGHT, ">", rob);
		back = mkButton(KeyEvent.VK_BACK_SPACE, "<--", rob);
		zero = mkButton(KeyEvent.VK_0, "0", rob);
		uno = mkButton(KeyEvent.VK_1, "1", rob);
		due = mkButton(KeyEvent.VK_2, "2", rob);
		tre = mkButton(KeyEvent.VK_3, "3", rob);
		quattro = mkButton(KeyEvent.VK_4, "4", rob);
		cinque = mkButton(KeyEvent.VK_5, "5", rob);
		sei = mkButton(KeyEvent.VK_6, "6", rob);
		sette = mkButton(KeyEvent.VK_7, "7", rob);
		otto = mkButton(KeyEvent.VK_8, "8", rob);
		nove = mkButton(KeyEvent.VK_9, "9", rob);
		enter = mkButton(KeyEvent.VK_ENTER, "ENTER", rob);


		panel.add(sette);
		panel.add(otto);
		panel.add(nove);
		panel.add(back);

		panel.add(quattro);
		panel.add(cinque);
		panel.add(sei);
		panel.add(tab);

		panel.add(uno);
		panel.add(due);
		panel.add(tre);
		panel.add(left);

		panel.add(zero);
		panel.add(punto);
		panel.add(segno);
		panel.add(enter);

		return panel;
	}

	/**
	 * Crea un pulsante che non accetta il focus, con un'azione che genera un
	 * evento KeyPress
	 */
	private JButton mkButton(int keyCode, String name, Robot r) {
		KeyAction a = new KeyAction(keyCode, name, r);
		JButton b = new JButton(a);
		b.setFocusable(false);
		b.setBorderPainted(false);
		return b;
	}

	/** Azione che genera un evento KeyPress attraverso un Robot */
	private class KeyAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		private Robot robot;
		private int key;

		public KeyAction(int keycode, String name, Robot r) {
			super(name);
			robot = r;
			key = keycode;
		}

		public KeyAction(int keycode, char keychar, Robot r) {
			this(keycode, String.valueOf(keychar), r);
		}

		public void actionPerformed(ActionEvent e) {
			robot.keyPress(key);
		}
	};
}
