package gui.buttons;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalButtonUI;
import javax.swing.plaf.metal.MetalToggleButtonUI;

public class ButtonSwitch implements Runnable {

	private JFrame frame = new JFrame("Event Sample");

	private ButtonSwitch() {
//		UIManager.put("Button.background", new Color(89, 91, 81));
//		UIManager.put("Button.select", new Color(200, 0, 0));

//		AbstractButton ib = new ChkButton("CIAO");
		final AbstractButton ib = new IconButton(Color.green, IconButton.dfltConfirmer);
		ib.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				System.out.println("ib.itemStateChanged(): " + (e.getStateChange()==ItemEvent.SELECTED ? "SELECTED" : "DESELECTED"));
			}
		});
//		ib.addChangeListener(new ChangeListener() {
//			public void stateChanged(ChangeEvent e) {
//				System.out.println("ib.stateChanged(): " + e.toString());
//			}
//		});
//		ib.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				if (e.getID()==ActionEvent.ACTION_PERFORMED) {
//					System.out.println("ib.actionPerformed()");
//				}
//			}
//		});


		SwButton c = new SwButton(Color.cyan);
		c.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getID()==ActionEvent.ACTION_PERFORMED) {
					ib.setSelected(!ib.isSelected());
				}
			}
		});

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
//		c.setPreferredSize(new Dimension(80, 80));
//		c.setFocusable(false);
//		c.setBorderPainted(false);
//		Insets i = c.getMargin();
//		c.setBorder(new BevelBorder(BevelBorder.RAISED));
//		c.setMargin(i);
//		c.setBackground(Color.yellow);
//		MetalToggleButtonUI bui;
//		BasicButtonUI bui = new BasicButtonUI();
//		c.setUI(bui);

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
		SwingUtilities.invokeLater(new ButtonSwitch());
		System.out.println("separator='" + File.separator + "'");
	}

}

class TBUI extends MetalToggleButtonUI {
    private final static TBUI ui = new TBUI();
//    private final static Color selectColor = new Color(200, 100, 0);

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

class SwButton extends JToggleButton implements ChangeListener {
	private static final long serialVersionUID = 1L;
	
	public SwButton(Color background) {
		super();
//		setUI(BUI.createUI(this));
//		MetalButtonUI bui;
//		if (background!=null) setBackground(background);
		setFocusable(false);
		setFocusPainted(false);
//		setBorderPainted(false);
//		setBorder(BorderFactory.createRaisedBevelBorder());
		setPreferredSize(new Dimension(70, 40));
		ToggleButtonModel m = (ToggleButtonModel)getModel();
		m.addChangeListener(this);
		update();
		setText("CIAO");
	}

	public void stateChanged(ChangeEvent e) {
		update();
	}

	private void update() {
//		ToggleButtonModel model = (ToggleButtonModel)getModel();
//		setText(model.isSelected() ? "ON" : "OFF");
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

}


class BUI extends MetalButtonUI {
    private final static BUI ui = new BUI();
//    private final static Color selectColor = new Color(200, 100, 0);

    // ********************************
    //          Create PLAF
    // ********************************
    public static ComponentUI createUI(JComponent c) {
        return ui;
    }

//    @Override
//    protected Color getSelectColor() {
//    	return selectColor;
//    }

    @Override
    protected void paintIcon(Graphics g, JComponent c, Rectangle iconRect) {
		IconButton b = (IconButton)c;
        drawButton(g, b);
        drawLed(g, b);
    }

	private boolean withImage = true;

    private void drawButton(Graphics g, IconButton b) {
		if (withImage) {
			ButtonModel model = b.getModel();
			if (true) {
		        Graphics2D g2 = (Graphics2D)g.create();
				Icon icon;
				if (model.isArmed() && model.isPressed()) {
					icon = b.getPressedIcon();
				} else {
					icon = b.getIcon();
				}
				g2.scale((double)b.getWidth() / icon.getIconWidth(), (double)b.getHeight() / icon.getIconHeight());
				icon.paintIcon(b, g2, 0, 0);
				g2.dispose();
			} else {
				ImageIcon icon;
				if (model.isArmed() && model.isPressed()) {
					icon = (ImageIcon)b.getPressedIcon();//imgPressed;
				} else {
					icon = (ImageIcon)b.getIcon();//img;
				}
				g.drawImage(icon.getImage(), 0, 0, b.getWidth(), b.getHeight(), b);
			}
		}
	}

	private void drawLed(Graphics g, IconButton b) {
		Icon led = b.isSelected() ? b.on : b.off;
		led.paintIcon(b, g, 3+b.getWidth()/9, 2+b.getHeight()/7);
	}

}

class LedIcon implements Icon {

	static final int DFLTWIDTH = 16;
	static final int DFLTHEIGHT = 0;
	public static final LedIcon squareSmallGray = new LedIcon(Color.gray, DFLTWIDTH, DFLTHEIGHT);
	public static final LedIcon squareSmallGreen = new LedIcon(Color.green, DFLTWIDTH, DFLTHEIGHT);

	Color color;
	int width;
	int height;

	public LedIcon(Color color, int width, int height) {
		this.color = color;
		this.width = width;
		this.height = height;
	}

	public LedIcon(Color color) {
		this(color, DFLTWIDTH, DFLTHEIGHT);
	}

	public int getIconHeight() {
		return height;
	}

	public int getIconWidth() {
		return width;
	}

