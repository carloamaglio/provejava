package tastierino;

import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class NumKeybTest implements Runnable {

	private JFrame frame;

	private NumKeybTest() {
		frame = new JFrame("Event Sample");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		NumKeyb.create(frame);

		JPanel panel = new JPanel(new FlowLayout());
		JTextField t;

		t = new CTextField(10);
		panel.add(t);

		t = new CTextField(10);
		panel.add(t);

		t = new JTextField(10);
		panel.add(t);

		t = new JTextField(10);
		panel.add(t);

		frame.setContentPane(panel);
	}

	public void run() {
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String... args) {
		SwingUtilities.invokeLater(new NumKeybTest());
	}

}
