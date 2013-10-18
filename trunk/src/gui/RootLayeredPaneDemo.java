package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

/*
 * RootLayeredPaneDemo.java requires images/dukeWaveRed.gif.
 */
public class RootLayeredPaneDemo extends JPanel
                                 implements ActionListener,
                                            MouseMotionListener {
    private int[] layers = {-30000, 0, 301};
    private String[] layerStrings = { "Yellow (-30000)",
                                      "Magenta (0)",
                                      "Cyan (301)" };
    private Color[] layerColors = { new Color(240, 240, 0, 100),
                                    new Color(240, 0, 240, 100),
                                    new Color(0, 240, 240, 100) };

    private JLayeredPane layeredPane;
    private JLabel dukeLabel;
    private JCheckBox onTop;
    private JComboBox layerList;

    //Action commands
    private static String ON_TOP_COMMAND = "ontop";
    private static String LAYER_COMMAND = "layer";

    //Adjustments to put Duke's toe at the cursor's tip.
    private static final int XFUDGE = 40;
    private static final int YFUDGE = 57;

    //Initial layer of dukeLabel.
    private static final int INITIAL_DUKE_LAYER_INDEX = 1;

    public RootLayeredPaneDemo(JLayeredPane layeredPane)    {
        super(new GridLayout(1,1));

        //Create and load the duke icon.
        final ImageIcon icon = createImageIcon("images/btnGrayUp.gif");

        //Create and set up the layered pane.
        this.layeredPane = layeredPane;
        layeredPane.addMouseMotionListener(this);

        //This is the origin of the first label added.
        Point origin = new Point(10, 100);

        //This is the offset for computing the origin for the next label.
        int offset = 35;

        //Add several overlapping, colored labels to the layered pane
        //using absolute positioning/sizing.
        for (int i = 0; i < layerStrings.length; i++) {
            JLabel label = createColoredLabel(layerStrings[i],
                                              layerColors[i], origin);
            layeredPane.add(label, new Integer(layers[i]));
            origin.x += offset;
            origin.y += offset;
        }

        //Create and add the Duke label to the layered pane.
        dukeLabel = new JLabel("CIAO");
        dukeLabel.setBackground(new Color(200, 0, 0, 100));
        dukeLabel.setOpaque(true);
        if (icon != null) {
            dukeLabel.setBounds(15, 25,
                                icon.getIconWidth(),
                                icon.getIconHeight());
        } else {
            System.err.println("Duke icon not found; using black square instead.");
            dukeLabel.setBounds(15, 225, 30, 30);
            dukeLabel.setOpaque(true);
            dukeLabel.setBackground(Color.BLACK);
        }
        layeredPane.add(dukeLabel,
                        new Integer(layers[INITIAL_DUKE_LAYER_INDEX]),
                        0);

        //Add control pane to this JPanel.
        add(createControlPanel());
    }

    /** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = RootLayeredPaneDemo.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    //Create and set up a colored label.
    private JLabel createColoredLabel(String text,
                                      Color color,
                                      Point origin) {
        JLabel label = new JLabel(text);
        label.setVerticalAlignment(JLabel.TOP);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setOpaque(true);
        label.setBackground(color);
        label.setForeground(Color.black);
        label.setBorder(BorderFactory.createLineBorder(Color.black));
        label.setBounds(origin.x, origin.y, 140, 140);
        return label;
    }

    //Create the control pane for the top of the frame.
    private JPanel createControlPanel() {
        onTop = new JCheckBox("Top Position in Layer");
        onTop.setSelected(true);
        onTop.setActionCommand(ON_TOP_COMMAND);
        onTop.addActionListener(this);

        layerList = new JComboBox(layerStrings);
        layerList.setSelectedIndex(INITIAL_DUKE_LAYER_INDEX);
        layerList.setActionCommand(LAYER_COMMAND);
        layerList.addActionListener(this);

        JPanel controls = new JPanel();
        controls.add(layerList);
        controls.add(onTop);
        controls.setBorder(BorderFactory.createTitledBorder(
                                 "Choose Duke's Layer and Position"));
        return controls;
    }

    //Make Duke follow the cursor.
    public void mouseMoved(MouseEvent e) {
        if (false) dukeLabel.setLocation(e.getX()-XFUDGE, e.getY()-YFUDGE);
    }
    public void mouseDragged(MouseEvent e) {} //do nothing

    //Handle user interaction with the check box and combo box.
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        if (ON_TOP_COMMAND.equals(cmd)) {
            if (onTop.isSelected())
                layeredPane.moveToFront(dukeLabel);
            else
                layeredPane.moveToBack(dukeLabel);

        } else if (LAYER_COMMAND.equals(cmd)) {
            int position = onTop.isSelected() ? 0 : -1;
            layeredPane.setLayer(dukeLabel,
                                 layers[layerList.getSelectedIndex()],
                                 position);
        }
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("RootLayeredPaneDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        RootLayeredPaneDemo newContentPane = new RootLayeredPaneDemo(
          frame.getLayeredPane());
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.setSize(new Dimension(300, 350));
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
