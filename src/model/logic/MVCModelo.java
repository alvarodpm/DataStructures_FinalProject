package model.logic;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.opencsv.CSVReader;

import model.data_structures.Edge;
import model.data_structures.HashTableSC;
import model.data_structures.Haversine;
import model.data_structures.IUndirectedGraph;
import model.data_structures.ListaEncadenada;
import model.data_structures.MaxHeapCP;
import model.data_structures.PrimMst;
import model.data_structures.UndirectedGraph;
import model.data_structures.Vertex;

/**
 * Definicion del modelo del mundo
 *
 */
public class MVCModelo {

	/**
	 * Atributos del modelo del mundo
	 */
	private UndirectedGraph<Integer, Interseccion> grafo;
	

	/**
	 * Constante con la ruta del archivo
	 */
	private static final String RUTA_ARCOS = "./data/bogota_arcos.txt/";
	private static final String RUTA_VERTICES = "./data/bogota_vertices.txt/";
	private static final String RUTA_JSON = "./data/myJSON.json/";
	private static final String RUTA_VIAJES_WEEKLY = "./data/bogota-cadastral-2018-1-WeeklyAggregate.csv";


	/**
	 * Constructor del modelo del mundo
	 */
	public MVCModelo()
	{
		grafo = new UndirectedGraph<Integer, Interseccion>(12000);
	}


	public void cargarMallaVial() throws Exception
	{
		try
		{ 
			CSVReader reader = new CSVReader(new FileReader(RUTA_VERTICES),';');
			String[] nextLine;
			reader.readNext();

			while((nextLine = reader.readNext()) != null)   
			{
				Interseccion nueva = new Interseccion(Integer.parseInt(nextLine[0]), Double.parseDouble(nextLine[1]), Double.parseDouble(nextLine[2]), Integer.parseInt(nextLine[3]));
				grafo.addVertex(nueva.id, nueva);
			}
		}
		catch (FileNotFoundException e) {
			throw new Exception("No se encontró la ruta del archivo de los vértices");
		}


		try
		{
			CSVReader reader = new CSVReader(new FileReader(RUTA_ARCOS),'\n');
			String[] nextLine;


			while((nextLine = reader.readNext()) != null)   
			{
				String nextLine2[] = nextLine[0].split(" ");
				int idVertexOrigen = Integer.parseInt(nextLine2[0]);

				Vertex<Integer, Interseccion> vertexOrigen = grafo.getVertex(idVertexOrigen);				

				if(vertexOrigen != null)
				{

					for(int i = 1; i < nextLine2.length; i++)
					{
						Vertex<Integer, Interseccion> vertexDestino = grafo.getVertex(Integer.parseInt(nextLine2[i]));

						if(vertexDestino != null)
						{
							double costoDistancia = Haversine.distance(vertexOrigen.info.latitud, vertexOrigen.info.longitud, vertexDestino.info.latitud, vertexDestino.info.longitud);

							grafo.addEdge(vertexOrigen.info.id, vertexDestino.info.id, costoDistancia, -1, -1);
						}

					}
				}
			}

			asignarOtrosDosCostos();
		}
		catch (FileNotFoundException e) {
			throw new Exception("No se encontró la ruta del archivo de los arcos");
		}
	}

