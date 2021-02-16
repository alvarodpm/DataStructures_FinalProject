package model.data_structures;

/**
 * 2019-01-23
 * Estructura de Datos Arreglo Dinamico de Strings.
 * El arreglo al llenarse (llegar a su maxima capacidad) debe aumentar su capacidad.
 * @author Fernando De la Rosa
 * @param <T>
 *
 */
public class ArregloDinamico<T extends Comparable<T>> implements IArregloDinamico<T>{
	/**
	 * Capacidad maxima del arreglo
	 */
	private int tamanoMax;
	/**
	 * Numero de elementos presentes en el arreglo (de forma compacta desde la posicion 0)
	 */
	private int tamanoAct;
	/**
	 * Arreglo de elementos de tamaNo maximo
	 */
	private T elementos[ ];

	/**
	 * Construir un arreglo con la capacidad maxima inicial.
	 * @param max Capacidad maxima inicial
	 */
	public ArregloDinamico( int max )
	{
		elementos = (T []) new Comparable [max];
		tamanoMax = max;
		tamanoAct = 0;
	}

	public void agregar( T dato )
	{
		if ( tamanoAct == tamanoMax )
		{  // caso de arreglo lleno (aumentar tamaNo)
			tamanoMax = 2 * tamanoMax;
			T [ ] copia = elementos;
			elementos = (T [ ]) new Comparable[tamanoMax];
			for ( int i = 0; i < tamanoAct; i++)
			{
				elementos[i] = copia[i];
			} 
		}	
		elementos[tamanoAct] = dato;
		tamanoAct++;
	}

	public int darCapacidad() {
		return tamanoMax;
	}

	public int darTamano() {
		return tamanoAct;
	}

	public T darElemento(int i) {
		// TODO implementar
		return elementos[i];
	}

	public T buscar(T dato) {
		// TODO implementar
		// Recomendacion: Usar el criterio de comparacion natural (metodo compareTo()) definido en Strings.
		for(int i = 0; i < tamanoAct; i++)
		{
			T actual = elementos[i];
			if(actual.compareTo(dato) == 0)
				return actual;
		}
		return null;
	}
	
	public void modificarElemento(int index, T elemento)
	{
		elementos[index] = elemento;
	}

	public T eliminar(T dato) {
		// TODO implementar
		// Recomendacion: Usar el criterio de comparacion natural (metodo compareTo()) definido en Strings.
		for(int i = 0; i < tamanoAct; i++)
		{
			if(elementos[i].compareTo(dato) == 0)
			{
				T aDevolver = elementos[i];
				for(int j = i; j < tamanoAct; j++)
				{
					elementos[j] = elementos[j+1];
				}
				return aDevolver;
			}
		}
		return null;
	}
	
	public Comparable<T>[] darElementos()
	{
		Comparable<T>[] losElementos = (T []) new Comparable [tamanoAct];
		
		for(int i = 0; i < tamanoAct; i++)
		{
			losElementos[i] = elementos[i];
		}
		
		return losElementos;
		
	}
}
