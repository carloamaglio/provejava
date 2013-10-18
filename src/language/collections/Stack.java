package language.collections;

import java.util.LinkedList;
import java.util.NoSuchElementException;


class Stack<T> extends LinkedList<T> {
	private static final long serialVersionUID = 1L;

	Stack() {
		super();
	}

	void push(T o) {
		add(o);
	}

	T pop() {
		T rv;
		try {
			rv = removeLast();
		} catch (NoSuchElementException e) {
			rv = null;
		}
		return rv;
	}
}
