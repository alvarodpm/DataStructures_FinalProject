package model.data_structures;

import java.util.Iterator;


public class ListaEncadenada<E> implements Lista<E> {

	//Atributos
	private Node<E> firstNode;
	private Node<E> lastNode;
	private int listSize;

	//Constructor
	public ListaEncadenada() {
		firstNode = null;
		lastNode = null;
	}

	public ListaEncadenada(E item1)
	{
		firstNode = new Node<E>(item1);
		lastNode = firstNode;
		listSize = 1;
	}

	@Override
	public Iterator<E> iterator() {
		return new IteradorLista<E>(firstNode);
	}

	@Override
	public void addFirst(E item) {
		Node<E> nuevoPrimero = new Node<E>(item);
		if(isEmpty())
		{
			firstNode = nuevoPrimero;
			lastNode = nuevoPrimero;
			listSize++;
		}
		else
		{
			nuevoPrimero.setNextNode(firstNode);
			firstNode = nuevoPrimero;
			listSize++;
		}
	}

	@Override
	public void addLast(E item) {
		Node<E> nuevoNode = new Node<E>(item);
		
		if(isEmpty())
		{
			firstNode = nuevoNode;
			lastNode = nuevoNode;
		}

		else
		{
			lastNode.setNextNode(nuevoNode);
			lastNode = nuevoNode;
		}
		listSize++;
	}

	@Override
	public void removeFirst() {
		if(!isEmpty()) 
		{
			if(listSize == 1)
			{
				firstNode = null;
				lastNode = null;
			}
			else
				firstNode = firstNode.getNext();

			listSize--;
		}
	}

	@Override
	public void remove(int pos) {
		if(pos < 0 || pos >= listSize)
			throw new IndexOutOfBoundsException();
		else
		{
			if(pos == 0)
				firstNode = firstNode.getNext();
			else if(pos == listSize-1)
			{
				lastNode = getNode(listSize - 2);
				lastNode.setNextNode(null);
			} 
			else
			{
				Node<E> actual = firstNode;
				int contador = 0;

				while(contador < pos-1)
				{
					contador++;
					actual = actual.getNext();
				}

				actual.setNextNode(actual.getNext().getNext());
			}
		}
		
		if(firstNode == null || lastNode == null)
		{
			lastNode = null;
			firstNode = null;
		}
		
		listSize--;
	}

	@Override
	public void clear() {
		firstNode = null;
		listSize = 0;
	}

	@Override
	public E get(int pos) {
		if(pos < 0 || pos >= listSize)
			throw new IndexOutOfBoundsException();
		else
		{
			Node<E> actual = firstNode;
			int contador = 0;

			while(contador < pos)
			{
				contador++;
				actual = actual.getNext();
			}

			return actual.getItem();
		}
	}
	
	public Node<E> getNode(int pos)
	{
		if(pos < 0 || pos >= listSize)
			throw new IndexOutOfBoundsException();
		else
		{
			Node<E> actual = firstNode;
			int contador = 0;

			while(contador < pos)
			{
				contador++;
				actual = actual.getNext();
			}

			return actual;
		}
	}
	
	public E getFirstItem()
	{
		return firstNode.getItem();
	}
	
	public E getLastItem()
	{
		return lastNode.getItem();
	}
	
	@Override
	public int size() {
		return listSize;
	}

	@Override
	public boolean isEmpty() {
		return listSize == 0;
	}

}
