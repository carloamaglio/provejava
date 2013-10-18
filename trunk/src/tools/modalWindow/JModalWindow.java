package tools.modalWindow;

import java.awt.Frame;

import javax.swing.JWindow;

public class JModalWindow extends JWindow {
	private static final long serialVersionUID = 1L;

	public JModalWindow(Frame owner, boolean modal) {
		super(owner);
//		MyWindowListener l = new MyWindowListener();
//		addWindowListener(l);
//		addWindowStateListener(l);
//		addWindowFocusListener(l);
	}

//	public void show() {
//		setVisible(true);
//	}

	public void setTransparent(float alfa) {
		
	}

//	private final class MyWindowListener implements WindowListener, WindowStateListener, WindowFocusListener {
//		public void windowActivated(WindowEvent e) {
//			System.out.println("windowActivated(): " + e);
//		}
//		public void windowClosed(WindowEvent e) {
//			System.out.println("windowClosed(): " + e);
//		}
//		public void windowClosing(WindowEvent e) {
//			System.out.println("windowClosing(): " + e);
//		}
//		public void windowDeactivated(WindowEvent e) {
//			System.out.println("windowDeactivated(): " + e);
//		}
//		public void windowDeiconified(WindowEvent e) {
//			System.out.println("windowDeiconified(): " + e);
//		}
//		public void windowIconified(WindowEvent e) {
//			System.out.println("windowIconified(): " + e);
//		}
//		public void windowOpened(WindowEvent e) {
//			System.out.println("windowOpened(): " + e);
//		}
//
//		public void windowStateChanged(WindowEvent e) {
//			System.out.println("windowStateChanged(): " + e);
//		}
//
//		public void windowGainedFocus(WindowEvent e) {
//			System.out.println("windowGainedFocus(): " + e);
//		}
//
//		public void windowLostFocus(WindowEvent e) {
//			System.out.println("windowLostFocus(): " + e);
//		}
//	}
}
