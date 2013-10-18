package _carlo;

import java.awt.AWTException;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
* Class which supports taking screen shots of the entire desktop, AWT Components, or Swing JComponents.
* This functionality is implemented in a series of <code>take</code> methods, each of which returns a BufferedImage.
* This class also offers convenience <code>write</code> methods for storing BufferedImages to files.
* <p>
* The images taken by this class should be the precise images seen on the screen.
* <b>However, the images written to files may deviate from the originals.</b>
* One obvious cause is limitations of the chosen file format (especially with lossy formats like jpeg).
* A subtle issue can occur, however, even when using lossless formats like png:
* if the file is subsequently opened by another application,
* that application may rescale the image, which can often cause visible artifacts.
* <blockquote>
*	To see this last problem on Windows XP,
*	call {@link #take()} which returns an image of the entire desktop and write it to a file,
*	and then open the file with XP's default graphics file viewer ("Windows Picture And Fax Viewer").
*	This program shrinks the desktop image in order to fit it inside the program's window,
*	and rescaling artifacts are readily seen, especially if the desktop image has any kind of text in it.
*	If "Windows Picture And Fax Viewer" instead cropped the image or had a scroll pane, then this should not happen.
* </blockquote>
* <p>
* Acknowledgement: this class was inspired by the program
* <a href="http://www.discoverteenergy.com/files/ScreenImage.java">ScreenImage</a>.
* Differences from the above program:
* <ol>
*  <li>this class uses {@link BufferedImage#TYPE_INT_ARGB} instead of {@link BufferedImage#TYPE_INT_RGB} in order to preserve alpha</li>
*  <li>this class's {@link #formatNameDefault default image file format} is PNG instead of JPEG</li>
*  <li>this class's <code>take</code> methods simply take snapshots and never have the side effect of writing image files</li>
*  <li>this class added a version of <code>take</code> which can get a snapshot of a region of a Component</li>
*  <li>
*		when taking a snapshot of a region of a Component or JComponent,
*		the Rectangle that specifies the region always has coordinates relative to the origin of the item
*  </li>
* </ol>
* See also:
* <a href="http://forum.java.sun.com/thread.jspa?forumID=57&threadID=597936">forum discussion #1 on screen shots</a>
* <a href="http://forum.java.sun.com/thread.jspa?forumID=256&threadID=529933">forum discussion #2 on screen shots</a>
* <a href="http://forum.java.sun.com/thread.jspa?forumID=57&threadID=622393">forum discussion #3 on screen shots</a>.
* <p>
* It might appear that this class is multithread safe
* because it is immutable (both its immediate state, as well as the deep state of its fields).
* However, typical Java gui code is not multithread safe, in particular, once a component has been realized
* it should only be accessed by the event dispatch thread
* (see <a href="http://java.sun.com/developer/JDCTechTips/2003/tt1208.html#1">Multithreading In Swing</a>
* and <a href="http://java.sun.com/developer/JDCTechTips/2004/tt0611.html#1">More Multithreading In Swing</a>).
* So, in order to enforce that requirement, all methods of this class which deal with components
* require the calling thread to be the event dispatch thread.
* See the javadocs of each method for its thread requirements.
* <p>
* @author bbatman
*/
public class ScreenShot {
	
	
	// -------------------- constants --------------------
	
 
	/**
	* Defines the image type for the BufferedImages that will create when taking snapshots.
	* The current value is {@link BufferedImage#TYPE_INT_ARGB}, which was chosen because
	* <ol>
	*  <li>the 'A' in its name means that it preserves any alpha in the image (cannot use the "non-A" types)</li>
	*  <li>the "_INT" types are the fastest types (the "BYTE" types are slower)
	* </ol>
	* @see <a href="http://forum.java.sun.com/thread.jspa?threadID=709109&tstart=0">this forum posting</a>
	*/
	private static final int imageType = BufferedImage.TYPE_INT_ARGB;
	
	
	/**
	* Default value for the graphics file format that will be written by this class.
	* The current value is "png" because the PNG format is by far the best lossless format currently available.
	* Furthermore, java cannot write to GIF anyways (only read).
	* <p>
	* @see <a href="http://www.w3.org/TR/PNG/">Portable Network Graphics (PNG) Specification (Second Edition)</a>
	* @see <a href="http://www.w3.org/QA/Tips/png-gif">GIF or PNG</a>
	* @see <a href="http://www.libpng.org/pub/png/">PNG Home Site</a>
	*/
	public static final String formatNameDefault = "png";
	
	
	// -------------------- take --------------------
	
	
	// desktop versions:
	
	
	/**
	* Takes a screen shot of the entire desktop.
	* <p>
	* Any thread may call this method.
	* <p>
	* @return a BufferedImage representing the entire screen
	* @throws AWTException if the platform configuration does not allow low-level input control. This exception is always thrown when GraphicsEnvironment.isHeadless() returns true 
	* @throws SecurityException if createRobot permission is not granted
	*/
	public static BufferedImage take() throws AWTException, SecurityException {
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle region = new Rectangle(0, 0, d.width, d.height);
		return take(region);
	}
	
	
	/**
	* Takes a screen shot of the specified region of the desktop.
	* <p>
	* Any thread may call this method.
	* <p>
	* @param region the Rectangle within the screen that will be captured
	* @return a BufferedImage representing the specified region within the screen
	* @throws IllegalArgumentException if region == null; region's width and height are not greater than zero
	* @throws AWTException if the platform configuration does not allow low-level input control. This exception is always thrown when GraphicsEnvironment.isHeadless() returns true 
	* @throws SecurityException if createRobot permission is not granted
	*/
	public static BufferedImage take(Rectangle region) throws IllegalArgumentException, AWTException, SecurityException {
		if (region == null) throw new IllegalArgumentException("region == null");
		
		return new Robot().createScreenCapture( region );	// altho not currently mentioned in its javadocs, if you look at its source code, the Robot class is synchronized so it must be multithread safe, which is why any thread should be able to call this method
	}
	
	
	// AWT Component versions:
	
	
	/**
	* Takes a screen shot of that part of the desktop whose area is where component lies.
	* Any other gui elements in this area, including ones which may lie on top of component,
	* will be included, since the result always reflects the current desktop view.
	* <p>
	* Only {@link EventQueue}'s {@link EventQueue#isDispatchThread dispatch thread} may call this method.
	* <p>
	* @param component AWT Component to take a screen shot of
	* @return a BufferedImage representing component
	* @throws IllegalArgumentException if component == null; component's width and height are not greater than zero
	* @throws IllegalStateException if calling thread is not EventQueue's dispatch thread
	* @throws AWTException if the platform configuration does not allow low-level input control. This exception is always thrown when GraphicsEnvironment.isHeadless() returns true 
	* @throws SecurityException if createRobot permission is not granted
	*/
	public static BufferedImage take(Component component) throws IllegalArgumentException, IllegalStateException, AWTException, SecurityException {
		if (component == null) throw new IllegalArgumentException("component == null");
		if (!EventQueue.isDispatchThread()) throw new IllegalStateException("calling thread (" + Thread.currentThread().toString() + ") is not EventQueue's dispatch thread");
		
		Rectangle region = component.getBounds();
		region.x = 0;	// CRITICAL: this and the next line are what make region relative to component
		region.y = 0;
		return take(component, region);
	}
	
	
	/**
	* Takes a screen shot of that part of the desktop whose area is the region relative to where component lies.
	* Any other gui elements in this area, including ones which may lie on top of component,
	* will be included, since the result always reflects the current desktop view.
	* <p>
	* Only {@link EventQueue}'s {@link EventQueue#isDispatchThread dispatch thread} may call this method.
	* <p>
	* @param component AWT Component to take a screen shot of
	* @param region the Rectangle <i>relative to</i> component that will be captured
	* @return a BufferedImage representing component
	* @throws IllegalArgumentException if component == null; component's width and height are not greater than zero; region == null
	* @throws IllegalStateException if calling thread is not EventQueue's dispatch thread
	* @throws AWTException if the platform configuration does not allow low-level input control. This exception is always thrown when GraphicsEnvironment.isHeadless() returns true 
	* @throws SecurityException if createRobot permission is not granted
	*/
	public static BufferedImage take(Component component, Rectangle region) throws IllegalArgumentException, IllegalStateException, AWTException, SecurityException {
		if (component == null) throw new IllegalArgumentException("component == null");
		if (region == null) throw new IllegalArgumentException("region == null");
		if (!EventQueue.isDispatchThread()) throw new IllegalStateException("calling thread (" + Thread.currentThread().toString() + ") is not EventQueue's dispatch thread");
		
		Point p = new Point(0, 0);
		SwingUtilities.convertPointToScreen(p, component);
		region.x += p.x;
		region.y += p.y;
		return take(region);
	}
	
	
	// Swing JComponent versions:
	
	
	/**
	* Takes a screen shot of <i>just</i> jcomponent
	* (no other gui elements will be present in the result).
	* <p>
	* Only {@link EventQueue}'s {@link EventQueue#isDispatchThread dispatch thread} may call this method.
	* <p>
	* @param jcomponent Swing JComponent to take a screen shot of
	* @return a BufferedImage representing jcomponent
	* @throws IllegalArgumentException if jcomponent == null
	* @throws IllegalStateException if calling thread is not EventQueue's dispatch thread
	*/
	public static BufferedImage take(JComponent jcomponent) throws IllegalArgumentException, IllegalStateException {
		if (jcomponent == null) throw new IllegalArgumentException("jcomponent == null");
		if (!EventQueue.isDispatchThread()) throw new IllegalStateException("calling thread (" + Thread.currentThread().toString() + ") is not EventQueue's dispatch thread");
		
		Dimension d = jcomponent.getSize();
		Rectangle region = new Rectangle(0, 0, d.width, d.height);
		return take(jcomponent, region);
	}
	
	
	/**
	* Takes a screen shot of <i>just</i> the specified region of jcomponent
	* (no other gui elements will be present in the result).
	* <p>
	* Only {@link EventQueue}'s {@link EventQueue#isDispatchThread dispatch thread} may call this method.
	* <p>
	* @param jcomponent Swing JComponent to take a screen shot of
	* @param region the Rectangle <i>relative to</i> jcomponent that will be captured
	* @return a BufferedImage representing the region within jcomponent
	* @throws IllegalArgumentException if jcomponent == null; region == null
	* @throws IllegalStateException if calling thread is not EventQueue's dispatch thread
	*/
	public static BufferedImage take(JComponent jcomponent, Rectangle region) throws IllegalArgumentException, IllegalStateException {
		if (jcomponent == null) throw new IllegalArgumentException("jcomponent == null");
		if (region == null) throw new IllegalArgumentException("region == null");
		if (!EventQueue.isDispatchThread()) throw new IllegalStateException("calling thread (" + Thread.currentThread().toString() + ") is not EventQueue's dispatch thread");
		
		boolean opaquenessOriginal = jcomponent.isOpaque();
		Graphics2D g2d = null;
		try {
			jcomponent.setOpaque( true );
			BufferedImage image = new BufferedImage(region.width, region.height, imageType);
			g2d = image.createGraphics();
			g2d.translate(-region.x, -region.y) ;	// CRITICAL: this and the next line are what make region relative to component
			g2d.setClip( region );
			jcomponent.paint( g2d );
			return image;
		}
		finally {
			jcomponent.setOpaque( opaquenessOriginal );
			if (g2d != null) g2d.dispose();
		}
	}
	
	
	// -------------------- write --------------------
	
	
	/**
	* Writes image to a newly created File named fileName.
	* The graphics format will either be the extension found in fileName
	* or else {@link #formatNameDefault} if no extension exists.
	* <p>
	* Any thread may call this method.
	* <p>
	* @param image the BufferedImage to be written
	* @param fileName name of the File that will write image to
	* @throws IllegalArgumentException if image == null; fileName is blank
	* @throws IOException if an I/O problem occurs
	*/
	public static void write(BufferedImage image, String fileName) throws IllegalArgumentException, IOException {
//		if (image == null) throw new IllegalArgumentException("image == null");
//		if (StringUtil.isBlank(fileName)) throw new IllegalArgumentException("fileName is blank");
//		
//		File file = new File(fileName);
//		
//		String formatName = FileUtil.getExtension(file);
//		if (formatName.length() == 0) formatName = formatNameDefault;
//		
//		write(image, formatName, file);
	}
	
	
	/**
	* Writes image to file in the format specified by formatName.
	* <p>
	* Any thread may call this method.
	* <p>
	* @param image the BufferedImage to be written
	* @param formatName the graphics file format (e.g. "pnj", "jpeg", etc);
	* must be in the same set of values supported by the formatName arg of {@link ImageIO#write(RenderedImage, String, File)}
	* @param file the File that will write image to
	* @throws IllegalArgumentException if image == null; type is blank; file == null
	* @throws IOException if an I/O problem occurs
	*/
	public static void write(BufferedImage image, String formatName, File file) throws IllegalArgumentException, IOException {
		if (image == null) throw new IllegalArgumentException("image == null");
//		if (StringUtil.isBlank(formatName)) throw new IllegalArgumentException("formatName is blank");
		if (file == null) throw new IllegalArgumentException("file == null");
		
		ImageIO.write(image, formatName, file);
	}
	
	
	// -------------------- Test (inner class) --------------------
	
	
	/**
	* An inner class that consists solely of test code for the parent class.
	* <p>
	* Putting all the test code in this inner class (rather than a <code>main</code> method of the parent class) has the following benefits:
	* <ol>
	*  <li>test code is cleanly separated from working code</li>
	*  <li>any <code>main</code> method in the parent class is now reserved for a true program entry point</li>
	*  <li>test code may be easily excluded from the shipping product by removing all the Test class files (e.g. on Windoze, delete all files that end with <code>$Test.class</code>)</li>
	* </ol>
	* Putting all the test code in this inner class (rather than a shadow external class) has the following benefits:
	* <ol>
	*  <li>non-public members may be accessed</li>
	*  <li>the test code lives very close to (so is easy to find) yet is distinct from the working code</li>
	*  <li>there is no need to set up a test package structure</li>
	* </ol>
	*/
	public static class Test {
				
		public static void main(String[] args) throws Exception {
			Gui gui = new Gui();
			EventQueue.invokeLater( gui.getBuilder() );
//			new Timer(1000, gui.getTimerActionListener()).start();
		}
		
		private static class Gui {
		
			private Frame frame;
			private TextField textField;
			private JFrame jframe;
			private JLabel jlabel;
			private JPanel jpanel;
			
			private int count = 0;
						
			private Runnable getBuilder() {
				return new Runnable() {
					public void run() {
						System.out.println("Creating a Frame with AWT widgets inside...");
						frame = new Frame("ScreenShot.Test.main Frame");
						textField = new TextField();
						textField.setText( "Waiting for the screen shot process to automatically start..." );
						frame.add(textField);						
						frame.pack();
						frame.setLocationRelativeTo(null);	// null will center it in the middle of the screen
						frame.setVisible(true);
						
						System.out.println("Creating a JFrame with Swing widgets inside...");
						jframe = new JFrame("ScreenShot.Test.main JFrame");
						jlabel = new JLabel(
							"<html>" +
								"To be, or not to be: that is the question:" + "<br>" +
								"Whether 'tis nobler in the mind to suffer" + "<br>" +
								"The slings and arrows of outrageous fortune," + "<br>" +
								"Or to take arms against a sea of troubles," + "<br>" +
								"And by opposing end them?" + "<br>" +
								"To die: to sleep; No more;" + "<br>" +
								"and by a sleep to say we end" + "<br>" +
								"The heart-ache and the thousand natural shocks" + "<br>" +
								"That flesh is heir to," + "<br>" +
								"'tis a consummation Devoutly to be wish'd." + "<br>" +
							"</html>"
						);
						jpanel = new JPanel();
						jpanel.setBorder( BorderFactory.createEmptyBorder(20, 20, 20, 20) );
						jpanel.add(jlabel);
						jframe.getContentPane().add(jpanel);
						jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
						jframe.pack();
						Point p = frame.getLocation();
						p.translate(0, frame.getSize().height  + 10);
						jframe.setLocation(p);
						jframe.setVisible(true);
					}
				};
			}
			
			private ActionListener getTimerActionListener() {
				return new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						try {
							switch (count++) {
								case 0:
									displayMessage("Taking a screen shot of the entire desktop...");
									ScreenShot.write( ScreenShot.take(), "desktop.png" );
									break;
								case 1:
									displayMessage("Taking a screen shot of the central rectangle of the desktop...");
									Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
									Rectangle region = getCenteredRectangle(d);
									ScreenShot.write( ScreenShot.take(region), "desktopCenteredRectangle.png" );
									break;
								case 2:
									displayMessage("Taking a screen shot of the TextField...");
									ScreenShot.write( ScreenShot.take(textField), "textField.png" );
									break;
								case 3:
									displayMessage("Taking a screen shot of the central rectangle of the TextField...");
									d = textField.getSize();
									region = getCenteredRectangle(d);
									ScreenShot.write( ScreenShot.take(textField, region), "textFieldCenteredRectangle.png" );
									break;
								case 4:
									displayMessage("Taking a screen shot of the JLabel...");
									ScreenShot.write( ScreenShot.take(jlabel), "jlabel.png" );
									break;
								case 5:
									displayMessage("Taking a screen shot of the central rectangle of the JLabel...");
									d = jpanel.getSize();
									region = getCenteredRectangle(d);
									ScreenShot.write( ScreenShot.take(jpanel, region), "jpanelCenteredRectangle.png" );
									break;
								default:
									System.exit(0);
									break;
							}
						}
						catch (Throwable t) {
							t.printStackTrace(System.err);
						}
					}
					
					private void displayMessage(String text) {
						System.out.println(text);
						textField.setText(text);
						frame.pack();
						frame.invalidate();
					}
					
					private Rectangle getCenteredRectangle(Dimension d) {
						int x = d.width / 4;
						int y = d.height / 4;
						int width = d.width / 2;
						int height = d.height / 2;
						return new Rectangle(x, y, width, height);
					}
				};
			}
			
		}
		
	}
}
