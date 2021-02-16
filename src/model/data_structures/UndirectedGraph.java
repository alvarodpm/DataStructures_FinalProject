package model.data_structures;

import java.util.Comparator;
import java.util.Iterator;

import model.logic.Interseccion;

/**
 * Estructura tomada del capítulo 4: Graphs, del libro guía Algorithms de Sedgewick y Wayne.
 */
public class UndirectedGraph<K extends Comparable<K>, I> implements IUndirectedGraph<K, I> {

	//Atributos

	private int V;         		 // number of vertices   
	private int E;                // number of edges   
	private HashTableSC<K, Vertex<K, I>> vertices;   // adjacency lists
	private ArregloDinamico<Integer> verticesPorComponente;

	public UndirectedGraph(int cantidadVertices) 
	{
		V = 0;
		E = 0;
		vertices = new HashTableSC<K, Vertex<K,I>>(cantidadVertices);
		verticesPorComponente = new ArregloDinamico<Integer>(200);
	}

	@Override
	public int V() {
		return V;
	}

	@Override
	public int E() {
		return E;
	}

	@Override
	public void addEdge(K idVertexIni, K idVertexFin, double costoDistancia, double costoTiempo, double costoVelocidad) 
	{
		Vertex<K, I> vertex1 = getVertex(idVertexIni);
		Vertex<K, I> vertex2 = getVertex(idVertexFin);

		if(vertex1 != null && vertex2 != null && !existeArco(idVertexIni, idVertexFin))
		{
			Edge<K, I> newEdge = new Edge<K, I>(vertex1, vertex2, costoDistancia, costoTiempo, costoVelocidad);

			vertex1.addEdge(newEdge);
			vertex2.addEdge(newEdge);

			E++;
		}
	}

	@Override
	public I getInfoVertex(K idVertex) {
		Vertex<K, I> vertex = getVertex(idVertex);

		if(vertex != null)
			return vertex.info;
		else
			return null;
	}

	@Override
	public void setInfoVertex(K idVertex, I infoVertex) 
	{
		Vertex<K, I> vertex = getVertex(idVertex);

		if(vertex != null)
			vertex.setInfo(infoVertex);
	}

	@Override
	public double getCostArc(K idVertexIni, K idVertexFin) {
		double costo = -1;

		Vertex<K, I> vertexIni = getVertex(idVertexIni);

		if(vertexIni != null)
		{
			Edge<K, I> edge = vertexIni.getEdge(idVertexFin);

			if(edge != null)
				costo = edge.costoDistancia;
		}

		return costo;
	}

	@Override
	public void setCostArc(K idVertexIni, K idVertexFin, double cost) {
		Vertex<K, I> vertexIni = getVertex(idVertexIni);

		if(vertexIni != null)
		{
			Edge<K, I> edge = vertexIni.getEdge(idVertexFin);

			if(edge != null)
				edge.setCostoDistancia(cost);
		}
	}

	@Override
	public void addVertex(K idVertex, I infoVertex) 
	{
		if(!existeVertice(idVertex)) 
		{
			Vertex<K, I> newVertex = new Vertex<K, I>(idVertex, infoVertex);

			vertices.putInSet(idVertex, newVertex);
			V++;	
		}
	}

	@Override
	public Iterable<K> adj(K idVertex) 
	{
		Vertex<K, I> vertex = getVertex(idVertex);

		if(vertex != null)
			return vertex.adj();
		else
			return new ListaEncadenada<K>();
	}

	@Override
	public void uncheck() {
		Iterator<K> ids = vertices.keys();

		while(ids.hasNext())
		{
			K idActual = ids.next();

			Vertex<K, I> vertexActual = getVertex(idActual);

			if(vertexActual != null)
				vertexActual.uncheck();
		}
	}

