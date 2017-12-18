package ss.src.ProgramingAssignment2.copy;

import java.util.NoSuchElementException;

/**
 * @author estebanacosta
 *
 */
public class DCLL {

	/** A reference to the first node in the Doubly Linked List */
	private DLLNode head;

	/** A reference to the last node in the Doubly Linked List */
	private DLLNode tail;

	/**
	 * Appends the specified element to the end of this list.
	 * @param o
	 * @return
	 */
	public boolean add(Object o){
		DLLNode n = new DLLNode(o);

		if(head == null) {
			// list is empty
			head = n;
			tail = n;
		} else {
			// list is not empty
			// Set the current tail node's next to be the new node
			tail.setNext(n);

			// Set the new node's previous to be the current tail
			n.setPrevious(tail);

			//Set the head as the new node's next node
			n.setNext(head);

			//Set the tail to be node before the head
			head.setPrevious(n);

			// Update the tail of the list to refer to the new node
			tail = n;
		}
		return true;

	}


	/***
	 * Inserts the specified element at the specified position in this list
	 * @param i
	 * @param o
	 * @throws IndexOutOfBoundsException
	 */
	public void add(int i, Object o) throws IndexOutOfBoundsException{
		/*
		 * These two exceptions prevent the user from entering values that are  out of bounds
		 */

		if(i < 0){
			throw new IndexOutOfBoundsException("Index must be greater than 0 or equal to zero");

		}
		else if(i > size()){
			throw new IndexOutOfBoundsException("Index must be less than the list size ");
		}


		DLLNode current = head;
		DLLNode newNode = new DLLNode(o);
		int count = 0;
		//Loop through the list until we find the index user is looking for
		while(count < i - 1){

			current = current.getNext();
			count++;

		}

		//If user wants to insert the node at index zero...
		if(i == 0){

			//...set the new node's next node as the current head
			newNode.setNext(head);

			//set the current head's previous node as the new node
			head.setPrevious(newNode);

			//set the tail as the new node's previous node 
			newNode.setPrevious(tail);

			//set the new node as the tail's next node
			tail.setNext(newNode);

			//make the new node the head of list
			head = newNode;

		}

		//If user wishes to insert the node at the end of the list...
		else if(i == size()){
			//call the add() method
			add(o);
		}

		//Otherwise...
		else{

			newNode.setNext(current.getNext());
			newNode.setPrevious(current);
			current.setNext(newNode);
			newNode.getNext().setPrevious(newNode);
		}

	}
	/**
	 * Returns the element at the specified position in this list
	 * @param i
	 * @return
	 * @throws IndexOutOfBoundsException
	 */
	public Object get(int i) throws IndexOutOfBoundsException{
		if(i < 0 || i > this.size() - 1) {
			throw new IndexOutOfBoundsException("index is not valid");
		}

		DLLNode current = head;
		int count = 0;

		// Traverse the list to the desired item
		while(current.getNext() != null && current.getNext() != head && count < i ) {
			current = current.getNext();
			count++;
		}
		return current.getData();
	}

	/**
	 * Removes all of the elements from this list
	 */
	public void clear(){
		//First set the next node as null
		head.setNext(null);
		//Then remove the head of the linkedList
		head = null;
	}

	/**
	 * Returns true if this list contains the specified element.
	 * @param o
	 * @return
	 */
	public boolean contains(Object o){

		DLLNode current = head;
		//if the head contains the data we are searching for, return true
		if(current.getData() == o){
			return true;
		}

		//otherwise loop through the list until we find that data
		while(current.getNext()!= null && current.getNext() != head){

			current = current.getNext();

			if(current.getData() == o){
				return true;
			}
		}

		//return false if the data isn't found
		return false;
	}

	/**
	 * Returns true if this list contains no elements.
	 * @return
	 */
	public boolean isEmpty(){
		//if the size of the list is zero then that must mean the list is empty
		if(size() == 0){
			return true;
		}
		//if the size of the list isn't zero, the list isn't empty and we return false
		return false;
	}

	/**
	 * Returns the index of the first occurrence of the specified element in this list, 
	 * or -1 if this list does not contain the element.
	 * @param o
	 * @return count
	 */
	public int indexOf(Object o) throws NoSuchElementException {

		if(contains(o) == false){
			throw new NoSuchElementException("Object doesn't exist in the list");
		}

		DLLNode current = head;
		int count = 0;
		//loop through the list
		while(count < size()){
			//if object is equal to the data in that node
			if(current.getData().equals(o)){
				//return position
				return count;
			}
			//otherwise continue looping
			count++;
			current = current.getNext();
		}
		//if we can't find the data, return -1
		return -1;
	}