	public void paintIcon(Component c, Graphics g, int x, int y) {
		Graphics g2 = g.create();
		int h = height>0 ? height : c.getHeight() - 2 * y;
		g2.setColor(Color.gray);
		g2.draw3DRect(x, y, width, h, false);
		g2.setColor(color);
		g2.fill3DRect(x+2, y+2, width-4, h-4, true);
		g2.setColor(color.brighter());
		g2.drawLine(x+4, y+5, x+7, y+5);
		g2.dispose();
	}

}

interface Confirmer {
	boolean Confirm(Component c);
}

class IconButton extends JButton implements ChangeListener, ActionListener {
	private static final long serialVersionUID = 1L;
	static final ImageIcon img = new ImageIcon("images/btnGrayUp.gif");
	static final ImageIcon imgPressed = new ImageIcon("images/btnGrayDown.gif");

	Icon on;
	Icon off;

	private boolean withBorder = false;
//	private boolean withImage = false;
	private final Confirmer confirmer;

	public IconButton(Icon on, Icon off, Confirmer confirmer) {
		super();
		this.on = on;
		this.off = off;
		this.confirmer = confirmer;

		if (confirmer==null) this.setModel(new JToggleButton.ToggleButtonModel());
		setUI(BUI.createUI(this));
//		this.setHorizontalAlignment(SwingConstants.CENTER);
		this.setHorizontalTextPosition(SwingConstants.CENTER);
//		this.setAlignmentX(0.1f);
//		this.setAlignmentY(0.1f);
//		if (withImage) {
			setIcon(img);
			setPressedIcon(imgPressed);
//		}
//		MetalButtonUI bui;
//		if (background!=null) setBackground(background);
		setFocusable(false);
		setFocusPainted(false);
//		setBorderPainted(false);
//		setBorder(BorderFactory.createRaisedBevelBorder());
		setPreferredSize(new Dimension(70, 40));
		if (confirmer!=null) this.addActionListener(this);
//		this.getModel().addChangeListener(this);
	}

	public IconButton(Icon on, Icon off) {
		this(on, off, null);
	}

	public IconButton(Color ledColorOn, Color ledColorOff, Confirmer confirmer) {
		this(new LedIcon(ledColorOn), new LedIcon(ledColorOff), confirmer);
	}

	public IconButton(Color ledColorOn, Color ledColorOff) {
		this(new LedIcon(ledColorOn), new LedIcon(ledColorOff), null);
	}

	public IconButton(Color ledColor, Confirmer confirmer) {
		this(ledColor, ledColor.darker().darker(), confirmer);
	}

	public IconButton(Color ledColor) {
		this(ledColor, ledColor.darker().darker());
	}

	public IconButton() {
		this(LedIcon.squareSmallGreen, LedIcon.squareSmallGray);
	}

//	int cntr;
	public void stateChanged(ChangeEvent e) {
//		ButtonModel model = getModel();
////		System.out.println("state changed: " + cntr++ + ", " + e);
//		if (model.isArmed() && model.isPressed()) {
//			setBorder(BorderFactory.createLoweredBevelBorder());
//			model.setSelected(true);
//		} else {
//			setBorder(BorderFactory.createRaisedBevelBorder());
//			model.setSelected(false);
//		}
	}

//	boolean state = false;
	public void actionPerformed(ActionEvent e) {
		if (e.getID() == ActionEvent.ACTION_PERFORMED) {
			if (confirmer!=null) {
				if (confirmer.Confirm(this)) {
					this.setSelected(!this.isSelected());
				}
			}
		}
	}


	@Override
	public void paint(Graphics g) {
		super.paint(g);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
//		drawButton(g);
//		drawLed(g);
	}

//	private void drawButton(Graphics g) {
//		if (withImage) {
//			ButtonModel model = getModel();
//			ImageIcon icon;
//			if (model.isArmed() && model.isPressed()) {
//				icon = imgPressed;
//			} else {
//				icon = img;
//			}
//			g.drawImage(icon.getImage(), 0, 0, getWidth(), getHeight(), this);
//		}
//	}

//	private void drawLed(Graphics g) {
//		if (true) {
//			Graphics g2 = g.create();
//			if (this.isSelected()) {
//				g2.setColor(Color.green);
//			} else {
//				g2.setColor(Color.gray);
//			}
//			g2.fill3DRect(3, 5, 10, this.getHeight()-10, true);
//			g2.dispose();
//		}
//	}

	private final static Border releasedBorder = BorderFactory.createLoweredBevelBorder();
	private final static Border pressedBorder = BorderFactory.createRaisedBevelBorder();

	@Override
    protected void paintBorder(Graphics g) {
		if (withBorder) {
			ButtonModel model = getModel();
			Border border;
			if (model.isArmed() && model.isPressed()) {
				border = releasedBorder;
			} else {
				border = pressedBorder;
			}

	        if (border != null) {
	            border.paintBorder(this, g, 0, 0, getWidth(), getHeight());
	        }
		}
    }

	public static final Confirmer dfltConfirmer = new Confirmer() {
		public boolean Confirm(Component c) {
			int n;
			if (!((IconButton)c).isSelected()) {
				n = JOptionPane.showConfirmDialog(
					    null,
					    "Confermi?",
					    "Confermare",
					    JOptionPane.YES_NO_OPTION);
			} else {
				n = 0;
			}
			return n==0;
		}
	};

}

class ChkButton extends JCheckBox {
	private static final long serialVersionUID = 1L;

	public ChkButton(String text) {
		super(text);
//		MetalCheckBoxUI
	}
}
