package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.MouseInfo;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ButtonModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.PopupFactory;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalButtonUI;

public class Buttons implements Runnable {

	private JFrame frame = new JFrame("Event Sample");

	private Buttons() {
		UIManager.put("Button.background", new Color(89, 91, 81));
//		UIManager.put("Button.select", new Color(200, 0, 0));

		PopupFactory.setSharedInstance(new OfficePopupFactory()); // just do this one time, when you start your application !!

		JToggleButton c = new JToggleButton("TOGGLE");
//		c.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				System.out.println("action: " + e.getID() + ", " + e);
//				if (e.getID()==ActionEvent.ACTION_PERFORMED) {
//					JToggleButton me = (JToggleButton)e.getSource();
//					boolean isSelected = me.getModel().isSelected();
////					System.out.println("action: " + e.getID() + ", " + e);
////					System.out.println("isSelected=" + isSelected);
//					Insets i = me.getMargin();
//					me.setBorder(new BevelBorder(isSelected ? BevelBorder.LOWERED : BevelBorder.RAISED));
//					i.left = 10;
//					me.setMargin(i);
//				}
//			}
//		});
//		c.setContentAreaFilled(false);
//		c.setBackground(Color.RED);
		c.setPreferredSize(new Dimension(80, 80));
		c.setFocusable(false);
//		c.setBorderPainted(false);
		Insets i = c.getMargin();
		c.setBorder(new BevelBorder(BevelBorder.RAISED));
		c.setMargin(i);
		c.setBackground(Color.RED);
//		MetalToggleButtonUI bui;
//		BasicButtonUI bui = new BasicButtonUI();
//		c.setUI(bui);

		JButton ib = new IconButton(Color.RED);

		JButton a = mkButton("a");

		JButton b = mkButton("b");
//		JTextField t = new JTextField(10);

		JPanel panel;
		if (false) {
			panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
			panel.setBackground(Color.cyan);
			panel.add(ib);
			panel.add(a);
			panel.add(b);
			panel.add(c);
			panel.setBorder(BorderFactory.createLineBorder(Color.magenta));
//			panel.add(t);
		} else {
			panel = new JPanel(new BorderLayout());
			panel.setBackground(Color.cyan);
			panel.add(ib, BorderLayout.CENTER);
			panel.add(c, BorderLayout.EAST);
		}
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
	private JButton mkButton(String lbl) {
		JButton b = new JButton(lbl);
		b.setFocusable(false);
		b.setBorderPainted(false);
		return b;
	}

	public static void main(String... args) {
		SwingUtilities.invokeLater(new Buttons());
		System.out.println("separator='" + File.separator + "'");
	}

}

class BUI extends MetalButtonUI {
    private final static BUI ui = new BUI();
    private final static Color selectColor = new Color(200, 0, 0);

    // ********************************
    //          Create PLAF
    // ********************************
    public static ComponentUI createUI(JComponent c) {
        return ui;
    }

    protected Color getSelectColor() {
    	return selectColor;
    }
}

class IconButton extends JButton implements ChangeListener {
	private static final long serialVersionUID = 1L;
	static final ImageIcon img = new ImageIcon("images/power_on.gif");

	public IconButton(Color background) {
		super(img);
		this.setUI(BUI.createUI(this));
//		MetalButtonUI bui;
		if (background!=null) setBackground(background);
		setFocusable(false);
		setFocusPainted(false);
//		setBorderPainted(false);
		setBorder(BorderFactory.createRaisedBevelBorder());
		setPreferredSize(new Dimension(70, 40));
		this.getModel().addChangeListener(this);
//		setToolTipText("<html><b>Office 2007 tooltips</b><br><p style='margin-left:10pt;margin-left:10pt;margin-top:5pt;margin-bottom:5pt;'>They look good.<br>Real good !</p><hr></hr><b>Type F1 for more help</b><html>");
		setToolTipText("Una semplice prova...");
	}

	Balloon b;

	int cntr;
	public void stateChanged(ChangeEvent e) {
		ButtonModel model = getModel();
//		System.out.println("state changed: " + cntr++ + ", " + e);

		if (true) {
			final Balloon b = new Balloon();
			JPanel l = OfficePopupFactory.buildPanel(new JLabel("<html><b><big>Office 2007 tooltips</big></b><br><p style='margin-left:10pt;margin-left:10pt;margin-top:5pt;margin-bottom:5pt;'>They look good.<br>Real good !</p><hr></hr><b>Type F1 for more help</b><html>"));
			l.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent evt) {
					b.setVisible(false);
					b.dispose();
				}
			});
			b.add(l);
// b.followCursor();
			b.setLocation(MouseInfo.getPointerInfo().getLocation());
			b.pack();
			b.setVisible(true);

//			b = new Balloon(new JLabel("yehaaaaa" + cntr++), false);
//			b.setVisible(true);
//		} else {
//			b.setVisible(false);
//			b = null;
		}

//		final JLabel lbl = new JLabel("CIAO" + cntr++);
//		final Popup popup = PopupFactory.getSharedInstance().getPopup(this, lbl, 10, 10);
//		popup.show();
//		lbl.addMouseListener(new java.awt.event.MouseAdapter() {
//            public void mouseClicked(java.awt.event.MouseEvent evt) {
//                popup.hide();
//            }
//        });
//		lbl.addMouseMotionListener(new MouseMotionListener() {
//			public void mouseDragged(MouseEvent e) {
//			}
//
//			public void mouseMoved(MouseEvent e) {
//				lbl.setLocation(e.getX(), e.getY());
//			}
//		});

		if (model.isArmed() && model.isPressed()) {
			setBorder(BorderFactory.createLoweredBevelBorder());
		} else {
			setBorder(BorderFactory.createRaisedBevelBorder());
		}
	}

	
}
