package test.data_structures;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import model.data_structures.UndirectedGraph;
import model.data_structures.Vertex;

public class UndirectedGraphTest {

	private UndirectedGraph<Integer, String> graph;

	@Before
	public void setUpEscenario1()
	{
		graph = new UndirectedGraph<Integer, String>(10);

		//Se crean los vértices

		graph.addVertex(1, "Uno");

		graph.addVertex(2, "Dos");

		graph.addVertex(3, "Tres");

		graph.addVertex(4, "Cuatro");

		graph.addVertex(5, "Cinco");

		graph.addVertex(6, "Seis");

		graph.addVertex(7, "Siete");

		graph.addVertex(8, "Ocho");

		//Se crean los arcos, quedando 4 componentes conectadas

		graph.addEdge(1, 2, 1.0, -1, -1);

		graph.addEdge(3, 4, 1.0, -1, -1);

		graph.addEdge(5, 6, 1.0, -1, -1);

		graph.addEdge(7, 8, 1.0, -1, -1);
	}

	@Test
	public void testAddEdge()
	{
		graph.addEdge(2, 3, 1, -1, -1);

		assertTrue("El arco debería existir", graph.existeArco(2, 3));

		graph.addEdge(6, 7, 1, -1, -1);

		assertTrue("El arco debería existir", graph.existeArco(7, 6));
	}

	@Test
	public void testGetInfoVertex()
	{
		assertEquals("La información del vértice no es la esperada", "Uno", graph.getInfoVertex(1));

		assertEquals("La información del vértice no es la esperada", "Dos", graph.getInfoVertex(2));
	}

	@Test
	public void testSetInfoVertex()
	{
		graph.setInfoVertex(1, "One");

		assertEquals("La información del vértice no es la esperada", "One", graph.getInfoVertex(1));

		graph.setInfoVertex(2, "Deux");

		assertEquals("La información del vértice no es la esperada", "Deux", graph.getInfoVertex(2));
	}

	@Test
	public void testGetCostArc()
	{
		assertEquals("El costo del arco no es el esperado", (Double) 1.0, (Double) graph.getCostArc(1, 2));

		assertEquals("El costo del arco no es el esperado", (Double) 1.0, (Double) graph.getCostArc(3, 4));
	}

	@Test
	public void testSetCostArc()
	{
		graph.setCostArc(1, 2, 5.2);
		assertEquals("El costo del arco no es el esperado", (Double) 5.2, (Double) graph.getCostArc(1, 2));

		graph.setCostArc(4, 3, 8.6);
		assertEquals("El costo del arco no es el esperado", (Double) 8.6, (Double) graph.getCostArc(3, 4));
	}

	@Test
	public void testAddVertex()
	{
		graph.addVertex(9, "Nueve");

		graph.addVertex(10, "Diez");

		assertTrue("El vértice debería existir", graph.existeVertice(9));
		assertTrue("El vértice debería existir", graph.existeVertice(10));
	}

	@Test
	public void testAdj()
	{
		graph.addEdge(2, 3, 1, -1, -1);
		graph.addEdge(4, 2, 1, -1, -1);

		Iterator<Integer> iteradorIDs = graph.adj(2).iterator();

		assertNotNull("Debería existir este vértice adyacente", iteradorIDs.next());
		assertNotNull("Debería existir este vértice adyacente", iteradorIDs.next());
		assertNotNull("Debería existir este vértice adyacente", iteradorIDs.next());

		try
		{
			iteradorIDs.next();
			fail("Debería arrojar una excepción");
		}
		catch (Exception e) {

		}
	}

	@Test
	public void testUncheck()
	{
		graph.getVertex(1).check();
		graph.getVertex(2).check();

		assertTrue("El vértice debería estar marcado", graph.getVertex(1).isChecked());
		assertTrue("El vértice debería estar marcado", graph.getVertex(2).isChecked());

		graph.uncheck();

		assertFalse("El vértice no debería estar marcado", graph.getVertex(1).isChecked());
		assertFalse("El vértice no debería estar marcado", graph.getVertex(2).isChecked());
	}

	@Test
	public void testDFS()
	{
		graph.addEdge(2, 3, 1, -1, -1);
		graph.addEdge(4, 2, 1, -1, -1);

		graph.dfs(2, 2);

		assertTrue("El vértice debería estar marcado", graph.getVertex(1).isChecked());
		assertTrue("El vértice debería estar marcado", graph.getVertex(2).isChecked());
		assertTrue("El vértice debería estar marcado", graph.getVertex(3).isChecked());
		assertTrue("El vértice debería estar marcado", graph.getVertex(4).isChecked());

		assertEquals("La componente del vértice no es la esperada", 2, graph.getVertex(1).componente);
		assertEquals("La componente del vértice no es la esperada", 2, graph.getVertex(2).componente);
		assertEquals("La componente del vértice no es la esperada", 2, graph.getVertex(3).componente);
		assertEquals("La componente del vértice no es la esperada", 2, graph.getVertex(4).componente);
	}

	@Test
	public void testCC()
	{
		int cantidadComponentes = graph.cc();

		assertEquals("La cantidad de componentes no es la esperada", 4, cantidadComponentes);

		graph.addEdge(2, 3, 1, -1, -1);

		cantidadComponentes = graph.cc();

		assertEquals("La cantidad de componentes no es la esperada", 3, cantidadComponentes);
	}

	@Test
	public void testGetCC()
	{
		graph.addEdge(2, 3, 1, -1, -1);

		graph.cc();

		try {
			Iterator<Integer> iterator = graph.getCC(1).iterator();

			assertNotNull("Debería llegar a este vértice", iterator.next());
			assertNotNull("Debería llegar a este vértice", iterator.next());
			assertNotNull("Debería llegar a este vértice", iterator.next());

			try
			{
				iterator.next();
				fail("Debería arrojar una excepción");
			}
			catch (Exception e) {

			}

		} catch (Exception e) {
			fail("No debería arrojar la excepción");
		}
	}
	
	@Test
	public void cheapestDistancePathTest()
	{
		graph.addEdge(1, 3, 10, 0, 0);
		graph.addEdge(3, 5, 10, 0, 0);
		graph.addEdge(5, 7, 10, 0, 0);
		graph.addEdge(2, 4, 1, 0, 0);
		graph.addEdge(4, 6, 1, 0, 0);
		graph.addEdge(6, 8, 1, 0, 0);
		
		Iterator<Vertex<Integer, String>> iterator;
		try {
			
			iterator = graph.shortestPathDijkstraCostDistance(1, 8).iterator();
			
			assertEquals("No es el vértice que se eseperaba", (Integer) 1, iterator.next().key);
			assertEquals("No es el vértice que se eseperaba", (Integer) 2, iterator.next().key);
			assertEquals("No es el vértice que se eseperaba", (Integer) 4, iterator.next().key);
			assertEquals("No es el vértice que se eseperaba", (Integer) 6, iterator.next().key);
			assertEquals("No es el vértice que se eseperaba", (Integer) 8, iterator.next().key);
			
		} catch (Exception e) {
			fail("No debería arrojar excepción");
		}
	}
}