	public void crearArchivoJSON()
	{
		JSONObject jsonGraph = new JSONObject();
		JSONArray intersecciones = new JSONArray();
		JSONArray adyacencias = new JSONArray();

		Iterator<Integer> keys = grafo.keys();


		int contadorKeys = 0;

		while(keys.hasNext())
		{

			Vertex<Integer, Interseccion> vertexActual = grafo.getVertex(keys.next());
			Interseccion interActual = vertexActual.info;

			JSONObject vertex = new JSONObject();
			JSONObject conjuntoAdyacencias = new JSONObject();
			JSONArray adyacentes = new JSONArray();

			vertex.put("id", interActual.id);
			vertex.put("longitud", interActual.longitud);
			vertex.put("latitud", interActual.latitud);
			vertex.put("MOVEMENT_ID", interActual.MOVEMENT_ID);

			Iterator<Edge<Integer, Interseccion>> arcos = vertexActual.edges().iterator();

			conjuntoAdyacencias.put("id", interActual.id);

			while(arcos.hasNext())
			{
				Edge<Integer, Interseccion> arcoActual = arcos.next();

				if(arcoActual.v1 == vertexActual)
					adyacentes.add(arcoActual.v2.info.id);
				else
					adyacentes.add(arcoActual.v1.info.id);
			}

			conjuntoAdyacencias.put("adyacentes", adyacentes);

			intersecciones.add(vertex);
			adyacencias.add(conjuntoAdyacencias);
			contadorKeys ++;
		}

		jsonGraph.put("intersecciones", intersecciones);
		jsonGraph.put("adyacencias", adyacencias);


		try
		{
			FileWriter file = new FileWriter(RUTA_JSON);
			file.write(jsonGraph.toJSONString());
			file.flush();

		}catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void cargarArchivoJSON() throws Exception {

		grafo = new UndirectedGraph<Integer, Interseccion>(12000);

		JSONParser parser = new JSONParser();

		FileReader reader = new FileReader(RUTA_JSON);

		JSONObject jsonObject = (JSONObject) parser.parse(reader);

		JSONArray intersecciones = (JSONArray) jsonObject.get("intersecciones");

		Iterator<JSONObject> iteradorIntersecciones = intersecciones.iterator();

		//System.out.println("1");
		while(iteradorIntersecciones.hasNext())
		{
			JSONObject interseccionActual = (JSONObject) iteradorIntersecciones.next();
			//System.out.println("2");
			double longitud = Double.parseDouble( interseccionActual.get("longitud").toString());
			//System.out.println("3");
			double latitud = Double.parseDouble(interseccionActual.get("latitud").toString());
			//System.out.println("4");
			long movementLong = (Long) interseccionActual.get("MOVEMENT_ID");
			int MOVEMENT_ID = (int) movementLong;
			//System.out.println("5");
			long idLong = (Long) interseccionActual.get("id");
			int id = (int) idLong;
			//System.out.println("6");

			Interseccion nuevaInterseccion = new Interseccion(id, longitud, latitud, MOVEMENT_ID);

			grafo.addVertex(id, nuevaInterseccion);
		}

		JSONArray adyacencias = (JSONArray) jsonObject.get("adyacencias");
		//System.out.println("7");
		Iterator<JSONObject> iteradorAdyacencias = adyacencias.iterator();
		//System.out.println("8");
		while(iteradorAdyacencias.hasNext())
		{
			JSONObject conjuntoAdyacenciasActual = (JSONObject) iteradorAdyacencias.next();
			//System.out.println("9");

			long idLong = (Long) conjuntoAdyacenciasActual.get("id");
			int id = (int) idLong;

			if(grafo.getVertex(id) != null)
			{	
				Interseccion interseccion1 = grafo.getVertex(id).info;
				//System.out.println("ID interseccion 1: " + id );
				//System.out.println("10");
				JSONArray adyacentes = (JSONArray) conjuntoAdyacenciasActual.get("adyacentes");
				//System.out.println("11");			

				for(Object actual : adyacentes)
				{
					//System.out.println("Entro al for");

					long idActualLong = (Long) actual;
					int idActual = (int) idActualLong;

					//System.out.println("ID actual: " + idActual);

					//System.out.println("12");

					if(grafo.getVertex(idActual) != null)
					{
						Interseccion interseccion2 = grafo.getVertex(idActual).info;
						//System.out.println("13");
						double costoDistancia = Haversine.distance(interseccion1.latitud, interseccion1.longitud, interseccion2.latitud, interseccion2.longitud);
						grafo.addEdge(interseccion1.id, interseccion2.id, costoDistancia, -1, -1);
						//System.out.println("14");
					}
				}
			}
		}	

		asignarOtrosDosCostos();
	}


	public int cantidadArcos()
	{
		return grafo.E();
	}

	public int cantidadVertices()
	{
		return grafo.V();
	}

	public int cantidadComponentesConectadas()
	{
		return grafo.cc();
	}


	public int darIndiceVertexMasCercano(double pLatitud, double pLongitud)
	{
		Iterator<Integer> iteradorIds = grafo.keys();

		int idMasCercano = -1;
		double distanciaMasCorta = Integer.MAX_VALUE;

		while(iteradorIds.hasNext())
		{
			int idActual = iteradorIds.next();
			Interseccion interseccionActual = grafo.getInfoVertex(idActual);

			double distanciaActual = Haversine.distance(pLatitud, pLongitud, interseccionActual.latitud, interseccionActual.longitud);

			if(distanciaActual < distanciaMasCorta)
			{
				distanciaMasCorta = distanciaActual;

				idMasCercano = idActual;
			}
		}

		return idMasCercano;
	}

	public Interseccion darInterseccionMasCercana(double pLatitud, double pLongitud)
	{
		Iterator<Integer> iteradorIds = grafo.keys();

		int idMasCercano = -1;
		double distanciaMasCorta = Integer.MAX_VALUE;

		while(iteradorIds.hasNext())
		{
			int idActual = iteradorIds.next();
			Interseccion interseccionActual = grafo.getInfoVertex(idActual);

			double distanciaActual = Haversine.distance(pLatitud, pLongitud, interseccionActual.latitud, interseccionActual.longitud);
			if(distanciaActual < distanciaMasCorta)
			{
				distanciaMasCorta = distanciaActual;

				idMasCercano = idActual;
			}
		}

		return grafo.getVertex(idMasCercano).info;
	}

	public Iterator<Integer> darCantidadVerticesDeLasNCCMasGrandes(int cantidadComponentes)
	{
		return grafo.darCantidadVerticesDeLasNCCMasGrandes(cantidadComponentes);
	}


	private void asignarOtrosDosCostos()
	{
		HashTableSC<String, Double> table = new HashTableSC<String, Double>(503);

		try
		{
			CSVReader reader = new CSVReader(new FileReader(RUTA_VIAJES_WEEKLY)); 
			String[] nextLine;
			reader.readNext();
			while((nextLine = reader.readNext()) != null)
			{
				table.putInSet(nextLine[0] + "-" + nextLine[1], Double.parseDouble(nextLine[3]));
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		Iterator<Integer> iteradorVertices = grafo.keys();

		while(iteradorVertices.hasNext())
		{
			Vertex<Integer, Interseccion> vertexActual = grafo.getVertex(iteradorVertices.next());

			Iterator<Edge<Integer, Interseccion>> iteradorEdges = vertexActual.edges().iterator();

			while(iteradorEdges.hasNext())
			{
				Edge<Integer, Interseccion> edgeActual = iteradorEdges.next();

				if(edgeActual.costoTiempo < 0)
				{
					Iterator<Double> iteradorTiemposPromedio = table.getSet(edgeActual.v1.info.MOVEMENT_ID + "-" + edgeActual.v2.info.MOVEMENT_ID);

					int contador = 0;
					double sumaTiemposPromedios = 0;

					while(iteradorTiemposPromedio.hasNext())
					{
						contador++;
						sumaTiemposPromedios += iteradorTiemposPromedio.next();
					}

					if(contador > 0)
						edgeActual.setCostoTiempo(sumaTiemposPromedios/contador);
					else if(edgeActual.v1.info.MOVEMENT_ID == edgeActual.v2.info.MOVEMENT_ID)
						edgeActual.setCostoTiempo(10);
					else
						edgeActual.setCostoTiempo(100);
				}

				if(edgeActual.costoVelocidad < 0)
				{
					edgeActual.setCostoVelocidad(edgeActual.costoDistancia/edgeActual.costoTiempo);
				}
			}
		}
	}



	public void graficarMapa(UndirectedGraph<Integer,Interseccion> nuevoGrafo)
	{
		Maps maps = new Maps(nuevoGrafo);
		maps.initFrame("Malla vial de Bogotá");
	}

	public void graficarMapa()
	{
		Maps maps = new Maps((UndirectedGraph) grafo);
		maps.initFrame("Malla vial de Bogotá");
	}

	public void graficarMapa(ListaEncadenada<Vertex<Integer, Interseccion>> lista)
	{
		Maps maps = new Maps((UndirectedGraph) grafo, lista);
		maps.initFrame("Malla vial de Bogotá");
	}

	public void graficarMapa(Iterable<Integer> cc, ListaEncadenada<Edge<Integer, Interseccion>> lista) {
		Maps maps = new Maps((UndirectedGraph) grafo, cc, lista);
		maps.initFrame("Malla vial de Bogotá");
	}

	public void graficarMapa(ListaEncadenada<Vertex<Integer, Interseccion>> lista, double latOrigen, double longOrigen)
	{
		Maps maps = new Maps((UndirectedGraph) grafo, lista, latOrigen, longOrigen);
		maps.initFrame("Malla vial de Bogotá");
	}

	public Iterable<Integer> darCCMasGrande()
	{
		return grafo.darCCMasGrande();
	}

	/**
	 * Requerimiento 1A
	 */
	/*public Iterable<Vertex<Integer,Interseccion>> darCaminoCostoMinimoTiempo(double latOrigen, double longOrigen, double latDestino, double longDestino)
	{
		Integer origen = darIndiceVertexMasCercano(latOrigen, longOrigen);
		Integer destino = darIndiceVertexMasCercano(latDestino, longDestino);
		return grafo.shortestPathDijkstraTime(origen, destino);

	}*/
		/**
		 * Requerimiento 2A
		 */
		public Iterable<Vertex<Integer,Interseccion>> darNVerticesMenorVelocidadPromedio(int cantidadVertices)
		{

			ListaEncadenada< Vertex<Integer, Interseccion>> lista = new ListaEncadenada<Vertex<Integer,Interseccion>>();
			MaxHeapCP<Integer> heap = new MaxHeapCP<>(cantidadVertices);

			Iterator<Integer>k = grafo.keys();
			int key = k.next();
			while(k.hasNext() ){

				Vertex<Integer, Interseccion> v = grafo.getVertex(key);
				int velocidad = 0;
				if(heap.darNumElementos() < cantidadVertices){

					ListaEncadenada<Edge<Integer, Interseccion>> arcos = v.edges();
					lista.addFirst(v);

					for(int i = 0; i< arcos.size(); i++){
						velocidad += arcos.get(i).costoDistancia;

					}
					heap.agregar(velocidad);
				}
				else{
					ListaEncadenada<Edge<Integer, Interseccion>> arcos = v.edges();

					for(int i = 0; i< arcos.size(); i++){
						velocidad += arcos.get(i).costoDistancia;

					}
					if(velocidad < heap.darMax()){

						heap.sacarMax();
						heap.agregar(velocidad);
						v = lista.get(cantidadVertices);
					}

				}
			}
			return lista;
		}

		/**
		 * Requerimiento 3A
		 * @throws Exception
		 */
		public Iterable<Edge<Integer,Interseccion>> darMSTSegunDistanciaDeLaCCMasGrandeUsandoPrim() throws Exception
		{

			PrimMst mst = new PrimMst(grafo);

			return mst.edges();
		}

		/**
		 * Requerimiento 1B
		 * @throws Exception 
		 */
		public ListaEncadenada<Vertex<Integer,Interseccion>> darCaminoCostoMinimoDistancia(double latOrigen, double longOrigen, double latDestino, double longDestino) throws Exception
		{
			Integer keyVertexOrigen = darIndiceVertexMasCercano(latOrigen, longOrigen);

			Integer keyVertexDestino = darIndiceVertexMasCercano(latDestino, longDestino);

			//return grafo.shortestPathDijkstraCostDistance(288, 155835);
			return grafo.shortestPathDijkstraCostDistance(keyVertexOrigen, keyVertexDestino);
		}

		/**
		 * Requerimiento 2B
		 */
		public ListaEncadenada<Vertex<Integer,Interseccion>> darVerticesAlcanzablesSegunTiempo(double latOrigen, double longOrigen, double tiempo)
		{
			Integer keyVertexOrigen = darIndiceVertexMasCercano(latOrigen, longOrigen);

			return grafo.darVerticesAlcanzablesSegunTiempo(keyVertexOrigen, tiempo);
		}

		/**
		 * Requerimiento 3B
		 * @throws Exception 
		 */
		public ListaEncadenada<Edge<Integer,Interseccion>> darMSTSegunDistanciaDeLaCCMasGrandeUsandoKruskal() throws Exception
		{
			return grafo.darMSTKruskalDistancia();
		}

		/**
		 * Requerimiento 1C
		 */
		public UndirectedGraph<Integer, Interseccion> construirGrafoSimplificado()
		{
			UndirectedGraph<Integer, Interseccion> nuevoGrafo = new UndirectedGraph<Integer, Interseccion>(1000);

			Iterator<Integer> keys = grafo.keys();

			while(keys.hasNext())
			{
				Integer kActual = keys.next();

				Vertex<Integer,Interseccion> vertexActual = grafo.getVertex(kActual);

				Interseccion interseccionActual = vertexActual.info;

				nuevoGrafo.addVertex(interseccionActual.MOVEMENT_ID, new Interseccion(interseccionActual.MOVEMENT_ID, interseccionActual.latitud, interseccionActual.latitud, interseccionActual.MOVEMENT_ID));

				for(Edge<Integer,Interseccion> edgeActual : vertexActual.edges())
				{
					Interseccion laConectada = edgeActual.darElOtroVertice(kActual).info;

					if(!grafo.existeVertice(laConectada.MOVEMENT_ID))
						nuevoGrafo.addVertex(laConectada.MOVEMENT_ID, new Interseccion(laConectada.MOVEMENT_ID, laConectada.longitud, laConectada.latitud, laConectada.MOVEMENT_ID));

					if(!nuevoGrafo.existeArco(interseccionActual.MOVEMENT_ID, laConectada.MOVEMENT_ID) && interseccionActual.MOVEMENT_ID != laConectada.MOVEMENT_ID)
					{
						if(edgeActual.costoTiempo == 10)
							nuevoGrafo.addEdge(interseccionActual.MOVEMENT_ID, laConectada.MOVEMENT_ID, 1, 200, 1);
						else
							nuevoGrafo.addEdge(interseccionActual.MOVEMENT_ID, laConectada.MOVEMENT_ID, edgeActual.costoTiempo, 200, 1);
					}
				}
			}
			
			

			return nuevoGrafo;
		}

		/**
		 * Requerimiento 2C
		 * @throws Exception 
		 */
		public ListaEncadenada<Vertex<Integer, Interseccion>> darCaminoCostoMinimoTiempoEntreDosZonas(int idZonaOrigen, int idZonaDestino) throws Exception
		{
			return grafo.shortestPathDijsktraCostTime(idZonaOrigen, idZonaDestino);
		}

		/**
		 * Requerimiento 3C
		 */
		public Iterable<Edge<Integer,Interseccion>> darCaminoAZonaMasDistanteDesdeUnaZona(int idZonaOrigen)
		{
			return null;
		}

		//Métodos auxiliares

		public double darTiempoEstimadoDeCamino(Iterator<Vertex<Integer, Interseccion>> iterator)
		{
			return grafo.darTiempoEstimadoDeCamino(iterator);
		}



		public double darDistanciaEstimadaDeCamino(Iterator<Vertex<Integer, Interseccion>> iterator)
		{
			return grafo.darDistanciaEstimadaDeCamino(iterator);
		}

		public int darCantidadVerticesCCMasGrande()
		{
			return grafo.cantidadVerticesCCMasGrande();
		}

		public double calcularCostoDistancia(Iterable<Edge<Integer,Interseccion>> iterable)
		{
			return grafo.calcularCostoDistancia(iterable);
		}
	}