	@Override
	public void dfs(K s, int comp) 
	{ 
		//System.out.println("1c");
		Vertex<K, I> vertex = getVertex(s);
		//System.out.println("2c");
		if(vertex != null && !vertex.isChecked())
		{
			//System.out.println("3c");
			vertex.check();
			//System.out.println("4c");
			vertex.setComponent(comp);
			//System.out.println("5c");
			if(verticesPorComponente.darElemento(comp) == null)
				verticesPorComponente.modificarElemento(comp, 1);
			else
				verticesPorComponente.modificarElemento(comp, verticesPorComponente.darElemento(comp) + 1);
			//System.out.println("6c");
			//System.out.println("La componente " + comp + " tiene " + verticesPorComponente.darElemento(comp) + " vertices");
			//System.out.println("7c");
			for(K idActual : vertex.adj())
			{
				//System.out.println("8c");
				dfs(idActual, comp);
				//System.out.println("9c");
			}
		}
	}

	@Override
	public int cc() 
	{
		//System.out.println("1b");
		uncheck();
		verticesPorComponente = new ArregloDinamico<Integer>(200);

		//System.out.println("2b");
		int cantidadComponentes = 0;
		//System.out.println("3b");
		Iterator<K> ids = vertices.keys();
		//System.out.println("4b");
		while(ids.hasNext())
		{
			//System.out.println("5b");
			K idActual = ids.next();
			//System.out.println("6b");
			Vertex<K, I> vertexActual = getVertex(idActual);
			//System.out.println("7b");

			if(!(vertexActual.isChecked()))
			{
				//System.out.println("8b");
				cantidadComponentes++;
				//System.out.println("9b");
				dfs(idActual, cantidadComponentes);
				//System.out.println("10b");
			}
			//System.out.println("11b");
		}
		//System.out.println("12b");
		return cantidadComponentes;
	}

	@Override
	public Iterable<K> getCC(K idVertex) throws Exception 
	{
		cc();
		Vertex<K, I> vertex = getVertex(idVertex);

		if(vertex.componente == 0)
			throw new Exception("Aún no se han contado las componentes");

		Iterator<K> ids = vertices.keys();
		ListaEncadenada<K> componenteConectada = new ListaEncadenada<K>();

		while(ids.hasNext())
		{
			K idActual = ids.next();
			Vertex<K, I> vertexActual = getVertex(idActual);

			if(vertex != vertexActual && vertexActual.componente == vertex.componente)
				componenteConectada.addFirst(idActual);
		}

		return componenteConectada;
	}

	@Override
	public Vertex<K, I> getVertex(K key)
	{
		Iterator<Vertex<K, I>> iteradorV = vertices.getSet(key);

		if(iteradorV.hasNext())
			return iteradorV.next();
		else
			return null;
	}

	public boolean existeVertice(K idVertice)
	{
		Iterator<Vertex<K, I>> iteradorV = vertices.getSet(idVertice);

		return iteradorV.hasNext();
	}

	public boolean existeArco(K idVertexIni, K idVertexFin) {
		Vertex<K, I> vertex = getVertex(idVertexIni);

		if(vertex != null)
			return vertex.tieneArco(idVertexFin);
		else
			return false;
	}

	@Override
	public Iterator<K> keys()
	{
		return vertices.keys();
	}


	public Iterator<Integer> darCantidadVerticesDeLasNCCMasGrandes(int cantidadComponentes)
	{
		//System.out.println("1");
		//System.out.println("2");
		MinHeapCP<Integer, Integer> heapCantidadVerticesPorComponente = new MinHeapCP<Integer, Integer>();
		//System.out.println("3");
		int compActual = 1;
		//System.out.println("4");
		while(verticesPorComponente.darElemento(compActual) != null)
		{
			//System.out.println("5");
			if(heapCantidadVerticesPorComponente.darNumElementos() < cantidadComponentes)
			{
				//System.out.println("6");
				heapCantidadVerticesPorComponente.agregar(verticesPorComponente.darElemento(compActual), verticesPorComponente.darElemento(compActual));
				//System.out.println("7");
			}
			else if(heapCantidadVerticesPorComponente.darMin() < verticesPorComponente.darElemento(compActual))
			{
				//System.out.println("8");
				heapCantidadVerticesPorComponente.sacarMin();
				//System.out.println("9");
				heapCantidadVerticesPorComponente.agregar(verticesPorComponente.darElemento(compActual), verticesPorComponente.darElemento(compActual));
				//System.out.println("10");
			}
			//System.out.println("11");
			compActual++;
			//System.out.println("Componente actual = " + compActual);
		}
		//System.out.println("12");
		ListaEncadenada<Integer> listaCantidadVertices = new ListaEncadenada<Integer>();
		//System.out.println("13");
		while(!heapCantidadVerticesPorComponente.esVacia())
		{
			//System.out.println("14");
			listaCantidadVertices.addFirst(heapCantidadVerticesPorComponente.sacarMin());
			//System.out.println("15");
		}

		//System.out.println("16");
		return listaCantidadVertices.iterator();
	}

