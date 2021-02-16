package model.data_structures;


public class MaxHeapCP <T extends Comparable<T>> implements IMaxCP<T>{

	private T[] pq;
	private int N = 0;
	
	/**
	 * Tomado del libro Algorithms, capítulo 2.4
	 */
	public MaxHeapCP()
	{
		pq = (T[]) new Comparable[11];
	}
	
	public MaxHeapCP(int max)
	{
		pq = (T[]) new Comparable[max + 1];
	}

	public MaxHeapCP(T[] items)
	{
		pq = (T[]) new Comparable[items.length + 1];
		
		for(T actual : items)
		{
			agregar(actual);
		}
	}
	
	/**
	 * Tomado del libro Algorithms, capítulo 2.4
	 */
	public void agregar(T item)
	{
		if(pq.length == N+1)
		{
			int newLength = 2*N;
			T[] copia = pq;
			pq = (T[]) new Comparable[newLength];
			for(int i = 1; i <= N; i++)
			{
				pq[i] = copia[i];
			}
		}
		pq[++N] = item;      
		swim(N);
	}
	
	/**
	 * Tomado del libro Algorithms, capítulo 2.4
	 */
	public T darMax()
	{
		return pq[1];
	}
	
	/**
	 * Tomado del libro Algorithms, capítulo 2.4
	 */
	public T sacarMax()
	{
		 T max = pq[1];           
	      
		 exch(1, N--);              
		   
		 pq[N+1] = null;            
		      
		 sink(1);
		 		      
		 return max;
	}
	
	/**
	 * Tomado del libro Algorithms, capítulo 2.4
	 */
	public boolean esVacia()
	{
		return N == 0;
	}
	
	/**
	 * Tomado del libro Algorithms, capítulo 2.4
	 */
	public boolean less(int i, int j)
	{
		return pq[i].compareTo(pq[j]) < 0;  
	}
	
	/**
	 * Tomado del libro Algorithms, capítulo 2.4
	 */
	public void exch(int i, int j)
	{
		T t = pq[i]; 
		pq[i] = pq[j]; 
		pq[j] = t; 
	}

	/**
	 * Tomado del libro Algorithms, capítulo 2.4
	 */
	public void swim(int k) 
	{   
		while (k > 1 && less(k/2, k))   
		{      
			exch(k/2, k);     
			k = k/2;   
		} 
	}

	/**
	 * Tomado del libro Algorithms, capítulo 2.4
	 */
	public void sink(int k) 
	{   
		while (2*k <= N)   
		{      
			int j = 2*k;      
			if (j < N && less(j, j+1)) 
				j++;      
			if (!less(k, j)) 
				break;      
			exch(k, j);      
			k = j;   
		} 
	}
	
	public int darNumElementos()
	{
		return N;
	}
}
