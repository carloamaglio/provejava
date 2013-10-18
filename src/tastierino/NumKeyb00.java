package tastierino;

import java.awt.AWTException;
import java.awt.FlowLayout;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class NumKeyb00 implements Runnable {

	private JFrame frame = new JFrame("Event Sample");

	private NumKeyb00() {
		/** Creazione Robot */
		Robot rob = null;
		try {
			rob = new Robot();
		} catch (AWTException ex) {
			ex.printStackTrace();
		}
		/** Pulsanti */
		JButton a = mkButton(KeyEvent.VK_A, 'a', rob);
		JButton b = mkButton(KeyEvent.VK_B, 'b', rob);
		JTextField t = new JTextField(10);
		JPanel panel = new JPanel(new FlowLayout());
		panel.add(a);
		panel.add(b);
		panel.add(t);
		frame.setContentPane(panel);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	public void run() {
		frame.pack();
		frame.setVisible(true);
	}

	/**
	 * Crea un pulsante che non accetta il focus, con un'azione che genera un
	 * evento KeyPress
	 */
	private JButton mkButton(int keyCode, char keyChar, Robot r) {
		KeyAction a = new KeyAction(keyCode, keyChar, r);
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

		public KeyAction(int keycode, char keychar, Robot r) {
			super(String.valueOf(keychar));
			robot = r;
			key = keycode;
		}

		public void actionPerformed(ActionEvent e) {
			robot.keyPress(key);
		}
	};

	public static void main(String... args) {
		SwingUtilities.invokeLater(new NumKeyb00());
	}

}