	public Vertex<K,I> darUnVerticeDeLaCCMasGrande()
	{
		cc();

		int ccMasGrande = -1;
		int cantidadVerticesCCMasGrande = -1;

		for(int i = 1; verticesPorComponente.darElemento(i) != null; i++)
		{
			if(verticesPorComponente.darElemento(i) > cantidadVerticesCCMasGrande)
			{
				cantidadVerticesCCMasGrande = verticesPorComponente.darElemento(i);
				ccMasGrande = i;
			}
		}


		Iterator<K> keys = keys();

		while(keys.hasNext())
		{
			Vertex<K, I> vertex = getVertex(keys.next());

			if(vertex.componente == ccMasGrande)
				return vertex;
		}

		return null;
	}

	public ListaEncadenada<Edge<K,I>> darMSTKruskalDistancia() throws Exception
	{
		Vertex<K,I> vertexOrigen = darUnVerticeDeLaCCMasGrande();

		MinHeapCP<Double, Edge<K, I>> colaEdges = new MinHeapCP<Double, Edge<K,I>>();

		for(K kActual : getCC(vertexOrigen.key))
		{
			Vertex<K, I> vertexActual = getVertex(kActual);

			for(Edge<K,I> edgeActual : vertexActual.edges())
			{
				colaEdges.agregar(edgeActual.costoDistancia, edgeActual);
			}
		}

		uncheck();

		ListaEncadenada<Edge<K, I>> edgesMST = new ListaEncadenada<Edge<K,I>>();
		
		//edgesMST.size() < (verticesPorComponente.darElemento(vertexOrigen.componente) - 1) || 
		while(!colaEdges.esVacia())
		{
			Edge<K, I> edgeActual = colaEdges.sacarMin();

			if(!edgeActual.losDosVerticesEstanMarcados())
			{
				edgesMST.addFirst(edgeActual);
				edgeActual.marcarVertices();
			}
		}

		return edgesMST;
	}

