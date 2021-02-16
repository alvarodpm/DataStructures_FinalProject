package model.data_structures;

import java.util.Iterator;

/**
 * Estructura tomada del capítulo 4: Graphs, del libro guía Algorithms de Sedgewick y Wayne.
 */
public interface IUndirectedGraph <K extends Comparable<K>, I>{
	
	int V();
	int E();
	void addEdge(K idVertexIni, K idVertexFin, double costoDistancia, double costoTiempo, double costoVelocidad);
	I getInfoVertex(K idVertex);
	void setInfoVertex(K idVertex, I infoVertex);
	double getCostArc(K idVertexIni, K idVertexFin);
	void setCostArc(K idVertexIni, K idVertexFin, double cost);
	void addVertex(K idVertex, I infoVertex);
	Iterable<K> adj (K idVertex) ;
	void uncheck() ;
	void dfs(K s, int comp);
	int cc();
	Iterable<K> getCC(K idVertex) throws Exception;
	Vertex<K, I> getVertex(K key);
	Iterator<K> keys();
}