	/**
	 * Returns the index of the last occurrence of the specified element in this list,
	 *  or -1 if this list does not contain the element.
	 * @param o
	 * @return count
	 */
	public int lastIndexOf(Object o) throws NoSuchElementException {

		if(contains(o) == false){
			throw new NoSuchElementException("Object doesn't exist in the list");
		}
		DLLNode current = tail;
		//Since we are starting with the tail end of the list
		//We start at the last position of the list (which is list size - 1)
		int count = size() - 1;

		//Since we are finding the last occurrence of the specified element, we start from the tail of the list
		//We start from the tail and loop through until we reach the head.
		while(current.getPrevious() != null  && current.getPrevious() != tail){
			//If the data contained in that node is equal to the object
			if(current.getData().equals(o)){
				//return the position
				return count;
			}	
			//otherwise continue looping
			current= current.getPrevious();
			//Since we start from the tail, we are going backwards
			//therefore we are subtracting 1 every time we loop through the list 
			count--;
		}

		//if we can't find the object, return -1
		return -1;

	}

	/**
	 * Removes the element at the specified position in this list
	 * @param i
	 * @throws IndexOutOfBoundsException
	 */
	public Object remove(int i)throws IndexOutOfBoundsException{
		DLLNode current = head;
		int count =0;
		Object obj = new Object();

		if(i < 0 || i > this.size() - 1){
			throw new IndexOutOfBoundsException("index is not valid");
		}

		//if we are removing the head of the list...
		if(i == 0){
			obj = current.getData();
			tail.setNext(current.getNext());
			current.getNext().setPrevious(tail);
			head = current.getNext();
		}
		//if we are removing the last element of the list...
		else if(i == size() - 1){

			obj = tail.getData();

			//Set the node before the tail to be the head's previous node
			current.setPrevious(tail.getPrevious());

			//Set the the head to be the tail's previous node's next node
			tail.getPrevious().setNext(current);

			//Make the node before the tail be the list's new tail
			tail =	tail.getPrevious();
		}

		//If we are not removing the head or the tail of the list, then...
		else{

			//loop through the linked list until...
			while(current.getNext()!=null && current.getNext() != head){
				//the index the user enters matches the index in the list
				//we want to stop at the node right before the node index user enters
				if(count == i - 1){
					//break out of the loop
					break;
				}
				//continue looping 
				current = current.getNext();
				count++;
			}

			//if there is a node after the node after the current node...
			if(current.getNext().getNext() != null){
				obj = current.getNext();
				//set the the node after the node we are removing to be the current's net node
				current.setNext(current.getNext().getNext());
				//set the node before the node we are removing to be previous node
				current.getNext().getNext().setPrevious(current);
			}
		}

		return obj;
	}

	/**
	 * Removes the first occurrence of the specified element from this list, if it is present 
	 * @param o
	 * @return
	 */
	public boolean remove(Object o) throws NoSuchElementException {

		DLLNode current = head;
		int count = 0;

		if(contains(o) == false){
			throw new NoSuchElementException("This element doesn't exist in the list");
		}
		//Loop through the array until we reach the end of the list
		while(count < size()){
			//if the data contained in the current node is equal to the object we are passing it
			if(current.getData().equals(o)){
				//call the remove method and pass it the current node's position
				remove(count);
				//return true if this works
				return true;
			}
			//otherwise continue looping
			current = current.getNext();
			count++;
		}
		//return false if the data isn't in the list
		return false;
	}

	/**
	 * Replaces the element at the specified position in this list with the specified element 
	 * @param i
	 * @param o
	 */
	public Object set(int i , Object o) throws IndexOutOfBoundsException{
		DLLNode current = head;
		Object obj = new Object();
		int count = 0;

		if(i < 0 || i > this.size() - 1){
			throw new IndexOutOfBoundsException("Index is not valid. Enter an index greater than or equal to zero or "
					+ " less than or equal to the last position");
		}
		//loop through the list until we find the last node
		while(current.getNext() != null){
			//if the position matches the position we pass it, replace the data in that
			//position with the object
			if(count == i){
				obj = current.getData();
				current.setData(o);
				//leave the loop once you're done
				break;
			}
			//otherwise continue looping
			current = current.getNext();
			count++;
		}

		return obj;
	}

	/**
	 * Returns the number of elements in this list
	 * @return
	 */
	public int size(){
		DLLNode current = head; // Reference to the current node
		int count = 0;
		while(current != null) {
			// Increment the count of objects in the list
			count++;

			//if the next node is the head, break out of the loop
			if(current.getNext() == head){
				break;
			}

			// Set current to the next node in the list
			current = current.getNext();
		}
		return count;
	}

	/**
	 * returns	a string representation	of the list	in	forward	order (head	to	tail)
	 * @return
	 */
	public String forwardString() {
		DLLNode current = head;
		String attached = "";
		//Loop through the list...
		while(current.getNext() != null && current.getNext() != head){
			//attach the node's data to the string
			attached += current.getData().toString() + ", ";
			//move on to the next node
			current = current.getNext();
		}
		//add the very last node's title to the string
		attached += current.getData().toString();
		return attached; 
	}

	/**
	 * 	returns	a string representation	of the	list in	backward order (tail to	head)
	 * @return
	 */
	public String reverseString() {
		DLLNode current = tail;
		String combined = "";
		//Loop backwards. Start at the tail and loop through until we reach the head
		while(current.getPrevious() != null && current.getPrevious() != tail){
			//Add every previous node's title to the string
			combined += current.getData().toString() + ", ";
			//move on to the previous node
			current = current.getPrevious();
		}
		//add the head node's title to the string
		combined += current.getData().toString();
		return combined; 

	}

}