	/**
	 * 
	 * @param keyOrigen Es el vértice del cuál partimos para buscar su camino hacia el vértice destino
	 * @param keyDestino Es el vértice al que queremos llegar
	 * @return Un Iterable con los vértices que conforman el camino de menor costo entre los dos vértices
	 * @throws Exception 
	 */
	public ListaEncadenada<Vertex<K, I>> shortestPathDijkstraCostDistance(K keyOrigen, K keyDestino) throws Exception
	{
		//Se desmarcan todos los vértices al comenzar el algoritmo
		uncheck();

		//HashTable que tiene la distancia más corta hasta el momento hasta el vértice que tiene la llave K
		HashTableSC<K, Double> shortestDistanceFromStartVertex = new HashTableSC<K, Double>(V * 2);

		//HashTable que tiene la llave del vértice previo al vértice con llave K, para el camino más corto desde el vértive con llave keyOrigen
		HashTableSC<K, K> previousVertex = new HashTableSC<K, K>(V * 2);

		//MinHeap en el que está la representación de los arcos alcanzables hasta el momento
		MinHeapCP<Double, K> colaEdges = new MinHeapCP<Double, K>(V);

		Iterator<K> keys = keys();

		int contador = 0;

		//Se inicializan los 4 HashTables
		while(keys.hasNext())
		{
			K kActual = keys.next();

			shortestDistanceFromStartVertex.putInSet(kActual, Double.POSITIVE_INFINITY);

			previousVertex.putInSet(kActual, null);	
			contador++;
		}

		//Se inicializa el costo de llegar al vértice origen en 0
		shortestDistanceFromStartVertex.putInSet(keyOrigen, (double) 0);

		colaEdges.agregar((double) 0, keyOrigen);


		//Se comienza el ciclo para encontrar el camino más barato hasta el vértice destino
		while(!colaEdges.esVacia())
		{
			System.out.println("1");
			//Se saca de la cola de prioridad la llave del vértice con la menor distancia desde el origen hasta ahora
			K llaveVertexMenor = colaEdges.sacarMin();
			System.out.println("2");
			//Se encuentra el vértice que tenga esa llave
			Vertex<K, I> vertexMenor = getVertex(llaveVertexMenor);
			System.out.println("3");
			//Si el vértice no ha sido visitado, se visita
			if(!vertexMenor.isChecked())
			{
				System.out.println("4");
				//Se recorren los arcos del vértice que guardamos
				for(Edge<K,I> edgeActual : vertexMenor.edges())
				{
					System.out.println("5");
					Vertex<K, I> vertexActual = edgeActual.darElOtroVertice(llaveVertexMenor);
					System.out.println("6");
					Double distanciaPorEsteCamino = shortestDistanceFromStartVertex.getLastValue(llaveVertexMenor) + edgeActual.costoDistancia;
					System.out.println("7");
					System.out.println("llaveVertexMenor:");
					System.out.println(llaveVertexMenor);
					System.out.println("distanciaPorEsteCamino:");
					System.out.println(distanciaPorEsteCamino);
					System.out.println("vertexActual.key:");
					System.out.println(vertexActual.key);
					System.out.println("shortestDistanceFromStartVertex.getLastValue(vertexActual.key):");
					System.out.println(shortestDistanceFromStartVertex.getLastValue(vertexActual.key));
					System.out.println("Hey");

					//Si encontramos un camino más corto, actualizamos los datos
					if(distanciaPorEsteCamino < shortestDistanceFromStartVertex.getLastValue(vertexActual.key))
					{
						System.out.println("8");
						shortestDistanceFromStartVertex.putInSet(vertexActual.key, distanciaPorEsteCamino);
						System.out.println("9");
						previousVertex.putInSet(vertexActual.key, llaveVertexMenor);
						System.out.println("10");
						colaEdges.agregar(distanciaPorEsteCamino, vertexActual.key);
						System.out.println("11");
					}
					System.out.println("12");
				}
				System.out.println("13");
				//Contamos la visita a este vértice
				vertexMenor.check();
				System.out.println("14");
			}
		}
		System.out.println("15");
		//Creamos la lista en la que vamos a guardar los vértices que conforman el camino de menor costo
		ListaEncadenada<Vertex<K, I>> camino = new ListaEncadenada<Vertex<K,I>>();
		System.out.println("16");
		Vertex<K, I> vertexActual = getVertex(keyDestino);
		System.out.println("17");
		camino.addFirst(vertexActual);
		System.out.println("18");
		System.out.println(vertexActual.key);
		//Se agregan los vértices que conforman el camino a la lista
		while(previousVertex.getLastValue(vertexActual.key) != null)
		{
			System.out.println("19");
			vertexActual = getVertex(previousVertex.getLastValue(vertexActual.key));
			System.out.println("20");
			camino.addFirst(vertexActual);
			System.out.println("21");
		}
		System.out.println("22");

		if(camino.getFirstItem().key.compareTo(keyOrigen) != 0)
			throw new Exception("No existe un camino entre los dos vértices");

		return camino;
	}

	public double darTiempoEstimadoDeCamino(Iterator<Vertex<K, I>> iterator)
	{
		Vertex<K, I> vertex1 = iterator.next();
		double tiempo = 0;
		while(iterator.hasNext())
		{
			Vertex<K,I> vertex2 = iterator.next();

			tiempo += vertex1.getEdge(vertex2.key).costoTiempo;
			vertex1 = vertex2;
		}
		return tiempo;
	}

