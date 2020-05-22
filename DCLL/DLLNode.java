

/**
 * 
 * @author estebanacosta
 * This class works in tandem with the DCLL class
 */

public class DLLNode {
	
	/** The data contained in this node */
	private Object data;

	/** A reference to the next node in the list */
	private DLLNode next;

	/**A reference to the previous node in the list*/
	private DLLNode previous;

	/**
	 * Constructs a node.
	 *
	 * @param b The data to put into the new node.
	 */

	public DLLNode(){

	}

	public DLLNode(Object b) {
		setData(b);
		next = null;
		previous = null;
	}

	/**
	 * Mutator method for the data held within this node.
	 *
	 * @param b The book to contain in this node.
	 */
	public void setData(Object b) {
		data = b;
	}

	/**
	 * Mutator method for the next node data member.
	 *
	 * @param n The node to set as the next node.
	 */
	public void setNext(DLLNode n) {
		next = n;
	}

	/**
	 * Mutator method for the previous node data member.
	 *
	 * @param n The node to set as the previous node.
	 */
	public void setPrevious(DLLNode n){
		previous = n;

	}

	/**
	 * Returns the data contained in this node.
	 *
	 * @return The data contained in this node.
	 */
	public Object getData() {
		return data;
	}

	/**
	 * Returns a reference to the next node in the list.
	 *
	 * @return A reference to the next node in the list.
	 */
	public DLLNode getNext() {
		return next;
	}

	/**
	 * Returns a reference to the previous node in the list
	 * 
	 * @return A reference to the previous node in the list
	 */

	public DLLNode getPrevious(){
		return previous;
	}
}
