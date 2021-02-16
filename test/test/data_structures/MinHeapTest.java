package test.data_structures;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import model.data_structures.MinHeapCP;

public class MinHeapTest {
	
	private MinHeapCP<Integer, String> pq;
	
	@Before
	public void setUpEscenario1()
	{
		pq = new MinHeapCP<Integer, String>(10);
		
		pq.agregar(1, "Uno");
		
		pq.agregar(2, "Dos");
		
		pq.agregar(3, "Tres");
		
		pq.agregar(4, "Cuatro");
		
		pq.agregar(5, "Cinco");
		
		pq.agregar(6, "Seis");
		
		pq.agregar(7, "Siete");
		
		pq.agregar(8, "Ocho");
		
		pq.agregar(9, "Nueve");
		
		pq.agregar(10, "Diez");
	}
	
	@Test
	public void testSizeYAgregar()
	{
		assertEquals("El tamaño del Heap no es correcto", 10, pq.darNumElementos());
		
		pq.agregar(11, "Once");
		
		assertEquals("El tamaño del Heap no es correcto", 11, pq.darNumElementos());
	}
	
	@Test
	public void sacarMinTest()
	{
		assertEquals("El elemento menor no es el esperado", "Uno", pq.sacarMin());
		
		assertEquals("El elemento menor no es el esperado", "Dos", pq.sacarMin());
	}

}
