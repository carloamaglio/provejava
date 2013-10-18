package gui.buttons;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalToggleButtonUI;

public class ToggleButton implements Runnable {

	private JFrame frame = new JFrame("Event Sample");
	SwButton c;

	private ToggleButton() {
//		UIManager.put("Button.background", new Color(89, 91, 81));
//		UIManager.put("Button.select", new Color(200, 0, 0));

		c = new SwButton(Color.cyan);
		c.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				System.out.println("c.itemStateChanged(): " + (e.getStateChange()==ItemEvent.SELECTED?"SELECTED":"DESELECTED") + ", " + c.isSelected() + ", " + e);
			}
		});
//		c.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				System.out.println("c.actionPerformed(): " + e);
//				if (e.getID()==ActionEvent.ACTION_PERFORMED) {
////					System.out.println("c.actionPerformed(): " + e);
//				}
//			}
//		});

		JPanel panel;
		panel = new JPanel(new BorderLayout());
		panel.setBackground(Color.cyan);
		panel.add(c, BorderLayout.EAST);
		panel.add(new Button2(), BorderLayout.CENTER);
		frame.setContentPane(panel);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	public void run() {
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String... args) {
		SwingUtilities.invokeLater(new ToggleButton());
		System.out.println("separator='" + File.separator + "'");
	}

	private static class SwButton extends JToggleButton implements ChangeListener {
		private static final long serialVersionUID = 1L;
		
		public SwButton(Color background) {
			super();
//			setUI(BUI.createUI(this));
//			MetalButtonUI bui;
//			if (background!=null) setBackground(background);
			setFocusable(false);
			setFocusPainted(false);
//			setBorderPainted(false);
//			setBorder(BorderFactory.createRaisedBevelBorder());
			setPreferredSize(new Dimension(70, 40));
			ToggleButtonModel m = (ToggleButtonModel)getModel();
			m.addChangeListener(this);
			update();
			setText("CIAO");
		}

		public void stateChanged(ChangeEvent e) {
//			System.out.println("stateChanged(): " + e);
			update();
		}

		private void update() {
//			ToggleButtonModel model = (ToggleButtonModel)getModel();
//			setText(model.isSelected() ? "ON" : "OFF");
		}

		@Override
		public void paint(Graphics g) {
			super.paint(g);
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			if (false) {
				ToggleButtonModel model = (ToggleButtonModel)getModel();
				if (model.isSelected()) {
					g.setColor(Color.green);
				} else {
					g.setColor(Color.gray);
				}
				g.fill3DRect(3, 5, 10, this.getHeight()-10, true);
			}
		}

		private static final class TBUI extends MetalToggleButtonUI {
		    private final static TBUI ui = new TBUI();
//		    private final static Color selectColor = new Color(200, 100, 0);

		    // ********************************
		    //          Create PLAF
		    // ********************************
		    public static ComponentUI createUI(JComponent c) {
		        return ui;
		    }

		    @Override
		    protected Color getSelectColor() {
		    	return super.getSelectColor();//selectColor;
		    }

			@Override
		    public void paint(Graphics g, JComponent c) {
				super.paint(g, c);
				if (false) {
					AbstractButton b = (AbstractButton)c;
					JToggleButton.ToggleButtonModel model = (JToggleButton.ToggleButtonModel)b.getModel();
					if (model.isSelected()) {
						g.setColor(Color.green);
					} else {
						g.setColor(Color.gray);
					}
					g.fill3DRect(3, 5, 10, b.getHeight()-10, true);
				}
			}
		}

	}

	private final class Button2 extends JButton implements ActionListener {
		private static final long serialVersionUID = 1871743227203790439L;

		public Button2() {
			super();
			this.addActionListener(this);
		}

	    public void actionPerformed(ActionEvent e) {
	    	c.setSelected(false);
	    }
	}
}

