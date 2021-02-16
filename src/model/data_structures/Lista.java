package model.data_structures;

public interface Lista<E> extends Iterable<E>{

	public void addFirst(E item);
	public void addLast(E item);
	public void clear();
	public void removeFirst();
	public void remove(int pos);
	public E get(int pos);
	public int size();
	public boolean isEmpty();
	public E getFirstItem();
	public E getLastItem();
}
