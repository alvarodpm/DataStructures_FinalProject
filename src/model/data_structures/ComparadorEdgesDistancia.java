package model.data_structures;

import java.util.Comparator;

import model.data_structures.Edge;

public class ComparadorEdgesDistancia<K extends Comparable<K>,V> implements Comparator<Edge<K,V>>{

	@Override
	public int compare(Edge<K, V> e1, Edge<K, V> e2) {
		if(e1.costoDistancia > e2.costoDistancia)
			return 1;
		else if(e1.costoDistancia < e2.costoDistancia)
			return -1;
		else
			return 0;
	}

}
