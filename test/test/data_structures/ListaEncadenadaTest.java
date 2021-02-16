package test.data_structures;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

import model.data_structures.IteradorLista;
import model.data_structures.ListaEncadenada;

public class ListaEncadenadaTest {
	
	private ListaEncadenada<Integer> lista;
	
	private static final int[] ARREGLO_SCENARIO_1 = {1, 2, 3, 4, 5, 6, 7, 8, 9};
	
	@Before
	public void setUpEscenario1()
	{
		lista = new ListaEncadenada<Integer>();
		for(int actual : ARREGLO_SCENARIO_1)
		{
			lista.addLast(actual);
		}
	}
	
	@Test
	public void testSize()
	{
		assertEquals("El tama�o de la lista no es correcto", 9, lista.size());
		
		lista.addLast(10);
		lista.addLast(11);
		
		assertEquals("El tama�o de la lista no es correcto", 11, lista.size());
		
		lista.clear();
		
		assertEquals("El tama�o de la lista no es correcto", 0, lista.size());
	}
	
	@Test
	public void testIterator()
	{
		lista.clear();
		IteradorLista<Integer> iterador = (IteradorLista<Integer>) lista.iterator();
		assertFalse("Con la lista vac�a dice que puede avanzar al siguiente elemento", iterador.hasNext());
		
		try
		{
			iterador.next();
			fail("Si no tiene elementos, no deber�a avanzar");
		}
		catch (NoSuchElementException e) {
			//No avanz� porque no hay elementos
		}
		
		lista.addLast(5);
		lista.addLast(6);
		
		iterador = (IteradorLista<Integer>) lista.iterator();
		
		assertTrue("Con elementos el la lista dice que no puede avanzar al siguiente elemento", iterador.hasNext());
		
		try
		{
			Integer siguiente = iterador.next();
			assertTrue("Estando en el primer elemento de la lista, dice que no puede avanzar al segundo", iterador.hasNext());
			assertNotNull(siguiente);
			siguiente = iterador.next();
		}
		catch(NoSuchElementException e)
		{
			fail("No avanz� el iterador, dice que no hay m�s elementos.");
		}
		
		assertFalse("Estando en el �ltimo elemento de la lista dice que no puede avanzar.", iterador.hasNext());
		
		try
		{
			iterador.next();
			fail("Si est� en el �ltimo elemento, no deber�a avanzar.");
		}
		catch(NoSuchElementException e)
		{
			//No avanz� proque no hay m�s elementos.
		}
	}
	
	@Test
	public void testAddFirstYLast()
	{
		lista.addFirst(15);
		int primero = 15;
		int resultado = lista.get(0);
		assertEquals("El primer n�mero no es el esperado", primero, resultado);
		
		lista.addLast(18);
		int ultimo = 18;
		int resultado2 = lista.get(lista.size() -1);
		assertEquals("El n�mero no es el esperado", ultimo, resultado2);
		
	}
	
	@Test
	public void testRemoveFirstYPorIndex()
	{
		lista.removeFirst();
		int primero = 2;
		int resultado = lista.get(0);
		assertEquals("El primer elemento no es el que deber�a ser", primero, resultado);
	}	
}
