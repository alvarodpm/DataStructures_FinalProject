package model.data_structures;

import java.util.Comparator;


public class MinHeapCP <T extends Comparable<T>, V> implements IMinCP<T,V>{
	
	/**
	 * Clase interna que representa el nodo de un Heap
	 */
	private class HeapNode<T extends Comparable<T>, V> implements Comparable<HeapNode<T, V>>
	{
		//Atributos
		
		T key;
		V val;
		
		public HeapNode(T key, V val)
		{
			this.key = key;
			this.val = val;
		}

		@Override
		public int compareTo(HeapNode<T, V> hn) {
			return this.key.compareTo(hn.key);
		}
	}
	
	private HeapNode<T,V>[] pq;
	private int N = 0;
	
	public MinHeapCP()
	{
		pq = (HeapNode<T, V> []) new HeapNode[11];
	}
	
	public MinHeapCP(int max)
	{
		pq = (HeapNode<T, V> []) new HeapNode[max + 1];
	}

	@Override
	public void agregar(T key, V val) {
		if(pq.length == N+1)
		{
			int newLength = 2*N;
			HeapNode<T,V>[] copia = (HeapNode<T, V> []) pq;
			pq = (HeapNode<T, V> []) new HeapNode[newLength];
			for(int i = 1; i <= N; i++)
			{
				pq[i] = copia[i];
			}
		}
		pq[++N] = new HeapNode<T, V>(key, val);      
		swim(N);
	}
	
	@Override
	public void agregar(T key, V val, Comparator<T> comparador) {
		if(pq.length == N+1)
		{
			int newLength = 2*N;
			HeapNode<T,V>[] copia = (HeapNode<T, V> []) pq;
			pq = (HeapNode<T, V> []) new HeapNode[newLength];
			for(int i = 1; i <= N; i++)
			{
				pq[i] = copia[i];
			}
		}
		pq[++N] = new HeapNode<T, V>(key, val);      
		swim(N, comparador);
	}

	@Override
	public V darMin() {
		return pq[1].val;
	}

	@Override
	public V sacarMin() {
		V max = pq[1].val;           
	      
		 exch(1, N--);              
		   
		 pq[N+1] = null;            
		      
		 sink(1);
		 		      
		 return max;
	}
	
	@Override
	public V sacarMin(Comparator<T> comparador) {
		V max = pq[1].val;           
	      
		 exch(1, N--);              
		   
		 pq[N+1] = null;            
		      
		 sink(1, comparador);
		 		      
		 return max;
	}

	@Override
	public boolean esVacia() {
		return N == 0;
	}

	@Override
	public int darNumElementos() {
		return N;
	}
	
	public boolean more(int i, int j)
	{
		return pq[i].compareTo(pq[j]) > 0;  
	}
	
	public boolean more(int i, int j, Comparator<T> comparador)
	{
		return comparador.compare(pq[i].key, pq[j].key) > 0;
	}
	
	public void exch(int i, int j)
	{
		HeapNode<T,V> t = pq[i]; 
		pq[i] = pq[j]; 
		pq[j] = t; 
	}
	
	public void swim(int k) 
	{   
		while (k > 1 && more(k/2, k))   
		{      
			exch(k/2, k);     
			k = k/2;   
		} 
	}
	
	public void swim(int k, Comparator<T> comparador) 
	{   
		while (k > 1 && more(k/2, k, comparador))   
		{      
			exch(k/2, k);     
			k = k/2;   
		} 
	}
	
	public void sink(int k) 
	{   
		while (2*k <= N)   
		{      
			int j = 2*k;      
			if (j < N && more(j, j+1)) 
				j++;      
			if (!more(k, j)) 
				break;      
			exch(k, j);      
			k = j;   
		} 
	}
	
	public void sink(int k, Comparator<T> comparador) 
	{   
		while (2*k <= N)   
		{      
			int j = 2*k;      
			if (j < N && more(j, j+1, comparador)) 
				j++;      
			if (!more(k, j, comparador)) 
				break;      
			exch(k, j);      
			k = j;   
		} 
	}
	
	public boolean contains(int T)
	{
	return pq[T] != null;
	}
}
