package language.collections;


public class BooleanStack {
	private static final long serialVersionUID = 1L;

	private Stack<Boolean> s = new Stack<Boolean>();

	public BooleanStack() {
		super();
	}

	public void push(boolean notify) {
		s.push(new Boolean(notify));
	}

	public boolean pop() {
		Boolean b = s.pop();
		boolean rv = (b!=null) ? b.booleanValue() : true;
		return rv;
	}

	public static void main(String[] args) {
		BooleanStack s = new BooleanStack();
		s.push(false);
		s.push(false);
		s.push(true);
		s.push(true);
		s.push(false);
		s.push(true);
		s.push(false);
		for (int i=0; i<10; i++) {
			System.out.println("" + s.pop());
		}
	}
}

