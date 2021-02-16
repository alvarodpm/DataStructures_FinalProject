package model.data_structures;

import com.teamdev.jxmaps.internal.internal.e;

public class Edge<K extends Comparable<K>, I> implements Comparable<Edge<K,I>>{

	//Atributos 

	public Vertex<K, I> v1;
	public Vertex<K, I> v2;
	public double costoDistancia;
	public double costoTiempo;
	public double costoVelocidad;

	//Constructor
	public Edge(Vertex<K, I> v1, Vertex<K, I> v2, double costoDistancia, double costoTiempo, double costoVelocidad) {
		this.v1 = v1;
		this.v2 = v2;
		this.costoDistancia = costoDistancia;
		this.costoTiempo = costoTiempo;
		this.costoVelocidad = costoVelocidad;
	}

	public void setCostoTiempo(double costoTiempo) {
		this.costoTiempo = costoTiempo;
	}

	public void setCostoVelocidad(double costoVelocidad) {
		this.costoVelocidad = costoVelocidad;
	}

	public void setCostoDistancia(double costoDistancia) {
		this.costoDistancia = costoDistancia;
	}

	@Override
	public int compareTo(Edge<K, I> edge) {
		if(this.costoDistancia > edge.costoDistancia)
			return 1;
		else if(this.costoDistancia < edge.costoDistancia)
			return -1;
		else
			return 0;
	}	
	
	public Vertex<K, I> darElOtroVertice(K key)
	{
		if(v1.key.compareTo(key) == 0)
			return v2;
		else
			return v1;
	}
	
	public Vertex<K, I> darElQueNoEstaMarcado()
	{
		if(v1.isChecked())
			return v2;
		else
			return v1;
	}
	
	public boolean losDosVerticesEstanMarcados()
	{
		if(v1.isChecked() && v2.isChecked())
			return true;
		else
			return false;
	}
	
	public void marcarVertices()
	{
		v1.check();
		v2.check();
	}
}