	public double darDistanciaEstimadaDeCamino(Iterator<Vertex<K, I>> iterator)
	{
		Vertex<K, I> vertex1 = iterator.next();
		double distancia = 0;
		while(iterator.hasNext())
		{
			Vertex<K,I> vertex2 = iterator.next();

			distancia += vertex1.getEdge(vertex2.key).costoDistancia;
			vertex1 = vertex2;
		}
		return distancia;
	}

	public ListaEncadenada<Vertex<K, I>> darVerticesAlcanzablesSegunTiempo(K keyOrigen, double tiempo) 
	{
		//Se desmarcan todos los vértices al comenzar el algoritmo
		uncheck();

		//Contador que se usará para saber cuántos vértices se han visitado
		int verticesVisitados = 0;

		//HashTable que tiene el tiempo más corto hasta el momento hasta el vértice que tiene la llave K
		HashTableSC<K, Double> shortestTimeFromStartVertex = new HashTableSC<K, Double>(V * 2);

		//MinHeap en el que está la representación de los arcos alcanzables hasta el momento
		MinHeapCP<Double, K> colaEdges = new MinHeapCP<Double, K>(V);

		Iterator<K> keys = keys();

		int contador = 0;

		//Se inicializa el HashTable
		while(keys.hasNext())
		{
			K kActual = keys.next();

			shortestTimeFromStartVertex.putInSet(kActual, Double.POSITIVE_INFINITY);

			contador++;
		}

		//Se inicializa el costo de llegar al vértice origen en 0
		shortestTimeFromStartVertex.putInSet(keyOrigen, (double) 0);

		colaEdges.agregar((double) 0, keyOrigen);


		//Se comienza el ciclo para encontrar el camino más barato hasta todos los vértices de la CC
		while(!colaEdges.esVacia())
		{
			System.out.println("1");
			//Se saca de la cola de prioridad la llave del vértice con el menor tiempo desde el origen hasta ahora
			K llaveVertexMenor = colaEdges.sacarMin();
			System.out.println("2");
			//Se encuentra el vértice que tenga esa llave
			Vertex<K, I> vertexMenor = getVertex(llaveVertexMenor);
			System.out.println("3");
			//Si el vértice no ha sido visitado, se visita
			if(!vertexMenor.isChecked())
			{
				System.out.println("4");
				//Se recorren los arcos del vértice que guardamos
				for(Edge<K,I> edgeActual : vertexMenor.edges())
				{
					System.out.println("5");
					Vertex<K, I> vertexActual = edgeActual.darElOtroVertice(llaveVertexMenor);
					System.out.println("6");
					Double tiempoPorEsteCamino = shortestTimeFromStartVertex.getLastValue(llaveVertexMenor) + edgeActual.costoTiempo;
					System.out.println("7");
					System.out.println("llaveVertexMenor:");
					System.out.println(llaveVertexMenor);
					System.out.println("distanciaPorEsteCamino:");
					System.out.println(tiempoPorEsteCamino);
					System.out.println("vertexActual.key:");
					System.out.println(vertexActual.key);
					System.out.println("shortestDistanceFromStartVertex.getLastValue(vertexActual.key):");
					System.out.println(shortestTimeFromStartVertex.getLastValue(vertexActual.key));
					System.out.println("Hey");

					//Si encontramos un camino más corto, actualizamos los datos
					if(tiempoPorEsteCamino < shortestTimeFromStartVertex.getLastValue(vertexActual.key))
					{
						System.out.println("8");
						shortestTimeFromStartVertex.putInSet(vertexActual.key, tiempoPorEsteCamino);
						System.out.println("9");
						System.out.println("10");
						colaEdges.agregar(tiempoPorEsteCamino, vertexActual.key);
						System.out.println("11");
					}
					System.out.println("12");
				}
				System.out.println("13");
				//Contamos la visita a este vértice
				vertexMenor.check();
				System.out.println("14");
				verticesVisitados++;
			}
		}
		System.out.println("15");
		//Creamos la lista en la que vamos a guardar los vértices que son alcanzables en el tiempo indicado
		ListaEncadenada<Vertex<K, I>> verticesAlcanzados = new ListaEncadenada<Vertex<K,I>>();

		Iterator<K> iterador = shortestTimeFromStartVertex.keys();

		while(iterador.hasNext())
		{
			K kActual = iterador.next();

			if(shortestTimeFromStartVertex.getLastValue(kActual) <= tiempo)
			{
				verticesAlcanzados.addFirst(getVertex(kActual));
			}
		}

		return verticesAlcanzados;
	}

