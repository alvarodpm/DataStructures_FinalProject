package model.data_structures;

public interface IMaxCP <T extends Comparable<T>>{
	
	public void agregar(T item);
	public T darMax();
	public T sacarMax();
	public boolean esVacia();
	public int darNumElementos();
}
