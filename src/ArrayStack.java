/**
 * The ArrayStack class implements a stack using an array
 * 
 * @author Abdul Khan
 *
 * @param <T> a Generic is included in the header of the class to indicated that
 *            any type of object may be store in the stack and use the class
 *            methods
 */
public class ArrayStack<T> implements ArrayStackADT<T> {

	private T[] stack;
	private int top = -1;
	public static String sequence = "";

	/**
	 * The default constructor creates an array and initializes it with a size of 14
	 */
	public ArrayStack() {
		stack = (T[]) (new Object[14]);
		sequence = "";
	}

	/**
	 * If an additional int argument is provided, the size of the array will be
	 * initialized with the value of the argument
	 * 
	 * @param initialCapacity the size of the array
	 */
	public ArrayStack(int initialCapacity) {
		stack = (T[]) (new Object[initialCapacity]);
	}

	/**
	 * The push method pushes objects onto the top of the stack and expands the
	 * capacity if needed
	 */
	public void push(T dataItem) {

		if (size() == stack.length) {

			if (stack.length < 50) {
				T[] newStack = (T[]) (new Object[stack.length + 10]);
				for (int index = 0; index < stack.length; index++) {
					newStack[index] = stack[index];
				}
				stack = newStack;
			} else {
				T[] largeStack = (T[]) (new Object[stack.length * 2]);
				for (int index = 0; index < stack.length; index++) {
					largeStack[index] = stack[index];
				}
				stack = largeStack;
			}
		}
		if (isEmpty()) {
			stack[0] = dataItem;
			top = 0;
		} else {
			top++;
			stack[top] = dataItem;

		}

		if (dataItem instanceof MapCell) {
			sequence += "push" + ((MapCell) dataItem).getIdentifier();
		} else {
			sequence += "push" + dataItem.toString();
		}
	}

	/**
	 * The pop method removes the top item from the stack and decreases the capacity
	 * to increase the efficiency and use of memory. Throws an exception if the
	 * stack is empty
	 */
	public T pop() throws EmptyStackException {
		T result;
		if (size() == 0) {
			throw new EmptyStackException("stack");
		}
		result = stack[top];
		stack[top] = null;
		top--;

		if (size() < (stack.length / 4)) {
			if ((stack.length / 2) > 14) {
				T[] smallerArray = (T[]) (new Object[stack.length / 2]);
				for (int index = 0; index <= top; index++) {
					smallerArray[index] = stack[index];
				}
				stack = smallerArray;
			}
			if ((stack.length / 2) < 14) {
				T[] smallerArray = (T[]) (new Object[14]);
				for (int index = 0; index <= top; index++) {
					smallerArray[index] = stack[index];
				}
				stack = smallerArray;

			}
		}
		if (result instanceof MapCell) {
			sequence += "pop" + ((MapCell) result).getIdentifier();
		} else {
			sequence += "pop" + result.toString();
		}
		return result;
	}

	/**
	 * The peek method returns the item at the top of the stack and throws an
	 * exception if the stack is empty
	 */
	public T peek() throws EmptyStackException {
		if (top == -1) {
			throw new EmptyStackException("stack");
		}
		return stack[top];
	}

	/**
	 * The isEmpty method returns true if the stack is empty and vice versa
	 */
	public boolean isEmpty() {
		return (top == -1);
	}

	/**
	 * The size method returns the number of data items in the stack
	 */
	public int size() {
		return (top + 1);
	}

	/**
	 * The length method returns the capacity of the stack
	 * 
	 * @return
	 */
	public int length() {
		return stack.length;
	}

	/**
	 * The toString method returns a string representation of the stack, from the
	 * bottom to the top
	 */
	public String toString() {
		String str = "Stack: " + stack[0];
		for (int i = 1; i < size(); i++) {
			str = str + ", ";
			str = str + stack[i];
			;
		}
		return str;
	}

}
