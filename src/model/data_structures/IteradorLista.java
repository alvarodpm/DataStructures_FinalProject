package model.data_structures;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class IteradorLista<E> implements Iterator<E> {
	
	//Atributos
	private Node<E> actual;
	
	//Constructor
	public IteradorLista(Node<E> primero) {
		actual = primero;
	}

	@Override
	public boolean hasNext() {
		return actual != null;
	}

	@Override
	public E next() throws NoSuchElementException{
		if(actual == null)
			throw new NoSuchElementException("Se ha alcanzado el final de la lista");
		
		E valor = actual.getItem();
		actual = actual.getNext();
		return valor;
	}

}
