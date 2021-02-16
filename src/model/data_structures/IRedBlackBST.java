package model.data_structures;

import java.util.Iterator;

/**
 * Estructura tomada de  la sección 3.3 Balanced Search Trees del libro guía Algorithms de Sedgewick y Wayne.
 */
public interface IRedBlackBST<K extends Comparable<K>, V> {

	public int size();
	public boolean isEmpty();
	public V get(K key);
	public int getHeight(K key);
	public boolean contains(K key);
	public void put(K key, V val) throws Exception;
	public int height();
	public K minKey();
	public K maxKey();
	public V minValue();
	public V maxValue();
	public boolean check();
	public Iterator <K> keys();
	public Iterator<V> valuesInRange(K init, K end);
	public Iterator<K> keysInRange(K init, K end);	
}