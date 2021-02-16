package model.data_structures;

import java.util.Iterator;

import model.data_structures.HashTableSC.EntryDupla;

//Algoritmos basados en el capítulo 3.4 del libro Algorithms

/**
 * Clase que representa un HashTable genérico que utiliza Separate Chaining para solucionar las colisiones
 * Los métodos de esta clase fueron tomados del libro Algorithms, capítulo 3.4
 */
public class HashTableSC<K extends Comparable<K>, V> implements IHashTable<K, V> {

	//Clase interna que representa las duplas Key y Values
	public class EntryDupla
	{
		private K key;
		private ListaEncadenada<V> values;
		private EntryDupla nextEntry;
		private boolean marcada = false;

		public EntryDupla(K key, V value)
		{
			this.key = key;
			this.values = new ListaEncadenada<V>(value);
			this.nextEntry = null;
		}


		public void addValue(V newValue)
		{
			values.addFirst(newValue);
		}

		public K getKey() {
			return key;
		}

		public Iterator<V> getValues() {
			return  values.iterator();
		}

		public EntryDupla getNextEntry() {
			return nextEntry;
		}

		public void setNextEntry(EntryDupla nextEntry) {
			this.nextEntry = nextEntry;
		}
		
		public void marcar()
		{
			marcada = true;
		}
		
		public boolean estaMarcada()
		{
			return marcada;
		}
	}

	//Atributos

	/**
	 * Cantidad de cadenas de Entrys que tiene el HashTable
	 */
	private int numOfChains;

	/**
	 * Array de cadenas de Entrys
	 */
	private EntryDupla[] entrys;

	/**
	 * Cantidad de entrys que tiene el HashTable hasta el momento
	 */
	private int numOfEntrys;

	/**
	 * Parámetro con el número máximo del factor de carga del HashTable
	 */
	private static final double MAX_LOAD_FACTOR = 5.0;

	//Constructor
	public HashTableSC(int numOfChains) {
		this.numOfChains = numOfChains;
		this.numOfEntrys = 0;
		entrys = new HashTableSC.EntryDupla[this.numOfChains];
	}

	//Métodos

	@Override
	public void putInSet(K key, V value) {
		int i = hash(key);
		if(entrys[i] == null)
		{
			entrys[i] = new EntryDupla(key, value);		
			numOfEntrys++;
			if(darLoadFactor() > MAX_LOAD_FACTOR)
				rehash();
		}
		else
		{
			boolean loAgregue = false;
			EntryDupla actual = entrys[i];
			while(actual != null && !loAgregue)
			{
				if (key.equals(actual.getKey())) 
				{ 
					actual.addValue(value); 
					loAgregue = true; 
				}

				actual = actual.getNextEntry();
			}
			if(!loAgregue)
			{
				EntryDupla nueva = new EntryDupla(key, value);
				nueva.setNextEntry(entrys[i]);
				entrys[i] = nueva;
				numOfEntrys++;
				if(darLoadFactor() > MAX_LOAD_FACTOR)
					rehash();
			}
		}
	}

	@Override
	public Iterator<V> getSet(K key) {
		int i = hash(key);
		if(entrys[i] == null)
			return new IteradorLista<V>(null);
		
		for (EntryDupla actual = entrys[i]; actual!=null; actual = actual.getNextEntry())
			if (key.equals(actual.getKey())) return actual.getValues();
		
		return new IteradorLista<V>(null);
	}

	@Override
	public Iterator<V> deleteSet(K key) 
	{
		int i = hash(key);
		Iterator<V> respuesta = new IteradorLista<V>(null);

		EntryDupla actual = entrys[i];
		if(actual == null)
			return respuesta;
		
		if(actual.getKey().equals(key))
		{
			respuesta = actual.getValues();
			entrys[i] = entrys[i].getNextEntry();
			numOfEntrys--;
			if(darLoadFactor() > MAX_LOAD_FACTOR)
				rehash();
			return respuesta;
		}
		else
		{
			while(actual.getNextEntry() != null)
			{
				if(actual.getNextEntry().getKey().equals(key))
				{
					respuesta = actual.getNextEntry().getValues();
					actual.setNextEntry(actual.getNextEntry().getNextEntry());
					numOfEntrys--;
					if(darLoadFactor() > MAX_LOAD_FACTOR)
						rehash();
					return respuesta;
				}
				actual = actual.getNextEntry();
			}
			return respuesta;
		}	
	}

	@Override
	public Iterator<K> keys() {
		ListaEncadenada<K> lista = new ListaEncadenada<K>();

		for(int i = 0; i <= numOfChains-1; i++)
		{
			EntryDupla actual = entrys[i];

			while(actual != null)
			{
				lista.addFirst(actual.getKey());
				actual = actual.getNextEntry();
			}
		}

		return lista.iterator();
	}
	
	@Override
	public void rehash() {
		EntryDupla[] temp = entrys;
		numOfChains *= 2;
		entrys = new HashTableSC.EntryDupla[numOfChains];
		numOfEntrys = 0;
		
		for(int i = 0; i <= temp.length-1; i++)
		{
			EntryDupla actual = temp[i];

			while(actual != null)
			{
				Iterator<V> iterador = actual.getValues();
				putInSet(actual.getKey(), iterador.next());
				EntryDupla elQueAcaboDeAgregar = getEntry(actual.getKey());
				while(iterador.hasNext())
				{
					elQueAcaboDeAgregar.addValue(iterador.next());
				}

				actual = actual.getNextEntry();
			}
		}
	}


	@Override
	public int hash(K key) 
	{
		return (key.hashCode() & 0x7fffffff) % numOfChains;
	}


	@Override
	public double darLoadFactor()
	{
		return ((double)numOfEntrys)/((double)numOfChains);
	}

	public EntryDupla getEntry(K key)
	{
		int i = hash(key);

		EntryDupla elBuscado = null;
		EntryDupla actual = entrys[i];
		while(actual != null && elBuscado == null)
		{
			if (key.equals(actual.getKey())) 
			{ 
				elBuscado = actual;
			}

			actual = actual.getNextEntry();
		}

		return elBuscado;
	}

	public int getNumOfChains()
	{
		return numOfChains;
	}

	public int getNumOfEntrys()
	{
		return numOfEntrys;
	}
	
	public V getLastValue(K key)
	{
		int i = hash(key);
		if(entrys[i] == null)
			return null;
		
		for (EntryDupla actual = entrys[i]; actual!=null; actual = actual.getNextEntry())
			if (key.equals(actual.getKey())) return actual.values.getFirstItem();
		
		return null;
	}
}
