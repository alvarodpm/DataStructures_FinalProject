package view;

import java.util.Iterator;

import model.data_structures.Edge;
import model.data_structures.Vertex;
import model.logic.Interseccion;
import model.logic.Zona;

public class MVCView 
{
	/**
	 * Metodo constructor
	 */
	public MVCView()
	{

	}

	public void printMenu()
	{
		System.out.println("\n1. Cargar el grafo de la malla vial");
		System.out.println("2. Crear el archivo .json en la carpeta data");
		System.out.println("3. Cargar el grafo del archivo JSON");
		System.out.println("4. Consultar el id del vértice más cercano a una latitud y longitud dada");
		System.out.println("\nRequerimientos - Parte A");
		System.out.println("5. Encontrar el	camino de costo mínimo (tiempo) entre dos localizaciones");
		System.out.println("6. Determinar los n	vértices con menor velocidad promedio en la ciudad de Bogotá");
		System.out.println("7. Calcular un MST con criterio distancia, usando Prim, aplicado al subgrafo más grande");
		System.out.println("\nRequerimientos - Parte B");
		System.out.println("8. Encontrar el	camino de costo mínimo (distancia) entre dos localizaciones");
		System.out.println("9. A partir de unas coordenadas, buscar cuáles vértices son alcanzables para un tiempo");
		System.out.println("10. Calcular un MST con criterio distancia, usando Kruskal, aplicado al subgrafo más grande");
		System.out.println("\nRequerimientos - Parte C");
		System.out.println("11. Construir un nuevo grafo simplificado");
		System.out.println("12. Calcular el camino de de costo mínimo, usando Dijkstra, basado en el tiempo promedio entre dos zonas");
		System.out.println("13. De una zona origen, calcular los caminos de menor longitud a todas sus zonas alcanzables");
		System.out.println("14. Exit");
		System.out.println("Dar el numero de opcion a resolver, luego oprimir tecla Return: (e.g., 1):");
	}

	public void reportarCargaDeUnArchivo(int cantidadVertices, int cantidadArcos, int cantidadComponentesConectadas, Iterator<Integer> cantidadVerticesEnLas5ComponentesConectadasMasGrandes)
	{
		System.out.println("\nSe cargaron " + cantidadVertices + " vértices");
		System.out.println("\nSe cargaron " + cantidadArcos + " arcos");
		System.out.println("\nEn el grafo hay " + cantidadComponentesConectadas + " componentes conectadas");

		System.out.println("\nLas 5 componentes conectadas más grandes tienen las siguientes cantidades de vértices:");

		while(cantidadVerticesEnLas5ComponentesConectadasMasGrandes.hasNext())
		{
			System.out.println("\n" + cantidadVerticesEnLas5ComponentesConectadasMasGrandes.next());
		}
	}

	public void reportarCantdadComponentesConectadas(int cantidadComponentesConectadas) 
	{
		System.out.println("\nLa malla vial tiene " + cantidadComponentesConectadas + " componentes conectadas");
	}

	public void reportarIdVerticeMasCercano(int id)
	{
		System.out.println("\nEl id del vértice más cercano es: " + id);
	}

	public void reportarCaminoCostoMinimoTiempo(Iterable<Edge<Integer,Interseccion>> iterable)
	{

	}

	public void reportarNVerticesMenorVelocidadPromedio(Iterable<Vertex<Integer,Interseccion>> iterable, int cantidadVertices)
	{

	}

	public void reportarMSTSegunDistanciaDeLaCCMasGrandeUsandoPrim(Iterable<Edge<Integer,Interseccion>> iterable)
	{

	}

	public void reportarCaminoCostoMinimoDistancia(Iterable<Vertex<Integer,Interseccion>> iterable, double tiempoTotal, double distanciaTotal)
	{
		int contador = 0;

		System.out.println("\nEl camino de costo mínimo según el costo de distancia entre las dos intersecciones es:");

		for(Vertex<Integer, Interseccion> vertexActual : iterable)
		{
			contador++;

			System.out.println("\nID: " + vertexActual.key + ", Latitud: " + vertexActual.info.getLatitud() + ", Longitud: " + vertexActual.info.getLongitud());
		}

		System.out.println("\nEn total se recorren " + contador + " intersecciones");

		System.out.println("\nEl tiempo estimado es " + tiempoTotal);

		System.out.println("\nLa distancia total recorrida es " + distanciaTotal);
	}

	public void reportarVerticesAlcanzablesSegunTiempo(Iterable<Vertex<Integer,Interseccion>> iterable)
	{
		System.out.println("\nLos vértices alcanzables en ese tiempo son:");

		for(Vertex<Integer, Interseccion> vertexActual : iterable)
		{			
			System.out.println("\nID: " + vertexActual.key + ", Latitud: " + vertexActual.info.getLatitud() + ", Longitud: " + vertexActual.info.getLongitud());
		}
	}

	public void reportarMSTSegunDistanciaDeLaCCMasGrandeUsandoKruskal(Iterable<Edge<Integer,Interseccion>> iterable, double tiempo, int cantVertices, double costoDistancia)
	{
		System.out.println("\nLos arcos que hacen parte del MST son:");

		for(Edge<Integer, Interseccion> edge : iterable)
		{
			System.out.println(edge.v1.info.getId() + " --------- " + edge.v2.info.getId());
		}

		System.out.println("\nSe tardó " + tiempo + " milisegundos en calcular el MST");

		System.out.println("\nEn la componente hay " + cantVertices + " vértices");

		System.out.println("\nEl costo total del MST es " + costoDistancia);
	}

	public void reportarConstruccionGrafoSimplificado(int cantidadArcos, int cantidadVertices)
	{
		System.out.println("\nSe construyó el grafo simplificado");
		System.out.println("\nEl nuevo grafo tiene " + cantidadArcos + " arcos");
		System.out.println("\nEl nuevo grafo tiene " + cantidadVertices + " vértices");
	}

	public void reportarCaminoCostoMinimoTiempoEntreDosZonas(Iterable<Vertex<Integer,Interseccion>> iterable, double costoTiempo, double tiempoDelAlgoritmo)
	{
		int contador = 0;

		System.out.println("\nEl camino de costo mínimo según el costo de tiempo entre las dos zonas es:");

		for(Vertex<Integer, Interseccion> vertexActual : iterable)
		{
			contador++;

			System.out.println("\nMOVEMENT_ID: " + vertexActual.info.getMOVEMENT_ID());
		}

		System.out.println("\nEn total se recorren " + contador + " zonas");

		System.out.println("\nEl tiempo estimado es " + costoTiempo);
		
		System.out.println("\nEl tiempo que tarda el algoritmo en encontrar la solución es " + tiempoDelAlgoritmo);

	}

	public void reportarCaminoAZonaMasDistanteDesdeUnaZona(Iterable<Edge<Integer,Interseccion>> iterable)
	{

	}

	public void printMessage(String message)
	{
		System.out.println(message);
	}
}
