package model.data_structures;

import java.util.Comparator;

public interface IMinCP <T extends Comparable<T>, V>{
	public void agregar(T key, V val);
	public V darMin();
	public V sacarMin();
	public boolean esVacia();
	public int darNumElementos();
	void agregar(T key, V val, Comparator<T> comparador);
	V sacarMin(Comparator<T> comparador);
}
