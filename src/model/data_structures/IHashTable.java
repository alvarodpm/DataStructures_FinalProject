package model.data_structures;

import java.util.Iterator;

public interface IHashTable<K extends Comparable<K>, V> {
	public void putInSet(K key, V value);
	public Iterator<V> getSet(K key);
	public Iterator<V> deleteSet(K key);
	public Iterator<K> keys();
	public void rehash();
	public int hash(K key);
	public double darLoadFactor();
}
