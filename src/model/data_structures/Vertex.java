package model.data_structures;

import java.util.Iterator;

public class Vertex<K extends Comparable<K>, I> {
	
	//Atributos
	public K key;
	
	public I info;
	
	private ListaEncadenada<Edge<K, I>> edges;
	
	private boolean checked;
	
	public int componente;

	//Constructor
	public Vertex(K key, I value) {
		this.key = key;
		this.info = value;
		edges = new ListaEncadenada<Edge<K,I>>();
		checked = false;
		componente = 0;
	}
	
	//Métodos
	public void addEdge(Edge<K, I> newEdge)
	{
		edges.addFirst(newEdge);
	}
	
	public void setInfo(I info)
	{
		this.info = info;
	}
	
	public boolean isChecked()
	{
		return checked;
	}
	
	public void check()
	{
		checked = true;
	}
	
	public void uncheck()
	{
		checked = false;
	}
	
	public Edge<K, I> getEdge(K idOtherVertex)
	{
		Edge<K, I> buscado = null;
		
		Iterator<Edge<K, I>> iteradorEdges = edges.iterator();
		
		while (iteradorEdges.hasNext() && buscado == null) 
		{
			Edge<K, I> edgeActual = iteradorEdges.next();
			
			if(edgeActual.v1.key.compareTo(idOtherVertex) == 0 || edgeActual.v2.key.compareTo(idOtherVertex) == 0)
				buscado = edgeActual;
		}
		
		return buscado;
	}
	
	public Iterable<K> adj()
	{
		ListaEncadenada<K> identificadores = new ListaEncadenada<K>();
		
		Iterator<Edge<K, I>> iteradorEdges = edges.iterator();
		
		while(iteradorEdges.hasNext())
		{
			Edge<K, I> edgeActual = iteradorEdges.next();
			
			if(edgeActual.v1.key.compareTo(this.key) != 0)
				identificadores.addFirst(edgeActual.v1.key);
			else
				identificadores.addFirst(edgeActual.v2.key);
		}
		
		return identificadores;
	}
	
	public void setComponent(int comp)
	{
		componente = comp;
	}
	
	public void clearComponent()
	{
		componente = 0;
	}
	
	public boolean tieneArco(K idOtroVertex)
	{
		return getEdge(idOtroVertex) != null;
	}
	
	public ListaEncadenada<Edge<K, I>> edges()
	{
		return edges;
	}
}