	public int cantidadVerticesCCMasGrande()
	{
		cc();
		int cantidadVerticesCCMasGrande = -1;

		for(int i = 1; verticesPorComponente.darElemento(i) != null; i++)
		{
			if(verticesPorComponente.darElemento(i) > cantidadVerticesCCMasGrande)
			{
				cantidadVerticesCCMasGrande = verticesPorComponente.darElemento(i);
			}
		}

		return cantidadVerticesCCMasGrande;
	}

	public double calcularCostoDistancia(Iterable<Edge<K,I>> iterable)
	{
		double costo = 0;

		for(Edge<K, I> edge : iterable)
		{
			costo += edge.costoDistancia;
		}

		return costo;
	}

	public Iterable<K> darCCMasGrande() {
		try {
			return getCC(darUnVerticeDeLaCCMasGrande().key);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public ListaEncadenada<Vertex<K, I>>shortestPathDijkstraTime(K origen, K destino) throws Exception{

		uncheck();
		ListaEncadenada<Vertex<K, I>> camino = new ListaEncadenada<>();
		int visitados = 0;

		HashTableSC<K, Double> tabla = new HashTableSC<>(V*2);

		HashTableSC<K, K> verticeAnterior = new HashTableSC<>(V*2);

		MinHeapCP<Double, K> edges = new MinHeapCP<>(V);
		Iterator<K> keys = keys();
		K actual = keys.next();
		while(keys.hasNext()){

		tabla.putInSet(actual, Double.POSITIVE_INFINITY);
		   verticeAnterior.putInSet(actual, null);
		}

		tabla.putInSet(origen, (double) 0);
		edges.agregar((double) 0,origen);

		while(!edges.esVacia()){

		K llaveMenor = edges.sacarMin();
		Vertex<K, I> vertexMenor = getVertex(llaveMenor);

		if(!vertexMenor.isChecked()){

		for(Edge<K, I> edActual: vertexMenor.edges()){

		Vertex<K, I> vertexActual = edActual.darElOtroVertice(llaveMenor);
		Double tiempo = tabla.getLastValue(llaveMenor) + edActual.costoTiempo;

		if(tabla.getLastValue(vertexActual.key) == null || tiempo < tabla.getLastValue(vertexActual.key)){

		tabla.putInSet(vertexActual.key, tiempo);
		verticeAnterior.putInSet(vertexActual.key, llaveMenor);
		edges.agregar(tiempo, vertexActual.key);
		}
		}
		visitados++;
		}

		Vertex<K, I> VActual = getVertex(destino);
		camino.addFirst(VActual);

		while(verticeAnterior.getLastValue(VActual.key) != null){

		VActual = getVertex(verticeAnterior.getLastValue(VActual.key));
		camino.addFirst(VActual);
		}

		if(camino.getFirstItem().key.compareTo(origen) != 0){

		throw new Exception("No existe un camino entre los dos vértices");

		}
		}

		return camino;

		}
	
	public ListaEncadenada<Vertex<K,I>> shortestPathDijsktraCostTime(K keyOrigen, K keyDestino) throws Exception
	{
		//Se desmarcan todos los vértices al comenzar el algoritmo
				uncheck();

				//Contador que se usará para saber cuántos vértices se han visitado
				int verticesVisitados = 0;

				//HashTable que tiene el tiempo más corto hasta el momento hasta el vértice que tiene la llave K
				HashTableSC<K, Double> shortestTimeFromStartVertex = new HashTableSC<K, Double>(V * 2);
				
				//HashTable que tiene la llave del vértice previo al vértice con llave K, para el camino más corto desde el vértive con llave keyOrigen
				HashTableSC<K, K> previousVertex = new HashTableSC<K, K>(V * 2);

				//MinHeap en el que está la representación de los arcos alcanzables hasta el momento
				MinHeapCP<Double, K> colaEdges = new MinHeapCP<Double, K>(V);

				Iterator<K> keys = keys();

				int contador = 0;

				//Se inicializa el HashTable
				while(keys.hasNext())
				{
					K kActual = keys.next();

					shortestTimeFromStartVertex.putInSet(kActual, Double.POSITIVE_INFINITY);
					
					previousVertex.putInSet(kActual, null);	

					contador++;
				}

				//Se inicializa el costo de llegar al vértice origen en 0
				shortestTimeFromStartVertex.putInSet(keyOrigen, (double) 0);

				colaEdges.agregar((double) 0, keyOrigen);


				//Se comienza el ciclo para encontrar el camino más barato hasta todos los vértices de la CC
				while(!colaEdges.esVacia())
				{
					System.out.println("1");
					//Se saca de la cola de prioridad la llave del vértice con el menor tiempo desde el origen hasta ahora
					K llaveVertexMenor = colaEdges.sacarMin();
					System.out.println("2");
					//Se encuentra el vértice que tenga esa llave
					Vertex<K, I> vertexMenor = getVertex(llaveVertexMenor);
					System.out.println("3");
					//Si el vértice no ha sido visitado, se visita
					if(!vertexMenor.isChecked())
					{
						System.out.println("4");
						//Se recorren los arcos del vértice que guardamos
						for(Edge<K,I> edgeActual : vertexMenor.edges())
						{
							System.out.println("5");
							Vertex<K, I> vertexActual = edgeActual.darElOtroVertice(llaveVertexMenor);
							System.out.println("6");
							Double tiempoPorEsteCamino = shortestTimeFromStartVertex.getLastValue(llaveVertexMenor) + edgeActual.costoTiempo;
							System.out.println("7");
							System.out.println("llaveVertexMenor:");
							System.out.println(llaveVertexMenor);
							System.out.println("distanciaPorEsteCamino:");
							System.out.println(tiempoPorEsteCamino);
							System.out.println("vertexActual.key:");
							System.out.println(vertexActual.key);
							System.out.println("shortestDistanceFromStartVertex.getLastValue(vertexActual.key):");
							System.out.println(shortestTimeFromStartVertex.getLastValue(vertexActual.key));
							System.out.println("Hey");

							//Si encontramos un camino más corto, actualizamos los datos
							if(tiempoPorEsteCamino < shortestTimeFromStartVertex.getLastValue(vertexActual.key))
							{
								System.out.println("8");
								shortestTimeFromStartVertex.putInSet(vertexActual.key, tiempoPorEsteCamino);
								System.out.println("9");
								previousVertex.putInSet(vertexActual.key, llaveVertexMenor);
								System.out.println("10");
								colaEdges.agregar(tiempoPorEsteCamino, vertexActual.key);
								System.out.println("11");
							}
							System.out.println("12");
						}
						System.out.println("13");
						//Contamos la visita a este vértice
						vertexMenor.check();
						System.out.println("14");
						verticesVisitados++;
					}
				}
				System.out.println("15");
				//Creamos la lista en la que vamos a guardar los vértices que son alcanzables en el tiempo indicado
				ListaEncadenada<Vertex<K, I>> camino = new ListaEncadenada<Vertex<K,I>>();
				System.out.println("16");
				Vertex<K, I> vertexActual = getVertex(keyDestino);
				System.out.println("17");
				camino.addFirst(vertexActual);
				System.out.println("18");
				System.out.println(vertexActual.key);
				//Se agregan los vértices que conforman el camino a la lista
				while(previousVertex.getLastValue(vertexActual.key) != null)
				{
					System.out.println("19");
					vertexActual = getVertex(previousVertex.getLastValue(vertexActual.key));
					System.out.println("20");
					camino.addFirst(vertexActual);
					System.out.println("21");
				}
				System.out.println("22");

				if(camino.getFirstItem().key.compareTo(keyOrigen) != 0)
					throw new Exception("No existe un camino entre los dos vértices");

				return camino;
	}
}
