package model.data_structures;

public class Node<E> {
	
	//Atributos
	private Node<E> next;
	private E item;
	
	//Constructor
	public Node (E item1)
	{
		next = null;
		this.item = item1;
	}
	
	//Métodos
	
	public Node<E> getNext()
	{
		return next;
	}
	
	public void setNextNode(Node<E> next1)
	{
		this.next = next1;
	}
	
	public E getItem()
	{
		return item;
	}
	
	public void setItem(E item1)
	{
		this.item = item1;
	}

}
