/*
 * Created on 28-lug-2005
 *
 * TODO Tutto
 */
package _carlo;

/*
 * A 1.4 application that uses SpringLayout to create a single row
 * of components, similar to that produced by a horizontal BoxLayout.
 * Other files required: SpringUtilities.java.
 */

import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SpringLayout;

import tools.uiutils.layoutManagers.SpringUtilities;

public class SpringBox {
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);

        //Create and set up the window.
        JFrame frame = new JFrame("SpringBox");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Set up the content pane.
        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new SpringLayout());

        //Add the buttons.
        contentPane.add(new JButton("Button 1"));
        contentPane.add(new JButton("Button 2"));
        contentPane.add(new JButton("Button 3"));
        contentPane.add(new JButton("Long-Named Button 4"));
        contentPane.add(new JButton("5"));
        contentPane.add(new JButton("6"));

        //Lay out the buttons in one row and as many columns
        //as necessary, with 6 pixels of padding all around.
        SpringUtilities.makeCompactGrid(contentPane, 3,
                                        2,
                                        6, 6, 6, 6);

        //Display the window.
        frame.pack();
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
