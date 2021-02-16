package model.data_structures;

import java.util.Iterator;

import model.logic.Interseccion;

public class PrimMst
{
	private Edge[] edgeTo; // shortest edge from tree vertex
	private double[] distTo; // distTo[w] = edgeTo[w].weight()
	private boolean[] marked; // true if v on tree
	private MinHeapCP<Double, Double> pq; // eligible crossing edges

	public PrimMst(UndirectedGraph<Integer, Interseccion> G) throws Exception
	{
		Iterator<Integer>k= G.keys();
		int key = k.next();
		double max = 0;
		Iterable<Integer> buscada = null;
		Vertex<Integer, Interseccion> v = null;
		while(k.hasNext()){

			Iterable<Integer> cc = G.getCC(key);
			Iterator<Integer> componente = cc.iterator();
			int cantidad = 0;
			while(componente.hasNext()){
				cantidad ++;
			}
			if(cantidad> max){

				max = cantidad;
				buscada = cc;
				v = G.getVertex(key);
			}
		}

		edgeTo = new Edge[G.V()];
		distTo = new double[G.V()];
		marked = new boolean[G.V()];
		for (int i = 0; i< G.V(); i++)
			distTo[i] = Double.POSITIVE_INFINITY;
		pq = new MinHeapCP<Double, Double>(G.V());
		pq.agregar(max, max);

		while (!pq.esVacia())

			visit(G, pq.sacarMin().intValue()); // Add closest vertex to tree.

	}


	private void visit(UndirectedGraph<Integer, Interseccion> G, int v)
	{ // Add v to tree; update data structures.

		marked[v] = true;  
		Iterable<Integer> lista = G.adj(v);
		Iterator<Integer> iterator = lista.iterator();
		int it = iterator.next();

		while(iterator.hasNext()){

			Vertex<Integer, Interseccion> ver = G.getVertex(it);
			ListaEncadenada<Edge<Integer, Interseccion>>vertices =ver.edges();

			for(int i = 0; i< vertices.size(); i++){

				Vertex<Integer, Interseccion> w = vertices.get(i).darElOtroVertice(ver.key);
				if(marked[w.key]){
					continue;
				}
				if(vertices.get(i).costoDistancia < distTo[w.key]){

					edgeTo[w.key] = vertices.get(i);
					distTo[w.key] = vertices.get(i).costoDistancia;

					if(pq.contains(w.key)){
						pq.exch(w.key,(int) distTo[w.key]);
					}
					else{
						pq.agregar(w.key.doubleValue(), w.key.doubleValue());
					}
				}
			}
		}
	}

	public Iterable<Edge<Integer, Interseccion>> edges()
	{
		ListaEncadenada<Edge<Integer, Interseccion>> mst = new ListaEncadenada<Edge<Integer, Interseccion>>();

		for (int v = 1; v < edgeTo.length; v++){

			mst.addFirst(edgeTo[v]);
		}
		return mst;
	}
}