package tastierino;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;

public class CTextField extends JFormattedTextField implements FocusListener {
	private static final long serialVersionUID = -5661756632346802551L;

	public CTextField(int columns) {
		super(NumberFormat.getIntegerInstance());
		this.setColumns(columns);
//		this.addComponentListener(this);
		this.addFocusListener(this);
		this.setInputVerifier(new Verifier());
	}

    @Override
    protected void processComponentKeyEvent(KeyEvent e) {
//    	super.processComponentKeyEvent(e);
    	if (e.getKeyCode()==KeyEvent.VK_CLEAR) {
    		setText("");
    		e.consume();
    	}
    }

    @Override
    public void focusGained(FocusEvent e) {
    	selectAll();
    	NumKeyb.get().setVisible(this);
    }

    @Override
    public void focusLost(FocusEvent e) {
//    	NumKeyb.get().setVisible(false);
    }

    private final class Verifier extends InputVerifier {
    	Verifier() { }

		@Override
		public boolean verify(JComponent input) {
			System.out.println("verify: input='" + input.getClass().getCanonicalName() + "', this='" + this.getClass().getCanonicalName() + "'");
			System.out.println("input='" + ((CTextField)input).getText() + "', this='" + CTextField.this.getText() + "'");
			return true;
		}
    }

}
